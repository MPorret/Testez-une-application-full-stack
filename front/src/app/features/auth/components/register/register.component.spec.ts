import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { expect } from '@jest/globals';
import "@testing-library/jest-dom/jest-globals";

import { RegisterComponent } from './register.component';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { getByTestId } from '@testing-library/dom';
import { RouterTestingModule } from '@angular/router/testing';
import { LoginComponent } from '../login/login.component';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Location } from '@angular/common';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  const formMock: RegisterRequest =  {
    email: "user@test.com",
    firstName: "User",
    lastName: "Test",
    password: "test!31",
  }

  const invalidFormMock: RegisterRequest =  {
    email: "usertest.com",
    firstName: "User",
    lastName: "Test",
    password: "test!31",
  }

  const authServiceMock = {
    register: jest.fn()
  }

  describe('Unit tests', () => {
    const routerMock = {
      navigate: jest.fn()
    }

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [RegisterComponent],
        imports: [
          MatCardModule,
          MatFormFieldModule,
        ],
        providers: [
          { provide: AuthService, useValue: authServiceMock},
          { provide: Router, useValue: routerMock},
          FormBuilder
        ]
      })
        .compileComponents();
  
      fixture = TestBed.createComponent(RegisterComponent);
      component = fixture.componentInstance;
    });

    test('should call register and navigate', () => {
      component.form.setValue(formMock);
      authServiceMock.register.mockReturnValue(of({}));
      component.submit();
      expect(authServiceMock.register).toBeCalledWith(formMock);
      expect(routerMock.navigate).toBeCalledWith(['/login']);
    })
  
    test('should set onError to true when register', () => {
      component.form.setValue(formMock);
      authServiceMock.register.mockReturnValue(throwError(() => new Error('Error')));
      component.submit();
      expect(authServiceMock.register).toBeCalledWith(formMock);
      expect(component.onError).toBe(true);
    })
  })

  describe('Integration tests', () => {
    let router: Router;
    let location: Location;
    
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [RegisterComponent],
        imports: [
          MatCardModule,
          MatFormFieldModule,
          MatInputModule,
          BrowserAnimationsModule,
          ReactiveFormsModule,
          RouterTestingModule.withRoutes([
            { path: 'login', component: LoginComponent }
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
      fixture = TestBed.createComponent(RegisterComponent);
      component = fixture.componentInstance;
      router.initialNavigation();
    });

    test('should navigate to login page after submit', async () => {
      // Complete form with correct informations
      component.form.setValue(formMock);
      // Simulate a succeed response
      authServiceMock.register.mockReturnValue(of({}));
      component.submit();
      await fixture.whenStable();
      // Expect to be redirect to the login page
      expect(location.path()).toBe('/login');
    })

    test('should show an error message', () => {
      // Complete fom with correct informations
      component.form.setValue(formMock);
      // Simulate an error when the form is submit
      authServiceMock.register.mockReturnValue(throwError(() => new Error('Error')));
      component.submit();
      fixture.detectChanges();
      // Expect to show the error message
      expect(getByTestId(fixture.nativeElement, "register-error")).toBeVisible();
    })

    test('should disable the button when the form is invalid', () => {
      // Complete form with incorrect email format
      component.form.setValue(invalidFormMock);
      fixture.detectChanges();
      // Expect the button to be disabled
      expect(getByTestId(fixture.nativeElement, "register-invalid-form")).toBeDisabled();

      // Complete form with correct informations
      component.form.setValue(formMock);
      fixture.detectChanges();
      // Expect the button te bo enable
      expect(getByTestId(fixture.nativeElement, "register-invalid-form")).toBeEnabled();
    })
  })
});
