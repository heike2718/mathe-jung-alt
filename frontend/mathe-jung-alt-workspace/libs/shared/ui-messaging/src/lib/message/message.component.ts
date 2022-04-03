import { Component, OnDestroy, OnInit } from "@angular/core";
import { Subscription } from "rxjs";
import { Message } from "./message";
import { MessageStore } from "./message.store";


@Component({
    selector: 'mja-message',
    styles: [
        `
          .error {
            background: #e9c4c4;
          }
    
          .info {
            background: #C5CAE9;
          }
        `,
    ],
    templateUrl: './message.component.html'  
})
export class MessageComponent implements OnInit, OnDestroy{

    messages: Message[] = [];

    #messagesSubscription: Subscription = new Subscription();

    constructor(private messageStore: MessageStore) { }


    ngOnInit(): void {

        this.#messagesSubscription = this.messageStore.messages$.subscribe(
            messages => this.messages.push(messages)
        );        
    }

    ngOnDestroy(): void {
        
        this.#messagesSubscription.unsubscribe();
    }

}