## DESCRIPTION
A SWIFT code, also known as a Bank Identifier Code (BIC), is a unique identifier of a bank's branch or headquarter. It ensures that international wire transfers are directed to the correct bank and branch, acting as a bank's unique address within the global financial network. Currently, SWIFT-related data for various countries is stored in a spreadsheet. While this format is convenient for offline management, we need to make this data accessible to our applications.

## SYSTEM REQUIREMENTS
- JDK 21
- Maven 3.6 or higher
- Spring Boot 3.4.1
- PostgreSQL 12 or higher (local database)

## TOOLS
- Anything that supports Java and Maven
- Docker 20.10 or higher

## RUNNING INSTRUCTION
- Open downloaded project
- Open Docker
- in terminal:
  - go to project folder
  - mvn clean install
  - docker build -t swiftapi .
  - docker-compose -f compose.yaml up
- to run tests:
  - mvn test

## HOW TO USE
- first endpoint:
  - showing data for given SWIFT Code
  - terminal:
    - curl -X GET http://localhost:8080/v1/swift-codes/{swift-code}
    - example: curl -X GET http://localhost:8080/v1/swift-codes/AAISALTRXXX
  - browser:
    - http://localhost:8080/v1/swift-codes/{swift-code}
    - example: http://localhost:8080/v1/swift-codes/AAISALTRXXX
- second endpoint:
  - showing all SWIFT Codes for given ISO2 Code
  - terminal:
    - curl -X GET http://localhost:8080/v1/swift-codes/country/{country-ISO2}
    - curl -X GET http://localhost:8080/v1/swift-codes/country/PL
  - browser:
    - http://localhost:8080/v1/swift-codes/country/{country-ISO2}
    - http://localhost:8080/v1/swift-codes/country/PL
- third endpoint:
  - adding new SWIFT Code to Database
  - terminal:
    - curl -X POST "http://localhost:8080/v1/swift-codes" -H "Content-Type: application/json" -d '{"address":{bank-address},
      "bankName":{bank-name}, "countryISO2":{country-ISO2}, "countryName":{country-name}, "swiftCode":{swift-code}, "headquarter":{is-headquarter}}'
    - curl -X POST "http://localhost:8080/v1/swift-codes" -H "Content-Type: application/json" -d '{"address":"TEST STREET 1",
      "bankName":"TESTBANK", "countryISO2":"PL", "countryName":"POLAND", "swiftCode":"ABCDEFGHXXX", "headquarter":true}'
  - browser:
    - http://localhost:8080/v1/swift-codes
    - press Submit button
- fourth endpoint:
  - deleting existing SWIFT Code from Database
  - terminal:
    - curl -X DELETE http://localhost:8080/v1/swift-codes/{swift-code}
    - curl -X DELETE http://localhost:8080/v1/swift-codes/ABCDEFGHIJ
