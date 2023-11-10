import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { SelectItemsFacade } from '@mja-ws/core/api';
import {
  DeskriptorUI,
  FONT_NAME,
  LATEX_LAYOUT_ANTWORTVORSCHLAEGE,
  OUTPUTFORMAT,
  PageDefinition,
  PaginationState,
  QuelleUI,
  SCHRIFTGROESSE,
  SelectableItem,
  SelectItemsCompomentModel
} from '@mja-ws/core/model';
import { fromRaetsel, raetselActions } from '@mja-ws/raetsel/data';
import { EditRaetselPayload, initialRaetselDetails, Raetsel, RaetselDetails, RaetselSuchfilter } from '@mja-ws/raetsel/model';
import { deepClone, filterDefined } from '@mja-ws/shared/ngrx-utils';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RaetselFacade {

  #store = inject(Store);
  #router = inject(Router);

  loaded$: Observable<boolean> = this.#store.select(fromRaetsel.isLoaded);
  page$: Observable<Raetsel[]> = this.#store.select(fromRaetsel.page);
  paginationState$: Observable<PaginationState> = this.#store.select(fromRaetsel.paginationState);

  raetselDetails$: Observable<RaetselDetails> = this.#store.select(fromRaetsel.raetselDetails).pipe(filterDefined, deepClone);
  suchfilter$: Observable<RaetselSuchfilter> = this.#store.select(fromRaetsel.suchfilter);
  generateLatexError$: Observable<boolean> = this.#store.select(fromRaetsel.generateLatexError);

  editModus$: Observable<boolean> = this.#store.select(fromRaetsel.editModus);

  #selectItemsFacade = inject(SelectItemsFacade);

  triggerSearch(admin: boolean, suchfilter: RaetselSuchfilter, pageDefinition: PageDefinition): void {

    this.#store.dispatch(raetselActions.rAETSEL_SELECT_PAGE({ pageDefinition }));
    this.#store.dispatch(raetselActions.fIND_RAETSEL({admin, suchfilter, pageDefinition }));
  }

  selectRaetsel(raetsel: Raetsel): void {
    this.#store.dispatch(raetselActions.rAETSEL_SELECTED({ raetsel }));
  }

  generiereRaetselOutput(raetselID: string, outputFormat: OUTPUTFORMAT, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {

    switch (outputFormat) {
      case 'PNG': this.#store.dispatch(raetselActions.gENERATE_RAETSEL_PNG({ raetselID, font, schriftgroesse, layoutAntwortvorschlaege })); break;
      case 'PDF': this.#store.dispatch(raetselActions.gENERATE_RAETSEL_PDF({ raetselID, font, schriftgroesse, layoutAntwortvorschlaege })); break;
      default: throw new Error('Unbekanntes outputFormat ' + outputFormat);
    }
  }

  editRaetsel(): void {

    this.#enterEditMode();
    this.#router.navigateByUrl('raetsel/editor');

  }

  changeSuchfilterWithDeskriptoren(deskriptoren: DeskriptorUI[], suchstring: string) {
    this.#store.dispatch(raetselActions.rAETSELSUCHFILTER_CHANGED({ suchfilter: { deskriptoren, suchstring } }));
  }

  changeSuchfilterWithSelectableItems(selectedItems: SelectableItem[], suchstring: string): void {

    const deskriptoren: DeskriptorUI[] = [];
    selectedItems.forEach(item => {
      const theId = item.id as number;
      deskriptoren.push({ id: theId, name: item.name });
    });

    const suchfilter: RaetselSuchfilter = {
      suchstring: suchstring,
      deskriptoren: deskriptoren
    };

    this.#store.dispatch(raetselActions.rAETSELSUCHFILTER_CHANGED({ suchfilter }));
  }

  neueRaetselsuche(): void {
    this.#store.dispatch(raetselActions.rESET_RAETSELSUCHFILTER());
    this.#selectItemsFacade.resetSelection();
  }

  public cancelSelection(): void {
    this.#store.dispatch(raetselActions.rAETSEL_CANCEL_SELECTION());
  }

  createAndEditRaetsel(quelle: QuelleUI | undefined): void {

    if (quelle === undefined) {
      // TODO: Exception werfen!!!
      return;
    }
    this.#enterEditMode();
    const raetselDetails: RaetselDetails = { ...initialRaetselDetails, quelle: quelle };
    this.#store.dispatch(raetselActions.rAETSEL_DETAILS_LOADED({ raetselDetails: raetselDetails, navigateTo: 'raetsel/editor' }));
  }

  initSelectItemsCompomentModel(selectedDeskriptoren: DeskriptorUI[], alleDeskriptoren: DeskriptorUI[]): SelectItemsCompomentModel {

    const gewaehlteItems: SelectableItem[] = [];
    selectedDeskriptoren.forEach(d => gewaehlteItems.push({ id: d.id, name: d.name, selected: true }));

    const vorrat: SelectableItem[] = [];
    alleDeskriptoren.forEach(d => {

      const found = selectedDeskriptoren.find((descr) => { return descr.id === d.id });
      if (!found) {
        vorrat.push({ id: d.id, name: d.name, selected: false })
      }
    });

    const selectDeskriptorenComponentModel = {
      ueberschriftAuswahlliste: 'Deskriptoren',
      ueberschriftGewaehlteItems: 'gewählt:',
      vorrat: vorrat,
      gewaehlteItems: gewaehlteItems
    };

    return selectDeskriptorenComponentModel;

  }

  saveRaetsel(editRaetselPayload: EditRaetselPayload): void {
    this.#store.dispatch(raetselActions.sAVE_RAETSEL({ editRaetselPayload }));
  }


  downloadLatexLogs(schluessel: string): void {
    this.#store.dispatch(raetselActions.fIND_LATEXLOGS({ schluessel: schluessel }));
  }
  
  leaveEditMode() {
    this.#store.dispatch(raetselActions.fINISH_EDIT());
  } 


  #enterEditMode() {
    this.#store.dispatch(raetselActions.pREPARE_EDIT());
  }

}