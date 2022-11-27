import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { Message } from "./messaging.model";


@Injectable({ providedIn: 'root' })
export class MessageService {

    #messageSubject$ = new BehaviorSubject<Message | undefined>(undefined);

    public message$: Observable<Message | undefined> = this.#messageSubject$.asObservable();

    public info(text: string) {

        this.add({ message: text, level: 'INFO' });
    }

    public warn(text: string) {

        this.add({ message: text, level: 'WARN' });
    }

    public error(text: string) {

        this.add({ message: text, level: 'ERROR' });
    }

    public message(message: Message): void {
        this.add(message);
    }

    public clear(): void {

        this.#messageSubject$.next(undefined);
    }

    private add(message: Message) {
        this.#messageSubject$.next(message);
    }
}