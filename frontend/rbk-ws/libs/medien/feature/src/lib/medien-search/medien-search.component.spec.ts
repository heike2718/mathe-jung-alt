import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MedienSearchComponent } from './medien-search.component';

describe('MedienSearchComponent', () => {
  let component: MedienSearchComponent;
  let fixture: ComponentFixture<MedienSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MedienSearchComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(MedienSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
