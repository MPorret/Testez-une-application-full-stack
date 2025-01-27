import { ComponentFixture, TestBed, waitForAsync } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatSelectModule } from "@angular/material/select";
import { By } from "@angular/platform-browser";
import { BrowserAnimationsModule, NoopAnimationsModule } from "@angular/platform-browser/animations";
import { of } from "rxjs";
import { FormComponent } from "src/app/features/sessions/components/form/form.component"
import { RouterTestingModule } from "@angular/router/testing";
import { MatSnackBar, MatSnackBarModule } from "@angular/material/snack-bar";
import { HttpClientModule } from "@angular/common/http";
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { MatCardModule } from "@angular/material/card";
import { MatIconModule } from "@angular/material/icon";
import { SessionService } from "src/app/services/session.service";
import { MatInputModule } from "@angular/material/input";
import { expect } from "@jest/globals";
import { Router } from "@angular/router";



describe('Session Creation Integration Tests Suite', () => {
    let component: FormComponent;
    let fixture: ComponentFixture<FormComponent>;
    let httpTesting: HttpTestingController;
    let router: Router;

    let sessionServiceMock: Partial<SessionService> = {
        sessionInformation: {
            token: 'token',
            type: 'Bearer',
            id: 1,
            username: 'admin@yoga.com',
            firstName: 'admin',
            lastName: 'admin',
            admin: true
        }
    };

    let matSnackBarMock: Partial<MatSnackBar> = {
        open: jest.fn()
    }

    beforeEach(waitForAsync(() => {
        TestBed.configureTestingModule({
            declarations: [FormComponent],
            imports: [
                ReactiveFormsModule,
                MatCardModule,
                MatSelectModule,
                MatFormFieldModule,
                MatSnackBarModule,
                MatIconModule,
                MatInputModule,
                BrowserAnimationsModule,
                RouterTestingModule,
                HttpClientModule,
                HttpClientTestingModule,
                NoopAnimationsModule
            ],
            providers: [
                { provide: SessionService, useValue: sessionServiceMock },
                { provide: MatSnackBar, useValue: matSnackBarMock }
            ]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(FormComponent);
        component = fixture.componentInstance;
        httpTesting = TestBed.inject(HttpTestingController);
        router = TestBed.inject(Router);

        component.teachers$ = of([
            {
                id: 1,
                lastName: 'DELAHAYE',
                firstName: 'Margot',
                createdAt: new Date('2025-01-20T09:36:43'),
                updatedAt: new Date('2025-01-20T09:36:43')
            }
        ]);

        fixture.detectChanges();

        jest.spyOn(router, 'navigate').mockResolvedValue(true);
    });

    it('should allow admin to create a session', async () => {

        // Set session name
        const nameInput = fixture.debugElement.query(By.css('input[formcontrolname="name"]'));
        nameInput.nativeElement.value = 'New Session';
        nameInput.nativeElement.dispatchEvent(new Event('input'));

        // Set session date
        const dateInput = fixture.debugElement.query(By.css('input[formcontrolname="date"]'));
        dateInput.nativeElement.value = '2025-01-01';
        dateInput.nativeElement.dispatchEvent(new Event('input'));

        // Set session description
        const descriptionInput = fixture.debugElement.query(By.css('textarea[formcontrolname="description"]'));
        descriptionInput.nativeElement.value = 'description';
        descriptionInput.nativeElement.dispatchEvent(new Event('input'));

        // Set session teacher
        const matSelect = fixture.debugElement.query(By.css('.mat-select-trigger'));
        matSelect.nativeElement.click();
        fixture.detectChanges();
        await fixture.whenStable();
        fixture.detectChanges();
        const options = fixture.debugElement.queryAll(By.css('mat-option'));
        options[0].nativeElement.click();
        fixture.detectChanges();

        // Click on submit button
        const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));
        submitButton.nativeElement.click();

        expect(component.onUpdate).toBe(false);

        const req = httpTesting.expectOne({
            method: 'POST',
            url: 'api/session'
        });

        req.flush({
            id: 23,
            name: 'New Session',
            description: 'description',
            date: new Date('2025-01-01'),
            teacher_id: 1,
            users: [],
            createdAt: new Date(),
            updatedAt: new Date()
        });

        expect(matSnackBarMock.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
        expect(router.navigate).toHaveBeenCalledWith(['sessions']);
    });
})