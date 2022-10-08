import { DOCUMENT } from "@angular/common";
import { Inject, Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { Message } from "./message";


@Injectable({ providedIn: 'root' })
export class MessageService {

    #messageSubject$ = new BehaviorSubject<Message | undefined>(undefined);

    public message$: Observable<Message | undefined> = this.#messageSubject$.asObservable();

    constructor(@Inject(DOCUMENT) private document: Document) { }

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
        this.scrollToTop();
    }   

    private scrollToTop() {
        let document = this.document;
        (function smoothscroll() {
            var currentScroll = document.documentElement.scrollTop || document.body.scrollTop;
            // console.log('currentScroll=' + currentScroll);
            if (currentScroll > 0) {
                window.requestAnimationFrame(smoothscroll);
                window.scrollTo(0, currentScroll - (currentScroll / 8));
            }
        })();
    }
}