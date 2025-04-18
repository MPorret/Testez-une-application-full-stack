import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  const userMock = {
    token: "token",
    type: "type",
    id: 42,
    username: "UserTes",
    firstName: "User",
    lastName: "Test",
    admin: false,
  }

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should log in and update sessionInformation', () => {
    service.logIn(userMock)
    expect(service.isLogged).toBe(true)
    expect(service.sessionInformation).toBe(userMock)

    service.$isLogged().subscribe((value) => {
      expect(value).toBe(true);
    })
  })

  it('should log out and reset sessionInformation', () => {
    service.logOut()
    expect(service.isLogged).toBe(false)
    expect(service.sessionInformation).toBeUndefined()

    service.$isLogged().subscribe((value) => {
      expect(value).toBe(false);
    })
  })
});
