import { Injectable, inject } from "@angular/core";
import { Router } from "@angular/router";
import { PageDefinition, PaginationState } from "@mja-ws/core/model";
import { fromMedien, medienActions } from "@mja-ws/medien/data";
import { LinkedRaetsel, MediensucheTrefferItem, MediumDto, initialMediumDto } from "@mja-ws/medien/model";
import { filterDefined } from "@mja-ws/shared/util";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class MedienFacade {

  #store = inject(Store);
  #router = inject(Router);

  page$: Observable<MediensucheTrefferItem[]> = this.#store.select(fromMedien.page);
  anzahlTrefferGesamt: Observable<number> = this.#store.select(fromMedien.anzahlTrefferGesamt);
  paginationState$: Observable<PaginationState> = this.#store.select(fromMedien.paginationState);
  selectedTrefferItem$: Observable<MediensucheTrefferItem> = this.#store.select(fromMedien.selectedTrefferItem).pipe(filterDefined);
  selectedMediumDetails$: Observable<MediumDto> = this.#store.select(fromMedien.selectedMediumDetails).pipe(filterDefined);
  allMedienDetails$: Observable<MediumDto[]> = this.#store.select(fromMedien.allMedienDetails);
  linkedRaetsel$: Observable<LinkedRaetsel[]> = this.#store.select(fromMedien.linkedRaetsel);


  triggerSearch(suchstring: string, pageDefinition: PageDefinition): void {
    this.#store.dispatch(medienActions.mEDIEN_SELECT_PAGE({ pageDefinition }));
    this.#store.dispatch(medienActions.fIND_MEDIEN({ suchstring, pageDefinition }));
  }

  selectMedium(medium: MediensucheTrefferItem): void {
    this.#store.dispatch(medienActions.sELECT_MEDIUM({ medium }));
  }

  unselectMedium(): void {
    this.#store.dispatch(medienActions.uNSELECT_MEDIUM());
    this.#router.navigateByUrl('medien/uebersicht');
  }

  saveMedium(medium: MediumDto): void {
    this.#store.dispatch(medienActions.sAVE_MEDIUM({ medium }));
  }

  cancelEdit(medium: MediumDto): void {

    if (medium.id === 'neu') {
      this.#store.dispatch(medienActions.uNSELECT_MEDIUM());
    } else {
      const item: MediensucheTrefferItem = {
        id: medium.id,
        kommentar: medium.kommentar,
        medienart: medium.medienart ? medium.medienart : 'BUCH',
        titel: ''
      }
      this.#store.dispatch(medienActions.sELECT_MEDIUM({ medium: item }));
    }
  }

  navigateToSuche(): void {
    this.#store.dispatch(medienActions.uNSELECT_MEDIUM());
  }

  editMedium(): void {
    this.#router.navigateByUrl('medien/editor');
  }

  createAndEditMedium(): void {

    this.#store.dispatch(medienActions.eDIT_MEDIUM({ medium: initialMediumDto, nextUrl: 'medien/editor' }));
  }

  findLinkedRaetsel(mediumId: string): void {
    this.#store.dispatch(medienActions.fIND_LINKED_RAETSEL({mediumId}));
  }
}
