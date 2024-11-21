import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { of } from 'rxjs';
import { SessionService } from './services/session.service';
import { Router } from '@angular/router';


describe('AppComponent', () => {
  let component: AppComponent;

    // Mock for SessionService
    const mockSessionService = {
      $isLogged: jest.fn(),
      logOut:jest.fn()

    };

    const mockRouter={
      navigate : jest.fn()

    }
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers:[   
             { provide: SessionService, useValue: mockSessionService },
             { provide:Router, useValue:mockRouter }
      ]
    }).compileComponents();

  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });
  it('should call $isLogged on sessionService and return its Observable', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    mockSessionService.$isLogged.mockReturnValue(of(true));

    app.$isLogged().subscribe((isLogged: boolean) => {
      // Vérifier que le résultat est true
      expect(isLogged).toBe(true);
    });

    // Vérifier que la méthode $isLogged() de sessionService a bien été appelée
    expect(mockSessionService.$isLogged).toHaveBeenCalled();
  });

  it('should call logOut on sessionService and navigate to the home page on logout', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    app.logout();
    expect(mockSessionService.logOut).toHaveBeenCalled();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });
});
