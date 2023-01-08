import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RaetselEditorComponent } from './raetsel-editor.component';

describe('RaetselEditorComponent', () => {
  let component: RaetselEditorComponent;
  let fixture: ComponentFixture<RaetselEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RaetselEditorComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RaetselEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
