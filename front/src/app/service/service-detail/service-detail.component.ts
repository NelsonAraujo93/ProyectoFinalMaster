import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PublicServicesService } from '../../services/public-services.service';
import { ServiceDetailed } from '../../app.models';
import { CommonModule } from '@angular/common';
import { RequestDetailsComponent } from '../../requests/request-details/request-details.component';

@Component({
  selector: 'app-service-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, RequestDetailsComponent],
  templateUrl: './service-detail.component.html',
  styleUrls: ['./service-detail.component.less']
})
export class ServiceDetailComponent implements OnInit {
  service: ServiceDetailed | null = null;

  constructor(
    private route: ActivatedRoute,
    private publicServicesService: PublicServicesService,
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.publicServicesService.getServiceById(id).subscribe(service => {
      this.service = service;
    });
  }

  requestService(): void {
    if (this.service?.service.id !== undefined) {
      this.publicServicesService.requestService(this.service.service.id, '').subscribe(
        (request) => {
          // Handle successful service request here
          console.log('Service request created:', request);
        },
        (error) => {
          // Handle error here, if necessary
          console.error('Error creating service request:', error);
        }
      );
    } 
  }
}
