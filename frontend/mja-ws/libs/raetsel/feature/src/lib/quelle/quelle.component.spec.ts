import { ComponentFixture, TestBed } from '@angular/core/testing';
import { QuelleComponent } from './quelle.component';

describe('QuelleComponent', () => {
  let component: QuelleComponent;
  let fixture: ComponentFixture<QuelleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuelleComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(QuelleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
