
export { coreQuelleActions } from './lib/quelle/core-autor.actions';
export { fromCoreQuelle } from './lib/quelle/core-autor.selectors';
export { CoreQuelleEffects } from './lib/quelle/core-autor.effects';
export { coreAutorFeature as coreQuelleFeature } from './lib/quelle/core-autor.reducer';

export { coreDeskriptorenActions } from './lib/descriptoren/core-deskriptoren.actions';
export { fromCoreDeskriptoren } from './lib/descriptoren/core-deskriptoren.selectors'
export { CoreDeskriptorUIEffects } from "./lib/descriptoren/core-deskriptoren.effects";
export { coreDeskriptorenUIFeature } from "./lib/descriptoren/core-deskriptoren.reducer";

export { ImagesHttpService } from './lib/images/images-http.service';

export { coreStatistikActions } from './lib/statistik/core-statistik.actions';
export { statistikFeature } from './lib/statistik/core-statistik.reducer';
export { fromStatistik } from './lib/statistik/core-statistik.selectors';
export { CoreStatistikEffects } from './lib/statistik/core-statistik.effects';
