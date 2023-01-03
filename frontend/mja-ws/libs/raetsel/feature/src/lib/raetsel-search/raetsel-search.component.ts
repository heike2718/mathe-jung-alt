import { Component, inject, OnInit } from '@angular/core';
import { AsyncPipe, CommonModule, NgFor, NgIf } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { RaetselFacade } from '@mja-ws/raetsel/api';
import { RaetselSuchfilter } from '@mja-ws/raetsel/model';
import { initialPageDefinition } from '@mja-ws/core/model';

@Component({
  selector: 'mja-raetsel-search',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule],
  templateUrl: './raetsel-search.component.html',
  styleUrls: ['./raetsel-search.component.scss']
})
export class RaetselSearchComponent implements OnInit {


  public raetselFacade = inject(RaetselFacade);

  ngOnInit(): void { }

  loadRaetsel(): void {


    const suchfilter: RaetselSuchfilter = {
      deskriptoren: [],
      suchstring: 'zählen'
    };

    this.raetselFacade.triggerSearch(suchfilter, initialPageDefinition);

  }
}
