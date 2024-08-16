import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ClientDashboardComponent } from './client/client-dashboard/client-dashboard.component';
import { PymeDashboardComponent } from './pyme/pyme-dashboard/pyme-dashboard.component';
import { ClientGuard } from './guards/client.guard';
import { PymeGuard } from './guards/pyme.guard';
import { PymeDetailComponent } from './pyme/pyme-detail/pyme-detail.component';
import { ServiceDetailComponent } from './service/service-detail/service-detail.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { ContactComponent } from './contact/contact.component';
import { StoreComponent } from './store/store.component';

export const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'pymes/:id', component: PymeDetailComponent },
  { path: 'services/:id', component: ServiceDetailComponent },
  { path: 'client-dashboard', component: ClientDashboardComponent, canActivate: [ClientGuard] },
  { path: 'pyme-dashboard', component: PymeDashboardComponent, canActivate: [PymeGuard] },
  { path: 'about', component: AboutUsComponent },
  { path: 'store', component: StoreComponent },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: '' }
];
