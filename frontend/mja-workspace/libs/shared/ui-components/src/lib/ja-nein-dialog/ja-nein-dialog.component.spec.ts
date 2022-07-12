import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JaNeinDialogComponent } from './ja-nein-dialog.component';

describe('JaNeinDialogComponent', () => {
  let component: JaNeinDialogComponent;
  let fixture: ComponentFixture<JaNeinDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [JaNeinDialogComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(JaNeinDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
