import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { expect } from '@jest/globals';
import { User } from 'src/app/interfaces/user.interface';


describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;
  const mockSession: Session = {
    id: 123,
    name: 'Test Session',
    description: 'A sample session description',
    date: new Date('2024-10-18'),
    teacher_id: 1,
    users: [1, 2, 3],
    createdAt: new Date('2024-10-01'),
    updatedAt: new Date('2024-10-15')
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Utilisation classique du module de test HTTP
    });

    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Vérifie qu'aucune requête HTTP supplémentaire n'a été effectuée
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve session details by ID', () => {


    // Appeler la méthode `detail` du service
    service.detail('123').subscribe((session) => {
      // Vérifier que les données retournées correspondent à celles attendues
      expect(session).toEqual(mockSession);
    });

    // Intercepter la requête HTTP
    const req = httpTestingController.expectOne('api/session/123');
    expect(req.request.method).toBe('GET');

    // Simuler la réponse HTTP avec les données de la session
    req.flush(mockSession);
  });

  it('should retrieve an array of 2 sessions when all is called', ()=>{
    
    const mockSession2: Session = {
      id: 456,
      name: 'Test Session 2',
      description: 'A sample session description 2',
      date: new Date('2024-10-21'),
      teacher_id: 2,
      users: [1,  3],
      createdAt: new Date('2024-10-02'),
      updatedAt: new Date('2024-10-16')
    };
    const mockSessions: Session[] = [mockSession, mockSession2];

    // Appeler la méthode `all` du service
    service.all().subscribe((sessions:Session[]) => {
      // Vérifier que les données retournées correspondent à celles attendues
      expect(sessions).toEqual(mockSessions);
    });
    // Intercepter la requête HTTP
    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toBe('GET');

    // Simuler la réponse HTTP avec les données de la session
    req.flush(mockSession);

  })
  it('sould call delete method with 123 when delete is called', () => {
    service.delete('123').subscribe((session:Session) => {
      expect(session).toBe(mockSession)    
  });
  const req = httpTestingController.expectOne('api/session/123');
  expect(req.request.method).toBe('DELETE');

  req.flush(mockSession);

  });
  it('sould call create method with mockSession when create is called', () => {
    service.create(mockSession).subscribe((session:Session) => {
      expect(session).toBe(mockSession)    
  });
  const req = httpTestingController.expectOne('api/session');
  expect(req.request.method).toBe('POST');

  req.flush(mockSession);

  });
  it('sould call put http method with mockSession when update is called', () => {
    const mockSession2: Session = {
      id: 123,
      name: 'Test Session 2',
      description: 'A sample session description 2',
      date: new Date('2024-10-21'),
      teacher_id: 2,
      users: [1,  3],
      createdAt: new Date('2024-10-02'),
      updatedAt: new Date('2024-10-16')
    };
    service.update('123',mockSession2).subscribe((session:Session) => {
      expect(session).toBe(mockSession2)    
  });
  const req = httpTestingController.expectOne('api/session/123');
  expect(req.request.method).toBe('PUT');

  req.flush(mockSession2);

  });
  it('sould call post http method with a session for user when participate is called', () => {

    service.participate('123','222').subscribe(() => {

  });
  const req = httpTestingController.expectOne('api/session/123/participate/222');
  expect(req.request.method).toBe('POST');

  req.flush(mockSession);

  });
});
