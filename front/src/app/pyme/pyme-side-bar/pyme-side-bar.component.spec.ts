import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PymeSideBarComponent } from './pyme-side-bar.component';

describe('PymeSideBarComponent', () => {
  let component: PymeSideBarComponent;
  let fixture: ComponentFixture<PymeSideBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PymeSideBarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PymeSideBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
