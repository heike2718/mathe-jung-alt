import { Medienart } from '@mja-ws/core/model';

export interface MediumDto {
  readonly id: string;
  readonly medienart: Medienart | undefined;
  readonly titel: string | undefined;
  readonly autor: string | undefined;
  readonly url: string | undefined;
  readonly kommentar: string | undefined;
  readonly schreibgeschuetzt: boolean;
  readonly ownMedium: boolean;
};

export const initialMediumDto: MediumDto = {
  id: 'neu',
  medienart: undefined,
  titel: undefined,
  autor: undefined,
  url: undefined,
  kommentar: undefined,
  schreibgeschuetzt: true,
  ownMedium: true
};


export interface MediensucheTrefferItem {
  readonly id: string;
  readonly medienart: Medienart;
  readonly titel: string;
  readonly kommentar: string | undefined;
};

export interface MediensucheResult {
  readonly trefferGesamt: number;
  readonly treffer: MediensucheTrefferItem[];
};


// ////////////////////////////////////////////////////////////////////////////////////
//    helper classes for the mapping between Java enums and UI-Models
// ////////////////////////////////////////////////////////////////////////////////////

export interface GuiMedienart {
  readonly id: Medienart;
  readonly label: string;
};

export const initialGuiMedienart: GuiMedienart = {
  id: 'NOOP',
  label: ''
};
export class GuiMedienartenMap {

  #medienarten: Map<Medienart, string> = new Map();
  #medienartenInvers: Map<string, Medienart> = new Map();

  constructor() {

    this.#medienarten.set('NOOP', '');
    this.#medienarten.set('BUCH', 'Buch');
    this.#medienarten.set('INTERNET', 'Internet');
    this.#medienarten.set('ZEITSCHRIFT', 'Zeitschrift');

    this.#medienartenInvers.set('', 'NOOP');
    this.#medienartenInvers.set('Buch', 'BUCH');
    this.#medienartenInvers.set('Internet', 'INTERNET');
    this.#medienartenInvers.set('Zeitschrift', 'ZEITSCHRIFT');
  }

  public getMedienartOfLabel(label: string): Medienart {

    const value: Medienart | undefined = this.#medienartenInvers.get(label);
    return value ? value : 'NOOP';
  }

  public getGuiMedienart(refTyp: Medienart): GuiMedienart {

    if (this.#medienarten.has(refTyp)) {

      const label = this.#medienarten.get(refTyp);

      if (label) {
        return { id: refTyp, label: label };
      } else {
        return initialGuiMedienart;
      }
    }

    return initialGuiMedienart;
  }

  public toGuiArray(): GuiMedienart[] {

    const result: GuiMedienart[] = [];
    this.#medienarten.forEach((l: string, key: Medienart) => {
      result.push({ id: key, label: l });
    });

    return result;
  }

  public getLabelsSorted(): string[] {

    const result: string[] = [];

    this.toGuiArray().forEach(element => result.push(element.label));
    return result;
  }

};