import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RequestGroup, ServiceRequest } from '../app.models';

@Injectable({
  providedIn: 'root'
})
export class ClientServicesService {
  private apiUrl_requests = 'http://localhost:8080/requests';

  constructor(private http: HttpClient) {}

  getGroupedServiceRequests(): Observable<RequestGroup> {
    return this.http.get<RequestGroup>(this.apiUrl_requests + '/grouped');
  }

  finalizeRequest(requestId: string, rating: number, comment: string): Observable<ServiceRequest> {
    const payload = { rating, comment };
    return this.http.patch<ServiceRequest>(`${this.apiUrl_requests}/${requestId}/finalize`, payload);
  }
}
