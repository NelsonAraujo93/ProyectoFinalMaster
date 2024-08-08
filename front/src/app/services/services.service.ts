import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Service } from '../app.models';

@Injectable({
  providedIn: 'root'
})
export class ServicesService {
  private apiUrl = 'http://localhost:8080/services';
  private servicesSubject = new BehaviorSubject<Service[]>([]);
  services$ = this.servicesSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadAllServices();
  }

  private loadAllServices(): void {
    this.http.get<Service[]>(this.apiUrl).subscribe(services => this.servicesSubject.next(services));
  }

  getAllServices(): Observable<Service[]> {
    return this.services$;
  }

  getServiceById(id: number): Observable<Service> {
    return this.http.get<Service>(`${this.apiUrl}/${id}`);
  }

  createService(service: Service): Observable<Service> {
    return this.http.post<Service>(this.apiUrl, service).pipe(
      tap(() => this.loadAllServices())
    );
  }

  updateService(service: Service): Observable<Service> {
    return this.http.put<Service>(`${this.apiUrl}/${service.id}`, service).pipe(
      tap(() => this.loadAllServices())
    );
  }

  deleteService(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => this.loadAllServices())
    );
  }
}
