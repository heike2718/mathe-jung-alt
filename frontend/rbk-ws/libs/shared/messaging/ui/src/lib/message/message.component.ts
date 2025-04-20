import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MessageService } from '@rbk-ws/shared/messaging/api';
import { MatIconModule } from '@angular/material/icon';


@Component({
    selector: 'mja-message',
    imports: [CommonModule, MatIconModule],
    templateUrl: './message.component.html',
    styleUrls: ['./message.component.scss']
})
export class MessageComponent {

  messageService = inject(MessageService); 

  close(): void {
    this.messageService.clear();
  }
}
