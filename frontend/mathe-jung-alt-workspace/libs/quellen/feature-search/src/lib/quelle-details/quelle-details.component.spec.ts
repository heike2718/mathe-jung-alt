import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuelleDetailsComponent } from './quelle-details.component';

describe('QuelleDetailsComponent', () => {
  let component: QuelleDetailsComponent;
  let fixture: ComponentFixture<QuelleDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [QuelleDetailsComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QuelleDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
