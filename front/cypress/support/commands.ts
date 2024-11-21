Cypress.Commands.add('login', () => {
  cy.visit('/login');


cy.get('input[formControlName=email]').type("yoga@studio.com");
cy.get('input[formControlName=password]').type("test!1234{enter}{enter}");

cy.url().should('include', '/sessions');

});
Cypress.Commands.add('loginNotAdmin', () => {
  cy.visit('/login');
  cy.get('input[formControlName=email]').type("test@studio.com");
cy.get('input[formControlName=password]').type("test!1234{enter}{enter}");

cy.url().should('include', '/sessions');


});

Cypress.Commands.add('loginAndGetToken', (): Cypress.Chainable<string> => {
  cy.visit('/login');

  // Intercepter la requête POST vers le point de terminaison de connexion pour capturer la réponse
  cy.intercept('POST', '/api/auth/login').as('loginRequest');

  cy.get('input[formControlName=email]').type("yoga@studio.com");
  cy.get('input[formControlName=password]').type("test!1234{enter}{enter}");

  cy.url().should('include', '/sessions');

  // Attendre la requête interceptée et retourner le token
  return cy.wait('@loginRequest').then((interception) => {
    if (interception.response && interception.response.body.token) {
      const token = interception.response.body.token;
      //cy.log('Token généré : ' + token);
      //localStorage.setItem('authToken', token); // Sauvegarde dans le localStorage si nécessaire
      return cy.wrap(token).then(() => token); // Retourne une chaîne de caractères dans une chaîne Cypress
    } else {
      throw new Error('Le token n\'a pas été trouvé dans la réponse de la requête');
    }
  });
});

  
  // Commande pour créer une session (ajuster selon les besoins)
Cypress.Commands.add('createSession', (token) => {
    cy.request({
      method: 'POST',
      url: 'http://localhost:8080/api/session',
      headers: {
        'Authorization': `Bearer ${token}`
      },
      body: {
        name: "session 1",
        date: "2012-01-01",
        teacher_id: 1,
        users: null,
        description: "Yoga Advanced Class Mock test"
      }
    }).then((response) => {
      expect(response.status).to.eq(200); // Vérifie que la création est réussie
    });
  });

  //create teacher
  Cypress.Commands.add('createTeacher',(token)=>{
    cy.request({
        method:'POST',
        url:'http://localhost:8080/api/teacher',
        headers:{
            'Authorization':`Bearer ${token}`
            },
        body:{
          
        lastName: "ABDEL",
        firstName: "Karim",
        createdAt: "2024-10-15T05:27:53",
        updatedAt: "2024-10-15T05:27:53"
        }

    }).then((Response) =>{
        expect(Response.status).to.eq(200)
    })
  })
  