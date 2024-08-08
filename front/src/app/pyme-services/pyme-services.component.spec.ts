import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PymeServicesComponent } from './pyme-services.component';

describe('PymeServicesComponent', () => {
  let component: PymeServicesComponent;
  let fixture: ComponentFixture<PymeServicesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PymeServicesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PymeServicesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
