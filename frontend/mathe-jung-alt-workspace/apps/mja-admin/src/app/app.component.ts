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
    this.messageService.info('Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Rhoncus urna neque viverra justo nec ultrices dui. Adipiscing enim eu turpis egestas.');
  }

  showWarn(): void {
    this.messageService.warn('Ja hallo, das ist eine Warnung');
  }

  showError(): void {
    this.messageService.error('Ja hallo, das ist ein Error');
  }


}


