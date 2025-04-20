import { DOCUMENT } from "@angular/common";
import { Inject, Injectable, signal } from "@angular/core";
import { Message } from "./messaging.model";


@Injectable({ providedIn: 'root' })
export class MessageService {

    #messageSignal = signal<Message | undefined>(undefined);

    constructor(@Inject(DOCUMENT) private document: Document) { }

    get message() {
        return this.#messageSignal;
    }

    public info(text: string) {
        this.#add({ message: text, level: 'INFO' });
        setTimeout(() => {
            this.clear();
        }, 3000); // Clear after 3 seconds
    }

    public warn(text: string) {

        this.#add({ message: text, level: 'WARN' });
    }

    public error(text: string) {

        this.#add({ message: text, level: 'ERROR' });
    }

    public setMessage(message: Message): void {
        this.#add(message);
    }

    public clear(): void {

        this.#messageSignal.set(undefined);
    }

    #add(message: Message) {
        this.#messageSignal.set(message);
        this.#scrollToTop();
    }

    #scrollToTop() {
        const document = this.document;
        (function smoothscroll() {
            const currentScroll = document.documentElement.scrollTop || document.body.scrollTop;
            // console.log('currentScroll=' + currentScroll);
            if (currentScroll > 0) {
                window.requestAnimationFrame(smoothscroll);
                window.scrollTo(0, currentScroll - (currentScroll / 8));
            }
        })();
    }
}