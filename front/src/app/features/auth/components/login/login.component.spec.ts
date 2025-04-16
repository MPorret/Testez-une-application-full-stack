import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import "@testing-library/jest-dom/jest-globals";
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { getByTestId } from '@testing-library/dom';
import { ListComponent } from 'src/app/features/sessions/components/list/list.component';
import { Location } from '@angular/common';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const responseMock = {
    token: "token",
    type: "type",
    id: 42,
    username: "UserTes",
    firstName: "User",
    lastName: "Test",
    admin: true,
  }

  const formMock ={
    email: "user@test.fr",
    password: "test!31",
  }

  const invalidFormMock ={
    email: "usertest.fr",
    password: "test!31",
  }

  const authServiceMock = {
    login: jest.fn()
  }

  const sessionServiceMock = {
    logIn: jest.fn()
  }

  describe("Unit tests", () => {
    const routerMock = {
      navigate: jest.fn()
    }

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [LoginComponent],
        providers: [
          {provide: SessionService, useValue: sessionServiceMock},
          {provide: AuthService, useValue: authServiceMock},
          {provide: Router, useValue: routerMock},
          FormBuilder
        ],
        imports: [
          MatCardModule,
          MatIconModule,
          MatFormFieldModule,
        ]
      })
        .compileComponents();
      fixture = TestBed.createComponent(LoginComponent);
      component = fixture.componentInstance;
    });

    test("should call login, logIn and navigate", () => {
      component.form.setValue(formMock);
      authServiceMock.login.mockReturnValue(of(responseMock))
      component.submit()
      expect(authServiceMock.login).toBeCalledWith(formMock)
      expect(sessionServiceMock.logIn).toBeCalledWith(responseMock);
      expect(routerMock.navigate).toBeCalledWith(['/sessions'])
    })

    test("should throw an error when submit", () => {
      component.form.setValue(formMock);
      authServiceMock.login.mockReturnValue(throwError(() => new Error("error")))
      component.submit()
      expect(authServiceMock.login).toBeCalledWith(formMock)
      expect(component.onError).toBe(true)
    })
  })

  describe("Integration tests", () => {
    let router: Router;
    let location: Location;
    
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [LoginComponent],
        imports: [
          MatCardModule,
          MatFormFieldModule,
          MatIconModule,
          MatInputModule,
          BrowserAnimationsModule,
          ReactiveFormsModule,
          RouterTestingModule.withRoutes([
            { path: 'sessions', component: ListComponent }
          ])
        ],
        providers: [
          { provide: AuthService, useValue: authServiceMock},
          FormBuilder
        ]
      })
        .compileComponents();

      router = TestBed.inject(Router);
      location = TestBed.inject(Location);
      fixture = TestBed.createComponent(LoginComponent);
      component = fixture.componentInstance;
      router.initialNavigation();
    });

    test('should navigate to login page after submit', async () => {
      // Complete form with correct informations
      component.form.setValue(formMock);
      // Simulate a succeed response
      authServiceMock.login.mockReturnValue(of({}));
      component.submit();
      await fixture.whenStable();
      // Expect to be redirect to the login page 
      expect(location.path()).toBe('/sessions');
    })

    test('should show an error message', () => {
      // Complete fom with correct informations
      component.form.setValue(formMock);
      // Simulate an error when the form is submit
      authServiceMock.login.mockReturnValue(throwError(() => new Error('Error')));
      component.submit();
      fixture.detectChanges();
      // Expect to show the error message
      expect(getByTestId(fixture.nativeElement, "login-error")).toBeVisible();
    })

    test('should disable the button when the form is invalid', () => {
      // Complete form with incorrect email format
      component.form.setValue(invalidFormMock);
      fixture.detectChanges();
      // Expect the button to be disabled
      expect(getByTestId(fixture.nativeElement, "login-invalid-form")).toBeDisabled();

      // Complete form with correct informations
      component.form.setValue(formMock);
      fixture.detectChanges();
      // Expect the button te bo enable
      expect(getByTestId(fixture.nativeElement, "login-invalid-form")).toBeEnabled();
    })
  })
});
