import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  const mockRouter={
    navigate:jest.fn()
  }

  const mockAuthService={
    register:jest.fn()
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers:[
        {provide:Router, useValue:mockRouter},
        {provide:AuthService, useValue:mockAuthService}
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should submit registerRequest to the service and navigate /login', () => {
    const registerRequest = {
      email: 'test@example.com',
     firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    };
    component.form.setValue(registerRequest);
    mockAuthService.register.mockReturnValue(of(true));
    console.log('Form values:', component.form.value);

    component.submit();
    expect(mockAuthService.register).toHaveBeenCalledWith(registerRequest);
   expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });
  it('should set onError to true if registration fails', () => {
    // Simuler les données du formulaire
    const registerRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    };
    component.form.setValue(registerRequest);

    // Simuler une réponse d'erreur lors de l'enregistrement
    mockAuthService.register.mockReturnValue(throwError('Registration failed'));

    // Appeler la méthode submit
    component.submit();

    // Vérifier que la méthode register a été appelée
    expect(mockAuthService.register).toHaveBeenCalledWith(registerRequest);

    // Vérifier que la propriété onError est définie à true
    expect(component.onError).toBe(true);
  });
});
