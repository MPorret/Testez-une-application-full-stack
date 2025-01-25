import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { RegisterComponent } from "src/app/features/auth/components/register/register.component"
import { AuthService } from "src/app/features/auth/services/auth.service";
import { expect } from '@jest/globals';
import { HttpClientModule } from "@angular/common/http";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatCardModule } from "@angular/material/card";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { RouterTestingModule } from "@angular/router/testing";

describe('User Registration Integration Tests Suite', () => {
    let component: RegisterComponent;
    let fixture: ComponentFixture<RegisterComponent>;
    let httpTesting: HttpTestingController;
    let router: Router;

    let firstNameInput: HTMLInputElement;
    let lastNameInput: HTMLInputElement;
    let emailInput: HTMLInputElement;
    let passwordInput: HTMLInputElement;
    let submitButton: HTMLButtonElement;

    function initHTMLElements(fixture: ComponentFixture<RegisterComponent>) {
        firstNameInput = fixture.nativeElement.querySelector('input[formcontrolname="firstName"]');
        lastNameInput = fixture.nativeElement.querySelector('input[formcontrolname="lastName"]');
        emailInput = fixture.nativeElement.querySelector('input[formcontrolname="email"]');
        passwordInput = fixture.nativeElement.querySelector('input[formcontrolname="password"]');
        submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    }

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [RegisterComponent],
            imports: [
                ReactiveFormsModule,
                HttpClientModule,
                MatCardModule,
                MatFormFieldModule,
                MatInputModule,
                BrowserAnimationsModule,
                HttpClientTestingModule,
                RouterTestingModule
            ],
            providers: [AuthService]
        }).compileComponents();
        fixture = TestBed.createComponent(RegisterComponent);
        component = fixture.componentInstance;
        httpTesting = TestBed.inject(HttpTestingController);
        router = TestBed.inject(Router);
        fixture.detectChanges();
        initHTMLElements(fixture);

        jest.spyOn(router, 'navigate').mockResolvedValue(true);
    });

    it('should create the register component', () => {
        expect(component).toBeTruthy();
    });

    it('should enable submit button when all inputs are valid', () => {

        // provide valid inputs for first name, last name, email and password
        firstNameInput.value = 'Valid';
        firstNameInput.dispatchEvent(new Event('input'));
        lastNameInput.value = 'Valid';
        lastNameInput.dispatchEvent(new Event('input'));
        emailInput.value = 'valid@email.com';
        emailInput.dispatchEvent(new Event('input'));
        passwordInput.value = 'validpassword';
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        // the form is valid and the submit button is enabled
        expect(component.form.valid).toBe(true);
        expect(submitButton.disabled).toBe(false);
    });

    it('should disable submit button when email is not valid', () => {

        firstNameInput.value = 'Valid';
        firstNameInput.dispatchEvent(new Event('input'));
        lastNameInput.value = 'Valid';
        lastNameInput.dispatchEvent(new Event('input'));
        emailInput.value = 'invalid-email.com'; // invalid email
        emailInput.dispatchEvent(new Event('input'));
        passwordInput.value = 'validpassword';
        passwordInput.dispatchEvent(new Event('input'));

        fixture.detectChanges();

        // the form is not valid and the submit button is disabled
        expect(component.form.valid).toBe(false);
        expect(submitButton.disabled).toBe(true);
    });

    it('should allow user to register when the form is valid', () => {

        firstNameInput.value = 'Valid';
        firstNameInput.dispatchEvent(new Event('input'));
        lastNameInput.value = 'Valid';
        lastNameInput.dispatchEvent(new Event('input'));
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
            url: 'api/auth/register'
        });

        req.flush({ status: 200 }); // user registration is successful

        fixture.detectChanges();

        const errorMessage: HTMLSpanElement = fixture.nativeElement.querySelector('span.error');

        expect(errorMessage).toBeFalsy(); // no error message
        expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });

    it('should display error message if registration fails', () => {

        firstNameInput.value = 'Valid';
        firstNameInput.dispatchEvent(new Event('input'));
        lastNameInput.value = 'Valid';
        lastNameInput.dispatchEvent(new Event('input'));
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
            url: 'api/auth/register'
        });

        req.flush(
            { message: 'Error: Email is already taken!' }, // user registration failed
            { status: 400, statusText: 'Bad Request' }
        );

        fixture.detectChanges();

        const errorMessage: HTMLSpanElement = fixture.nativeElement.querySelector('span.error');

        expect(component.onError).toBe(true);
        expect(errorMessage.textContent).toEqual('An error occurred') // the error message is displayed
        expect(router.navigate).not.toHaveBeenCalled();
    });
})