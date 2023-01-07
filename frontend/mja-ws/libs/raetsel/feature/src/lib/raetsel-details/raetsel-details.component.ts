import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { TextFieldModule } from '@angular/cdk/text-field';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatExpansionModule } from '@angular/material/expansion';
import { RaetselFacade } from '@mja-ws/raetsel/api';
import { AuthFacade } from '@mja-ws/shared/auth/api';
import { Router } from '@angular/router';
import { GrafikInfo, RaetselDetails } from '@mja-ws/raetsel/model';
import { Subscription, tap } from 'rxjs';
import { FileUploadComponent, FrageLoesungImagesComponent } from '@mja-ws/shared/components';
import { AntwortvorschlagComponent } from '../antwortvorschlag/antwortvorschlag.component';
import { Message } from '@mja-ws/shared/messaging/api';
import { GrafikFacade } from '@mja-ws/grafik/api';
import { GrafikDetailsComponent } from '../grafik-details/grafik-details.component';

@Component({
  selector: 'mja-raetsel-details',
  standalone: true,
  imports: [
    CommonModule,
    CdkAccordionModule,
    FlexLayoutModule,
    MatExpansionModule,
    MatButtonModule,
    MatDialogModule,
    MatIconModule,
    MatFormFieldModule,
    MatListModule,
    TextFieldModule,
    FrageLoesungImagesComponent,
    AntwortvorschlagComponent,
    FileUploadComponent,
    GrafikDetailsComponent
  ],
  templateUrl: './raetsel-details.component.html',
  styleUrls: ['./raetsel-details.component.scss'],
})
export class RaetselDetailsComponent implements OnInit, OnDestroy {

  public raetselFacade = inject(RaetselFacade);
  public authFacade = inject(AuthFacade);
  public grafikFacade = inject(GrafikFacade);
  public dialog = inject(MatDialog);

  #router = inject(Router);

  #raetselDetailsSubscription = new Subscription();
  #raetselDetails!: RaetselDetails;

  ngOnInit(): void {

    this.#raetselDetailsSubscription = this.raetselFacade.raetselDetails$.pipe(
      tap((details) => this.#raetselDetails = details)
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.#raetselDetailsSubscription.unsubscribe();
  }

  startEdit(): void {
    console.log('start edit');
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

  grafikLaden(link: string): void {
    this.grafikFacade.grafikPruefen(link);
  }

  onGrafikHochgeladen($event: Message): void {
    if ($event.level === 'INFO') {
      this.raetselFacade.selectRaetsel(this.#raetselDetails);
    }
  }
}
