import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSuchfilterComponent } from './admin-suchfilter.component';

describe('AdminSuchfilterComponent', () => {
  let component: AdminSuchfilterComponent;
  let fixture: ComponentFixture<AdminSuchfilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminSuchfilterComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminSuchfilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
