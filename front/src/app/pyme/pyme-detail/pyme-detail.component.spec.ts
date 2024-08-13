import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PymeDetailComponent } from './pyme-detail.component';

describe('PymeDetailComponent', () => {
  let component: PymeDetailComponent;
  let fixture: ComponentFixture<PymeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PymeDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PymeDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
