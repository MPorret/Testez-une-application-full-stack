import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { Router, ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

describe('DetailComponent unit test suites', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  // Mock services
  const mockRouter = {
    navigate: jest.fn()
  };
  const mockSessionApiService = {
    delete: jest.fn().mockReturnValue(of(void 0)),
    participate: jest.fn().mockReturnValue(of(void 0)),
    unParticipate: jest.fn().mockReturnValue(of(void 0)),
    detail: jest.fn().mockReturnValue(of({
      id: '123',
      users: [1],
      teacher_id: 1
    }))
  };
  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };
  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: (key: string) => '123' // Example session ID
      }
    }
  };
 

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([]), // Configure RouterTestingModule with empty routes
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,NoopAnimationsModule,MatCardModule,MatIconModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute } // Provide mock ActivatedRoute
      ]
    }).compileComponents();

    // Create the component and initialize it
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    // Trigger initial data binding and lifecycle hooks
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
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

  it('should call sessionApiService.delete and navigate to sessions list', () => {
    // Set the component's sessionId to match the expected value
    component.sessionId = '123';
  
    // Call the delete method
    component.delete();
  
      // Verify that sessionApiService.delete was called with the correct session ID
      expect(mockSessionApiService.delete).toHaveBeenCalledWith('123');
  
      // Verify that the router navigated to the 'sessions' list
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
  
      // Mark the test as done
   
  });


});
