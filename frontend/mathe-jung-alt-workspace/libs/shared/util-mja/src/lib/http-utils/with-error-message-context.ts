import { HttpContext } from '@angular/common/http';
import { DEFAULT_ERROR_MESSAGE_CONTEXT } from './http.context';

export function withErrorMessageContext(message: string) {
  return new HttpContext().set(DEFAULT_ERROR_MESSAGE_CONTEXT, message);
}
