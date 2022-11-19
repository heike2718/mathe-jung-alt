import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BreakpointObserver, Breakpoints, BreakpointState } from '@angular/cdk/layout';
import { tap } from 'rxjs';

@Component({
  selector: 'mja-admin-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class LayoutComponent implements OnInit {

  isMenuOpen = true;
  #isHandset = false;

  #breakpointObserver = inject(BreakpointObserver);

  get isHandset(): boolean {
    return this.#isHandset;
  }

  ngOnInit(): void {
    this.#breakpointObserver.observe(Breakpoints.Handset).pipe(
      tap((state: BreakpointState) => {
        if (state.matches) {
          this.#isHandset = true;
          this.isMenuOpen = false;
        } else {
          this.#isHandset = false;
          this.isMenuOpen = true;
        }
      })
    ).subscribe();
  }
}
