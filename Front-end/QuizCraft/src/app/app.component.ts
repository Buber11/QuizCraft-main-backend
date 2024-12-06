import { Component } from '@angular/core';
import { RouterOutlet, RouterModule } from '@angular/router';
import { HomePageComponent } from './home-page/home-page.component';
import { LoginComponent } from './login/login.component';
import { NavbarComponent } from './navbar/navbar.component';
import { DashboardComponent} from './dashboard/dashboard.component';
import { routes } from './app.routes';

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
    DashboardComponent
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'QuizCraft';
}

RouterModule.forRoot(routes);
