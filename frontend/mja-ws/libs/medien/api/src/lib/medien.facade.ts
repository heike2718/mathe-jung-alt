import { Injectable, inject } from "@angular/core";
import { Router } from "@angular/router";
import { PageDefinition, PaginationState } from "@mja-ws/core/model";
import { fromMedien, medienActions } from "@mja-ws/medien/data";
import { MediensucheTrefferItem, MediumDto, initialMediumDto } from "@mja-ws/medien/model";
import { deepClone, filterDefined } from "@mja-ws/shared/util";
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
  selectedTrefferItem$: Observable<MediensucheTrefferItem> = this.#store.select(fromMedien.selectedTrefferItem).pipe(filterDefined, deepClone);
  selectedMediumDetails$: Observable<MediumDto> = this.#store.select(fromMedien.selectedMediumDetails).pipe(filterDefined, deepClone);
  allMedienDetails$: Observable<MediumDto[]> = this.#store.select(fromMedien.allMedienDetails);


  triggerSearch(suchstring: string, pageDefinition: PageDefinition): void {
    this.#store.dispatch(medienActions.mEDIEN_SELECT_PAGE({ pageDefinition }));
    this.#store.dispatch(medienActions.fIND_MEDIEN({ suchstring, pageDefinition }));
  }

  selectMedium(medium: MediensucheTrefferItem): void {
    this.#store.dispatch(medienActions.sELECT_MEDIUM({ medium }));
  }

  saveMedium(medium: MediumDto): void {
    this.#store.dispatch(medienActions.sAVE_MEDIUM({ medium }));
  }

  cancelEdit(medium: MediumDto): void {

    if (medium.id === 'neu') {
      this.#store.dispatch(medienActions.uNSELECT_MEDIUM());
    } else {
      this.#store.dispatch(medienActions.eDIT_MEDIUM({medium: medium, nextUrl: 'medien/editor'}));
    }
  }

  navigateToSuche(): void {
    this.#store.dispatch(medienActions.uNSELECT_MEDIUM());
  }

  createAndEditMedium(): void {

    this.#store.dispatch(medienActions.eDIT_MEDIUM({ medium: initialMediumDto, nextUrl: 'medien/editor' }));
  }
}
