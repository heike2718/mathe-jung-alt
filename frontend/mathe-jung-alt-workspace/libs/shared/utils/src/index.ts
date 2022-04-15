export { SharedUtilsModule } from './lib/shared-utils.module';
export { MjaEntity as MjaElementWithId, MjaEntityWrapper as MjaEntity, MjaSetUtils } from './lib/collections/shared-collection-utils';

export { ErrorInterceptor } from './lib/http/error.interceptor';
export * from './lib/http/http.context';
export { withErrorMessageContext } from './lib/http/with-error-message-context';