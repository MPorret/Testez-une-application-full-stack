import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { Router } from '@angular/router';
import { of, take } from 'rxjs';


describe('AppComponent', () => {
  let app: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let sessionService: SessionService;
  let router: Router;

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
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;

    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
  });

  it('should create the app', () => {
    expect(app).toBeTruthy();
  });

  it('should allow user to log out', () => {

    const sessionServiceSpy = jest.spyOn(sessionService, 'logOut').mockImplementation(() => { });
    const routerSpy = jest.spyOn(router, 'navigate').mockResolvedValue(true);

    app.logout();

    expect(sessionServiceSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['']);

    sessionServiceSpy.mockRestore();
    routerSpy.mockRestore();
  });

  it('should return whether user is logged in or not', () => {

    const sessionServiceSpy = jest.spyOn(sessionService, '$isLogged').mockReturnValue(of(true));

    app.$isLogged()
      .pipe(take(1))
      .subscribe((isLogged) => {
        expect(isLogged).toBe(true);
      })

    sessionServiceSpy.mockRestore();
  });


});
