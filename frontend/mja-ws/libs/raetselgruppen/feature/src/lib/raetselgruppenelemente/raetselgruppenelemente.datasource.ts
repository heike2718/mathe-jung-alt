import { DataSource } from '@angular/cdk/collections';
import { inject, Injectable } from '@angular/core';
import { RaetselgruppenFacade } from '@mja-ws/raetselgruppen/api';
import { Raetselgruppenelement } from "@mja-ws/raetselgruppen/model";
import { Observable } from 'rxjs';

@Injectable({providedIn: 'root'})
export class RaetselgruppenelementeDataSource extends DataSource<Raetselgruppenelement> {

    #raetselgruppenFacade = inject(RaetselgruppenFacade);

    /**
   * Connect this data source to the table. The table will only update when
   * the returned stream emits new items.
   * @returns A stream of the items to be rendered.
   */
  connect(): Observable<Raetselgruppenelement[]> {
    return this.#raetselgruppenFacade.raetselgruppenelemente$;
  }  

  /**
   *  Called when the table is being destroyed. Use this function, to clean up
   * any open connections or free any held resources that were set up during connect.
   */
  disconnect(): void { 
    // bleibt leer
  }
}