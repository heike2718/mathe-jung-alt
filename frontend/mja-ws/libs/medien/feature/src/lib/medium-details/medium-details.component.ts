import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MedienFacade } from '@mja-ws/medien/api';
import { AuthFacade } from '@mja-ws/core/api';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { LinkedRaetsel } from '@mja-ws/medien/model';
import { LinkedRaetselComponent } from '../linked-raetsel/linked-raetsel.component';
import { MatBadgeModule } from '@angular/material/badge';
import { RaetselFacade } from '@mja-ws/raetsel/api';

@Component({
  selector: 'mja-medium-details',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatBadgeModule,
    MatButtonModule,
    MatCheckboxModule,
    MatTooltipModule,
    LinkedRaetselComponent
  ],
  templateUrl: './medium-details.component.html',
  styleUrl: './medium-details.component.scss',
})
export class MediumDetailsComponent implements OnInit, OnDestroy {

  medienFacade = inject(MedienFacade);

  authFacade = inject(AuthFacade);  

  owner = false;

  #raetselFacade = inject(RaetselFacade);

  #mediumSubscription = new Subscription();

  ngOnInit(): void {
    this.#mediumSubscription = this.medienFacade.selectedMediumDetails$.subscribe((medium) => {
      if (medium && medium.ownMedium) {
        this.owner = true;
      }
      if (medium) {
        this.medienFacade.findLinkedRaetsel(medium.id);
      }
    });
  }

  ngOnDestroy(): void {
    this.#mediumSubscription.unsubscribe();
  }

  startEdit(): void {
    this.medienFacade.editMedium();
  }

  gotoMedienuebersicht(): void {
    this.medienFacade.unselectMedium();
  }

  detailsClicked($event: LinkedRaetsel): void {
    this.#raetselFacade.selectRaetsel($event.schluessel);
  }
}
