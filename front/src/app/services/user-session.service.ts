import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Client, Pyme } from '../app.models';

interface UserResponse {
  token: string;
  data: Pyme | Client;
  tokenBlacklisted: boolean;
  userType?: 'client' | 'pyme' | undefined | string;
}

@Injectable({
  providedIn: 'root'
})
export class UserSessionService {
  private currentUserSubject: BehaviorSubject<UserResponse | null>;
  public currentUser: Observable<UserResponse | null>;

  private apiBaseUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient, private router: Router) {
    const storedUser = localStorage.getItem('currentUser');
    this.currentUserSubject = new BehaviorSubject<UserResponse | null>(storedUser ? JSON.parse(storedUser) : null);
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): UserResponse | null {
    return this.currentUserSubject.value;
  }

  login(username: string, password: string): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${this.apiBaseUrl}/authenticate`, { username, password }).pipe(
      map(response => {
        const userType = response.data.roles.includes('PYME') ? 'pyme' : 'client';
        const user = {
          ...response,
          userType: userType
        };
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
        return user;
      })
    );
  }

  registerClient(client: any): Observable<any> {
    return this.http.post<any>(`${this.apiBaseUrl}/register/client`, client);
  }

  registerPyme(pyme: any): Observable<any> {
    return this.http.post<any>(`${this.apiBaseUrl}/register/pyme`, pyme);
  }

  logout(): Observable<any> {
    return this.http.post<any>(`${this.apiBaseUrl}/logout`, {}).pipe(
      map(() => {
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
        this.router.navigate(['/login']);
      })
    );
  }

  isClient(): boolean {
    return this.currentUserValue?.userType === 'client';
  }

  isPyme(): boolean {
    return this.currentUserValue?.userType === 'pyme';
  }

  isLoggedIn(): boolean {
    return !!this.currentUserValue;
  }
}
