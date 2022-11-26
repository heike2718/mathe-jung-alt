import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, concatMap, finalize, Observable, of, tap } from 'rxjs';

@Injectable({
    providedIn: 'root'
  })
export class LoadingIndicatorFacade {

  #loadingSubject = new BehaviorSubject<boolean>(false);

    loading$: Observable<boolean> = this.#loadingSubject.asObservable();

    showLoaderUntilCompleted<T>(obs$: Observable<T>): Observable<T> {

        // of(.) immediately emmits ist value
        // concatMap triggers the obs$ to emmit its values

        return of(null).pipe(
            tap(() => this.loadingOn()),
            concatMap(() => obs$),
            finalize(() => this.loadingOff())
        );
    }

    private loadingOn(): void {
        this.#loadingSubject.next(true);
    }


    private loadingOff(): void {
        this.#loadingSubject.next(false);
    }

}