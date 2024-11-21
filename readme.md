# Yoga App 🧘
Lien du projet : https://github.com/aflouat/p5-start-app

Ce projet comprend trois répertoires : front, backend et ressources.



## Start the project

Git clone:

> git clone https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing

Go inside folder:

> cd yoga/front

Install dependencies for the front end

> npm install

Launch Front-end:

> npm run start;

Launch unit and intergration tests : 
> npm test

Launch coverage report for the front end
> ng run yoga:e2e-ci



## Backend testing : avec le profil test
> mvn test



## Ressources



### Postman collection

For Postman import the collection

> ressources/postman/yoga.postman_collection.json 

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman


### MySQL
Create a MySQL database, yoga_db
SQL script for creating the schema is available `ressources/sql/script.sql`

By default the admin account is:
- login: yoga@studio.com
- password: test!1234

and another MySQL database is created for testing purpose,look at application-test.properties. the test database is yoga_db_test

