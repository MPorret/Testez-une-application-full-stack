import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { firstValueFrom } from 'rxjs';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let teacherService: TeacherService;
  let httpTesting: HttpTestingController;

  const teachers: Teacher[] = [
    {
      id: 1,
      lastName: 'DELAHAYE',
      firstName: 'Margot',
      createdAt: new Date('2025-01-20T09:36:43'),
      updatedAt: new Date('2025-01-20T09:36:43')
    },
    {
      id: 2,
      lastName: 'THIERCELIN',
      firstName: 'Hélène',
      createdAt: new Date('2025-01-20T09:36:43'),
      updatedAt: new Date('2025-01-20T09:36:43')
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    httpTesting = TestBed.inject(HttpTestingController);
    teacherService = TestBed.inject(TeacherService);
  });

  afterEach(() => {
    TestBed.inject(HttpTestingController).verify();
  });

  it('should be created', () => {
    expect(teacherService).toBeTruthy();
  });

  it('should get all teachers', async () => {

    const teachers$ = teacherService.all();
    const teachersPromise = firstValueFrom(teachers$);

    const req = httpTesting.expectOne({
      method: 'GET',
      url: 'api/teacher'
    });

    const expectedTeachers = teachers;

    req.flush(expectedTeachers);

    expect(await teachersPromise).toEqual(expectedTeachers);
  });

  it('should get teacher detail', async () => {

    const teacher$ = teacherService.detail('1');
    const teacherPromise = firstValueFrom(teacher$);

    const req = httpTesting.expectOne({
      method: 'GET',
      url: 'api/teacher/1'
    });

    const expectedTeacher: Teacher = teachers[0];

    req.flush(expectedTeacher);

    expect(await teacherPromise).toEqual(expectedTeacher);
  });
});
