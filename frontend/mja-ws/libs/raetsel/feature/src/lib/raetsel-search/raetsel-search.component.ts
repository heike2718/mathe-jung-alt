import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'mja-raetsel-search',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './raetsel-search.component.html',
  styleUrls: ['./raetsel-search.component.scss'],
})
export class RaetselSearchComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
