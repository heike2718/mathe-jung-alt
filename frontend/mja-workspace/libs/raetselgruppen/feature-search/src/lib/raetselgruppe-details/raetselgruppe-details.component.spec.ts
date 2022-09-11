import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RaetselgruppeDetailsComponent } from './raetselgruppe-details.component';

describe('RaetselgruppeDetailsComponent', () => {
  let component: RaetselgruppeDetailsComponent;
  let fixture: ComponentFixture<RaetselgruppeDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RaetselgruppeDetailsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RaetselgruppeDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
