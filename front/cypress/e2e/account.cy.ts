import { User } from '../../src/app/interfaces/user.interface';
import { Session } from '../../src/app/features/sessions/interfaces/session.interface'

describe('Account spec', () => {
    const user: User = {
        id: 1,
        email: 'yoga@studio.com',
        password: 'test!1234',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false,
        createdAt: new Date()
    }

    const admin: User = {
        ...user,
        admin: true
    }
    
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
        cy.intercept('POST', '/api/auth/login', {
            body: user,
        })
        cy.intercept('GET', '/api/session', {
          body: [session]
        })
        cy.intercept('GET', '/api/user/1', {
            body: user
        })
        cy.intercept('DELETE', '/api/user/1', {})
    
        cy.visit('/login')
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    })

    it('See all user informations and go back', () => {
        cy.get('[routerLink=me]').click()
        cy.get('[data-testid=user-card]')
        .should('contain.text', `${user.firstName} ${user.lastName.toUpperCase()}`)
        .and('contain.text', user.email)
        .and('contain.text', user.createdAt.toLocaleDateString('en-US', {year: 'numeric', month: 'long', day: 'numeric' }))
        cy.get('[data-testid=back-button]').click()
        cy.url().should('eq', Cypress.config().baseUrl + 'sessions')
    })

    it('Delete user account', () => {
        cy.get('[routerLink=me]').click()
        cy.get('[data-testid=delete-button]').click()
        cy.get('[routerLink=me]').should('not.exist')
        cy.get('[data-testid=login-menu]').should('exist')
        cy.url().should('eq', Cypress.config().baseUrl)
    })

    it('See user is admin', () => {
        cy.intercept('GET', '/api/user/1', {
            body: admin
        })
        cy.get('[routerLink=me]').click()
        cy.get('[data-testid=user-admin]').should('be.visible')
    })
})