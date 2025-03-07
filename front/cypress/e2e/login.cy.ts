describe('Login spec', () => {
  //  Successful Login
  it('Login successful', () => {
    // Visit the login page
    cy.visit('/login');

    // Mock the API response for a successful login
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    // Mock the API response for fetching session data
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []
    ).as('session');

    // Fill in the login form
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    // Verify that the user is redirected to the sessions page
    cy.url().should('include', '/sessions');
  });

  //  Error message displayed for incorrect login credentials
  it('displays an error for incorrect login or password', () => {
    // Visit the login page
    cy.visit('/login');

    // Mock the API response for incorrect login attempt
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        path: '/api/auth/login',
        error: 'Unauthorized',
        message: 'Bad credentials',
        status: 401,
      },
    });

    // Fill in the login form with incorrect credentials
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(`${'test'}{enter}{enter}`);

    // Verify that the error message is displayed
    cy.get('div.login')
      .find('mat-card')
      .find('form.login-form')
      .find('p')
      .contains('An error occurred')
      .should('be.visible');
  });

  // Error displayed when required fields are missing
  it('shows an error when a required field is missing', () => {
    // Visit the login page
    cy.visit('/login');

    // Mock the API response for an unsuccessful login due to missing fields
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        path: '/api/auth/login',
        error: 'Unauthorized',
        message: 'Bad credentials',
        status: 401,
      },
    });

    // Attempt to submit the form without entering email or password
    cy.get('input[formControlName=email]').type('{enter}{enter}{enter}');

    // Verify that form fields are marked as invalid
    cy.get('div.login')
      .find('mat-card')
      .find('form.login-form')
      .find('mat-card-content')
      .each(($row) => {
        cy.wrap($row)
          .find('mat-form-field')
          .should('have.class', 'mat-form-field-invalid');
      });

    // Verify that the error message is displayed
    cy.get('div.login')
      .find('mat-card')
      .find('form.login-form')
      .find('p')
      .contains('An error occurred')
      .should('be.visible');
  });
});
