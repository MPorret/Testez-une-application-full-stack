# Yoga Application Testing Guide (IntelliJ IDEA)

## Requirements
- Git
- MySQL
- Node.js
- OpenJDK 11
- IntelliJ IDEA

## Database Setup

1. Start MySQL server.
2. In IntelliJ IDEA, connect to MySQL via:
    - **View → Tool Windows → Database**
    - Click `+` → **Data Source** → **MySQL**.
    - Configure connection settings.
3. Right-click your MySQL connection → **New → Query Console**, then run:

```sql
CREATE DATABASE test_yoga;
USE test_yoga;
SOURCE /path_to_file/ressources/sql/script.sql;
```

## Frontend Setup

- Open IntelliJ built-in terminal (**View → Tool Windows → Terminal**):
```bash
cd /path_to_file/front
npm install
npm run start
```
- Open [http://localhost:4200](http://localhost:4200) in browser.

## Running Tests

### Backend Tests

- Open `/back` project in IntelliJ.
- Run via **Maven panel**:
    - Expand Lifecycle → double-click `clean`, then `test`.
- Or via built-in terminal:
```shell
cd /path_to_file/back
mvn clean test
```

**Coverage Report:**  
Open HTML report in your browser:
```
/path_to_file/back/target/site/jacoco/index.html
```

### Frontend Unit Tests

- Terminal in IntelliJ:
```shell
cd /path_to_file/front
npm run test:coverage
```

**Coverage Report:**  
Open in browser:
```
/path_to_file/front/coverage/jest/lcov-report/index.html
```

### Frontend E2E Tests

- Terminal in IntelliJ:
```shell
npm run e2e
```
- Cypress window opens automatically:
    - Select and run file: `main.cy.js`

- After E2E tests:
```shell
npm run e2e:coverage
```

**Coverage Report:**  
Open in browser:
```
/path_to_file/front/coverage/lcov-report/index.html
```

Ensure all tests pass successfully.
