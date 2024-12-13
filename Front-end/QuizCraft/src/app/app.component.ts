import { Component } from '@angular/core';
import { RouterOutlet, RouterModule } from '@angular/router';
import { HomePageComponent } from './home-page/home-page.component';
import { LoginComponent } from './login/login.component';
import { NavbarComponent } from './navbar/navbar.component';
import { DashboardComponent} from './dashboard/dashboard.component';
import { routes } from './app.routes';
import {AuthService} from './auth/auth-service.service';
import {HttpClientModule} from '@angular/common/http';

@Component
({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HomePageComponent,
    LoginComponent,
    RouterModule,
    NavbarComponent,
    DashboardComponent,
    HttpClientModule
  ],
  providers: [AuthService],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'QuizCraft';
}

RouterModule.forRoot(routes);
