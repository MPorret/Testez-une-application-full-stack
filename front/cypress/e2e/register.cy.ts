import { Session } from "../../src/app/features/sessions/interfaces/session.interface";

describe('Register spec', () => {
  const session: Session = {
    id: 42,
    name: "Session",
    description: "Description of session",
    date: new Date(),
    teacher_id: 7,
    users: [3, 18, 42],
    createdAt: new Date(),
    updatedAt: new Date()
  }

    beforeEach(() => {
      cy.visit('/register')
      cy.intercept('GET', '/api/session', {
        body: [session]
      })
    })
  
    it('Register successfull', () => {
      cy.intercept('POST', '/api/auth/register', {
        body: {
          id: 1,
          username: 'userName',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: true
        },
      })
  
      cy.get('input[formControlName=firstName]').type("User")
      cy.get('input[formControlName=lastName]').type("Test")
      cy.get('input[formControlName=email]').type("user@test.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
      cy.url().should('include', '/login')
    })
  
    it('Register failed, email already exist', () => {
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 409,
      error: 'email already exists'
      })
  
      cy.get('input[formControlName=firstName]').type("User")
      cy.get('input[formControlName=lastName]').type("Test")
      cy.get('input[formControlName=email]').type("user@test.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
      cy.get('[data-testid=register-error]').should('exist')
    })
  
    it('Mandatory field missing', () => {
      cy.get('input[formControlName=firstName]').type("User")
      cy.get('input[formControlName=lastName]').type("Test")
      cy.get('input[formControlName=password]').type(`${"test!1234"}`)
      cy.get('[data-testid=register-invalid-form]').should('be.disabled')
  
      cy.get('input[formControlName=email]').type("user@test.com")
      cy.get('input[formControlName=password]').clear()
      cy.get('[data-testid=register-invalid-form]').should('be.disabled')

      cy.get('input[formControlName=password]').type(`${"test!1234"}`)      
      cy.get('input[formControlName=firstName]').clear()
      cy.get('[data-testid=register-invalid-form]').should('be.disabled')

    })
  });