import { Component, OnInit } from '@angular/core';
import { FilterComponent } from './filter/filter.component';
import { ServiceCardComponent } from '../layout/service-card/service-card.component';
import { CommonModule } from '@angular/common';
import { ServicesService } from '../services/services.service';
import { Service } from '../app.models';
import { PublicServicesService } from '../services/public-services.service';

@Component({
  selector: 'app-store',
  standalone: true,
  imports: [FilterComponent, ServiceCardComponent, CommonModule],
  templateUrl: './store.component.html',
  styleUrls: ['./store.component.less'] // Update to "styleUrls" for correct syntax
})
export class StoreComponent implements OnInit {
  services: Service[] | null = [];

  constructor(private publicServices: PublicServicesService) {}

  ngOnInit(): void {
    this.loadAllServices();
  }

  applyFilter(filterValues: any): void {
    this.publicServices.filterServices(filterValues).subscribe(services => {
      this.services = services;
    });
  }

  clearFilter(): void {
    this.loadAllServices();
  }

  private loadAllServices(): void {
    this.publicServices.getAllServices().subscribe(services => {
      this.services = services;
    });
  }
}
