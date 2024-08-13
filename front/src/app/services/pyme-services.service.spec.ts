import { TestBed } from '@angular/core/testing';

import { PymeServicesService } from './pyme-services.service';

describe('PymeServicesService', () => {
  let service: PymeServicesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PymeServicesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
