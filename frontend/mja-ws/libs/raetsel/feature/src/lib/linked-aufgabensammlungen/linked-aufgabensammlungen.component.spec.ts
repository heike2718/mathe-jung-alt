import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LinkedAufgabensammlungenComponent } from './linked-aufgabensammlungen.component';

describe('LinkedAufgabensammlungenComponent', () => {
  let component: LinkedAufgabensammlungenComponent;
  let fixture: ComponentFixture<LinkedAufgabensammlungenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LinkedAufgabensammlungenComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(LinkedAufgabensammlungenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
