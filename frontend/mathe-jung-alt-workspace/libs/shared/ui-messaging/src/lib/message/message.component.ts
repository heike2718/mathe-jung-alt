import { Component } from "@angular/core";
import { MessageService } from "./message.service";


@Component({
  selector: 'mja-message',
  styleUrls: ['./message.component.scss'],
  templateUrl: './message.component.html'
})
export class MessageComponent {

  constructor(public messageService: MessageService) { }

  close(): void {
    this.messageService.clear();
  }

}