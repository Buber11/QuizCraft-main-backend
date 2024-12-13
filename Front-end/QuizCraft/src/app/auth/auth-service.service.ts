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

  constructor(private http: HttpClient) {
    const storedLoggedIn = sessionStorage.getItem('loggedIn');
    this.loggedIn = storedLoggedIn ? JSON.parse(storedLoggedIn) : false;
  }

  login(credentials: { username: string; password: string }): Observable<boolean> {
    return this.http.post(`${this.apiUrl}/login`, credentials, { withCredentials: true }).pipe(
      tap(() => {
        this.loggedIn = true;
        sessionStorage.setItem('loggedIn', JSON.stringify(this.loggedIn));
        console.log(this.loggedIn);
      }),
      map(() => true),
      catchError(() => {
        this.loggedIn = false;
        sessionStorage.setItem('loggedIn', JSON.stringify(this.loggedIn));
        console.log(this.loggedIn);
        return of(false);
      })
    );
  }

  logout(): Observable<boolean> {
    this.loggedIn = false;
    sessionStorage.setItem('loggedIn', JSON.stringify(this.loggedIn));
    return of(true);
  }

  isLoggedIn(): Observable<boolean> {
    console.log(this.loggedIn);
    if (this.loggedIn) {
      return this.http.get<{ view: boolean }>(`${this.apiUrl}/token-validation`, { withCredentials: true }).pipe(
        map(response => response.view),
        catchError(() => {
          this.loggedIn = false;
          sessionStorage.setItem('loggedIn', JSON.stringify(this.loggedIn));
          return of(false);
        })
      );
    }
    return of(false);
  }
}

