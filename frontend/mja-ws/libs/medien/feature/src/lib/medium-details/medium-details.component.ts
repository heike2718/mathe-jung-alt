import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MedienFacade } from '@mja-ws/medien/api';
import { AuthFacade } from '@mja-ws/core/api';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'mja-medium-details',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatCheckboxModule,
    MatTooltipModule
  ],
  templateUrl: './medium-details.component.html',
  styleUrl: './medium-details.component.scss',
})
export class MediumDetailsComponent implements OnInit, OnDestroy {

  medienFacade = inject(MedienFacade);

  authFacade = inject(AuthFacade);

  owner = false;

  #mediumSubscription = new Subscription();

  ngOnInit(): void {
    this.#mediumSubscription = this.medienFacade.selectedMediumDetails$.subscribe((medium) => {
      if (medium && medium.ownMedium) {
        this.owner = true;
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
}
