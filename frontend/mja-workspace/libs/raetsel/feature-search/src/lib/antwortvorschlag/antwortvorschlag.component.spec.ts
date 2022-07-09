import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AntwortvorschlagComponent } from './antwortvorschlag.component';

describe('AntwortvorschlagComponent', () => {
  let component: AntwortvorschlagComponent;
  let fixture: ComponentFixture<AntwortvorschlagComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AntwortvorschlagComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AntwortvorschlagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
