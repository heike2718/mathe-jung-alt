import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AntwortvorschlagGroupEditComponent } from './antwortvorschlag-group-edit.component';

describe('AntwortvorschlagGroupEditComponent', () => {
  let component: AntwortvorschlagGroupEditComponent;
  let fixture: ComponentFixture<AntwortvorschlagGroupEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AntwortvorschlagGroupEditComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AntwortvorschlagGroupEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
