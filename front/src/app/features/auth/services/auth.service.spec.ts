import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';


describe('AuthService', () => {
  let authService: AuthService;
  let httpMock: HttpTestingController;
  let mockRegisterRequest:RegisterRequest;
  let mockLoginRequest:LoginRequest;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    authService = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    mockRegisterRequest = { email: 'testUser@test.com',
        firstName:'testFirstName',lastName:'testLastName', password: 'testPass' };
    mockLoginRequest = {
        email: 'testUser@test.com',password: 'testPass'
    }
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu'il n'y a pas de requêtes HTTP non résolues
  });

  it('should register a new user', () => {
   

    authService.register(mockRegisterRequest).subscribe(response => {
      expect(response).toBeUndefined // Le retour attendu est void (undefined)
    });

    const req = httpMock.expectOne(`${authService['pathService']}/register`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBe(mockRegisterRequest);

    req.flush(null); // Simule une réponse vide pour un Observable<void>
  });

  it('should login and return session information', () => {
    const mockSessionInfo: SessionInformation = {
     
        token: 'abc123',
        type:'',
        id: 1,
        username:'test@example.com',
        firstName:'test',
        lastName:'test',
        admin:false
      };
    authService.login(mockLoginRequest).subscribe(sessionInfo => {
      expect(sessionInfo).toBe(mockSessionInfo);
    });

    const req = httpMock.expectOne(`${authService['pathService']}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBe(mockLoginRequest);

    req.flush(mockSessionInfo); // Simule une réponse avec les informations de session
  });
});
