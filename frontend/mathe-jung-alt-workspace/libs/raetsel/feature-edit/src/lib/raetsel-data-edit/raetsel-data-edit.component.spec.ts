import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RaetselDataEditComponent } from './raetsel-data-edit.component';

describe('RaetselDataEditComponent', () => {
  let component: RaetselDataEditComponent;
  let fixture: ComponentFixture<RaetselDataEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RaetselDataEditComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RaetselDataEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
