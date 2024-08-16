import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Pyme, RequestGroup, ServiceRequest } from '../app.models';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PymeServicesService {
  private apiUrl = 'http://localhost:8080/profile';
  private apiUrlRequests = 'http://localhost:8080/requests';
  private pymeSubject = new BehaviorSubject<Pyme | null>(null);
  pyme$ = this.pymeSubject.asObservable();
  
  constructor(private http: HttpClient) {
    this.loadPyme();
  }

  // Load the Pyme profile data
  public loadPyme(): void {
    this.http.get<Pyme>(this.apiUrl).subscribe(pyme => this.pymeSubject.next(pyme));
  }
  
  // Fetch grouped service requests for the Pyme user
  getGroupedServiceRequestsForPyme(): Observable<RequestGroup> {
    return this.http.get<RequestGroup>(`${this.apiUrlRequests}/pyme/requests`);
  }

  // Cancel a specific request
  cancelRequest(requestId: string): Observable<ServiceRequest> {
    return this.http.patch<ServiceRequest>(`${this.apiUrlRequests}/${requestId}/cancel`, {});
  }

  // Update a specific request to 'On Process' status
  updateRequestToOnProcess(requestId: string): Observable<ServiceRequest> {
    return this.http.patch<ServiceRequest>(`${this.apiUrlRequests}/${requestId}/on-process`, {});
  }

  // Update a specific request to 'Complete' status
  updateRequestToComplete(requestId: string): Observable<ServiceRequest> {
    return this.http.patch<ServiceRequest>(`${this.apiUrlRequests}/${requestId}/complete`, {});
  }
}
