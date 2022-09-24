import { DataSource } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { filter, map, tap } from 'rxjs/operators';
import { Observable, of as observableOf, merge, combineLatest } from 'rxjs';
import { Raetselgruppenelement, RaetselgruppenFacade } from '@mja-workspace/raetselgruppen/domain';
import { Details } from '@mui/icons-material';
import { EventEmitter } from '@angular/core';

/**
 * Data source for the Raetselgruppenelemente view. This class should
 * encapsulate all logic for fetching and manipulating the displayed data
 * (including sorting, pagination, and filtering).
 */
export class RaetselgruppenelementeDataSource extends DataSource<Raetselgruppenelement> {

  paginator: MatPaginator | undefined;
  sort: MatSort | undefined;

  constructor(public data: Raetselgruppenelement[]) {
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
      return merge(observableOf(this.data), this.paginator.page, this.sort.sortChange)
        .pipe(
          map(() => {
            return this.#getPagedData(this.#getSortedData([...this.data]));
          }))
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
  #getPagedData(data: Raetselgruppenelement[]): Raetselgruppenelement[] {
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
  #getSortedData(data: Raetselgruppenelement[]): Raetselgruppenelement[] {
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
