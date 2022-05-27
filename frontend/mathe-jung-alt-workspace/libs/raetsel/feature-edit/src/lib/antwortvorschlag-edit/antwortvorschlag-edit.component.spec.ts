import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AntwortvorschlagEditComponent } from './antwortvorschlag-edit.component';

describe('AntwortvorschlagEditComponent', () => {
  let component: AntwortvorschlagEditComponent;
  let fixture: ComponentFixture<AntwortvorschlagEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AntwortvorschlagEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AntwortvorschlagEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
