import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MessageService } from '@mja-ws/shared/messaging/api';
import { MatIconModule } from '@angular/material/icon';
import { ScrollService } from '@mja-ws/shared/util';


@Component({
  selector: 'mja-message',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.scss'],
})
export class MessageComponent implements OnInit {

  messageService = inject(MessageService);
  #scrollService = inject(ScrollService);

  ngOnInit(): void {
    this.#scrollService.scrollToTop();
  }
}
