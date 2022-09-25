import { DataSource } from '@angular/cdk/collections';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { Observable } from 'rxjs';
import { Raetselgruppenelement, RaetselgruppenFacade } from '@mja-workspace/raetselgruppen/domain';

/**
 * Data source for the Raetselgruppenelemente view. This class should
 * encapsulate all logic for fetching and manipulating the displayed data
 * (including sorting, pagination, and filtering).
 */
export class RaetselgruppenelementeDataSource extends DataSource<Raetselgruppenelement> {

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
    return this.raetselgruppenFacade.raetselgruppenelemente$;
  }  

  /**
   *  Called when the table is being destroyed. Use this function, to clean up
   * any open connections or free any held resources that were set up during connect.
   */
  disconnect(): void { }
}