import { Component, OnDestroy, OnInit } from "@angular/core";
import { Subscription } from "rxjs";
import { Message } from "./message";
import { MessageService } from "./message.service";
import { MessageStore } from "./message.store";


@Component({
  selector: 'mja-message',
  styleUrls: ['./message.component.scss'],
  templateUrl: './message.component.html'
})
export class MessageComponent {

  messages?: Message;

  constructor(public messageStore: MessageStore) { }

  close(): void {
    this.messageStore.clear();
  }

}