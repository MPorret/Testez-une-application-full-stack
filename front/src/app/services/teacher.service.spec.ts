import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import '@testing-library/jest-dom/jest-globals';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

import { TeacherService } from './teacher.service';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  const teacherMock = {
    id: 7,
    lastName: "User",
    firstName: "Test",
    createdAt: new Date(),
    updatedAt: new Date(),
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule],
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController)
    httpMock.verify();
  });

  it('should get all teachers', (done) => {
    service.all().subscribe((teachers) => {
      expect(teachers.length).toBe(1)
      expect(teachers).toEqual([teacherMock])
      done()
    })
    
    const req = httpMock.expectOne({
      method: 'GET',
      url: 'api/teacher'
    });
    req.flush([teacherMock]);
  });

  it('should get a teacher', (done) => {
    service.detail('7').subscribe((teacher) => {
      expect(teacher).toEqual(teacherMock)
      done()
    })
    
    const req = httpMock.expectOne({
      method: 'GET',
      url: 'api/teacher/7'
    });
    req.flush(teacherMock);
  });
});
