import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { SelectItemsFacade } from '@mja-ws/core/api';
import {
  DeskriptorUI,
  FontName,
  LaTeXLayoutAntwortvorschlaege,
  OutputFormat,
  PageDefinition,
  PaginationState,
  HerkunftRaetsel,
  Schriftgroesse,
  SelectableItem,
  SelectItemsCompomentModel,
  Quellenart,
  Medienart
} from '@mja-ws/core/model';
import { fromRaetsel, raetselActions } from '@mja-ws/raetsel/data';
import { EditRaetselPayload,
  initialRaetselDetails,
  MediumQuelleDto,
  ModusSucheMitDeskriptoren,
  ModusVolltextsuche,
  QuelleDto, 
  Raetsel,
  RaetselDetails,
  RaetselSuchfilter } from '@mja-ws/raetsel/model';
import { deepClone, filterDefined } from '@mja-ws/shared/util';
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
  quelle$: Observable<QuelleDto> = this.#store.select(fromRaetsel.quelle);
  medienForQuelle$: Observable<MediumQuelleDto[]> = this.#store.select(fromRaetsel.medienForQuelle);

  #selectItemsFacade = inject(SelectItemsFacade);

  triggerSearch(admin: boolean, suchfilter: RaetselSuchfilter, pageDefinition: PageDefinition): void {

    this.#store.dispatch(raetselActions.rAETSEL_SELECT_PAGE({ pageDefinition }));
    this.#store.dispatch(raetselActions.fIND_RAETSEL({admin, suchfilter, pageDefinition }));
  }

  selectRaetsel(schluessel: string): void {
    this.#store.dispatch(raetselActions.rAETSEL_SELECTED({ schluessel }));
  }

  generiereRaetselOutput(raetselID: string, outputFormat: OutputFormat, font: FontName, schriftgroesse: Schriftgroesse, layoutAntwortvorschlaege: LaTeXLayoutAntwortvorschlaege): void {

    switch (outputFormat) {
      case 'PNG': this.#store.dispatch(raetselActions.gENERATE_RAETSEL_PNG({ raetselID, font, schriftgroesse, layoutAntwortvorschlaege })); break;
      case 'PDF': this.#store.dispatch(raetselActions.gENERATE_RAETSEL_PDF({ raetselID, font, schriftgroesse, layoutAntwortvorschlaege })); break;
      default: throw new Error('Unbekanntes outputFormat ' + outputFormat);
    }
  }

  editRaetsel(): void {

    this.#router.navigateByUrl('raetsel/editor');

  }

  changeSuchfilterWithDeskriptoren(suchfilter: RaetselSuchfilter) {
    this.#store.dispatch(raetselActions.rAETSELSUCHFILTER_CHANGED({ suchfilter }));
  }

  changeSuchfilterWithSelectableItems(selectedItems: SelectableItem[], suchstring: string, modeFullTextSearch: ModusVolltextsuche, searchModeForDescriptors: ModusSucheMitDeskriptoren ): void {

    const deskriptoren: DeskriptorUI[] = [];
    selectedItems.forEach(item => {
      const theId = item.id as number;
      deskriptoren.push({ id: theId, name: item.name });
    });

    const suchfilter: RaetselSuchfilter = {
      suchstring: suchstring,
      deskriptoren: deskriptoren,
      modeFullTextSearch: modeFullTextSearch,
      searchModeForDescriptors: searchModeForDescriptors
    };

    this.#store.dispatch(raetselActions.rAETSELSUCHFILTER_CHANGED({ suchfilter }));
  }

  quelleChanged(quelle: QuelleDto): void {
    this.#store.dispatch(raetselActions.qUELLE_CHANGED({quelle}));
  }

  loadQuelle(quelleID: string): void {
    this.#store.dispatch(raetselActions.lOAD_QUELLE_ZU_RAETSEL({quelleID}));
  }

  findMedienForQuelle(quellenart: Quellenart): void {

    let medienart: Medienart = 'NOOP';

    switch (quellenart) {
      case 'BUCH': medienart = 'BUCH'; break;
      case 'INTERNET': medienart = 'INTERNET'; break;
      case 'ZEITSCHRIFT': medienart = 'ZEITSCHRIFT'; break;
    }

    if (medienart !== 'NOOP') {
      this.#store.dispatch(raetselActions.fIND_MEDIEN_FOR_QUELLE({ medienart }));
    }
  }

  neueRaetselsuche(): void {
    this.#store.dispatch(raetselActions.rESET_RAETSELSUCHFILTER());
    this.#selectItemsFacade.resetSelection();
  }

  public cancelSelection(): void {
    this.#store.dispatch(raetselActions.rAETSEL_CANCEL_SELECTION());
  }

  createAndEditRaetsel(autor: HerkunftRaetsel ): void {

    const raetselDetails: RaetselDetails = { ...initialRaetselDetails, herkunft: autor };
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

  downloadEmbeddedImages(raetselID: string): void {
    this.#store.dispatch(raetselActions.fIND_EMBEDDED_IMAGES({raetselID}));
  }

  downloadRaetselLaTeX(raetselID: string): void {
    this.#store.dispatch(raetselActions.fIND_RAETSEL_LATEX({raetselID}));
  }

}