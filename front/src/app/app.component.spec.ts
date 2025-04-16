import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import "@testing-library/jest-dom/jest-globals";

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { getByTestId } from '@testing-library/dom';


describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>
  let app: AppComponent

  const sessionServiceMock = {
    $isLogged: jest.fn(),
    logOut: jest.fn()
  }

  const routerMock = {
    navigate: jest.fn()
  }

  describe('Unit tests', () => {
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
        providers: [
          {provide: SessionService, useValue: sessionServiceMock},
          {provide: Router, useValue: routerMock}
        ]
      }).compileComponents();
      fixture = TestBed.createComponent(AppComponent);
      app = fixture.componentInstance;
    });

    test('$isLogged should to be true', () => {
      sessionServiceMock.$isLogged.mockReturnValue(of(true))
      app.$isLogged().subscribe(
        (isLogged) => expect(isLogged).toBe(true)
      )
      expect(sessionServiceMock.$isLogged).toBeCalled();
    })

    test('should call logOut and navigate', () => {
      app.logout();
      expect(sessionServiceMock.logOut).toBeCalled;
      expect(routerMock.navigate).toBeCalledWith([''])
    })
  })

  describe('Integration tests', () => {

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        imports: [
          RouterTestingModule,
          HttpClientModule,
          MatToolbarModule,
          RouterTestingModule.withRoutes([
            {path: "", component: AppComponent}
          ])
        ],
        declarations: [
          AppComponent
        ],
        providers: [
          {provide: SessionService, useValue: sessionServiceMock}
        ]
      }).compileComponents();
      fixture = TestBed.createComponent(AppComponent);
      app = fixture.componentInstance;
    });

    test('if if logged, should see log out and, on click, be unlogged', () => {
      sessionServiceMock.$isLogged.mockReturnValue(of(true))
      app.$isLogged()
      fixture.detectChanges()
      expect(getByTestId(fixture.nativeElement, "logged-menu")).toBeVisible();
      getByTestId(fixture.nativeElement, "logout-button").click()
      expect(sessionServiceMock.logOut).toBeCalled()
    })

    test('should see Login and Register if is not logged', () => {
      sessionServiceMock.$isLogged.mockReturnValue(of(false))
      app.$isLogged()
      fixture.detectChanges()
      expect(getByTestId(fixture.nativeElement, "register-menu")).toBeVisible();
      expect(getByTestId(fixture.nativeElement, "login-menu")).toBeVisible();
    })
  })
});
