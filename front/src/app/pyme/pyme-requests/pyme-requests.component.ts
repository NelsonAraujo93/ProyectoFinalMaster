import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { PymeServicesService } from '../../services/pyme-services.service';
import { RequestGroup, ServiceRequest } from '../../app.models';
import { CommonModule } from '@angular/common';
import { RequestCardComponent } from '../../requests/request-card/request-card.component';
import { RequestDetailsComponent } from '../../requests/request-details/request-details.component';

@Component({
  selector: 'app-pyme-requests',
  standalone: true,
  imports: [CommonModule, RequestCardComponent, RequestDetailsComponent],
  templateUrl: './pyme-requests.component.html',
  styleUrls: ['./pyme-requests.component.less']
})
export class PymeRequestsComponent implements OnInit, OnDestroy {
  selectedTab: string = 'Pending';
  requests: any;
  selectedRequest: ServiceRequest | null = null;
  private subscriptions: Subscription = new Subscription();

  tabs = [
    { label: 'Pending', value: 'Pending' },
    { label: 'On Process', value: 'On Process' },
    { label: 'Complete', value: 'Complete' },
    { label: 'Finalized', value: 'Finalized' },
    { label: 'Canceled', value: 'Canceled' }
  ];

  constructor(private pymeServices: PymeServicesService) {}

  ngOnInit(): void {
    this.loadRequests();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  // Load grouped service requests
  loadRequests(): void {
    this.subscriptions.add(
      this.pymeServices.getGroupedServiceRequestsForPyme().subscribe((data: RequestGroup) => {
        this.requests = data;
      })
    );
  }

  selectTab(tab: string): void {
    this.selectedTab = tab;
  }

  openRequestDetails(request: ServiceRequest): void {
    this.selectedRequest = request;
  }

  closeRequestDetails(): void {
    this.selectedRequest = null;
  }

  // Actions
  cancelRequest(requestId: string): void {
    this.pymeServices.cancelRequest(requestId).subscribe(
      () => {
        this.loadRequests(); // Reload requests after the update
      },
      error => {
        console.error('Error canceling request:', error);
      }
    );
  }

  updateRequestToOnProcess(requestId: string): void {
    this.pymeServices.updateRequestToOnProcess(requestId).subscribe(
      () => {
        this.loadRequests(); // Reload requests after the update
      },
      error => {
        console.error('Error updating request to On Process:', error);
      }
    );
  }

  updateRequestToComplete(requestId: string): void {
    this.pymeServices.updateRequestToComplete(requestId).subscribe(
      () => {
        this.loadRequests(); // Reload requests after the update
      },
      error => {
        console.error('Error updating request to Complete:', error);
      }
    );
  }
}
