import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { Message } from "./message";


@Injectable({ providedIn: 'root' })
export class MessageService {

    #messageSubject$ = new BehaviorSubject<Message | undefined>(undefined);

    public message$: Observable<Message | undefined> = this.#messageSubject$.asObservable();

    constructor() { }

    public info(text: string) {

        this.add({text: text, type: 'INFO'});
    }

    public warn(text: string) {

        this.add({text: text, type: 'WARN'});
    }

    public error(text: string) {

        this.add({text: text, type: 'ERROR'});
    }

    public clear(): void {

        this.#messageSubject$.next(undefined);
    }

    private add(message: Message){
        this.#messageSubject$.next(message);
    }


}