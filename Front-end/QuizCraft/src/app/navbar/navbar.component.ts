import { Component } from '@angular/core';
import {AuthService} from '../auth/auth-service.service';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports:[CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  loggedIn: boolean = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    if (typeof window !== 'undefined' && window.sessionStorage) {
      try {
        const storedLoggedIn = sessionStorage.getItem('loggedIn');
        this.loggedIn = storedLoggedIn ? JSON.parse(storedLoggedIn) : false;
      } catch (error) {
        console.error('sessionStorage:', error);
        this.loggedIn = false;
      }
    } else {
      this.loggedIn = false;
    }
  }

  logout(): void {
    this.authService.logout().subscribe(() => {
      this.loggedIn = false;
    });
  }
}
