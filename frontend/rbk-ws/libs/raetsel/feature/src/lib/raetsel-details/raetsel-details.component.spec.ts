import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RaetselDetailsComponent } from './raetsel-details.component';

describe('RaetselDetailsComponent', () => {
  let component: RaetselDetailsComponent;
  let fixture: ComponentFixture<RaetselDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RaetselDetailsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RaetselDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
