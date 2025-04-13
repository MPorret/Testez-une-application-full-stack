import { ComponentFixture, TestBed } from '@angular/core/testing';
import { beforeEach, describe, expect, jest, test} from "@jest/globals";
import { getByTestId } from "@testing-library/dom"

import { MeComponent } from './me.component';
import { of } from 'rxjs';
import { User } from 'src/app/interfaces/user.interface';
import "@testing-library/jest-dom/jest-globals";
import { UserService } from 'src/app/services/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const userMock: User = {
    id: 42,
    email: "user@test.com",
    lastName: "User",
    firstName: "Test",
    admin: false,
    password: "Test!31",
    createdAt: new Date(),
    updatedAt: new Date(),
  }
  
  const userServiceMock = {
    getById: jest.fn(),
    delete: jest.fn()
  }

  const sessionServiceMock = {
    sessionInformation: {
      id: 42
    },
    logOut: jest.fn()
  }

  const matSnackBarMock = {
    open: jest.fn(),
  }

  const routerMock = {
    navigate: jest.fn(),
  }
  
  const windowHistorySpy = jest.spyOn(window.history, "back").mockImplementation(() => {});

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatCardModule,
        MatIconModule,
      ],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: Router, useValue: routerMock },
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
  })

  afterEach(() => {
    jest.clearAllMocks();
  })

  describe('Unit tests', () => {

    test('should call getById on ngOnInit and set user', () => {
      userServiceMock.getById.mockReturnValue(of(userMock));  
      component.ngOnInit();
      expect(userServiceMock.getById).toHaveBeenCalledWith('42');
      expect(component.user).toEqual(userMock);
    });

    test('should call window.history.back', () => {
      component.back();
      expect(windowHistorySpy).toBeCalled();
    })

    test('should call delete, open, logout and navigate', () => {
      userServiceMock.delete.mockReturnValue(of({}));  
      component.delete();
      expect(userServiceMock.delete).toHaveBeenCalledWith('42');
      
      expect(matSnackBarMock.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
      expect(sessionServiceMock.logOut).toHaveBeenCalled();
      expect(routerMock.navigate).toBeCalledWith(['/']);
    })
    
  })

  describe('Integration tests', () => {
    
    test('should go to the account page as user and get all my informations', () => {
      userServiceMock.getById.mockReturnValue(of(userMock));
      fixture.detectChanges();
      expect(getByTestId(fixture.nativeElement, "user-informations")).toBeVisible();
      expect(getByTestId(fixture.nativeElement, "user-name")).toHaveTextContent(`Name: ${userMock.firstName} ${userMock.lastName.toUpperCase()}`);
      expect(getByTestId(fixture.nativeElement, "user-delete")).toBeVisible();
    })

    test('should go to the account page as admin and see my role', () => {
      const adminMock: User = {
        ...userMock,
        admin: true
      }
      userServiceMock.getById.mockReturnValue(of(adminMock));
      fixture.detectChanges();
      expect(getByTestId(fixture.nativeElement, "user-admin")).toBeVisible();
    })

    test('should delete my profil and don\'t have my informations anymore', () => {
      userServiceMock.delete.mockReturnValue(of({})); 
      component.delete();
      expect(getByTestId(fixture.nativeElement, "user-card")).toBeEmptyDOMElement();
    })
  })
  
});
