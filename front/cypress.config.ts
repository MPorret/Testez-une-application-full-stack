//cypress.config.ts
import { defineConfig } from 'cypress'
const codeCoverage = require('@cypress/code-coverage/task');
module.exports = defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200', // Remplacez par l'URL de votre application

    setupNodeEvents(on, config) {
      codeCoverage(on, config);
      return config;
    }
  }
});


export default defineConfig({
  videosFolder: 'cypress/videos',
  screenshotsFolder: 'cypress/screenshots',
  fixturesFolder: 'cypress/fixtures',
  video: false,
  e2e: {
    // We've imported your old cypress plugins here.
    // You may want to clean this up later by importing these.
    setupNodeEvents(on, config) {
      return require('./cypress/plugins/index.ts').default(on, config)
    },
    baseUrl: 'http://localhost:4200',
  },
})
