import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let activatedRoute: ActivatedRoute;
  let sessionApiService: SessionApiService;
  let matSnackBar: MatSnackBar;
  let sessionServiceMock: Partial<SessionService> = {
    sessionInformation: {
      token: 'token',
      type: 'Bearer',
      id: 1,
      username: 'admin',
      firstName: 'admin',
      lastName: 'admin',
      admin: true
    }
  };

  let matSnackBarMock: Partial<MatSnackBar> = {
    open: jest.fn()
  }

  const session: Session = {
    id: 23,
    name: 'Session 23',
    description: 'description of session 23',
    date: new Date('2025-01-01T18:00:00'),
    teacher_id: 1,
    users: []
  };

  beforeEach(async () => {

    sessionServiceMock.sessionInformation!.admin = true;

    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        SessionApiService
      ],
      declarations: [FormComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sessionApiService = TestBed.inject(SessionApiService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not allow non admin users to create or update a session', () => {

    sessionServiceMock.sessionInformation!.admin = false;

    const routerSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);

    component.ngOnInit();

    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);

    routerSpy.mockRestore();

  });

  it('it should allow admin to view the update form for a session', () => {
    const routerSpy = jest.spyOn(router, 'url', 'get').mockReturnValue('/update/23');
    const activatedRouteSpy = jest.spyOn(activatedRoute.snapshot.paramMap, 'get').mockImplementation((key) => {
      if (key === 'id') return '23';
      return null;
    });

    const sessionApiServiceSpy = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(session));

    component.ngOnInit();

    expect(component.onUpdate).toBe(true);
    expect(sessionApiServiceSpy).toHaveBeenCalledWith('23');
    expect(component.sessionForm).toBeDefined();

    routerSpy.mockRestore();
    activatedRouteSpy.mockRestore();
    sessionApiServiceSpy.mockRestore();
  });

  it('should allow admin user to create a session', () => {

    component.sessionForm?.controls['name'].setValue(session.name);
    component.sessionForm?.controls['date'].setValue(session.date);
    component.sessionForm?.controls['teacher_id'].setValue(session.teacher_id);
    component.sessionForm?.controls['description'].setValue(session.description);

    const sessionApiServiceSpy = jest.spyOn(sessionApiService, 'create').mockReturnValue(of(session));
    const routerSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
    const matSnackBarSpy = jest.spyOn(matSnackBarMock, 'open');

    expect(component.sessionForm?.valid).toBe(true);

    component.submit();

    expect(sessionApiServiceSpy).toHaveBeenCalled();
    expect(matSnackBarSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);

    sessionApiServiceSpy.mockRestore();
    routerSpy.mockRestore();
    matSnackBarSpy.mockRestore();
  });

  it('should allow admin user to update a session', () => {

    component.sessionForm?.controls['name'].setValue(session.name);
    component.sessionForm?.controls['date'].setValue(session.date);
    component.sessionForm?.controls['teacher_id'].setValue(session.teacher_id);
    component.sessionForm?.controls['description'].setValue(session.description);

    component.onUpdate = true;

    const sessionApiServiceSpy = jest.spyOn(sessionApiService, 'update').mockReturnValue(of(session));
    const routerSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
    const matSnackBarSpy = jest.spyOn(matSnackBarMock, 'open');

    expect(component.sessionForm?.valid).toBe(true);

    component.submit();

    expect(sessionApiServiceSpy).toHaveBeenCalled();
    expect(matSnackBarSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);

    sessionApiServiceSpy.mockRestore();
    routerSpy.mockRestore();
    matSnackBarSpy.mockRestore();
  });
});
