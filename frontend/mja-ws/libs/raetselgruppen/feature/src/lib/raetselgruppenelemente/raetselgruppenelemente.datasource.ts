import { DataSource } from '@angular/cdk/collections';
import { inject, Injectable } from '@angular/core';
import { RaetselgruppenFacade } from '@mja-ws/raetselgruppen/api';
import { Aufgabensammlungselement } from "@mja-ws/raetselgruppen/model";
import { Observable } from 'rxjs';

@Injectable({providedIn: 'root'})
export class AufgabensammlungselementeDataSource extends DataSource<Aufgabensammlungselement> {

    #raetselgruppenFacade = inject(RaetselgruppenFacade);

    /**
   * Connect this data source to the table. The table will only update when
   * the returned stream emits new items.
   * @returns A stream of the items to be rendered.
   */
  connect(): Observable<Aufgabensammlungselement[]> {
    return this.#raetselgruppenFacade.aufgabensammlungselemente$;
  }  

  /**
   *  Called when the table is being destroyed. Use this function, to clean up
   * any open connections or free any held resources that were set up during connect.
   */
  disconnect(): void { 
    // bleibt leer
  }
}