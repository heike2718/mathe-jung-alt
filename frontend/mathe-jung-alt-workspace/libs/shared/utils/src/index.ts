export { SharedUtilsModule } from './lib/shared-utils.module';
export { MjaEntity as MjaElementWithId, MjaEntityWrapper as MjaEntity, MjaSetUtils } from './lib/collection-utils/shared-collection-utils';

export { ErrorInterceptor } from './lib/http-utils/error.interceptor';
export * from './lib/http-utils/http.context';
export { withErrorMessageContext } from './lib/http-utils/with-error-message-context';

export { noopAction } from './lib/ngrx-utils/noop.action';
export { SafeNgrxService } from './lib/ngrx-utils/safe-ngrx.service';