*DESCRIPTION*\n
A SWIFT code, also known as a Bank Identifier Code (BIC), is a unique identifier of a bank's branch or headquarter. It ensures that international wire transfers are directed to the correct bank and branch, acting as a bank's unique address within the global financial network. Currently, SWIFT-related data for various countries is stored in a spreadsheet. While this format is convenient for offline management, we need to make this data accessible to our applications.

*SYSTEM REQUIREMENTS*
JDK 21
Maven 3.6 or higher
Spring Boot 3.4.1
PostgreSQL 12 or higher (local database)

*TOOLS*
Anything that supports Java and Maven
Docker 20.10 or higher

*RUNNING INSTRUCTION*
- Open downloaded project
- Open Docker
- in terminal:
  - mvn clear
  - mvn install
  - docker build -t swiftapi .
  - docker-compose -f compose.yaml up
- to run tests:
  - mvn test
