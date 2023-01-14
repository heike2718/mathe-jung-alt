import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'mja-raetselgruppen-search',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './raetselgruppen-search.component.html',
  styleUrls: ['./raetselgruppen-search.component.scss'],
})
export class RaetselgruppenSearchComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
