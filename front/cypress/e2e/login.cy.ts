/// <reference types="cypress" />

describe('Login spec', () => {
  beforeEach(() => {
    cy.visit('/login')

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')
  })

  it('Login successfull', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  it('Login failed', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      error: 'email or password wrong'
    })
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!12"}{enter}{enter}`)

    cy.get('[data-testid=login-error]').should('exist')
  })

  it('Mandatory field missing', () => {
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)
    cy.get('[data-testid=login-invalid-form]').should('be.disabled')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').clear()
    cy.get('[data-testid=login-invalid-form]').should('be.disabled')
  })
});