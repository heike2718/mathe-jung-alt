import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectPrintparametersDialogComponent } from './select-printparameters-dialog.component';

describe('SelectPrintparametersDialogComponent', () => {
  let component: SelectPrintparametersDialogComponent;
  let fixture: ComponentFixture<SelectPrintparametersDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SelectPrintparametersDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectPrintparametersDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
