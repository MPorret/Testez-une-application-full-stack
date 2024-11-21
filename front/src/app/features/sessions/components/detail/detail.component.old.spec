import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';


describe('DetailComponent unit test suites', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let mockRouter={
    navigate:jest.fn()

  }
  const mockSessionApiService ={
    delete:jest.fn().mockReturnValue(of(void 0))
  }

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
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
      providers: [{ provide: SessionService, useValue: mockSessionService },
        {provide:SessionApiService,useValue: mockSessionApiService},
        {provide:Router,useValue:mockRouter}
      ],
    })
      .compileComponents();
      service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
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

  it('should call sessionApiService.delete and navigate to sessions list',()=>{
    const sessionIdToBeDelted:String = "123456"
    component.delete();
    expect(mockSessionApiService.delete).toHaveBeenCalledWith(sessionIdToBeDelted)
    expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions'])

  })
});

