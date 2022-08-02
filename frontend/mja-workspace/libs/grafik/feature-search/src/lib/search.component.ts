import { Component, OnInit } from '@angular/core';
import { GrafikFacade } from '@mja-workspace/grafik/domain';

@Component({
  selector: 'grafik-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
})
export class SearchComponent implements OnInit {
  constructor(private grafikFacade: GrafikFacade) { }

  ngOnInit() { }
}
