import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { UserSessionService } from '../../services/user-session.service';
import { BehaviorSubject, Subscription } from 'rxjs';
import { Client, Pyme } from '../../app.models';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [RouterLinkActive, RouterLink, CommonModule],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.less'
})
export class NavBarComponent implements OnInit {
  user: any;
  userSubscription = new Subscription();
  userType: any;


  constructor(private readonly authService: UserSessionService) { }

  ngOnInit(): void {
    this.userSubscription = this.authService.currentUser.subscribe(user => {
      if (user) {
        this.user = user.data;
        this.userType = user.userType;
      }
    });
  }

  logout(): void {
    this.authService.logout().subscribe(
      () => {
        console.log('Logged out successfully');
        this.user = null;
      },
      (error) => {
        console.error('Logout failed', error);
      }
    );
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
  }
}
