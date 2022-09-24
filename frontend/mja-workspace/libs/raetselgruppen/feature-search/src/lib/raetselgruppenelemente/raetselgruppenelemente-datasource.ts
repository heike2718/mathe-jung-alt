import { DataSource } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { map } from 'rxjs/operators';
import { Observable, of as observableOf, merge } from 'rxjs';
import { Raetselgruppenelement, RaetselgruppenFacade } from '@mja-workspace/raetselgruppen/domain';

// TODO: Replace this with your own data model type
const EXAMPLE_DATA: Raetselgruppenelement[] = [
  { id: '1', nummer: 'A-1', name: 'Treppenlift 20', punkte: 300, raetselSchluessel: '00001' },
  { id: '2', nummer: 'A-2', name: 'Treppenlift 2', punkte: 300, raetselSchluessel: '00002' },
  { id: '3', nummer: 'A-3', name: 'Treppenlift 23', punkte: 300, raetselSchluessel: '00003' },
  { id: '4', nummer: 'A-4', name: 'Treppenlift 14', punkte: 300, raetselSchluessel: '00004' },
  { id: '5', nummer: 'B-1', name: 'Treppenlift 5', punkte: 400, raetselSchluessel: '00005' },
  { id: '6', nummer: 'B-2', name: 'Treppenlift 6', punkte: 400, raetselSchluessel: '00006' },
  { id: '7', nummer: 'B-3', name: 'Treppenlift 7', punkte: 400, raetselSchluessel: '00007' },
  { id: '8', nummer: 'B-4', name: 'Treppenlift 8', punkte: 400, raetselSchluessel: '00008' },
  { id: '9', nummer: 'C-1', name: 'Treppenlift 9', punkte: 500, raetselSchluessel: '00009' },
  { id: '10', nummer: 'C-2', name: 'Treppenlift 10', punkte: 500, raetselSchluessel: '00010' },
  { id: '11', nummer: 'C-3', name: 'Treppenlift 21', punkte: 500, raetselSchluessel: '00011' },
  { id: '12', nummer: 'C-4', name: 'Treppenlift 11', punkte: 500, raetselSchluessel: '00012' },
];

/**
 * Data source for the Raetselgruppenelemente view. This class should
 * encapsulate all logic for fetching and manipulating the displayed data
 * (including sorting, pagination, and filtering).
 */
export class RaetselgruppenelementeDataSource extends DataSource<Raetselgruppenelement> {
  data: Raetselgruppenelement[] = EXAMPLE_DATA;
  paginator: MatPaginator | undefined;
  sort: MatSort | undefined;

  constructor(private raetselgruppenFacade: RaetselgruppenFacade) {
    super();
  }

  /**
   * Connect this data source to the table. The table will only update when
   * the returned stream emits new items.
   * @returns A stream of the items to be rendered.
   */
  connect(): Observable<Raetselgruppenelement[]> {
    if (this.paginator && this.sort) {
      // Combine everything that affects the rendered data into one update
      // stream for the data-table to consume.
      return merge(this.raetselgruppenFacade.selectedGruppe$, this.paginator.page, this.sort.sortChange)
        .pipe(
          map(() => {
            return this.getPagedDataMocked(this.getSortedDataMocked([...this.data]));
          }));
    } else {
      throw Error('Please set the paginator and sort on the data source before connecting.');
    }
  }

  /**
   *  Called when the table is being destroyed. Use this function, to clean up
   * any open connections or free any held resources that were set up during connect.
   */
  disconnect(): void { }

  /**
   * Paginate the data (client-side). If you're using server-side pagination,
   * this would be replaced by requesting the appropriate data from the server.
   */
  private getPagedDataMocked(data: Raetselgruppenelement[]): Raetselgruppenelement[] {
    if (this.paginator) {
      const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
      return data.splice(startIndex, this.paginator.pageSize);
    } else {
      return data;
    }
  }

  /**
   * Sort the data (client-side). If you're using server-side sorting,
   * this would be replaced by requesting the appropriate data from the server.
   */
  private getSortedDataMocked(data: Raetselgruppenelement[]): Raetselgruppenelement[] {
    if (!this.sort || !this.sort.active || this.sort.direction === '') {
      return data;
    }

    return data.sort((a, b) => {
      const isAsc = this.sort?.direction === 'asc';
      switch (this.sort?.active) {
        case 'schluessel': return compare(a.raetselSchluessel, b.raetselSchluessel, isAsc);
        case 'nummer': return compare(a.nummer, b.nummer, isAsc);
        case 'name': return compare(a.name, b.name, isAsc);
        case 'punkte': return compare(a.punkte, b.punkte, isAsc);
        default: return 0;
      }
    });
  }
}

/** Simple sort comparator for example ID/Name columns (for client-side sorting). */
function compare(a: string | number, b: string | number, isAsc: boolean): number {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
