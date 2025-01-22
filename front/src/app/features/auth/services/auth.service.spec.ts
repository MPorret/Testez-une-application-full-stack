import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { AuthService } from "./auth.service"
import { expect } from '@jest/globals';
import { TestBed } from "@angular/core/testing";
import { HttpClientModule } from "@angular/common/http";
import { firstValueFrom } from "rxjs";
import { RegisterRequest } from "../interfaces/registerRequest.interface";
import { LoginRequest } from "../interfaces/loginRequest.interface";
import { SessionInformation } from "src/app/interfaces/sessionInformation.interface";

describe('AuthService', () => {
    let authService: AuthService;
    let httpTesting: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientModule,
                HttpClientTestingModule
            ]
        });
        httpTesting = TestBed.inject(HttpTestingController);
        authService = TestBed.inject(AuthService);
    });

    afterEach(() => {
        TestBed.inject(HttpTestingController).verify();
    });

    it('should be created', () => {
        expect(authService).toBeTruthy();
    });

    it('should allow user to register', () => {
        const registerRequest: RegisterRequest = {
            email: 'newuser@email.com',
            firstName: 'new',
            lastName: 'user',
            password: 'password'
        };
        firstValueFrom(authService.register(registerRequest));

        const req = httpTesting.expectOne({
            method: 'POST',
            url: 'api/auth/register'
        });

        req.flush({ status: 200 });

        expect(req.request.body).toEqual(registerRequest);
    });

    it('should allow user to login', async () => {
        const loginRequest: LoginRequest = {
            email: 'registered.user@email.com',
            password: 'password'
        };
        const sessionInfo$ = authService.login(loginRequest);
        const sessionInfoPromise = firstValueFrom(sessionInfo$);

        const req = httpTesting.expectOne({
            method: 'POST',
            url: 'api/auth/login'
        });

        const expectedSessionInfo: SessionInformation = {
            token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3Mzc1NTQ4NjEsImV4cCI6MTczNzY0MTI2MX0.cTKY1_YaucThVDHwweAz59fib8pgNV_OycpTFf6309OVf14Ae2Zj4THO1LD4jRy14dAt-gRLEXkP5ICEU2KJfA",
            type: "Bearer",
            id: 1,
            username: 'registered.user@email.com',
            firstName: 'Registered',
            lastName: "User",
            admin: false
        };

        req.flush(expectedSessionInfo);

        expect(await sessionInfoPromise).toEqual(expectedSessionInfo);
        expect(req.request.body).toEqual(loginRequest);
    });
})