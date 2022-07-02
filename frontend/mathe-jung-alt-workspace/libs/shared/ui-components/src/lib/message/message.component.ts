import { Component } from "@angular/core";
import { MessageService } from "@mathe-jung-alt-workspace/shared/util-mja";


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