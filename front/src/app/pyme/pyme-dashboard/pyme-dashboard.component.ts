import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { PymeMenuComponent } from '../pyme-menu/pyme-menu.component';
import { PymeSideBarComponent } from '../pyme-side-bar/pyme-side-bar.component';
import { MatIconModule } from '@angular/material/icon';
import { Pyme, Service } from '../../app.models';
import { UserSessionService } from '../../services/user-session.service';
import { PymeServicesService } from '../../services/pyme-services.service';
import { ServicesService } from '../../services/services.service';

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
  pymeSubscription = new Subscription();
  servicesSubscription = new Subscription();
  pyme: Pyme | null = null;
  services: Service[] = [];

  constructor(private userServices: UserSessionService,
              private pymeServices: PymeServicesService,
              private servicesService: ServicesService) { }

  ngOnInit(): void {
    this.userSubscription = this.userServices.currentUser.subscribe(user => {
      if (user) {
        this.user = user.data;
        this.loadServices();
        this.loadPyme();
      }
    });
  }

  ngOnDestroy(): void {
    this.servicesSubscription.unsubscribe();
    this.userSubscription.unsubscribe();
    this.pymeSubscription.unsubscribe();
  }

  loadServices(): void {
    this.servicesService.loadAllServices();
    this.servicesSubscription = this.servicesService.services$.subscribe(services => {
      this.services = services;
    });
  }
  
  loadPyme(): void {
    this.pymeSubscription = this.pymeServices.pyme$.subscribe(pyme => {
      if (!pyme) {
        this.pymeServices.loadPyme();
      }
      this.pyme = pyme;
    });
  }
}
