BankingProject:

Overview This project is a comprehensive banking platform designed to handle various aspects of Internet Banking, including user management, account management, beneficiary management, transactions, service requests(Loan). It follows a microservices architecture to ensure scalability and maintainability.

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Architecture:
The application is structured into several microservices:

  User service : User service manages user accounts and mamages them with the roles MANAGER, USER, ADMIN.

                 Admins can check all the data and based on requirement they can update the customer data.

                 Managers can check who are the customers under them, also approve the loan requests(From Request service) raised by those customers.

  Account service : Once successful creation of the customer they can create a bank account with different types like Savings. All the accounts are mapped with user CIF which is generated at the time of user creation.

  Beneficiary service : Users can register and manage their beneficiary from this service. (Benefcicary needs to be from same bank, which means beneficiary needs to be present as a bank customer and the account needs to be present in the accounts)

  Transaction service  : Transaction service allows customers to transfer funds from customer account to beneficiary accounts. Or customers can withdraw funds from their account and deposite funds into their account. From bank transfer process customer can select from their already registered beneficiaries or they can add the details of the new beneficiary at the time of transaction(ADHOC transaction), once the transaction is done new beneficiary will be added in the beneficiary service.

  Request service : Customers can raise a request for Loans through this service. Once new request is raised it manager of that customer will have the premission to approve or reject that request. Once it gets approved by the respective manager it details will be added to the Loans table and the ammount will be credited to the customer account.


++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


Database Queries

The DatabaseQueries directory contains SQL scripts and queries related to the database operations of the application.

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Technologies Used

Spring Boot: For building the microservices.

Spring Cloud: For implementing service discovery and configuration management.

Netflix Eureka: For service registration and discovery.

MySQL: As the relational database for storing data.

RabbitMQ: For handling asynchronous communication between services.

Swagger/OpenAPI: For API documentation.


++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Future changes:
1. Gateway service
2. Zipkin
  
