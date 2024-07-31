import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { Router } from '@angular/router';
import { NgZone } from '@angular/core';


/**
 * Test suite for the `RegisterComponent`.
 * This suite covers the validation logic for the registration form and the integration with the authentication service.
 */
describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;
  let ngZone: NgZone;

  const authServiceMock = {
    register: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    ngZone = TestBed.inject(NgZone);
    fixture.detectChanges();
  });

  /**
   * Unit test suite for the registration form in `RegisterComponent`.
   * This suite includes tests for form validation errors and correctness.
   */
  describe('Register Unit Test suite', ()=> {
    
    it('should create', () => {
      expect(component).toBeTruthy();
    });

    /**
     * Tests that an error is shown when the email field is empty.
     * Verifies that the email input has the `ng-invalid` class when the value is empty.
     */
    it('should show error when email is empty', () => {
      component.form.controls['email'].setValue('');

      component.form.controls['email'].markAsTouched();
      component.form.controls['email'].updateValueAndValidity();

      fixture.detectChanges();

      const emailInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="email"]');

      expect(emailInput.classList).toContain('ng-invalid');
    });

    /**
     * Tests that an error is shown when an invalid email is entered.
     * Verifies that the email input has the `ng-invalid` class when the value is invalid.
     */
    it('should show error when email is invalid', () => {

      component.form.controls['email'].setValue('invalidEmail');

      component.form.controls['email'].markAsTouched();
      component.form.controls['email'].updateValueAndValidity();

      fixture.detectChanges(); 

      const emailInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="email"]');

      expect(emailInput.classList).toContain('ng-invalid');
    });

    /**
     * Tests that an error is shown when the password field is empty.
     * Verifies that the password input has the `ng-invalid` class when the value is empty.
     */
    it('should show error when password is empty', () => {
      component.form.controls['password'].setValue('');

      component.form.controls['password'].markAsTouched();
      component.form.controls['password'].updateValueAndValidity();

      fixture.detectChanges();

      const passwordInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="password"]');

      expect(passwordInput.classList).toContain('ng-invalid');
    });

    /**
     * Tests that an error is shown when the password length is less than 3 characters.
     * Verifies that the password input has the `ng-invalid` class when the value is too short.
     */
    it('should show error when password length < 3', () => {
      component.form.controls['password'].setValue('te');

      component.form.controls['password'].markAsTouched();
      component.form.controls['password'].updateValueAndValidity();

      fixture.detectChanges();

      const passwordInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="password"]');

      expect(passwordInput.classList).toContain('ng-invalid');
    });

    /**
     * Tests that an error is shown when the password length exceeds 40 characters.
     * Verifies that the password input has the `ng-invalid` class when the value is too long.
     */
    it('should show error when password length > 40', () => {
      component.form.controls['password'].setValue('This is a string with exactly forty-one chars.');

      component.form.controls['password'].markAsTouched();
      component.form.controls['password'].updateValueAndValidity();

      fixture.detectChanges();

      const passwordInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="password"]');

      expect(passwordInput.classList).toContain('ng-invalid');
    });

    /**
     * Tests that an error is shown when the first name field is empty.
     * Verifies that the first name input has the `ng-invalid` class when the value is empty.
     */
    it('should show error when firstName is empty', () => {
      component.form.controls['firstName'].setValue('');

      component.form.controls['firstName'].markAsTouched();
      component.form.controls['firstName'].updateValueAndValidity();

      fixture.detectChanges();

      const firstNameInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="firstName"]');

      expect(firstNameInput.classList).toContain('ng-invalid');
    });

    
    it('should show error when firstName length < 3', () => {
      component.form.controls['firstName'].setValue('te');

      component.form.controls['firstName'].markAsTouched();
      component.form.controls['firstName'].updateValueAndValidity();

      fixture.detectChanges();

      const firstNameInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="firstName"]');

      expect(firstNameInput.classList).toContain('ng-invalid');
    });

    /**
     * Tests that an error is shown when the first name length exceeds 20 characters.
     * Verifies that the first name input has the `ng-invalid` class when the value is too long.
     */
    it('should show error when firstName length > 20', () => {
      component.form.controls['firstName'].setValue('This is exactly twenty-one');

      component.form.controls['firstName'].markAsTouched();
      component.form.controls['firstName'].updateValueAndValidity();

      fixture.detectChanges();

      const firstNameInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="firstName"]');

      expect(firstNameInput.classList).toContain('ng-invalid');
    });

    /**
     * Tests that an error is shown when the last name field is empty.
     * Verifies that the last name input has the `ng-invalid` class when the value is empty.
     */
    it('should show error when lastName is empty', () => {
      component.form.controls['lastName'].setValue('');

      component.form.controls['lastName'].markAsTouched();
      component.form.controls['lastName'].updateValueAndValidity();

      fixture.detectChanges();

      const lastNameInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="lastName"]');

      expect(lastNameInput.classList).toContain('ng-invalid');
    });

    /**
     * Tests that an error is shown when the last name length is less than 3 characters.
     * Verifies that the last name input has the `ng-invalid` class when the value is too short.
     */
    it('should show error when lastName length < 3', () => {
      component.form.controls['lastName'].setValue('te');

      component.form.controls['lastName'].markAsTouched();
      component.form.controls['lastName'].updateValueAndValidity();

      fixture.detectChanges();

      const lastNameInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="lastName"]');

      expect(lastNameInput.classList).toContain('ng-invalid');
    });

    /**
     * Tests that an error is shown when the last name length exceeds 20 characters.
     * Verifies that the last name input has the `ng-invalid` class when the value is too long.
     */
    it('should show error when lastName length > 20', () => {
      component.form.controls['lastName'].setValue('This is exactly twenty-one');

      component.form.controls['lastName'].markAsTouched();
      component.form.controls['lastName'].updateValueAndValidity();

      fixture.detectChanges();

      const lastNameInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="lastName"]');

      expect(lastNameInput.classList).toContain('ng-invalid');
    });

    /**
     * Tests that no error is shown when the last name is valid.
     * Verifies that the last name input has the `ng-valid` class when the value is valid.
     */
    it('should show no error when lastName is valid', () => {
      component.form.controls['lastName'].setValue('Portier');
 
      component.form.controls['lastName'].markAsTouched();
      component.form.controls['lastName'].updateValueAndValidity();

      fixture.detectChanges();

      const lastNameInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="lastName"]');

      expect(lastNameInput.classList).toContain('ng-valid');
    })

    /**
     * Tests that no error is shown when the first name is valid.
     * Verifies that the first name input has the `ng-valid` class when the value is valid.
     */
    it('should show no error when firstName is valid', () => {
      component.form.controls['firstName'].setValue('Romain');
 
      component.form.controls['firstName'].markAsTouched();
      component.form.controls['firstName'].updateValueAndValidity();

      fixture.detectChanges();

      const firstNameInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="firstName"]');

      expect(firstNameInput.classList).toContain('ng-valid');
    })

    /**
     * Tests that no error is shown when the email is valid.
     * Verifies that the email input has the `ng-valid` class when the value is valid.
     */
    it('should show no error when email is valid', () => {
      component.form.controls['email'].setValue('test@test.com');
 
      component.form.controls['email'].markAsTouched();
      component.form.controls['email'].updateValueAndValidity();

      fixture.detectChanges();

      const emailInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="email"]');

      expect(emailInput.classList).toContain('ng-valid');
    })

    /**
     * Tests that no error is shown when the password is valid.
     * Verifies that the password input has the `ng-valid` class when the value is valid.
     */
    it('should show no error when password is valid', () => {

      component.form.controls['password'].setValue('test');

      component.form.controls['password'].markAsTouched();
      component.form.controls['password'].updateValueAndValidity();

      fixture.detectChanges();

      const passwordInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="password"]');

      expect(passwordInput.classList).toContain('ng-valid');
    })

    /**
     * Tests that an error message is displayed when the registration fails.
     * Verifies that the error message is shown when the registration service returns an error.
     */
    it('should display "An error occured" when login after fails', () => {

      (authService.register as jest.Mock).mockReturnValue(throwError(() => new Error('Register failed')));

      component.submit();

      fixture.detectChanges();

      const errorElement = fixture.debugElement.nativeElement.querySelector('.error');

      expect(errorElement).toBeTruthy();
      expect(errorElement.textContent).toContain('An error occurred');
    })
  });

  /**
   * Integration test suite for the `RegisterComponent`.
   * This suite verifies the interaction between the component and the authentication service.
   */
  describe('Register Integration Test suite', ()=>{
      const registerRequest: RegisterRequest = {
        email: "test@test.com",
        firstName: "firstName",
        lastName: "lastName",
        password: "password",
    }

    /**
     * Tests that the registration process completes successfully and navigates to the login page.
     * Verifies that the `register` method is called with the correct request and that navigation occurs.
     */
    it('should register user and navigate to /login', ()=>{
      const registerSpy = jest.spyOn(authService, 'register').mockReturnValue(of(void 0));
      const navigateSpy = jest.spyOn(router, 'navigate');

      component.form.controls['email'].setValue(registerRequest.email);
      component.form.controls['password'].setValue(registerRequest.password);
      component.form.controls['firstName'].setValue(registerRequest.firstName);
      component.form.controls['lastName'].setValue(registerRequest.lastName);
      fixture.detectChanges();

      ngZone.run(() => {
        component.submit();
      });
      fixture.detectChanges();

      
      expect(registerSpy).toHaveBeenCalledWith(registerRequest);
      expect(navigateSpy).toHaveBeenCalledWith(['/login']);
    })
  })
});
