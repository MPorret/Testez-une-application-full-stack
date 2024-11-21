describe('me componenet', ()=>{

 

     
    it('show delete user as not admin',()=>{
      
      cy.loginNotAdmin(); // Appelle la commande login
      cy.get('[routerlink="me"]').click()
     cy.get('.my2 > .mat-focus-indicator').should('contain','Detail');
     cy.get('button[color="warn"]').click();


 
    })
    it('show me',()=>{
      cy.login(); // Appelle la commande login

            cy.get('[routerlink="me"]').click()
            cy.get('h1').should("contain","User information")
            cy.get('.mat-icon').click()
            cy.url().should('contain','sessions')
            cy.get('[routerlink="me"]').click()
            cy.get('.my2').should('contain','You are admin')

            
 
    })

})