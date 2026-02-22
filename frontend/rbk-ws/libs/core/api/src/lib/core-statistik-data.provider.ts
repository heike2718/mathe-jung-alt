import { CoreStatistikEffects, statistikFeature } from '@rbk-ws/core/data';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';

export const coreStatistikDataProvider = [provideState(statistikFeature), provideEffects(CoreStatistikEffects)];
