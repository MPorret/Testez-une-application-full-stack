import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('SessionsService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  const pathService = 'api/auth';

  const registerRequest = {
    email: "user@test.com",
    firstName: "User",
    lastName: "Test",
    password: "Test!31",
  }

  const loginRequest = {
    email: "user@test.com",
    password: "Test!31",
  }

  const loginResponse: SessionInformation = {
    token: "token",
    type: "type",
    id: 42,
    username: "UserTes",
    firstName: "User",
    lastName: "Test",
    admin: false,
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    httpMock.verify()
  });

  it('should register user', (done) => {
    service.register(registerRequest).subscribe(() => {
      done()
    })

    const req = httpMock.expectOne({
      method: 'POST',
      url: `${pathService}/register`
    })
    expect(req.request.body).toEqual(registerRequest)
    req.flush({})
  });

  it('should register user', (done) => {
    service.login(loginRequest).subscribe((response) => {
      expect(response).toEqual(loginResponse)
      done()
    })

    const req = httpMock.expectOne({
      method: 'POST',
      url: `${pathService}/login`
    })
    expect(req.request.body).toEqual(loginRequest)
    req.flush(loginResponse)
  });
});
