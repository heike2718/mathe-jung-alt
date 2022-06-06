import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Quelle, QuellenFacade } from '@mathe-jung-alt-workspace/quellen/domain';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { Subscription } from 'rxjs';

@Component({
  selector: 'mja-quelle-details',
  templateUrl: './quelle-details.component.html',
  styleUrls: ['./quelle-details.component.scss'],
})
export class QuelleDetailsComponent implements OnInit, OnDestroy {

  @Input()
  quelle!: Quelle

  constructor(public authFacade: AuthFacade) { }

  ngOnInit(): void { }

  ngOnDestroy(): void { }
}
