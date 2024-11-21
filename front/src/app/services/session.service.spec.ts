import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { take } from 'rxjs';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;
  const mockSessionInfo: SessionInformation = {
     
    token: 'abc123',
    type:'',
    id: 1,
    username:'test@example.com',
    firstName:'test',
    lastName:'test',
    admin:false
  };
  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should initialize isLoggedSubject with the correct value', (done) => {
    service.$isLogged().pipe(take(1)).subscribe((value) => {
      expect(value).toBe(false);
      done();
    });
  });

  it('should initialize isLoggedSubject with the correct value', (done) => {

    service.logIn(mockSessionInfo);
    service.$isLogged().pipe(take(1)).subscribe((value) => {
      expect(value).toBe(true);
      done();
    });
  });
});
