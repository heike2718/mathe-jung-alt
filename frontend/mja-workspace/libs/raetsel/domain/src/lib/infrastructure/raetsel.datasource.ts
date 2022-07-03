import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { Observable } from 'rxjs';
import { SearchFacade } from '../application/search.facade';
import { Raetsel } from '../entities/raetsel';


export class RaetselDataSource implements DataSource<Raetsel> {


    constructor(private raetselFacade: SearchFacade) { }

    connect(_collectionViewer: CollectionViewer): Observable<readonly Raetsel[]> {
       return this.raetselFacade.page$;
    }

    disconnect(_collectionViewer: CollectionViewer): void {
       // hängt am Store muss also nicht finalized werden?
    }

}
