import { Component, OnInit } from '@angular/core';
import { MessageService } from '@mathe-jung-alt-workspace/shared/ui-messaging';

@Component({
  selector: 'mja-admin-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'mja-admin';

  constructor(private messageService: MessageService) { }

  showInfo(): void {
    this.messageService.info('Ja hallo, das ist eine Info');
    this.messageService.warn('Ja hallo, das ist eine Warnung');
    this.messageService.error('Ja hallo, das ist ein Error');
  }
}


