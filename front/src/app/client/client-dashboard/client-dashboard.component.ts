import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ClientSideBarComponent } from '../client-side-bar/client-side-bar.component';
import { ClientMenuComponent } from '../client-menu/client-menu.component';
import { Subscription } from 'rxjs';
import { Client } from '../../app.models';
import { UserSessionService } from '../../services/user-session.service';

@Component({
  selector: 'app-client-dashboard',
  standalone: true,
  imports: [ClientMenuComponent, ClientSideBarComponent, MatIconModule],
  templateUrl: './client-dashboard.component.html',
  styleUrl: './client-dashboard.component.less'
})
export class ClientDashboardComponent {
  user: any;
  userSubscription = new Subscription();
  pymeSubscription = new Subscription();
  servicesSubscription = new Subscription();
  pyme: Client | null = null;

  constructor(private userServices: UserSessionService,) { }

  ngOnInit(): void {
    this.userSubscription = this.userServices.currentUser.subscribe(user => {
      if (user) {
        this.user = user.data;
      }
    });
  }

  ngOnDestroy(): void {
    this.servicesSubscription.unsubscribe();
    this.userSubscription.unsubscribe();
    this.pymeSubscription.unsubscribe();
  }
}
