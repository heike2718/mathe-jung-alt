import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Raetsel, SearchFacade } from '@mathe-jung-alt-workspace/raetsel/domain';
import { Subscription } from 'rxjs';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'raetsel-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
})
export class SearchComponent implements OnInit, OnDestroy {

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  raetselList$ = this.searchFacade.raetselList$;

  displayedRaetselColumns: string[] = ['id', 'schluessel', 'name'];
  dataSourceRaetsel!: MatTableDataSource<Raetsel>;

  private raetselListeSubscription: Subscription = new Subscription();

  constructor(private searchFacade: SearchFacade) { }

  ngOnInit() {
    this.load();
    
    this.raetselListeSubscription = this.searchFacade.raetselList$.subscribe(

      raetsel => {
        this.dataSourceRaetsel = new MatTableDataSource<Raetsel>(raetsel);
        this.dataSourceRaetsel.sort = this.sort;
        this.dataSourceRaetsel.paginator = this.paginator;
      }
    );
  }

  ngOnDestroy(): void {
    this.raetselListeSubscription.unsubscribe();
  }

  load(): void {
    this.searchFacade.load();
  }
}
