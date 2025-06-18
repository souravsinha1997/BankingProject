🏦 BankingProject

A comprehensive internet banking platform built using Spring Boot microservices architecture. It supports user and account management, fund transfers, beneficiary handling, loan service requests, and transaction processing. Designed for scalability, modularity, and maintainability, this project demonstrates real-world banking operations in a distributed system.




🧱 Architecture Overview

This application is decomposed into multiple microservices, each with a specific responsibility:



✅ User Service

Handles user registration and role-based access (ADMIN, MANAGER, USER).

Admins: Can view and update any customer details.

Managers: View customers assigned to them and approve/reject their loan requests.



🏦 Account Service

Customers can open accounts (e.g., Savings accounts).

Each account is linked to a CIF (Customer Information File) generated at user creation.


👥 Beneficiary Service

Users can add and manage beneficiaries.

Only internal (same bank) beneficiaries are allowed (must exist as users and have valid accounts).


💸 Transaction Service

Users can:

Transfer funds to beneficiaries.

Deposit or withdraw funds.

Supports:

Registered beneficiaries

Ad-hoc transfers: Allows entering beneficiary details during the transaction. If successful, the beneficiary is auto-registered.


📝 Request Service

Customers raise loan requests.

Respective Managers approve/reject requests.

Upon approval:

Entry added to Loans table.

Approved amount credited to the customer account.




🗃️ Database

The DatabaseQueries directory contains all essential SQL scripts used for initializing and managing the MySQL databases for the services.




🛠️ Technologies Used

Technology	Purpose

Spring Boot	Base framework for microservices

Spring Cloud	Configuration management, service discovery

Netflix Eureka	Service registry and discovery

MySQL	Persistent data storage

RabbitMQ	Asynchronous inter-service communication

Swagger/OpenAPI	API documentation




📈 Future Enhancements

✅ API Gateway Service (Zuul or Spring Cloud Gateway)

✅ Distributed Tracing with Zipkin





📂 Project Structure

BankingProject/
├── user-service/
├── account-service/
├── beneficiary-service/
├── transaction-service/
├── request-service/
├── DatabaseQueries/
└── README.md