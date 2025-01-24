import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';


import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { of } from 'rxjs';
import { User } from 'src/app/interfaces/user.interface';
import { Router } from '@angular/router';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: () => { }
  }

  let matSnackBarMock: Partial<MatSnackBar> = {
    open: jest.fn()
  }


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: matSnackBarMock },
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should define user on Init', () => {

    const user: User = {
      id: 1,
      email: 'admin@email.com',
      lastName: 'admin',
      firstName: 'admin',
      admin: true,
      password: 'admin-password',
      createdAt: new Date('2024-01-01T15:00:00')
    }
    const userServiceSpy = jest.spyOn(userService, 'getById').mockReturnValue(of(user));

    component.ngOnInit();

    expect(userServiceSpy).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(user);

    userServiceSpy.mockRestore();
  });

  it('should allow user to delete account', () => {
    const userServiceSpy = jest.spyOn(userService, 'delete').mockReturnValue(of(undefined));
    const routerSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);
    const sessionServiceSpy = jest.spyOn(mockSessionService, 'logOut');
    const matSnackBarSpy = jest.spyOn(matSnackBarMock, 'open');

    component.delete();

    expect(userServiceSpy).toHaveBeenCalledWith('1');
    expect(matSnackBarSpy).toHaveBeenCalled();
    expect(sessionServiceSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['/']);

    userServiceSpy.mockRestore();
    sessionServiceSpy.mockRestore();
    matSnackBarSpy.mockRestore();
    routerSpy.mockRestore();
  });

  it('should allow user to go back', () => {

    const historyBackSpy = jest.spyOn(window.history, 'back').mockImplementation(() => { });

    component.back();

    expect(historyBackSpy).toHaveBeenCalled();

    historyBackSpy.mockRestore();
  });
});
