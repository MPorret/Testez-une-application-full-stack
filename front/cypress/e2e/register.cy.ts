describe('Register spec', () => { 
  let existingUser = 0;

  before(() => {
    // Initialisation ou préparation des prérequis si nécessaire
  });

  it('Register successful', () => {
    cy.visit('/register');
    cy.intercept('POST', '/api/auth/register').as('registerRequest');

    cy.get('#mat-input-0').type('Aflouat');
    cy.get('#mat-input-1').type('ABDEL');
    cy.get('#mat-input-2').type('test@studio.com'); // Email déjà utilisé
    cy.get('#mat-input-3').type('test!1234');
    cy.get('.mat-button-wrapper').click();

    // Attendre la réponse de l'interception et vérifier la réponse
    cy.wait('@registerRequest').then((interception) => {
      if (interception.response) {
        // Vérifier que le code de statut est 400 et que le message est celui attendu
        expect(interception.response.statusCode).to.eq(200);
      } 
    });
  });
});
