import { HttpContext } from '@angular/common/http';
import { ERROR_MESSAGE_CONTEXT } from './http.context';

export function withErrorMessageContext(message: string): HttpContext {
  return new HttpContext().set(ERROR_MESSAGE_CONTEXT, message);
}
