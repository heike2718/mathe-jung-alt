import { Component, OnInit } from '@angular/core';
import { MessageService } from '@mja-workspace/shared/util-mja';

@Component({
  selector: 'mja-admin-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'mja-admin';

  constructor( private messageService: MessageService){}

  ngOnInit() {
    this.messageService.info('Ja hallo dann auch vom neuen Workspace.')
  }


}
