# Digital Wallet Challenge

This project is a Spring Boot-based Digital Wallet API that allows users to create wallets, make deposits, and withdraw money.

## Features

- Create and manage wallets in different currencies (TRY, USD, EUR)
- Deposit money to wallets
- Withdraw money from wallets
- View transaction history
- Approve or deny pending transactions
- Role-based access control (Admin/User)

## Technologies Used

- Java 17
- Spring Boot 3.4.3
- Spring Data JPA
- Spring Security
- H2 Database (in-memory)
- JUnit 5 & Mockito for testing
- Maven for build management

## Project Structure

The application follows a layered architecture:

- **Controllers**: Handle API requests and responses
- **Services**: Implement business logic
- **Repositories**: Interface with the database
- **Entities**: Model the domain objects
- **DTOs**: Transfer data between layers
- **Exceptions**: Custom exception handling

## Design Patterns Used

### Strategy Pattern

Used for transaction processing logic to handle different transaction types (deposit/withdrawal) without conditional logic sprawl.

### Template Method Pattern

Applied in AbstractTransactionProcessor to define a skeleton algorithm for processing transactions, with specific steps implemented by subclasses.

### Factory Pattern

Implemented in TransactionProcessorFactory to create the appropriate processor for a transaction type without exposing the creation logic.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher

### Running the Application

1. Clone the repository
2. Navigate to the project root directory
3. Run the application:

```bash
./mvnw spring-boot:run
```

or on Windows:

```bash
mvnw.cmd spring-boot:run
```

The application will start on port 8181 (as defined in application-dev.yml)

### H2 Console

You can access the H2 database console at:

```
http://localhost:8181/h2-console
```

Use the following credentials:
- JDBC URL: `jdbc:h2:mem:walletdb`
- Username: `sa`
- Password: (leave empty)

## API Endpoints

### Wallet Endpoints

- **Create Wallet**: `POST /api/v1/wallets/add`
- **List Wallets**: `GET /api/v1/wallets`
- **Update Wallet**: `PUT /api/v1/wallets/{id}`

### Transaction Endpoints

- **Process Transaction (Deposit/Withdraw)**: `POST /api/v1/transactions`
- **List Transactions**: `GET /api/v1/transactions/wallet/{walletId}`
- **Approve/Deny Transaction**: `PUT /api/v1/transactions/{transactionId}/approve`

## API Usage Examples

### Create a Wallet

```
POST /api/v1/wallets/add
Content-Type: application/json
Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ=

{
  "walletName": "My TRY Wallet",
  "currencyType": "TRY",
  "activeForShopping": true,
  "activeForWithdraw": true
}
```

### Process Transaction (Deposit)

```
POST /api/v1/transactions
Content-Type: application/json
Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ=

{
  "walletId": 1,
  "amount": 500.00,
  "type": "DEPOSIT",
  "oppositePartyType": "IBAN"
}
```

### Process Transaction (Withdraw)

```
POST /api/v1/transactions
Content-Type: application/json
Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ=

{
  "walletId": 1,
  "amount": 200.00,
  "type": "WITHDRAW",
  "oppositePartyType": "PAYMENT"
}
```

### Approve a Transaction

```
PUT /api/v1/transactions/1/approve
Content-Type: application/json
Authorization: Basic YWRtaW46cGFzc3dvcmQ=

{
  "status": "APPROVED"
}
```

## Authentication

The API uses Basic Authentication:

- Regular users can perform operations on their own wallets
- Admin users can perform operations on all wallets and approve/deny transactions

## Transaction Processing Rules

- Transactions with amounts greater than 1000 are saved with PENDING status
- Transactions with amounts less than or equal to 1000 are saved with APPROVED status
- For deposits:
    - Approved deposits affect both balance and usable balance
    - Pending deposits only affect balance
- For withdrawals:
    - Approved withdrawals affect both balance and usable balance
    - Pending withdrawals reserve funds from usable balance only

## Concurrency Control

The application uses pessimistic locking to ensure data integrity during transaction processing:

- When a transaction is being processed, the wallet is locked at the database level
- This prevents concurrent modifications to the same wallet
- Ensures accurate balance calculations even under high transaction loads
- Uses JPA's `@Lock(LockModeType.PESSIMISTIC_WRITE)` annotation to acquire locks

## Running Tests

To run tests:

```bash
./mvnw test
```

## License

This project is licensed under the GNU General Public License v3.0 - see the LICENSE file for details.
