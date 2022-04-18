import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { RaetselDataSource, RaetselFacade } from '@mathe-jung-alt-workspace/raetsel/domain';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { tap } from 'rxjs/operators';
import { merge, Subscription } from 'rxjs';
import { SuchfilterFacade } from '@mathe-jung-alt-workspace/shared/suchfilter/domain';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { Deskriptor } from '@mathe-jung-alt-workspace/deskriptoren/domain';

@Component({
  selector: 'mja-raetsel-search',
  templateUrl: './raetsel-search.component.html',
  styleUrls: ['./raetsel-search.component.scss'],
})
export class RaetselSearchComponent implements OnInit, AfterViewInit, OnDestroy {

  isAdmin$ = this.authFacade.isAdmin$;
  isSuchfilterReadyToGo$ = this.suchfilterFacade.isSuchfilterReadyToGo$;
  suchfilter$ = this.suchfilterFacade.suchfilter$;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: string[] = ['schluessel', 'name', 'deskriptoren'];
  dataSource!: RaetselDataSource;
  anzahlRaetsel: number = 0;

  private raetselSubscription: Subscription = new Subscription();

  constructor(public raetselFacade: RaetselFacade, private suchfilterFacade: SuchfilterFacade, private authFacade: AuthFacade) { }

  ngOnInit() {
    this.dataSource = new RaetselDataSource(this.raetselFacade);
    // this.raetselFacade.loadRaetsel();
    this.suchfilterFacade.changeSuchkontext('RAETSEL');

    this.raetselSubscription = this.raetselFacade.raetselList$.subscribe(
      liste => this.anzahlRaetsel = liste.length
    );
  }

  onDeskriptorenChanged($event: Deskriptor[]): void {

    if (this.paginator) {
      this.paginator.pageIndex = 0;
    }
    this.suchfilterFacade.changeDeskriptoren($event);
  }

  onInputChanged($event: string) {
    if ($event.trim().length > 0) {
      this.paginator.pageIndex = 0;
      this.suchfilterFacade.changeSuchtext($event);
    }
  }

  ngAfterViewInit(): void {

    // reset the paginator after sorting
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page).pipe(
      tap(() => this.loadRaetselPage())
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.raetselSubscription.unsubscribe();
  }

  onRowClicked(row: any): void {
    console.log('row clicked: ' + JSON.stringify(row));
  }

  private findRaetsel(value: string): void {
    this.suchfilterFacade.changeSuchtext(value);
    // this.raetselFacade.findRaetsel(value);
  }

  private loadRaetselPage(): void {
    this.raetselFacade.slicePage(this.sort.direction, this.paginator.pageIndex, this.paginator.pageSize);
  }
}
