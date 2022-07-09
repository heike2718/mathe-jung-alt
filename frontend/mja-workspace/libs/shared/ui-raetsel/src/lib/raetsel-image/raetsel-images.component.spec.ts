import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RaetselImagesComponent } from './raetsel-images.component';

describe('RaetselImageComponent', () => {
  let component: RaetselImagesComponent;
  let fixture: ComponentFixture<RaetselImagesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RaetselImagesComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RaetselImagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
