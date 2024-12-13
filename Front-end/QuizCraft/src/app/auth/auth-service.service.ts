import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/v1/auth';
  private loggedIn = false;

  constructor(private http: HttpClient) {}

  login(credentials: { username: string; password: string }): Observable<boolean> {
    return this.http.post(`${this.apiUrl}/login`, credentials, { withCredentials: true }).pipe(
      tap(() => {
        this.loggedIn = true;
      }),
      map(() => true),
      catchError(() => {
        this.loggedIn = false;
        return of(false);
      })
    );
  }

  logout(): Observable<boolean> {
    this.loggedIn = false;
    return of(true);
  }

  isLoggedIn(): Observable<boolean> {
    if (this.loggedIn) {
      return this.http.get<{ loggedIn: boolean }>(`${this.apiUrl}/check-token`, { withCredentials: true }).pipe(
        map(response => response.loggedIn),
        catchError(() => {
          this.loggedIn = false;
          return of(false);
        })
      );
    }
    return of(false);
  }
}
