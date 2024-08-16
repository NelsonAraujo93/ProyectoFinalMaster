import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PymeRequestsComponent } from './pyme-requests.component';

describe('PymeRequestsComponent', () => {
  let component: PymeRequestsComponent;
  let fixture: ComponentFixture<PymeRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PymeRequestsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PymeRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
