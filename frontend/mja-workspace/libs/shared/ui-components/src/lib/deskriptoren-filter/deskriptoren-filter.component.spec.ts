import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeskriptorenFilterComponent } from './deskriptoren-filter.component';

describe('DeskriptorenFilterComponent', () => {
  let component: DeskriptorenFilterComponent;
  let fixture: ComponentFixture<DeskriptorenFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeskriptorenFilterComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeskriptorenFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
