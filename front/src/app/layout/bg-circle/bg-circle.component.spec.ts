import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BgCircleComponent } from './bg-circle.component';

describe('BgCircleComponent', () => {
  let component: BgCircleComponent;
  let fixture: ComponentFixture<BgCircleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BgCircleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BgCircleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
