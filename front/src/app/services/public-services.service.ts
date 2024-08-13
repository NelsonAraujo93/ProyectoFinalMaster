import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Pyme, Service, ServiceRequest } from '../app.models';  // Import the Request interface
import { UserSessionService } from './user-session.service';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root',
})
export class PublicServicesService {
  private apiUrl = 'http://localhost:8080/public';
  private apiUrl_requests = 'http://localhost:8080/requests';

  private pymeSubject = new BehaviorSubject<Pyme[]>([]);
  pyme$ = this.pymeSubject.asObservable();

  private servicesSubject = new BehaviorSubject<Service[]>([]);
  services$ = this.servicesSubject.asObservable();

  private lastPymesSubject = new BehaviorSubject<Pyme[]>([]);
  lastPymes$ = this.lastPymesSubject.asObservable();

  private lastServicesSubject = new BehaviorSubject<Service[]>([]);
  lastServices$ = this.lastServicesSubject.asObservable();

  private loadingSubject = new BehaviorSubject<boolean>(false);
  loading$ = this.loadingSubject.asObservable();

  constructor(
    private http: HttpClient,
    private userSessionService: UserSessionService,
    private toastr: ToastrService
  ) {
    this.loadAllData();
    this.loadingSubject.next(true);
  }

  public loadAllData(): void {
    this.loadLastPymes();
    this.loadLastServices();
    this.loadPymes();
    this.loadServices();
  }

  private loadPymes(): void {
    this.http.get<Pyme[]>(`${this.apiUrl}/pymes`).subscribe(pymes => this.pymeSubject.next(pymes));
  }

  private loadServices(): void {
    this.http.get<Service[]>(`${this.apiUrl}/services`).subscribe(services => this.servicesSubject.next(services));
  }

  private loadLastPymes(): void {
    this.http.get<Pyme[]>(`${this.apiUrl}/pymes/latest`).subscribe(pymes => this.lastPymesSubject.next(pymes));
  }

  private loadLastServices(): void {
    this.http.get<Service[]>(`${this.apiUrl}/services/latest`).subscribe(services => this.lastServicesSubject.next(services));
  }

  getPymeById(id: number): Observable<Pyme> {
    return this.http.get<Pyme>(`${this.apiUrl}/pymes/${id}`);
  }

  getServiceById(id: number): Observable<Service> {
    return this.http.get<Service>(`${this.apiUrl}/services/${id}`);
  }

  requestService(serviceId: number, details: string): Observable<ServiceRequest> {
    if (!serviceId) {
      throw new Error('Invalid service id');
    }

    if (this.userSessionService.isLoggedIn()) {
      const requestPayload = {
        serviceId,
        details,
      };
      return this.http.post<ServiceRequest>(this.apiUrl_requests, requestPayload);
    } else {
      this.toastr.error('You need to be logged in to request a service');
      throw new Error('User is not logged in');
    }
  }

  filterServices(filterValues: { minPrice?: number; maxPrice?: number; minRating?: number; name?: string }): Observable<Service[]> {
    const params = new HttpParams()
      .set('minPrice', filterValues.minPrice?.toString() || '')
      .set('maxPrice', filterValues.maxPrice?.toString() || '')
      .set('minRating', filterValues.minRating?.toString() || '')
      .set('name', filterValues.name || '');
  
    return this.http.get<Service[]>(`${this.apiUrl}/services/filter`, { params });
  }
  

  getAllServices(): Observable<Service[]> {
    return this.http.get<Service[]>(`${this.apiUrl}/services`);
  }
}
