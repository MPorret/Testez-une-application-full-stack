import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

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
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController)
  });

  it('should get user', (done) => {
    service.getById('42').subscribe((user) => {
      expect(user).toBe(userMock)
      done()
    })

    const req = httpMock.expectOne({
      method: 'GET',
      url: 'api/user/42'
    })
    req.flush(userMock)
  })

  it('should delete user', (done) => {
    service.delete('42').subscribe(() => {
      done()
    })

    const req = httpMock.expectOne({
      method: 'DELETE',
      url: 'api/user/42'
    })
    req.flush({})
  })
});
