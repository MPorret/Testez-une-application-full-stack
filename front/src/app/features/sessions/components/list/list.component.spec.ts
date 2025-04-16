import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import "@testing-library/jest-dom/jest-globals"

import { ListComponent } from './list.component';
import { of } from 'rxjs';
import { SessionApiService } from '../../services/session-api.service';
import { getAllByTestId, getByTestId, queryByTestId } from '@testing-library/dom';
import { Router, RouterModule } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const userMock = {
    token: "token",
    type: "type",
    id: 42,
    username: "UserTes",
    firstName: "User",
    lastName: "Test",
    admin: false,
  }

  const adminUserMock = {
    ...userMock,
    admin: true,
  }

  const sessionsMock = [{
    id: 42,
    name: "Session",
    description: "Description of session",
    date: new Date(),
    teacher_id: 7,
    users: [3, 18],
  }]

  const mockSessionService = {
    sessionInformation: userMock
  }
  
  const sessionApiServiceMock = {
    all: jest.fn().mockReturnValue(of(sessionsMock))
  }
  
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule, RouterTestingModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: sessionApiServiceMock}
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
  });

  describe('Unit tests', () => {
    test('should fetch sessions on init', () => {
      component.sessions$.subscribe((sessions) => {
        expect(sessions).toEqual(sessionsMock)
      })
      expect(sessionApiServiceMock.all).toBeCalled()
    })
    
    test('should get user', () => {
      expect(component.user).toEqual(userMock)
    })
  })

  describe('Integration tests', () => {
    test('should have all the sessions', () => {
      fixture.detectChanges();
      // Check if all sessions are visible
      expect(getAllByTestId(fixture.nativeElement, "session-card").length).toEqual(sessionsMock.length)
    })

    test('should have create button if is admin', () => {
      mockSessionService.sessionInformation = adminUserMock;
      fixture.detectChanges()
      // Check if the button "create" is visible
      expect(getByTestId(fixture.nativeElement, 'create-button')).toBeVisible()
    })

    test('should not have create button if is not admin', () => {
      // Check the button "create" is not in DOM
      expect(queryByTestId(fixture.nativeElement, 'create-button')).toBeNull()
    })
  })
});
