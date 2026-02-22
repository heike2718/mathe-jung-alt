import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { inject, Injectable } from '@angular/core';
import { Raetsel } from '@rbk-ws/raetsel/model';
import { Observable } from 'rxjs';
import { RaetselFacade } from './raetsel.facade';
import { swallowEmptyArgument } from '@rbk-ws/shared/util';

@Injectable({ providedIn: 'root' })
export class RaetselDataSource implements DataSource<Raetsel> {
  #raetselFacade = inject(RaetselFacade);

  connect(_collectionViewer: CollectionViewer): Observable<readonly Raetsel[]> {
    swallowEmptyArgument(_collectionViewer, false);
    return this.#raetselFacade.page$;
  }

  disconnect(_collectionViewer: CollectionViewer): void {
    // hängt am Store muss also nicht finalized werden?
    swallowEmptyArgument(_collectionViewer, false);
  }
}
