import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MediumDetailsComponent } from './medium-details.component';

describe('MediumDetailsComponent', () => {
  let component: MediumDetailsComponent;
  let fixture: ComponentFixture<MediumDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MediumDetailsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(MediumDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
