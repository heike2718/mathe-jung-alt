import { CollectionViewer, DataSource } from "@angular/cdk/collections";
import { Injectable, inject } from "@angular/core";
import { MediensucheTrefferItem } from "@rbk-ws/medien/model";
import { MedienFacade } from "./medien.facade";
import { Observable } from "rxjs";
import { swallowEmptyArgument } from "@rbk-ws/shared/util";


@Injectable({
   providedIn: 'root'
})
export class MedienDataSource implements DataSource<MediensucheTrefferItem> {

    #medienFacade = inject(MedienFacade);

    connect(_collectionViewer: CollectionViewer): Observable<readonly MediensucheTrefferItem[]> {
        swallowEmptyArgument(_collectionViewer, false);
        return this.#medienFacade.page$;
     }
  
     disconnect(_collectionViewer: CollectionViewer): void {
        // hängt am Store muss also nicht finalized werden?
        swallowEmptyArgument(_collectionViewer, false);
     }
}