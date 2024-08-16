import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
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

  constructor(private http: HttpClient,
              private router: Router,
            ) {
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
    return this.http.post<any>(`${this.apiBaseUrl}/register/client`, client).pipe(
      map(response => {
        return response;
      }),
      catchError(err => {
        throw err;
      })
    );
  }

  registerPyme(pyme: Pyme): Observable<any> {
    return this.http.post<any>(`${this.apiBaseUrl}/register/pyme`, pyme).pipe(
      map(response => {
        console.log('API response:', response);
        return response;
      }),
      catchError(err => {
        console.error('API error:', err);
        throw err;
      })
    );
  }
  
  logout(): Observable<any> {
    const token = this.currentUserSubject.value?.token;
    if (!token) {
      return new Observable(observer => {
        observer.error('No token found');
        observer.complete();
      });
    }
  
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  
    return this.http.post(`${this.apiBaseUrl}/logout`, {}, { headers, responseType: 'text' }).pipe(
      map(() => {
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
        this.router.navigate(['/login']);
      }),
      catchError(error => {
        console.error('Logout error', error);
        return new Observable(observer => {
          observer.error('Logout failed');
          observer.complete();
        });
      })
    );
  }

  isClient(): boolean {
    return this.currentUserValue?.userType === 'client';
  }

  isPyme(): boolean {
    return this.currentUserValue?.userType === 'pyme';
  }

  validateToken(): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.apiBaseUrl}/validate-token`).pipe(
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

  isLoggedIn(): boolean {
    return !!this.currentUserValue;
  }
}
