describe('Login spec', () => {
  it('Login successfull', () => {
    cy.login();
    cy.get('.mat-card-title').should("contain","Yoga sessions available")
   })
});