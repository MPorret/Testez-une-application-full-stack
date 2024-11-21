describe('tests e2e pour les sessions de yoga',()=>{
    let token; // Variable pour stocker le token

    before(() => {
       // Obtenir le token de manière asynchrone et l'utiliser dans les tests
       cy.loginAndGetToken().then((authToken) => {
        token = authToken; // Stocke le token dans la variable `token`
        cy.log('Token récupéré : ' + token);
         // Utilise le token pour créer une session
      });
    });
      it('should create new session',()=>{
        cy.contains('Create').click();
        cy.get('#mat-input-2').type('Top session Yoga');
        cy.get('#mat-input-3').type('2024-11-01');
        // Ouvre la dropdown
// Ouvrir le mat-select correspondant à 'teacher' en utilisant formControlName ou un autre identifiant unique
cy.get('mat-select[formControlName=teacher_id]').click();

// Chercher l'option correspondant à l'enseignant souhaité, par exemple 'Apple Inc.', et cliquer dessus
cy.get('mat-option').contains('Margot DELAHAYE').click();
    

        cy.get('#mat-input-4').type('Ce programme es conçu pour offrir une expérience de bien-être complète, '
          +'axée sur des enchaînements fluides et harmonieux. Il vise à améliorer la flexibilité, '
          +'la force et la relaxation à travers des séances adaptées à différents niveaux de pratique.');
          cy.get('.mt2 > [fxlayout="row"] > .mat-focus-indicator').click();

      });

   
      it('should show list sessions, session details and then delete session created ', ()=>{
       cy.login();
       cy.get(':nth-child(1) > .mat-card-actions > :nth-child(1)').click()
       cy.get('[fxlayout="row"] > .mat-focus-indicator > .mat-button-wrapper > .mat-icon').click();
       cy.url().should('contain','sessions');
       //visualiser le detail d une session
       cy.get(':nth-child(1) > .mat-card-actions > :nth-child(1)').click()

       cy.get(':nth-child(2) > .mat-focus-indicator').click()
       cy.url().should('contain','sessions')
       cy.get(':nth-child(1) > .mat-card-actions > :nth-child(1)').click()
 cy.loginNotAdmin(); 
       cy.get('button').contains('Participate').should('be.visible');




      });/*
      it('should display session details correctly', () => {
        // Vérifie que le nom de la session est affiché
        cy.get('h1').should('exist').and('not.be.empty');
    
        // Vérifie le nombre de participants
        cy.get('.group').within(() => {
          cy.get('span').contains('attendees').should('be.visible');
        });
    
        // Vérifie la date de la session
        cy.get('.calendar_month').next('.ml1').should('not.be.empty');
    
        // Vérifie le professeur
        cy.get('.ml1').contains(/teacher/i).should('be.visible');*/
      });
    
 