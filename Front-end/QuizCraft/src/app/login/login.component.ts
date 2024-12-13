import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from '../navbar/navbar.component';
import {AuthService} from '../auth/auth-service.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, NavbarComponent],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    if (!this.username || !this.password) {
      this.errorMessage = 'Both fields are required';
      return;
    }

    this.authService.login({ username: this.username, password: this.password }).subscribe(
      success => {
        if (success) {
          this.router.navigate(['/dashboard']);
        } else {
          this.errorMessage = 'Invalid credentials';
        }
      },
      error => {
        this.errorMessage = 'An error occurred during login';
      }
    );
  }
}

