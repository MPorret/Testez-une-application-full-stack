import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { ListComponent } from '../list/list.component';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Teacher } from 'src/app/interfaces/teacher.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let httpTestingController:HttpTestingController;
  let sessionApiService: SessionApiService;

  const mockSession: Session = {
    id: 123,
    name: 'test',
    description: 'session de yoga',
    date: new Date('2024-10-18'), // Exemple de date, à remplacer par la date souhaitée
    teacher_id: 1, // Exemple d'identifiant du professeur
    users: [1, 2, 3], // Liste d'identifiants d'utilisateurs participant à la session
    createdAt: new Date('2024-10-01'), // Exemple de date de création
    updatedAt: new Date('2024-10-15') // Exemple de date de mise à jour
  };
  const mockSessionService = {
    sessionInformation: {
      token: 'abc123',
      type:'',
      id: 1,
      username:'test@example.com',
      firstName:'test',
      lastName:'test',
      admin: false
    }
  } 
  const mockRouter   = {
    navigate : jest.fn(),
    url: '/sessions'
  }
  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: (key: string) => '123' // Example session ID
      }
    }
  }
    const mockSessionApiService={
      detail : jest.fn().mockReturnValue(of(mockSession)),
      create : jest.fn().mockReturnValue(of(mockSession))
   
  };


  beforeEach(async () => {

    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule.withRoutes([]),
        HttpClientModule,HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,NoopAnimationsModule
      ],
      providers: [
        {provide:Router, useValue:mockRouter},
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        {provide : SessionApiService, useValue: mockSessionApiService}
         
      ],
      declarations: [FormComponent,ListComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    httpTestingController = TestBed.inject(HttpTestingController);
    

    fixture.detectChanges();
  });
  afterEach(() => {
    // Vérifie qu'aucune requête HTTP supplémentaire n'a été effectuée
    httpTestingController.verify();
  });

  it('should create component', () => {
      // Mock the GET request for 'api/teacher' before verifying the test
  const mockTeachers = [{ id: 1, name: 'Teacher 1' }, { id: 2, name: 'Teacher 2' }];
  const teacherReq = httpTestingController.expectOne('api/teacher');
  expect(teacherReq.request.method).toBe('GET');
  teacherReq.flush(mockTeachers); // Provide a mock response for the GET request

  // Now the test can proceed to check for any POST requests and other behaviors
  component.ngOnInit();
    expect(component).toBeTruthy();

  });

  it('should  navigate to /sessions if not admin', ()=>{
    mockSessionService.sessionInformation.admin=false;
    component.ngOnInit();
    const teacher:Teacher={id:123,firstName:'',lastName:'',createdAt:new Date(),
      updatedAt:new Date()
    }
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    const req = httpTestingController.expectOne('api/teacher')
    req.flush(teacher)
  })

  it('should run init Form if url include does not include update',()=>{
    mockSessionService.sessionInformation.admin=false
    mockRouter.url='api/session'
    component.ngOnInit();
    expect(component.sessionForm?.get('name')?.value).toBe('')
    const teacher:Teacher={id:123,firstName:'',lastName:'',createdAt:new Date(),
      updatedAt:new Date()
    }
    const req = httpTestingController.expectOne('api/teacher')
    req.flush(teacher)
  })
it('should update ',()=>{
  mockRouter.url='api/session/update'
  component.ngOnInit();
  expect(component.onUpdate).toBeTruthy();
  const teacher:Teacher={id:123,firstName:'',lastName:'',createdAt:new Date(),
    updatedAt:new Date()
  }
  const req = httpTestingController.expectOne('api/teacher')
  req.flush(teacher)
})
  it('should call submit with create test case',()=>{
    
    component.onUpdate=false
    component.sessionForm!.setValue({
      name: 'testName',
      date: new Date(), // format as yyyy-MM-dd
      teacher_id: 1,
      description: 'article bidule'
    });
    const mockSession = component.sessionForm?.value as Session;

    component.submit();

   
    const teacher:Teacher={id:123,firstName:'',lastName:'',createdAt:new Date(),
      updatedAt:new Date()
    }
    const req2 = httpTestingController.expectOne('api/teacher')
    req2.flush(teacher)

    
  })

  it('should display a message and navigate to /sessions', () => {
    // Arrange
    const message = 'Test message';
    const snackBarSpy = jest.spyOn(component['matSnackBar'], 'open');
    const routerSpy = jest.spyOn(component['router'], 'navigate');
  
    // Act
    component['exitPage'](message);
  
    // Assert
    expect(snackBarSpy).toHaveBeenCalledWith(message, 'Close', { duration: 3000 });
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
   const teacher:Teacher={id:123,firstName:'',lastName:'',createdAt:new Date(),
      updatedAt:new Date()
    }
    const req2 = httpTestingController.expectOne('api/teacher');
    req2.flush(teacher);
  })

})