import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Message } from './message';

@Injectable({ providedIn: 'root' })
export class MessageStore {
  
  #messageSubject$ = new BehaviorSubject<Message | undefined>(undefined);

  public message$: Observable<Message | undefined> = this.#messageSubject$.asObservable();

  add(message: Message) {
    this.#messageSubject$.next(message);
  }

  clear(): void {
    this.#messageSubject$.next(undefined);
  }
}
