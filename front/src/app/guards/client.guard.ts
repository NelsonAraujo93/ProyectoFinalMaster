import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserSessionService } from '../services/user-session.service';

@Injectable({
  providedIn: 'root'
})
export class ClientGuard implements CanActivate {
  constructor(private userSessionService: UserSessionService, private router: Router) { }

  canActivate(): boolean {
    if (this.userSessionService.isClient()) {
      return true;
    }
    this.router.navigate(['/login']);
    return false;
  }
}
