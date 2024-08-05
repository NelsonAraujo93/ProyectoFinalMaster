import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ClientDashboardComponent } from './client-dashboard/client-dashboard.component';
import { PymeDashboardComponent } from './pyme-dashboard/pyme-dashboard.component';
import { ClientGuard } from './guards/client.guard';
import { PymeGuard } from './guards/pyme.guard';

export const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'client-dashboard', component: ClientDashboardComponent, canActivate: [ClientGuard] },
  { path: 'pyme-dashboard', component: PymeDashboardComponent, canActivate: [PymeGuard] },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: '' }
];
