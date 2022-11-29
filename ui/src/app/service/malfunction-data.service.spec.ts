import { TestBed } from '@angular/core/testing';

import { MalfunctionDataService } from './malfunction-data.service';

describe('MalfunctionDataService', () => {
  let service: MalfunctionDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MalfunctionDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
