import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { take } from 'rxjs';

describe('SessionService', () => {
  let sessionService: SessionService;
  let isLoggedValues: boolean[] = [];

  beforeEach(() => {
    TestBed.configureTestingModule({});
    sessionService = TestBed.inject(SessionService);
    sessionService.$isLogged().pipe(take(2)).subscribe((value) => {
      isLoggedValues.push(value);
    });
  });

  afterEach(() => {
    isLoggedValues = [];
  });

  it('should be created', () => {
    expect(sessionService).toBeTruthy();
  });

  it('should log out user', () => {
    sessionService.logOut();
    expect(sessionService.isLogged).toBe(false);
    expect(sessionService.sessionInformation).toBeUndefined();
    expect(isLoggedValues).toEqual([false, false]);
  });

  it('should log in user', () => {
    const sessionInformation: SessionInformation = {
      token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3Mzc1MzgxNTQsImV4cCI6MTczNzYyNDU1NH0.naL26mF2lnsS-OhFcJig4L69OjjXRjAqCkmtkkOevHtBubBIHzSdsALcFyMZbTGkoG48aJg7fiYZok2dvwQVkw",
      type: "Bearer",
      id: 1,
      username: "yoga@studio.com",
      firstName: "Admin",
      lastName: "Admin",
      admin: true
    }
    sessionService.logIn(sessionInformation);
    expect(sessionService.isLogged).toBe(true);
    expect(sessionService.sessionInformation).not.toBeUndefined();
    expect(isLoggedValues).toEqual([false, true]);
  });
});
