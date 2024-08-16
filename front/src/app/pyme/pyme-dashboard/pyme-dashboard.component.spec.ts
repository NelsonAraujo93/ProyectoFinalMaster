import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PymeDashboardComponent } from './pyme-dashboard.component';

describe('PymeDashboardComponent', () => {
  let component: PymeDashboardComponent;
  let fixture: ComponentFixture<PymeDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PymeDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PymeDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
