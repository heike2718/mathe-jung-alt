import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GrafikDetailsComponent } from './grafik-details.component';

describe('GrafikDetailsComponent', () => {
  let component: GrafikDetailsComponent;
  let fixture: ComponentFixture<GrafikDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GrafikDetailsComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GrafikDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
