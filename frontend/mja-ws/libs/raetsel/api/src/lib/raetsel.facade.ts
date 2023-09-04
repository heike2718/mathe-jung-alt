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

  #selectItemsFacade = inject(SelectItemsFacade);

  public triggerSearch(suchfilter: RaetselSuchfilter, pageDefinition: PageDefinition): void {

    this.#store.dispatch(raetselActions.raetsel_select_page({ pageDefinition }));
    this.#store.dispatch(raetselActions.find_raetsel({ suchfilter, pageDefinition }));
  }

  public selectRaetsel(raetsel: Raetsel): void {
    this.#store.dispatch(raetselActions.raetsel_selected({ raetsel }));
  }

  public generiereRaetselOutput(raetselID: string, outputFormat: OUTPUTFORMAT, font: FONT_NAME, schriftgroesse: SCHRIFTGROESSE, layoutAntwortvorschlaege: LATEX_LAYOUT_ANTWORTVORSCHLAEGE): void {

    switch (outputFormat) {
      case 'PNG': this.#store.dispatch(raetselActions.generate_raetsel_png({ raetselID, font, schriftgroesse, layoutAntwortvorschlaege })); break;
      case 'PDF': this.#store.dispatch(raetselActions.generate_raetsel_pdf({ raetselID, font, schriftgroesse, layoutAntwortvorschlaege })); break;
      default: throw new Error('Unbekanntes outputFormat ' + outputFormat);
    }
  }

  public editRaetsel(): void {

    this.#router.navigateByUrl('raetsel/editor');

  }

  public changeSuchfilterWithDeskriptoren(deskriptoren: DeskriptorUI[], suchstring: string) {
    this.#store.dispatch(raetselActions.raetselsuchfilter_changed({ suchfilter: { deskriptoren, suchstring } }));
  }

  public changeSuchfilterWithSelectableItems(selectedItems: SelectableItem[], suchstring: string): void {

    const deskriptoren: DeskriptorUI[] = [];
    selectedItems.forEach(item => {
      const theId = item.id as number;
      deskriptoren.push({ id: theId, name: item.name });
    });

    const suchfilter: RaetselSuchfilter = {
      suchstring: suchstring,
      deskriptoren: deskriptoren
    };

    this.#store.dispatch(raetselActions.raetselsuchfilter_changed({ suchfilter }));
  }

  public neueRaetselsuche(): void {
    this.#store.dispatch(raetselActions.reset_raetselsuchfilter());
    this.#selectItemsFacade.resetSelection();
  }

  public cancelSelection(): void {
    this.#store.dispatch(raetselActions.raetsel_cancel_selection());
  }

  public createAndEditRaetsel(quelle: QuelleUI | undefined): void {

    if (quelle === undefined) {
      // Exception werfen!!!
      return;
    }
    const raetselDetails: RaetselDetails = { ...initialRaetselDetails, quelle: quelle };
    this.#store.dispatch(raetselActions.raetsel_details_loaded({ raetselDetails: raetselDetails, navigateTo: 'raetsel/editor' }));
  }

  public initSelectItemsCompomentModel(selectedDeskriptoren: DeskriptorUI[], alleDeskriptoren: DeskriptorUI[]): SelectItemsCompomentModel {

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

  public saveRaetsel(editRaetselPayload: EditRaetselPayload): void {
    this.#store.dispatch(raetselActions.save_raetsel({ editRaetselPayload }));
  }


  public downloadLatexLogs(schluessel: string): void {
    this.#store.dispatch(raetselActions.find_latexlogs({ schluessel: schluessel }));
  }
}