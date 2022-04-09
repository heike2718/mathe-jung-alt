import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { RaetselDataService } from './raetsel.data.service';
import { Raetsel } from '../entities/raetsel';

export class RaetselDataSource implements DataSource<Raetsel> {

    #raetselSubject = new BehaviorSubject<Raetsel[]>([]);
    #loadingSubject = new BehaviorSubject<boolean>(false);

    public loading$ = this.#loadingSubject.asObservable();

    constructor(private raestelService: RaetselDataService) { }


    connect(_collectionViewer: CollectionViewer): Observable<readonly Raetsel[]> {
        return this.#raetselSubject.asObservable();
    }

    disconnect(_collectionViewer: CollectionViewer): void {
        this.#raetselSubject.complete();
        this.#loadingSubject.complete();
    }

    loadRaetsel(filter = '', sortDirection = 'asc', pageIndex = 0, pageSize = 3): void {

        this.#loadingSubject.next(true);

        this.raestelService.loadPage(filter, sortDirection, pageIndex, pageSize).pipe(
            catchError(() => of([])),
            finalize(() => this.#loadingSubject.next(false))
        ).subscribe(
            raetsel => this.#raetselSubject.next(raetsel)
        );
    }
}