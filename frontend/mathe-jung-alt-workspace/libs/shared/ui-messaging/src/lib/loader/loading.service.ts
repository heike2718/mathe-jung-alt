import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";


@Injectable({
    providedIn: 'root'
})
export class LoadingService {

    // https://christianlydemann.com/four-ways-to-create-loading-spinners-in-an-angular-app/

    #loading$ = new BehaviorSubject(false);
    loading$ = this.#loading$.asObservable();

    constructor() { }

    start() {
        this.#loading$.next(true);
        // console.log('loading indicator started');

    }

    stop() {
        this.#loading$.next(false);
        // console.log('loading indicator stopped');
    }

}
