import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Service } from '../app.models';
import { ServicesService } from '../services/services.service';
import { Subscription } from 'rxjs';
import { UserSessionService } from '../services/user-session.service';
import { ReactiveFormsModule } from '@angular/forms';
import { ServiceFormComponent } from '../service-form/service-form.component';

@Component({
  selector: 'app-pyme-services',
  standalone: true,
  imports: [MatIconModule, CommonModule, ReactiveFormsModule, ServiceFormComponent],
  templateUrl: './pyme-services.component.html',
  styleUrls: ['./pyme-services.component.less']
})
export class PymeServicesComponent implements OnInit, OnDestroy {
  create: boolean = false;
  edit: boolean = false;
  userSubscription: Subscription = new Subscription();
  servicesSubscription: Subscription = new Subscription();
  services: Service[] = [];
  user: any;
  selectedService: Service | null = null;

  constructor(private servicesService: ServicesService, private userServices: UserSessionService) { }

  ngOnInit(): void {
    this.userSubscription = this.userServices.currentUser.subscribe(user => {
      if (user) {
        this.user = user.data;
        this.loadServices();
      }
    });
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
    this.servicesSubscription.unsubscribe();
  }

  loadServices(): void {
    this.servicesSubscription = this.servicesService.services$.subscribe(services => {
      this.services = services;
    });
  }

  onEdit(service: Service): void {
    this.selectedService = service;
    this.edit = true;
    this.create = false;
  }

  onCreate(): void {
    this.selectedService = null;
    this.create = true;
    this.edit = false;
  }

  onDelete(service: Service): void {
    this.servicesService.deleteService(service.id!).subscribe(() => {
      this.loadServices();
    });
  }

  onSave(service: Service): void {
    if (service.id) {
      this.servicesService.updateService(service).subscribe(() => {
        this.loadServices();
        this.edit = false;
      });
    } else {
      this.servicesService.createService(service).subscribe(() => {
        this.loadServices();
        this.create = false;
      });
    }
  }

  onCancel(): void {
    this.create = false;
    this.edit = false;
  }
}
