import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RaetselFacade } from '@mja-ws/raetsel/api';

@Component({
  selector: 'mja-raetsel-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './raetsel-details.component.html',
  styleUrls: ['./raetsel-details.component.scss'],
})
export class RaetselDetailsComponent implements OnInit {
  
  public raetselFacade = inject(RaetselFacade);

  ngOnInit(): void {}
}
