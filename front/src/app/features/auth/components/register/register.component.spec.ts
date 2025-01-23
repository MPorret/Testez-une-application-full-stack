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
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RouterTestingModule } from '@angular/router/testing';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let router: Router;
  let authService: AuthService;

  const registerRequest: RegisterRequest = {
    email: 'unregistered.user@email.com',
    firstName: 'unregistered',
    lastName: 'user',
    password: 'password'
  };


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [AuthService],
      imports: [
        RouterTestingModule,
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

    component.form.controls['email'].setValue(registerRequest.email);
    component.form.controls['firstName'].setValue(registerRequest.firstName);
    component.form.controls['lastName'].setValue(registerRequest.lastName);
    component.form.controls['password'].setValue(registerRequest.password);

    router = TestBed.inject(Router);
    authService = TestBed.inject(AuthService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should allow user to register', () => {
    const routerSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
    const authServiceSpy = jest.spyOn(authService, 'register').mockReturnValue(of(undefined));;

    expect(component.form.valid).toBe(true);

    component.submit();

    expect(component.onError).toBe(false);
    expect(routerSpy).toHaveBeenCalledWith(['/login']);
    expect(authServiceSpy).toHaveBeenCalledWith(registerRequest);

    routerSpy.mockRestore();
    authServiceSpy.mockRestore();
  });

  it('should set onError to true when an error occurs', () => {
    const errorResponse = new Error('Network error');
    const authServiceSpy = jest.spyOn(authService, 'register').mockReturnValue(throwError(() => errorResponse));

    expect(component.form.valid).toBe(true);

    component.submit();

    expect(component.onError).toBe(true);
    expect(authServiceSpy).toHaveBeenCalledWith(registerRequest);

    authServiceSpy.mockRestore();
  });


});
