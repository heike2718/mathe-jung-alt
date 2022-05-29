import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { QuellenFacade } from '@mathe-jung-alt-workspace/quellen/domain';
import { RaetselDetails, RaetselFacade } from '@mathe-jung-alt-workspace/raetsel/domain';
import { AuthFacade } from '@mathe-jung-alt-workspace/shared/auth/domain';
import { Subscriber, Subscription } from 'rxjs';

@Component({
  selector: 'mja-reaetsel-details',
  templateUrl: './reaetsel-details.component.html',
  styleUrls: ['./reaetsel-details.component.scss']
})
export class ReaetselDetailsComponent implements OnInit, OnDestroy {

  #raetselDetailsSubscription: Subscription = new Subscription();

  #raetselDetails!: RaetselDetails;

  constructor(public raetselFacade: RaetselFacade, public authFacade: AuthFacade, private quellenFacade: QuellenFacade) { }


  ngOnInit(): void {

    this.#raetselDetailsSubscription = this.raetselFacade.raetselDetails$.subscribe(
      details => {
        if (details) {
          this.#raetselDetails = details;
        }
      }
    );

  }

  ngOnDestroy(): void {
    this.#raetselDetailsSubscription.unsubscribe();
  }

  startEdit(): void {
    if (this.#raetselDetails) {
      this.quellenFacade.loadQuelle(this.#raetselDetails.quelleId);
      this.raetselFacade.startEditRaetsel(this.#raetselDetails);
    }
  }
}
