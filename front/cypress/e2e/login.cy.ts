/// <reference types="cypress" />

describe('Login spec', () => {
  const session = {
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
    cy.intercept('GET', '/api/session', {
      body: [session]
    })
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      }
    })

    cy.visit('/login')
  })

  it('Login successfull', () => {
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

    cy.get('input[formControlName=password]').clear()
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('[data-testid=login-invalid-form]').should('be.disabled')
  })

  it('Log out', () => {
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.get('[data-testid=logout-button]').click()
    cy.get('[data-testid=logout-button]').should('not.exist')
    cy.get('[data-testid=login-menu]').should('exist')
    cy.url().should('eq', Cypress.config().baseUrl)
  })
});