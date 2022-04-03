import { Injectable } from "@angular/core";
import { MessageStore } from "./message.store";


@Injectable({ providedIn: 'root' })
export class MessageService {

    constructor(private messageStore: MessageStore) { }

    public info(text: string) {

        this.messageStore.add({text: text, type: 'INFO'});
    }

    public warn(text: string) {

        this.messageStore.add({text: text, type: 'WARN'});
    }

    public error(text: string) {

        this.messageStore.add({text: text, type: 'ERROR'});
    }

    public clear(): void {

        this.messageStore.clear();
    }
}