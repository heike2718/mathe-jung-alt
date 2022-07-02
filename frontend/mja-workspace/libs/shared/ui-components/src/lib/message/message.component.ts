import { Component } from '@angular/core';
import { MessageService } from '@mja-workspace/shared/util-mja';

@Component({
  selector: 'mja-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss'],
})
export class MessageComponent {

  constructor(public messageService: MessageService) { }

  close(): void {
    this.messageService.clear();
  }
}
