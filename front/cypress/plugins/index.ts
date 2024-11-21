/**
 * @type {Cypress.PluginConfig}
 */
 import registerCodeCoverageTasks from '@cypress/code-coverage/task';
 import support from '@cypress/code-coverage/support';


 export default (on, config) => {
   return registerCodeCoverageTasks(on, config);
 };
