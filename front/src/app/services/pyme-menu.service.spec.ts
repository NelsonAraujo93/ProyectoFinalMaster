import { TestBed } from '@angular/core/testing';

import { PymeMenuService } from './pyme-menu.service';

describe('PymeMenuService', () => {
  let service: PymeMenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PymeMenuService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
