import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { firstValueFrom } from 'rxjs';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let sessionService: SessionApiService;
  let httpTesting: HttpTestingController;

  const sessions: Session[] = [
    {
      id: 2,
      name: 'Session2',
      date: new Date('2025-01-25T00:00:00'),
      teacher_id: 1,
      description: 'La session du 25 Janvier 2025 avec Margot',
      users: [
        2
      ],
      createdAt: new Date('2025-01-20T10:01:41'),
      updatedAt: new Date('2025-01-20T10:01:41')
    }
  ]

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    httpTesting = TestBed.inject(HttpTestingController);
    sessionService = TestBed.inject(SessionApiService);
  });

  afterEach(() => {
    TestBed.inject(HttpTestingController).verify();
  });

  it('should be created', () => {
    expect(sessionService).toBeTruthy();
  });

  it('should get all sessions', async () => {

    const sessions$ = sessionService.all();
    const sessionsPromise = firstValueFrom(sessions$);

    const req = httpTesting.expectOne({
      method: 'GET',
      url: 'api/session'
    });

    const expectedSessions = sessions;

    req.flush(expectedSessions);

    expect(await sessionsPromise).toEqual(expectedSessions);
  });

  it('should get a session detail', async () => {
    const session$ = sessionService.detail('2');
    const sessionPromise = firstValueFrom(session$);

    const req = httpTesting.expectOne({
      method: 'GET',
      url: 'api/session/2'
    });

    const expectedSession = sessions[0];

    req.flush(expectedSession);

    expect(await sessionPromise).toEqual(expectedSession);
  });

  it('should delete a session', () => {

    firstValueFrom(sessionService.delete('2'));

    const req = httpTesting.expectOne({
      method: 'DELETE',
      url: 'api/session/2'
    });

    req.flush({ status: 200 });
  });

  it('should create a session', async () => {

    const session: Session = {
      name: 'Session 23',
      description: 'Session 23 avec Margot',
      date: new Date('2025-01-22T14:00:00'),
      teacher_id: 1,
      users: []
    };

    const session$ = sessionService.create(session);
    const sessionPromise = firstValueFrom(session$);

    const req = httpTesting.expectOne({
      method: 'POST',
      url: 'api/session'
    });

    const expectedSession: Session = {
      id: 23,
      name: session.name,
      description: session.description,
      date: session.date,
      teacher_id: session.teacher_id,
      users: session.users,
      createdAt: new Date(),
      updatedAt: new Date()
    };

    req.flush(expectedSession);

    expect(await sessionPromise).toEqual(expectedSession);
    expect(req.request.body).toEqual(session);
  });

  it('should update a session', async () => {

    const sessionToBeUpdated: Session = {
      id: 23,
      name: 'New Session Name',
      date: new Date('2025-01-25T00:00:00'),
      teacher_id: 1,
      description: 'Description updated!',
      users: [
        2
      ]
    };

    const session$ = sessionService.update('23', sessionToBeUpdated);
    const sessionPromise = firstValueFrom(session$);

    const req = httpTesting.expectOne({
      method: 'PUT',
      url: 'api/session/23'
    });

    const expectedSession = sessionToBeUpdated;
    expectedSession.createdAt = new Date('2025-01-10T10:01:41');
    expectedSession.updatedAt = new Date();

    req.flush(expectedSession);

    expect(await sessionPromise).toEqual(expectedSession);
    expect(req.request.body).toEqual(sessionToBeUpdated);
  });

  it('should allow user to participate in a session', () => {

    firstValueFrom(sessionService.participate('2', '3'));

    const req = httpTesting.expectOne({
      method: 'POST',
      url: 'api/session/2/participate/3'
    });

    req.flush({ status: 200 });

    expect(req.request.body).toBeNull();
  })

  it('should allow user to cancel his participation', () => {

    firstValueFrom(sessionService.unParticipate('2', '3'));

    const req = httpTesting.expectOne({
      method: 'DELETE',
      url: 'api/session/2/participate/3'
    });

    req.flush({ status: 200 });

  });

});
