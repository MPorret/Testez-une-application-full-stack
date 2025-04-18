import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { describe, expect } from '@jest/globals';
import '@testing-library/jest-dom/jest-globals'
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { getByTestId } from '@testing-library/dom';
import { Location } from '@angular/common';
import { ListComponent } from '../list/list.component';
import { TeacherService } from 'src/app/services/teacher.service';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  
  const userMock = {
    token: "token",
    type: "type",
    id: 42,
    username: "UserTes",
    firstName: "User",
    lastName: "Test",
    admin: false,
  }

  const adminUserMock = {
    ...userMock,
    admin: true
  }

  const mockSessionService = {
    sessionInformation: adminUserMock
  } 

  const sessionMock: Session = {
    id: 42,
    name: "Session",
    description: "Description of session",
    date: new Date(),
    teacher_id: 7,
    users: [3, 18, 42],
  }

  const sessionFormMock = {
    name: "Session",
    description: "Description of session",
    date: new Date(),
    teacher_id: 7
  }

  const invalidSessionFormMock = {
    name: "Session",
    description: "Description of session",
    date: new Date(),
    teacher_id: "7"
  }

  const teacherMock = {
    id: 7,
    lastName: "User",
    firstName: "Test",
    createdAt: new Date(),
    updatedAt: new Date(),
  }

  const teacherServiceMock = {
    all: jest.fn().mockReturnValue(of([teacherMock]))
  }

  const sessionApiServiceMock = {
    create: jest.fn().mockReturnValue(of({})),
    update: jest.fn().mockReturnValue(of({})),
    detail: jest.fn().mockReturnValue(of(sessionMock))
  }

  const routerMock = {
    navigate: jest.fn().mockReturnValue(of()),
    url : {
      includes: jest.fn()
    }
  }

  const routeMock = {
    snapshot: {
      paramMap: {
          get: jest.fn().mockReturnValue("42"),
      },
    },
  }

  const matSnackBarMock = {
    open: jest.fn()
  }

  afterEach(() => {
    jest.clearAllMocks()
  })

  describe('Unit tests', () => {
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [
          RouterTestingModule,
          HttpClientModule,
          MatCardModule,
          MatIconModule,
          MatFormFieldModule,
          MatInputModule,
          ReactiveFormsModule,
          MatSelectModule,
          BrowserAnimationsModule
        ],
        providers: [
          { provide: SessionService, useValue: mockSessionService },
          { provide: SessionApiService, useValue: sessionApiServiceMock},
          { provide: ActivatedRoute, useValue: routeMock},
          { provide: Router, useValue: routerMock },
          { provide: MatSnackBar, useValue: matSnackBarMock }
        ],
        declarations: [FormComponent]
      })
        .compileComponents();
  
      fixture = TestBed.createComponent(FormComponent);
      component = fixture.componentInstance;
    });
    
    test('should call detail and init form with session informations on init component if url include update', () => {
      routerMock.url.includes.mockReturnValue(true)
      component.ngOnInit()
      expect(sessionApiServiceMock.detail).toBeCalledWith("42")
      expect(component.sessionForm?.controls['name'].value).toEqual(sessionMock.name)
      expect(component.sessionForm?.controls['date'].value).toEqual(new Date(sessionMock.date).toISOString().split('T')[0])
      expect(component.sessionForm?.controls['teacher_id'].value).toEqual(sessionMock.teacher_id)
      expect(component.sessionForm?.controls['description'].value).toEqual(sessionMock.description)
    })

    test('should init form on init component if url not include update', () => {
      routerMock.url.includes.mockReturnValue(false)
      component.ngOnInit()
      expect(sessionApiServiceMock.detail).not.toBeCalled()
      expect(component.sessionForm?.controls['name'].value).toEqual("")
      expect(component.sessionForm?.controls['date'].value).toEqual("")
      expect(component.sessionForm?.controls['teacher_id'].value).toEqual("")
      expect(component.sessionForm?.controls['description'].value).toEqual("")
    })

    test('should navigate if is not admin', () => {
      mockSessionService.sessionInformation = userMock
      component.ngOnInit()
      expect(routerMock.navigate).toBeCalledWith(['/sessions'])
    })

    test('should call create if onUpdate false and navigate on submit', () => {
      fixture.detectChanges()
      component.sessionForm?.setValue(sessionFormMock);
      component.submit()
      expect(sessionApiServiceMock.create).toBeCalledWith(sessionFormMock)
      expect(matSnackBarMock.open).toBeCalledWith('Session created !', 'Close', { duration: 3000 })
      expect(routerMock.navigate).toBeCalledWith(['/sessions'])
    })

    test('should call update if onUpdate true and navigate on submit', () => {
      routerMock.url.includes.mockReturnValue(true)
      fixture.detectChanges()
      component.submit()
      expect(sessionApiServiceMock.update).toBeCalledWith("42", {...sessionFormMock, date: sessionFormMock.date.toISOString().split('T')[0]})
      expect(matSnackBarMock.open).toBeCalledWith('Session updated !', 'Close', { duration: 3000 })
      expect(routerMock.navigate).toBeCalledWith(['/sessions'])
    })
  })

  describe('Integration tests', () => {
    let location: Location;
    let router: Router;

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [
          RouterTestingModule.withRoutes([
            {path: "sessions", component: ListComponent}
          ]),
          HttpClientModule,
          MatCardModule,
          MatIconModule,
          MatFormFieldModule,
          MatInputModule,
          ReactiveFormsModule,
          MatSelectModule,
          BrowserAnimationsModule
        ],
        providers: [
          { provide: SessionService, useValue: mockSessionService },
          { provide: SessionApiService, useValue: sessionApiServiceMock},
          { provide: ActivatedRoute, useValue: routeMock},
          { provide: MatSnackBar, useValue: matSnackBarMock },
          { provide: TeacherService, useValue: teacherServiceMock}
        ],
        declarations: [FormComponent]
      })
        .compileComponents();

      location = TestBed.inject(Location);
      router = TestBed.inject(Router)
      fixture = TestBed.createComponent(FormComponent);
      component = fixture.componentInstance;
    });

    test('should show Create session when url is "/" and init formSession', () => {
      fixture.detectChanges();
      expect(getByTestId(fixture.nativeElement, "title")).toHaveTextContent('Create session')
      // Check value of the form
      expect(getByTestId(fixture.nativeElement, "name")).toHaveValue("")
      expect(getByTestId(fixture.nativeElement, "date")).toHaveValue("")
      expect(getByTestId(fixture.nativeElement, "description")).toHaveValue("")
    })

    test('should show Update session when url is "/update" and init formSession with existing session', async () => {
      jest.spyOn(router, 'url', 'get').mockReturnValue('/update')
      fixture.detectChanges()
      await fixture.whenStable()
      fixture.detectChanges()
      expect(getByTestId(fixture.nativeElement, "title")).toHaveTextContent('Update session')
      // Check value of the form
      expect(getByTestId(fixture.nativeElement, "name")).toHaveValue(sessionFormMock.name);
      expect(getByTestId(fixture.nativeElement, "date")).toHaveValue(sessionFormMock.date.toISOString().split('T')[0]);
      expect(fixture.nativeElement.querySelector(".mat-select-min-line")).toHaveTextContent(`${teacherMock.firstName} ${teacherMock.lastName}`);
      expect(getByTestId(fixture.nativeElement, "description")).toHaveValue(sessionFormMock.description);
    })

    test('should navigate to sessions page after submit', async () => {
      // Complete form with correct informations
      component.sessionForm?.setValue(sessionFormMock);
      component.submit();
      await fixture.whenStable();
      // Expect to be redirect to the sessions page 
      expect(location.path()).toBe('/sessions');
    })

    test('should disable the button when the form is invalid', () => {
      // Complete form with incorrect teacher id format
      component.sessionForm?.setValue(invalidSessionFormMock);
      fixture.detectChanges();
      // Expect the button to be disabled
      expect(getByTestId(fixture.nativeElement, "session-button")).toBeDisabled();

      // Complete form with correct informations
      component.sessionForm?.setValue(sessionFormMock);
      fixture.detectChanges();
      // Expect the button te bo enable
      expect(getByTestId(fixture.nativeElement, "session-button")).toBeEnabled();
    })
  })
});
