import { BreakpointObserver, Breakpoints, BreakpointState } from '@angular/cdk/layout';
import { CommonModule } from '@angular/common';
import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'mja-app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss'],
  standalone: true,
  imports: [CommonModule]
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
