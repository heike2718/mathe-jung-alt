import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReaetselDetailsComponent } from './reaetsel-details.component';

describe('ReaetselDetailsComponent', () => {
  let component: ReaetselDetailsComponent;
  let fixture: ComponentFixture<ReaetselDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReaetselDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReaetselDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
