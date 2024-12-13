import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HomePageComponent } from './home-page/home-page.component';
import { LoginComponent } from './login/login.component';
import { NavbarComponent } from './navbar/navbar.component';
import { DashboardComponent} from './dashboard/dashboard.component';
import { routes } from './app.routes';
import {AuthService} from './auth/auth-service.service';
import {AuthGuard} from './auth/auth.guard';

@Component
({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HomePageComponent,
    LoginComponent,
    NavbarComponent,
    DashboardComponent,
  ],
  providers: [AuthService,AuthGuard],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'QuizCraft';
}

