import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { LoginComponent } from 'src/app/features/auth/components/login/login.component';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { SessionService } from 'src/app/services/session.service';


describe('User Login Integration Tests Suite', () => {
    let component: LoginComponent;
    let fixture: ComponentFixture<LoginComponent>;
    let httpTesting: HttpTestingController;
    let router: Router;
    let sessionService: SessionService;

    let emailInput: HTMLInputElement;
    let passwordInput: HTMLInputElement;
    let submitButton: HTMLButtonElement;

    const sessionInfo: SessionInformation = {
        token: 'token',
        type: 'Bearer',
        id: 10,
        username: 'valid@email.com',
        firstName: 'valid',
        lastName: 'user',
        admin: false
    };

    function initHTMLElements(fixture: ComponentFixture<LoginComponent>) {
        emailInput = fixture.nativeElement.querySelector('input[formcontrolname="email"]');
        passwordInput = fixture.nativeElement.querySelector('input[formcontrolname="password"]');
        submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    }

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [LoginComponent],
            imports: [
                HttpClientModule,
                ReactiveFormsModule,
                MatCardModule,
                MatFormFieldModule,
                MatIconModule,
                MatInputModule,
                BrowserAnimationsModule,
                HttpClientTestingModule,
                RouterTestingModule
            ],
            providers: [AuthService, SessionService]
        }).compileComponents();

        fixture = TestBed.createComponent(LoginComponent);
        component = fixture.componentInstance;
        httpTesting = TestBed.inject(HttpTestingController);
        router = TestBed.inject(Router);
        sessionService = TestBed.inject(SessionService);
        fixture.detectChanges();
        initHTMLElements(fixture);

        jest.spyOn(router, 'navigate').mockResolvedValue(true);
    });

    it('should create the login component', () => {
        expect(component).toBeTruthy();
    })

    it('should enable submit button when the email and password are valid', () => {

        // provide valid inputs for email and password
        emailInput.value = 'valid@email.com';
        emailInput.dispatchEvent(new Event('input'));
        passwordInput.value = 'validpassword';
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        // the form is valid and the submit button is enabled
        expect(component.form.valid).toBe(true);
        expect(submitButton.disabled).toBe(false);
    });

    it('should disable submit button when email is invalid', () => {

        emailInput.value = 'invalid-email.com'; // invalid email input
        emailInput.dispatchEvent(new Event('input'));
        passwordInput.value = 'validpassword';
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        expect(component.form.valid).toBe(false);
        expect(submitButton.disabled).toBe(true); // submit button is disabled
    });

    it('should disable submit button when password is invalid', () => {

        emailInput.value = 'valid@email.com';
        emailInput.dispatchEvent(new Event('input'));
        passwordInput.value = ''; // invalid password input
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        expect(component.form.valid).toBe(false);
        expect(submitButton.disabled).toBe(true); // submit button is disabled
    });

    it('should allow user to login when credentials are correct', () => {

        emailInput.value = 'valid@email.com';
        emailInput.dispatchEvent(new Event('input'));
        passwordInput.value = 'validpassword';
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        expect(component.form.valid).toBe(true);
        expect(submitButton.disabled).toBe(false);

        submitButton.click();

        const req = httpTesting.expectOne({
            method: 'POST',
            url: 'api/auth/login'
        });

        req.flush(sessionInfo); // login is successful

        fixture.detectChanges();

        const errorMessage: HTMLParagraphElement = fixture.nativeElement.querySelector('p.error');

        expect(sessionService.isLogged).toBe(true); // user is logged in
        expect(errorMessage).toBeFalsy(); // no error message
        expect(sessionService.sessionInformation).toEqual(sessionInfo);
        expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
        expect(component.onError).toBe(false);
    });

    it('should display error message when credentials are incorrect', () => {

        emailInput.value = 'valid@email.com';
        emailInput.dispatchEvent(new Event('input'));
        passwordInput.value = 'incorrect-password';
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        expect(component.form.valid).toBe(true);
        expect(submitButton.disabled).toBe(false);

        submitButton.click();

        const req = httpTesting.expectOne({
            method: 'POST',
            url: 'api/auth/login'
        });

        req.flush({
            path: '/api/auth/login',
            error: 'Unauthorized',
            message: 'Bad credentials',
            status: 401
        }, { status: 401, statusText: 'Bad credentials' }); // login failed

        fixture.detectChanges();

        const errorMessage: HTMLParagraphElement = fixture.nativeElement.querySelector('p.error');

        expect(sessionService.isLogged).toBe(false); // user is not logged in
        expect(errorMessage.textContent).toEqual('An error occurred'); // the error message is displayed
        expect(sessionService.sessionInformation).toBeUndefined();
        expect(router.navigate).not.toHaveBeenCalled();
        expect(component.onError).toBe(true);
    });
})