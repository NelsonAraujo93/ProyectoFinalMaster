import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PymeCardComponent } from './pyme-card.component';

describe('PymeCardComponent', () => {
  let component: PymeCardComponent;
  let fixture: ComponentFixture<PymeCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PymeCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PymeCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
