import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PymeHistoryComponent } from './pyme-history.component';

describe('PymeHistoryComponent', () => {
  let component: PymeHistoryComponent;
  let fixture: ComponentFixture<PymeHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PymeHistoryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PymeHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
