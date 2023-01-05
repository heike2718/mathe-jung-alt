import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { TextFieldModule } from '@angular/cdk/text-field';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatExpansionModule } from '@angular/material/expansion';
import { RaetselFacade } from '@mja-ws/raetsel/api';
import { AuthFacade } from '@mja-ws/shared/auth/api';
import { Router } from '@angular/router';
import { GrafikInfo, RaetselDetails } from '@mja-ws/raetsel/model';
import { Subscription, tap } from 'rxjs';

@Component({
  selector: 'mja-raetsel-details',
  standalone: true,
  imports: [
    CommonModule,
    CdkAccordionModule,
    FlexLayoutModule,
    MatExpansionModule,
    MatButtonModule,
    MatFormFieldModule,
    TextFieldModule
  ],
  templateUrl: './raetsel-details.component.html',
  styleUrls: ['./raetsel-details.component.scss'],
})
export class RaetselDetailsComponent implements OnInit, OnDestroy {

  public raetselFacade = inject(RaetselFacade);
  public authFacade = inject(AuthFacade);

  #router = inject(Router);

  #raetselDetails!: RaetselDetails;

  #raetselDetailsSubscription = new Subscription();

  ngOnInit(): void {

    this.#raetselDetailsSubscription = this.raetselFacade.raetselDetails$.pipe(
      tap((details) => this.#raetselDetails = details)
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.#raetselDetailsSubscription.unsubscribe();
  }

  startEdit(): void {

  }

  openPrintPNGDialog(): void {
    // this.openPrintDialog('PNG');
  }

  openPrintPDFDialog(): void {
    // this.openPrintDialog('PDF');
  }

  gotoRaetselUebersicht(): void {
    this.#router.navigateByUrl('/raetsel');
  }

  generierenDiabled(): boolean {
    const grafikInfosOhneFile: GrafikInfo[] = this.#raetselDetails.grafikInfos.filter(gi => !gi.existiert);
    return grafikInfosOhneFile.length > 0;
  }
}
