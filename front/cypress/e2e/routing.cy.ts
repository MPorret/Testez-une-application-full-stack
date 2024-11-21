describe('App Routing Tests', () => {
    it('should redirect to /login when accessing /sessions without authentication', () => {
      cy.visit('/sessions');
      cy.url().should('include', '/login');
    });
  
    it('should navigate to /sessions when authenticated', () => {
      // Simuler la connexion
      cy.loginAndGetToken().then(token => {
        //cy.setTokenInLocalStorage(token);
        //cy.visit('/sessions');
        cy.url().should('include', '/sessions');
      });
    });
  
    it('should load 404 page for unknown routes', () => {
      cy.visit('/unknown-path');
      cy.get('app-not-found').should('be.visible');
      cy.get('h1').should('contain','Page not found !');

    });
  });
  