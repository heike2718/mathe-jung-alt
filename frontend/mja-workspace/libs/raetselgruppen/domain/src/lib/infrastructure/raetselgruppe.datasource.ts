import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { Observable, of } from 'rxjs';
import { RaetselgruppenFacade } from '../application/raetselgruppen.facade';
import { RaetselgruppensucheTreffer, RaetselgruppensucheTrefferItem } from '../entities/raetselgruppen';

export class RaetselgruppeDatasource implements DataSource<RaetselgruppensucheTrefferItem> {

    constructor(private raetselgruppenFacade: RaetselgruppenFacade) { }


    connect(_collectionViewer: CollectionViewer): Observable<readonly RaetselgruppensucheTrefferItem[]> {
        return this.raetselgruppenFacade.page$;
    }

    disconnect(_collectionViewer: CollectionViewer): void {
        // hängt am Store muss also nicht finalized werden?
    }

}

