import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NavbarComponent} from '../navbar/navbar.component';
import {RegisterService} from './service/register-service.service';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    NavbarComponent,
    CommonModule
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  username: string = '';
  password: string = '';
  isSubmitting: boolean = false;
  errorMessage: string = '';

  constructor(private registerService: RegisterService) {}

  onSubmit() {
    if (this.username && this.password) {
      this.isSubmitting = true;
      const formData = {
        username: this.username,
        password: this.password,
      };

      this.registerService.register(formData).subscribe(
        response => {
          console.log('Successfully registered!', response);
          this.isSubmitting = false;
        },
        error => {
          console.error('Error during registration:', error);
          this.errorMessage = 'An error occurred during registration. Please try again.';
          this.isSubmitting = false;
        }
      );
    } else {
      this.errorMessage = 'All fields are required!';
    }
  }
}

