import { Injectable } from "@angular/core";
import { MessageStore } from "./message.store";


@Injectable({ providedIn: 'root' })
export class MessageService {

    constructor(private messageStore: MessageStore) { }

    public info(text: string) {

        this.messageStore.add({text: text, type: 'info'});
    }

    public warn(text: string) {

        this.messageStore.add({text: text, type: 'warn'});
    }

    public error(text: string) {

        this.messageStore.add({text: text, type: 'error'});
    }
}