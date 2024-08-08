import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { PymeMenuComponent } from '../pyme-menu/pyme-menu.component';
import { PymeSideBarComponent } from '../pyme-side-bar/pyme-side-bar.component';
import { MatIconModule } from '@angular/material/icon';
import { ServicesService } from '../services/services.service';
import { Service } from '../app.models';
import { UserSessionService } from '../services/user-session.service';

@Component({
  selector: 'app-pyme-dashboard',
  standalone: true,
  imports: [PymeMenuComponent, PymeSideBarComponent, MatIconModule],
  templateUrl: './pyme-dashboard.component.html',
  styleUrls: ['./pyme-dashboard.component.less']
})
export class PymeDashboardComponent implements OnInit, OnDestroy {
  user: any;
  userSubscription = new Subscription();
  servicesSubscription = new Subscription();
  services: Service[] = [];

  constructor(private userServices: UserSessionService, private servicesService: ServicesService) { }

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
}
