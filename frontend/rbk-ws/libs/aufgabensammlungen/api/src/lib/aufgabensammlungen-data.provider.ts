import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';
import { aufgabensammlungenFeature, AufgabensammlungenEffects } from '@rbk-ws/aufgabensammlungen/data';

export const aufgabensammlungenDataProvider = [
  provideState(aufgabensammlungenFeature),
  provideEffects(AufgabensammlungenEffects),
];
