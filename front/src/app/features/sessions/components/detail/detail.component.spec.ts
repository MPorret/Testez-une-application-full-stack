import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { expect } from '@jest/globals'; 
import '@testing-library/jest-dom/jest-globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { of } from 'rxjs';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { ActivatedRoute, Router } from '@angular/router';
import { getByTestId, queryByTestId } from '@testing-library/dom';
import { RouterTestingModule } from '@angular/router/testing';
import { Location } from '@angular/common';
import { ListComponent } from '../list/list.component';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 

  const sessionMock = {
    id: 42,
    name: "Session",
    description: "Description of session",
    date: new Date(),
    teacher_id: 7,
    users: [3, 18, 42],
  }

  const teacherMock = {
    id: 42,
    lastName: "User",
    firstName: "Test",
    createdAt: new Date(),
    updatedAt: new Date(),
  }
  
  const userMock = {
    token: "token",
    type: "type",
    id: 42,
    username: "UserTes",
    firstName: "User",
    lastName: "Test",
    admin: false,
  }

  const mockSessionService = {
    sessionInformation: userMock
  }

  const teacherServiceMock = {
    detail: jest.fn().mockReturnValue(of(teacherMock))
  }

  const sessionApiServiceMock = {
    detail: jest.fn().mockReturnValue(of(sessionMock)),
    participate: jest.fn().mockReturnValue(of()),
    unParticipate: jest.fn().mockReturnValue(of()),
    delete: jest.fn().mockReturnValue(of({})),
  }

  const routerMock = {
    navigate: jest.fn().mockReturnValue(of())
  }

  const routeMock = {
    snapshot: {
      paramMap: {
          get() {return '42'},
      },
    },
  }
  
  const matSnackBarMock = {
    open: jest.fn() 
  }

  const windowHistorySpy = jest.spyOn(window.history, "back").mockImplementation(() => {});

  describe("Unit tests", () => {
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [
          MatSnackBarModule,
          MatIconModule,
          MatCardModule,
          ReactiveFormsModule
        ],
        declarations: [DetailComponent], 
        providers: [
          { provide: SessionService, useValue: mockSessionService },
          { provide: SessionApiService, useValue: sessionApiServiceMock },
          { provide: TeacherService, useValue: teacherServiceMock },
          { provide: Router, useValue: routerMock },
          { provide: ActivatedRoute, useValue: routeMock },
          { provide: MatSnackBar, useValue: matSnackBarMock }
        ],
      })
        .compileComponents();
  
      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
    });

    test('should fetch session on init and teacher, session and isParticipate are initialized', () => {
      component.ngOnInit()
      expect(sessionApiServiceMock.detail).toBeCalledWith('42');
      expect(teacherServiceMock.detail).toBeCalledWith(sessionMock.teacher_id.toString())

      expect(component.teacher).toEqual(teacherMock)
      expect(component.session).toEqual(sessionMock)
      expect(component.isParticipate).toEqual(true)
    })
    
    test('should call window history when back', () => {
      component.back()
      expect(windowHistorySpy).toBeCalled()
    })
    
    test('should call open and navigate when delete session', () => {
      component.delete()
      expect(sessionApiServiceMock.delete).toBeCalledWith("42")
      expect(matSnackBarMock.open).toBeCalledWith('Session deleted !', 'Close', { duration: 3000 })
      expect(routerMock.navigate).toBeCalledWith(['sessions'])
    })
    
    test('should call participate', () => {
      component.participate()
      expect(sessionApiServiceMock.participate).toBeCalledWith('42', '42')
      expect(sessionApiServiceMock.detail).toBeCalledWith('42');
      expect(teacherServiceMock.detail).toBeCalledWith(sessionMock.teacher_id.toString())

      fixture.detectChanges()
      expect(component.teacher).toEqual(teacherMock)
      expect(component.session).toEqual(sessionMock)
      expect(component.isParticipate).toEqual(true)
    })
    
    test('should call unParticipate', () => {
      component.unParticipate()
      expect(sessionApiServiceMock.unParticipate).toBeCalledWith('42', '42')
      expect(sessionApiServiceMock.detail).toBeCalledWith('42');
      expect(teacherServiceMock.detail).toBeCalledWith(sessionMock.teacher_id.toString())

      fixture.detectChanges()
      expect(component.teacher).toEqual(teacherMock)
      expect(component.session).toEqual(sessionMock)
      expect(component.isParticipate).toEqual(true)
    })
  })

  describe('Integration tests', () => {
    let location: Location;

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [
          MatSnackBarModule,
          MatIconModule,
          MatCardModule,
          ReactiveFormsModule,
          RouterTestingModule.withRoutes([
            {path: 'sessions', component: ListComponent}
          ])
        ],
        declarations: [DetailComponent], 
        providers: [
          { provide: SessionService, useValue: mockSessionService },
          { provide: SessionApiService, useValue: sessionApiServiceMock },
          { provide: TeacherService, useValue: teacherServiceMock },
          { provide: ActivatedRoute, useValue: routeMock },
          { provide: MatSnackBar, useValue: matSnackBarMock }
        ],
      })
        .compileComponents();
  
      fixture = TestBed.createComponent(DetailComponent);
      location = TestBed.inject(Location);
      component = fixture.componentInstance;
    });

    test('should fetch data and get informations of the session', () => {
      fixture.detectChanges()
      expect(getByTestId(fixture.nativeElement, "teacher-name")).toContainHTML(`${teacherMock.firstName} ${teacherMock.lastName.toUpperCase()}`)
      expect(getByTestId(fixture.nativeElement, "number-attendees")).toContainHTML(`${sessionMock.users.length} attendees`)
    })

    test('as admin, should have the delete button visible and delete the session on click', () => {
      component.isAdmin = true
      fixture.detectChanges();

      // Check if delete button is visible
      expect(getByTestId(fixture.nativeElement, "delete-button")).toBeVisible();
      // Click on delete button
      getByTestId(fixture.nativeElement, "delete-button").click()
      // Expect session to be delete
      expect(sessionApiServiceMock.delete).toBeCalled()
      // Expect to be redirected on ListComponent
      expect(location.path()).toBe('/sessions')
    })

    test('as user not admin, should not have the delete button visible', () => {
      fixture.detectChanges()
      // Check if delete button is visible
      expect(queryByTestId(fixture.nativeElement, "delete-button")).toBeNull();
    })

    test('as user not admin, should have button unparticipate', () => {
      fixture.detectChanges()
      // Check if participate button is visible
      expect(getByTestId(fixture.nativeElement, "unparticipate-button")).toBeVisible()
      // Click on participate button
      getByTestId(fixture.nativeElement, "unparticipate-button").click()
      expect(sessionApiServiceMock.unParticipate).toBeCalled()
    })
  })
});

