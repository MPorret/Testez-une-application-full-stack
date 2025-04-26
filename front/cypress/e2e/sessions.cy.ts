/// <reference types="cypress" />

describe('Sessions spec', () => {
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

    const sessionParticipate = {
        ...session,
        users: [1, 3, 18, 42],
    }

    const admin = {
        id: 7,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
    }

    const user = {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
    }

    const teacher = {
        id: 7,
        lastName: 'Teacher',
        firstName: 'One',
        createdAt: new Date(),
        updatedAt: new Date(),
    }

    beforeEach(() => {       
        cy.intercept('GET', 'api/session', {
            body: [session]
        })

        cy.intercept('GET', 'api/session/42', {
            body: session
        })
        cy.intercept('GET', 'api/teacher/7', {
            body : teacher
        })
    })

    describe('Admin features', () => {
        beforeEach(() => {
            cy.intercept('POST', '/api/auth/login', {
              body: admin,
            })
            cy.intercept('DELETE', '/api/session/42', {})
            cy.intercept('PUT', '/api/session/42', {})
            cy.intercept('GET', '/api/teacher', {
                body: [teacher]
            })
            cy.intercept('POST', '/api/session', {})
        
            cy.visit('/login')
            cy.get('input[formControlName=email]').type("yoga@studio.com")
            cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        })
    
        it('List of sessions', () => {
            cy.get('[data-testid=session-card]').should('have.length', 1)
            cy.get('[data-testid=session-card]').should('contain.text', session.name).and('contain.text', session.date.toLocaleDateString('en-US', {year: 'numeric', month: 'long', day: 'numeric' }))
            cy.get('mat-card-content').should('contain.text', session.description)
        })

        it('Create a session, as admin', () => {
            cy.get('[data-testid=create-button]').click()
            cy.get('input[formControlName=name]').type("My session")
            cy.get('input[formControlName=date]').type('2029-12-07')
            cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains(teacher.lastName).click()
            cy.get('textarea[formControlName=description]').type("This is the description of my session")
            cy.get('[data-testid=session-button').click()
            cy.url().should('eq', Cypress.config().baseUrl + 'sessions')
        })

        it('Mandatory field not complete on creation session form, as admin', () => {
            cy.get('[data-testid=create-button]').click()      
            cy.get('input[formControlName=name]').type("My session")
            cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains(teacher.lastName).click()
            cy.get('textarea[formControlName=description]').type("This is the description of my session")
            cy.get('[data-testid=session-button').should('be.disabled')
        })

        it('Delete session, as admin', () => {
            cy.get('[data-testid=detail-button]').click()
            cy.get('[data-testid=delete-button]').click()
            cy.url().should('eq', Cypress.config().baseUrl + "sessions")          
        })

        it('Update a session, as admin', () => {
            cy.get('[data-testid=update-button]').click()
            cy.get('input[formControlName=date]').type('2029-12-07')
            cy.get('[data-testid=session-button').click()
            cy.url().should('eq', Cypress.config().baseUrl + 'sessions')        
        })

        it('Mandatory field not complete on update session form, as admin', () => {
            cy.get('[data-testid=update-button]').click()    
            cy.get('input[formControlName=date]').type('2029-12-07')
            cy.get('textarea[formControlName=description]').clear()
            cy.get('[data-testid=session-button').should('be.disabled')        
        })

        it('Admin can\'t participate at session', () => {
            cy.get('[data-testid=detail-button]').click()
            cy.get('[data-testid=participate-button]').should('not.exist')
        })
    })

    describe('User features', () => {
        beforeEach(() => {
            cy.intercept('POST', '/api/auth/login', {
              body: user,
            })        
            cy.intercept('POST', 'api/session/42/participate/1', {})
            cy.intercept('DELETE', 'api/session/42/participate/1', {})

            cy.visit('/login')
            cy.get('input[formControlName=email]').type("yoga@studio.com")
            cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        })

        it('User can\'t create session', () => {
            cy.get('[data-testid=create-button]').should('not.exist')
        })

        it('See all informations of session and go back to sessions\' list', () => {
            cy.get('[data-testid=detail-button]').click()
            cy.get('h1').should('contain.text', session.name)
            cy.get('[data-testid=teacher-name]').should('contain.text', `${teacher.firstName} ${teacher.lastName.toUpperCase()}`)
            cy.get('[data-testid=number-attendees').should('contain.text', session.users.length.toString())
            cy.get('.description').should('contain.text', session.description)
            cy.get('[data-testid=session-date]').should('contain.text', session.date.toLocaleDateString('en-US', {year: 'numeric', month: 'long', day: 'numeric' }))
            cy.get('.created').should('contain.text', session.createdAt.toLocaleDateString('en-US', {year: 'numeric', month: 'long', day: 'numeric' }))
            cy.get('.updated').should('contain.text', session.updatedAt.toLocaleDateString('en-US', {year: 'numeric', month: 'long', day: 'numeric' }))
            cy.get('[data-testid=back-button').click()
            cy.url().should('eq', Cypress.config().baseUrl + 'sessions')  
        })

        it('User can\'t delete or update session', () => {
            cy.get('[data-testid=detail-button]').click()
            cy.get('[data-testid=update-button]').should('not.exist')
            cy.get('[data-testid=delete-button]').should('not.exist')
        })

        it('User can participate or unparticipate a session', () => {
            cy.get('[data-testid=detail-button]').click()
            cy.get('[data-testid=participate-button]').should('exist')

            cy.intercept('GET', 'api/session/42', {
                body: sessionParticipate
            })
            cy.get('[data-testid=participate-button]').click()
            cy.get('[data-testid=unparticipate-button]').should('exist')
            cy.get('[data-testid=participate-button]').should('not.exist')

            cy.intercept('GET', 'api/session/42', {
                body: session
            })
            cy.get('[data-testid=unparticipate-button]').click()
            cy.get('[data-testid=unparticipate-button]').should('not.exist')
            cy.get('[data-testid=participate-button]').should('exist')
        })

    })
})