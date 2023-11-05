import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EmbeddableImageInfoComponent } from './embeddable-image-info.component';

describe('EmbeddableImageInfoComponent', () => {
  let component: EmbeddableImageInfoComponent;
  let fixture: ComponentFixture<EmbeddableImageInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmbeddableImageInfoComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(EmbeddableImageInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
