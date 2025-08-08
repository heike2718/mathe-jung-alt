import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';

import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'rbk-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss'],
  imports: [],
})
export class LayoutComponent implements OnInit, OnDestroy {
  #breakpointObserver = inject(BreakpointObserver);
  #subscription: Subscription = new Subscription();

  ngOnInit(): void {
    this.#breakpointObserver.observe(Breakpoints.Handset).subscribe();
  }

  ngOnDestroy(): void {
    this.#subscription.unsubscribe();
  }
}
