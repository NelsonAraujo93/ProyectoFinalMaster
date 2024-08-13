import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { ClientServicesService } from '../../services/client-services.service';
import { RequestGroup, ServiceRequest } from '../../app.models';
import { CommonModule } from '@angular/common';
import { RequestCardComponent } from '../../requests/request-card/request-card.component';
import { RequestDetailsComponent } from '../../requests/request-details/request-details.component';
import { FormsModule } from '@angular/forms'; // Ensure FormsModule is imported
import { ClientMenuService } from '../../services/client-menu.service';

@Component({
  selector: 'app-client-menu',
  standalone: true,
  imports: [CommonModule, RequestCardComponent, RequestDetailsComponent, FormsModule],
  templateUrl: './client-menu.component.html',
  styleUrls: ['./client-menu.component.less']
})
export class ClientMenuComponent implements OnInit, OnDestroy {
  selectedMenuItem: string = 'Pending';
  requests: any;
  selectedRequest: ServiceRequest | null = null;
  ratingValue: number = 0; // Initialize to default value
  commentValue: string = ''; // Initialize to default value
  private subscriptions: Subscription = new Subscription();

  constructor(
    private clientServices: ClientServicesService,
    private menuService: ClientMenuService
  ) {}

  ngOnInit(): void {
    this.subscriptions.add(
      this.clientServices.getGroupedServiceRequests().subscribe((data: RequestGroup) => {
        this.requests = data;
      })
    );

    this.subscriptions.add(
      this.menuService.getSelectedMenuItem().subscribe((selectedItem: string) => {
        this.selectedMenuItem = selectedItem;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  openRequestDetails(request: ServiceRequest): void {
    this.selectedRequest = request;
  }

  closeRequestDetails(): void {
    this.selectedRequest = null;
  }

  finalizeRequest(requestId: string, rating: number, comment: string): void {
    if (rating > 0 && comment) { // Basic validation
      this.clientServices.finalizeRequest(requestId, rating, comment).subscribe(
        updatedRequest => {
          this.loadRequests(); // Reload requests after the update
        },
        error => {
          console.error('Error finalizing request:', error);
        }
      );
    } else {
      console.error('Rating and comment are required to finalize the request.');
    }
  }
  
  private loadRequests(): void {
    this.clientServices.getGroupedServiceRequests().subscribe((data: RequestGroup) => {
      this.requests = data;
    });
  }
}
