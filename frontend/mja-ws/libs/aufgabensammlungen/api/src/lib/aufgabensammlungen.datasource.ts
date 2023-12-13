import { CollectionViewer, DataSource } from "@angular/cdk/collections";
import { Injectable, inject } from "@angular/core";
import { AufgabensammlungTrefferItem } from "@mja-ws/aufgabensammlungen/model";
import { Observable } from "rxjs";
import { AufgabensammlungenFacade } from "./aufgabensammlungen.facade";
import { swallowEmptyArgument } from "@mja-ws/shared/util";

@Injectable({ providedIn: 'root' })
export class AufgabensammlungenDataSource implements DataSource<AufgabensammlungTrefferItem> {


   #aufgabensammlungenFacade = inject(AufgabensammlungenFacade);

   connect(_collectionViewer: CollectionViewer): Observable<readonly AufgabensammlungTrefferItem[]> {
      swallowEmptyArgument(_collectionViewer, false);
      return this.#aufgabensammlungenFacade.page$;
   }

   disconnect(_collectionViewer: CollectionViewer): void {
      // hängt am Store muss also nicht finalized werden?
      swallowEmptyArgument(_collectionViewer, false);
   }

}