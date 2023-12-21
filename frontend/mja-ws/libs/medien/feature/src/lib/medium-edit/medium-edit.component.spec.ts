import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MediumEditComponent } from './medium-edit.component';

describe('MediumEditComponent', () => {
  let component: MediumEditComponent;
  let fixture: ComponentFixture<MediumEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MediumEditComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(MediumEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
