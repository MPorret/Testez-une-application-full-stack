import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { LoginRequest } from '../../interfaces/loginRequest.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let router: Router;
  let authService: AuthService;
  let sessionService: SessionService;

  const loginRequest: LoginRequest = {
    email: 'user@test.com',
    password: 'password'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [AuthService, SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    component.form.controls['email'].setValue(loginRequest.email);
    component.form.controls['password'].setValue(loginRequest.password);

    router = TestBed.inject(Router);
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should allow user to log in', () => {

    const sessionInfo: SessionInformation = {
      token: 'token',
      type: 'Bearer',
      id: 1,
      username: 'user@test.com',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: false
    };

    const routerSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
    const authServiceSpy = jest.spyOn(authService, 'login').mockReturnValue(of(sessionInfo));
    const sessionServiceSpy = jest.spyOn(sessionService, 'logIn').mockImplementation(() => { });

    expect(component.form.valid).toBe(true);

    component.submit();

    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
    expect(authServiceSpy).toHaveBeenCalledWith(loginRequest);
    expect(sessionServiceSpy).toHaveBeenCalledWith(sessionInfo);

    routerSpy.mockRestore();
    authServiceSpy.mockRestore();
    sessionServiceSpy.mockRestore();
  });

  it('should set onError to true when an error occurs', () => {

    const errorResponse = new Error('Network error');
    const authServiceSpy = jest.spyOn(authService, 'login').mockReturnValue(throwError(() => errorResponse));

    expect(component.form.valid).toBe(true);

    component.submit();

    expect(authServiceSpy).toHaveBeenCalledWith(loginRequest);
    expect(component.onError).toBe(true);

    authServiceSpy.mockRestore();
  })
});
