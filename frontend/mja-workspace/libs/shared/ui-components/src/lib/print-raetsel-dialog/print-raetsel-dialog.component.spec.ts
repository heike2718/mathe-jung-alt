import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrintRaetselDialogComponent } from './print-raetsel-dialog.component';

describe('PrintRaetselDialogComponent', () => {
  let component: PrintRaetselDialogComponent;
  let fixture: ComponentFixture<PrintRaetselDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PrintRaetselDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrintRaetselDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
