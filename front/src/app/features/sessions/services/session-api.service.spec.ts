import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const pathService = 'api/session';

  const sessionMock: Session = {
    id: 42,
    name: "Session",
    description: "Description of session",
    date: new Date(),
    teacher_id: 7,
    users: [3, 18, 42],
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
    httpMock.verify()
  });

  it('should get all sessions', (done) => {
    service.all().subscribe((sessions) => {
      expect(sessions.length).toEqual(1)
      expect(sessions).toEqual([sessionMock])
      done()
    })

    const req = httpMock.expectOne({
      method: 'GET',
      url: pathService
    })
    req.flush([sessionMock])
  });

  it('should get detail of session', (done) => {
    service.detail('42').subscribe((session) => {
      expect(session).toEqual(sessionMock)
      done()
    })

    const req = httpMock.expectOne({
      method: 'GET',
      url: `${pathService}/42`
    })
    req.flush(sessionMock)
  });

  it('should delete session', (done) => {
    service.delete('42').subscribe(() => {
      done()
    })

    const req = httpMock.expectOne({
      method: 'DELETE',
      url: `${pathService}/42`
    })
    req.flush({})
  });

  it('should create session', (done) => {
    service.create(sessionMock).subscribe((session) => {
      expect(session).toEqual(sessionMock)
      done()
    })

    const req = httpMock.expectOne({
      method: 'POST',
      url: pathService
    })
    expect(req.request.body).toEqual(sessionMock)
    req.flush(sessionMock)
  });

  it('should update session', (done) => {
    service.update('42', sessionMock).subscribe((session) => {
      expect(session).toEqual(sessionMock)
      done()
    })

    const req = httpMock.expectOne({
      method: 'PUT',
      url: `${pathService}/42`
    })
    expect(req.request.body).toEqual(sessionMock)
    req.flush(sessionMock)
  });

  it('should add a participation', (done) => {
    service.participate('42', '25').subscribe(() => {
      done()
    })

    const req = httpMock.expectOne({
      method: 'POST',
      url: `${pathService}/42/participate/25`
    })
    req.flush({})
  });

  it('should delete a participation', (done) => {
    service.unParticipate('42', '3').subscribe(() => {
      done()
    })

    const req = httpMock.expectOne({
      method: 'DELETE',
      url: `${pathService}/42/participate/3`
    })
    req.flush({})
  });
});
