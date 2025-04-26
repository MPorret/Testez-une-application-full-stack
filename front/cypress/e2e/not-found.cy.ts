describe('Not found page spec', () => {
    it('See not found page', () => {
        cy.visit('/thispagenotexist')
        cy.get('h1').should('contain.text', 'Page not found !')
    })
})