import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';
import { Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { TeacherService } from 'src/app/services/teacher.service';
import { Teacher } from 'src/app/interfaces/teacher.interface';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let router: Router;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  const session: Session = {
    id: 23,
    name: 'Session 23',
    description: 'Session 23 description',
    date: new Date('2025-01-01T15:00:00'),
    teacher_id: 1,
    users: [1]
  };

  const teacher: Teacher = {
    id: 1,
    lastName: 'Margot',
    firstName: 'DELAHAYE',
    createdAt: new Date('2020-01-01T10:00:00'),
    updatedAt: new Date('2020-01-01T10:00:00')
  };

  let matSnackBarMock: Partial<MatSnackBar> = {
    open: jest.fn()
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: matSnackBarMock }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should allow user to go back', () => {

    const historyBackSpy = jest.spyOn(window.history, 'back').mockImplementation(() => { });

    component.back();

    expect(historyBackSpy).toHaveBeenCalled();

    historyBackSpy.mockRestore();

  });

  it('should allow a user to delete the session', () => {

    const sessionApiServiceSpy = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of(undefined));
    const routerSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
    const matSnackBarSpy = jest.spyOn(matSnackBarMock, 'open');

    component.sessionId = '23';
    component.delete();

    expect(sessionApiServiceSpy).toHaveBeenCalledWith(component.sessionId);
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
    expect(matSnackBarSpy).toHaveBeenCalled();

    sessionApiServiceSpy.mockRestore();
    routerSpy.mockRestore();
    matSnackBarSpy.mockRestore();
  });

  it('should allow user to participate in a session', () => {

    const sessionApiServiceParticipateSpy = jest.spyOn(sessionApiService, 'participate').mockReturnValue(of(undefined));
    const sessionApiServiceDetailSpy = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(session));
    const teacherServiceSpy = jest.spyOn(teacherService, 'detail').mockReturnValue(of(teacher));

    component.sessionId = session.id!.toString();
    component.userId = mockSessionService.sessionInformation.id.toString();

    component.participate();

    expect(sessionApiServiceParticipateSpy).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect(sessionApiServiceDetailSpy).toHaveBeenCalledWith(component.sessionId);
    expect(teacherServiceSpy).toHaveBeenCalledWith(teacher.id.toString());
    expect(component.session).toEqual(session);
    expect(component.isParticipate).toBe(true);
    expect(component.teacher).toEqual(teacher);

    sessionApiServiceParticipateSpy.mockRestore();
    sessionApiServiceDetailSpy.mockRestore();
    teacherServiceSpy.mockRestore();
  });

  it('should allow user to unparticipate to a session', () => {

    const sessionApiServiceUnParticipateSpy = jest.spyOn(sessionApiService, 'unParticipate').mockReturnValue(of(undefined));
    session.users = [];
    const sessionApiServiceDetailSpy = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(session));
    const teacherServiceSpy = jest.spyOn(teacherService, 'detail').mockReturnValue(of(teacher));

    component.sessionId = session.id!.toString();
    component.userId = mockSessionService.sessionInformation.id.toString();

    component.unParticipate();

    expect(sessionApiServiceUnParticipateSpy).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect(sessionApiServiceDetailSpy).toHaveBeenCalledWith(component.sessionId);
    expect(teacherServiceSpy).toHaveBeenCalledWith(teacher.id.toString());
    expect(component.session).toEqual(session);
    expect(component.isParticipate).toBe(false);
    expect(component.teacher).toEqual(teacher);

    sessionApiServiceUnParticipateSpy.mockRestore();
    sessionApiServiceDetailSpy.mockRestore();
    teacherServiceSpy.mockRestore();

  });


});

