import { Component, OnInit } from '@angular/core';
import { PymeDashboardComponent } from '../pyme-dashboard/pyme-dashboard.component';
import { UserSessionService } from '../services/user-session.service';
import { Subscription } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [PymeDashboardComponent, MatIconModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.less'
})
export class ProfileComponent implements OnInit {
  user: any;
  userSubscription = new Subscription();

  constructor(private userServices: UserSessionService) { }

  ngOnInit(): void {
    this.userSubscription = this.userServices.currentUser.subscribe(user => {
      debugger;
      if (user) {
        this.user = user.data;
      }
    });
  }

}
