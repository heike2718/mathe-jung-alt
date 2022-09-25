import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RaetselgruppenelementDialogComponent } from './raetselgruppenelement-dialog.component';

describe('RaetselelementDialogComponent', () => {
  let component: RaetselgruppenelementDialogComponent;
  let fixture: ComponentFixture<RaetselgruppenelementDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RaetselgruppenelementDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RaetselgruppenelementDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
