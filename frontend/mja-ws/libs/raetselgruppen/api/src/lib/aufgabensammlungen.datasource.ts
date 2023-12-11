import { CollectionViewer, DataSource } from "@angular/cdk/collections";
import { inject, Injectable } from "@angular/core";
import { AufgabensammlungTrefferItem } from "@mja-ws/raetselgruppen/model";
import { Observable } from "rxjs";
import { RaetselgruppenFacade } from "./aufgabensammlungen.facade";

@Injectable({providedIn: 'root'})
export class RaetselgruppenDataSource implements DataSource<AufgabensammlungTrefferItem> {


    #raetselgruppenFacade = inject(RaetselgruppenFacade);

    connect(_collectionViewer: CollectionViewer): Observable<readonly AufgabensammlungTrefferItem[]> {
        return this.#raetselgruppenFacade.page$;
     }
 
     disconnect(_collectionViewer: CollectionViewer): void {
        // hängt am Store muss also nicht finalized werden?
     }

}