import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RaetselgruppenSearchComponent } from './raetselgruppen-search.component';

describe('RaetselgruppenSearchComponent', () => {
  let component: RaetselgruppenSearchComponent;
  let fixture: ComponentFixture<RaetselgruppenSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RaetselgruppenSearchComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RaetselgruppenSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
