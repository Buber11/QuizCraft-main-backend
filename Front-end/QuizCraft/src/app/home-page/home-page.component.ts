import {Component, NgModule} from '@angular/core';
import { NavbarComponent} from '../navbar/navbar.component';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [
    NavbarComponent
  ],
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss']
})
export class HomePageComponent { }
