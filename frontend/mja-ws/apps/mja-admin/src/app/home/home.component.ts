import { Component } from '@angular/core';
import { NgIf } from '@angular/common';

@Component({
  selector: 'mja-admin-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: true
})
export class HomeComponent {

  version = "1.3.0";
}
