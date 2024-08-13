import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PymeMenuComponent } from './pyme-menu.component';

describe('PymeMenuComponent', () => {
  let component: PymeMenuComponent;
  let fixture: ComponentFixture<PymeMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PymeMenuComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PymeMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
