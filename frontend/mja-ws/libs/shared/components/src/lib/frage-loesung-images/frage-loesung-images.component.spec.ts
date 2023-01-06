import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FrageLoesungImagesComponent } from './frage-loesung-images.component';

describe('FrageLoesungImagesComponent', () => {
  let component: FrageLoesungImagesComponent;
  let fixture: ComponentFixture<FrageLoesungImagesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FrageLoesungImagesComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FrageLoesungImagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
