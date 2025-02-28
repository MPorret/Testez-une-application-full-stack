import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  const mockUser: User = {
    id: 1,
    email: 'toto@mail.com', 
    lastName: 'tata', 
    firstName: 'toto', 
    admin: true, 
    password: 'passwd', 
    createdAt: new Date(), 
    updatedAt: new Date()
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule,
        HttpClientModule
      ],
      providers: [
        UserService
      ]
    });

    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch a user by ID using the getById() method', () => {
    const userId = '1';

    service.getById(userId).subscribe((user) => {
      expect(user).toEqual(mockUser);
    });

    const req = httpMock.expectOne(`api/user/${userId}`); // Intercept the HTTP request
    expect(req.request.method).toBe('GET');             // Verify the request method
    req.flush(mockUser);                                // Simulate a successful response
  });

  it('should delete a user by ID using the delete() method', () => {
    const userId = '1';

    service.delete(userId).subscribe((response) => {
      expect(response).toEqual({ success: true });
    });

    const req = httpMock.expectOne(`api/user/${userId}`); // Intercept the HTTP request
    expect(req.request.method).toBe('DELETE');           // Verify the request method
    req.flush({ success: true });                        // Simulate a successful response
  });
});