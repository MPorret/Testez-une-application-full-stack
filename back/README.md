# Yoga App !

## Configuration

### Create the database

SQL script for creating the schema is available: [/ressources/sql/script.sql](../ressources/sql/script.sql)

### Add the environment variables

In your IDE, add and custom the following environment variables:
```
DB_URL=127.0.0.1
DB_PORT=3306
DB_NAME=myDatabase
DB_USERNAME=root
DB_PASSWORD=password
DB_TEST_NAME=myDatabase
DB_TEST_USERNAME=root
DB_TEST_PASSWORD=password
```

## Start the project

Git clone:
> git clone https://github.com/MPorret/Testez-une-application-full-stack

Go inside folder:
> cd Testez-une-application-full-stack/back

Install dependencies:
> mvn clean install

Launch API:
> mvn spring-boot:run

## Start the tests

Launch all the tests to have the coverage with jacoco:
> mvn clean test