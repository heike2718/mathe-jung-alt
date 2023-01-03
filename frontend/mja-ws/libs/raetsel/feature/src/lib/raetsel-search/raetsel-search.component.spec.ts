import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RaetselSearchComponent } from './raetsel-search.component';

describe('RaetselSearchComponent', () => {
  let component: RaetselSearchComponent;
  let fixture: ComponentFixture<RaetselSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RaetselSearchComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RaetselSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
