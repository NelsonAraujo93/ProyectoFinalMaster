import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PymeRequestDetailsComponent } from './pyme-request-details.component';

describe('PymeRequestDetailsComponent', () => {
  let component: PymeRequestDetailsComponent;
  let fixture: ComponentFixture<PymeRequestDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PymeRequestDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PymeRequestDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
