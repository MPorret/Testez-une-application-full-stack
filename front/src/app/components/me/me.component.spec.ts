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
import { User } from 'src/app/interfaces/user.interface';
import { UserService } from 'src/app/services/user.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('MeComponent', () => {
  let component: MeComponent;
  let service: UserService;

  let fixture: ComponentFixture<MeComponent>;
  const mockUserService = {
    getById: jest.fn().mockReturnValue(
      of({
        id: 1,
        email: 'john@example.com',
        lastName: 'Doe',
        firstName: 'John',
        admin: true,
        password: 'hashedpassword',
        createdAt: new Date(),
      } as User)
    ),
  
    delete: jest.fn().mockReturnValue(of({})) // Simule un retour d'Observable vide après suppression
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()

  }

  const mockRouter = {
    navigate: jest.fn() // Simule l'appel à navigate() du Router
  };

  const mockMatSnackBar = {
    open: jest.fn() // Simule l'appel à open() pour afficher un message avec MatSnackBar
  };
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
      providers: [   { provide: UserService, useValue: mockUserService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    service = TestBed.inject(UserService);

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  
  it('should set the user property on ngOnInit', () => {
    component.ngOnInit();

    expect(service.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(expect.objectContaining({
      id: 1,
      email: 'john@example.com',
      lastName: 'Doe',
      firstName: 'John',
      admin: true,
    }));
  });

  it('should call window.history.back when back() is called', () => {
    // Spy sur window.history.back
    const historyBackSpy = jest.spyOn(window.history, 'back');
  
    // Appel de la méthode back
    component.back();
  
    // Vérification que window.history.back a été appelé
    expect(historyBackSpy).toHaveBeenCalled();
  
    // Nettoyage du spy après le test
    historyBackSpy.mockRestore();
  });


  it('should delete the user, show a snackbar message, log out, and navigate to home', () => {
    // Appel de la méthode delete
    component.delete();

    // Vérifier que userService.delete a été appelé avec le bon ID
    expect(service.delete).toHaveBeenCalledWith('1');

    // Simuler l'abonnement de l'observable de delete et vérifier que les autres méthodes ont été appelées
    expect(mockMatSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });

});
