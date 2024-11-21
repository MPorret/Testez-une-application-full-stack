import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;
  const mockUser:User={
    id:123,
    email:'toto@test.com',
    lastName:'Toto',
    firstName:'toto',
    password:'223322',
    admin:false,
    createdAt:new Date(),
    updatedAt:new Date()


  }


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,HttpClientTestingModule
      ]
    });
    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });
  afterEach(() => {
    // Vérifie qu'aucune requête HTTP supplémentaire n'a été effectuée
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should getById when getById is called',()=>{
    service.getById('123').subscribe((user)=>{
      expect(user).toBe(mockUser);
    })
    const req = httpTestingController.expectOne('api/user/123')
    expect(req.request.method).toBe('GET')
    req.flush(mockUser);
    
  })
  it('should run delete on backend when delete is called',()=>{
    service.delete('123').subscribe((user)=>{
      expect(user).toBe(mockUser);
    })
    const req = httpTestingController.expectOne('api/user/123')
    expect(req.request.method).toBe('DELETE')

    req.flush(mockUser);
  })

});
