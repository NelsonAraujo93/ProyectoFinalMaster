import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';
import { Pyme, Service } from '../app.models';
import { PublicServicesService } from '../services/public-services.service';
import { RouterLink } from '@angular/router';
import { PymeCardComponent } from '../layout/pyme-card/pyme-card.component';
import { ServiceCardComponent } from '../layout/service-card/service-card.component';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, PymeCardComponent, ServiceCardComponent, MatProgressSpinnerModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.less']
})
export class HomeComponent implements OnInit, OnDestroy {
  latestPymes: Pyme[] = [];
  latestServices: Service[] = [];
  loading = false;

  private subscriptions: Subscription = new Subscription();

  constructor(private publicServicesService: PublicServicesService) {}

  ngOnInit(): void {
    this.subscriptions.add(
      this.publicServicesService.lastPymes$.subscribe(latestPymes => {
        this.latestPymes = latestPymes;
      })
    );

    this.subscriptions.add(
      this.publicServicesService.lastServices$.subscribe(latestServices => {
        this.latestServices = latestServices;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
