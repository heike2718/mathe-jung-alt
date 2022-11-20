import { filter, Observable } from 'rxjs';
import { isDefined } from '@mja-ws/shared/util';

export function filterDefined<T>(
  source$: Observable<T>
): Observable<NonNullable<T>> {
  return source$.pipe(filter(isDefined));
}
