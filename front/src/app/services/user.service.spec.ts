import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { UserService } from './user.service';
import { firstValueFrom } from 'rxjs';

describe('UserService', () => {
  let userService: UserService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    httpTesting = TestBed.inject(HttpTestingController);
    userService = TestBed.inject(UserService);
  });

  afterEach(() => {
    TestBed.inject(HttpTestingController).verify();
  });

  it('should be created', () => {
    expect(userService).toBeTruthy();
  });

  it('should get user by id', async () => {

    const user$ = userService.getById('123');
    const userPromise = firstValueFrom(user$);

    const req = httpTesting.expectOne({
      method: 'GET',
      url: 'api/user/123'
    });

    const expectedUser = {
      id: 123,
      email: 'user123@test.com',
      lastName: '123',
      firstName: 'User',
      admin: false,
      createdAt: new Date('2025-01-20T10:00:19'),
      updatedAt: new Date('2025-01-20T10:00:19')
    };

    req.flush(expectedUser);

    expect(await userPromise).toEqual(expectedUser);
  });

  it('should delete user by id', () => {

    firstValueFrom(userService.delete('123'));

    const req = httpTesting.expectOne({
      method: 'DELETE',
      url: 'api/user/123'
    });

    req.flush({ status: 200 });
  });

});
