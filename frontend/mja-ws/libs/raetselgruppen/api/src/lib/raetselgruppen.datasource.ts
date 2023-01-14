import { CollectionViewer, DataSource } from "@angular/cdk/collections";
import { inject } from "@angular/core";
import { RaetselgruppenTrefferItem } from "@mja-ws/raetselgruppen/model";
import { Observable } from "rxjs";
import { RaetselgruppenFacade } from "./raetselgruppen.facade";


export class RaetselgruppenDatasource implements DataSource<RaetselgruppenTrefferItem> {


    #raetselgruppenFacade = inject(RaetselgruppenFacade);

    connect(_collectionViewer: CollectionViewer): Observable<readonly RaetselgruppenTrefferItem[]> {
        return this.#raetselgruppenFacade.page$;
     }
 
     disconnect(_collectionViewer: CollectionViewer): void {
        // hängt am Store muss also nicht finalized werden?
     }

}