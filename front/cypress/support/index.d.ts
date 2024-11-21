// cypress/support/index.d.ts

declare namespace Cypress {
    interface Chainable {
      login(): Chainable<void>;
       /**
     * Custom command to log in and return the auth token.
     * @returns {Chainable<string>} The token as a string.
     */
    loginAndGetToken(): Chainable<string>;
    loginNotAdmin(): Chainable<string>;

    /**
     * Custom command to create a teacher with a given token.
     * @param token - The authentication token.
     */
    createTeacher(token: string): Chainable<void>;

    /**
     * Custom command to create a session with a given token.
     * @param token - The authentication token.
     */
    createSession(token: string): Chainable<void>;
    
    checkAndCreateIfNotExists(token:string): Chainable<void>;
    }
  }
  