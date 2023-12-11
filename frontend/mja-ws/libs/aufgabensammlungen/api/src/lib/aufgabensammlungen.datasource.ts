import { CollectionViewer, DataSource } from "@angular/cdk/collections";
import { inject, Injectable } from "@angular/core";
import { AufgabensammlungTrefferItem } from "@mja-ws/aufgabensammlungen/model";
import { Observable } from "rxjs";
import { AufgabensammlungenFacade } from "./aufgabensammlungen.facade";

@Injectable({providedIn: 'root'})
export class AufgabensammlungenDataSource implements DataSource<AufgabensammlungTrefferItem> {


    #aufgabensammlungenFacade = inject(AufgabensammlungenFacade);

    connect(_collectionViewer: CollectionViewer): Observable<readonly AufgabensammlungTrefferItem[]> {
        return this.#aufgabensammlungenFacade.page$;
     }
 
     disconnect(_collectionViewer: CollectionViewer): void {
        // hängt am Store muss also nicht finalized werden?
     }

}