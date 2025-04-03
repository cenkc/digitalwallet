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

## Testing concurrent transaction handling w/wo pessimistic locking
Used "hey" benchmarking tool on MacOS with both pessimistic locking on and off. Here are the tests and their results:

### Test 1: Pessimistic Locking On

```bash
~ hey -n 1200 -c 10 -m POST -H "Content-Type: application/json" -H "Authorization: Basic bWRlbWlyOm1kZW1pcg==" -d '{"walletId": 5, "amount": 8.00, "type": "WITHDRAW", "oppositePartyType": "PAYMENT", "oppositePartyIdentifier": "PAY123456789"}' http://localhost:8181/api/v1/transactions/
```
Expecting 1200 requests to be processed with a maximum concurrency of 10. In result, 1200 requests were processed successfully with a response time of around 310ms per request. The application handled the load well without any errors or timeouts. TPS is around 31.87 requests/sec. DB queries shows that the transactions were processed with pessimistic locking, ensuring data integrity. 1200 transactions were processed with 0 errors and Wallet #5's balance was updated correctly ```12000 - (1200 x 8) = 2400```.

"hey" app output:
```
Summary:
  Total:	37.6533 secs
  Slowest:	0.4589 secs
  Fastest:	0.2807 secs
  Average:	0.3132 secs
  Requests/sec:	31.8697
  

Response time histogram:
  0.281 [1]	|
  0.298 [94]	|■■■■■
  0.316 [723]	|■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.334 [336]	|■■■■■■■■■■■■■■■■■■■
  0.352 [33]	|■■
  0.370 [3]	|
  0.388 [0]	|
  0.405 [0]	|
  0.423 [5]	|
  0.441 [2]	|
  0.459 [3]	|


Latency distribution:
  10% in 0.2999 secs
  25% in 0.3058 secs
  50% in 0.3119 secs
  75% in 0.3183 secs
  90% in 0.3263 secs
  95% in 0.3316 secs
  99% in 0.3557 secs

Details (average, fastest, slowest):
  DNS+dialup:	0.0000 secs, 0.2807 secs, 0.4589 secs
  DNS-lookup:	0.0000 secs, 0.0000 secs, 0.0010 secs
  req write:	0.0000 secs, 0.0000 secs, 0.0005 secs
  resp wait:	0.3130 secs, 0.2805 secs, 0.4561 secs
  resp read:	0.0001 secs, 0.0000 secs, 0.0009 secs

Status code distribution:
  [201]	1200 responses
```
### Test 2: Pessimistic Locking Off

```bash
~ hey -n 1200 -c 10 -m POST -H "Content-Type: application/json" -H "Authorization: Basic bWRlbWlyOm1kZW1pcg==" -d '{"walletId": 5, "amount": 8.00, "type": "WITHDRAW", "oppositePartyType": "PAYMENT", "oppositePartyIdentifier": "PAY123456789"}' http://localhost:8181/api/v1/transactions/
```
This time, I disabled the pessimistic locking in the code. The test was run with the same parameters as before. The results were significantly different. All 1200 requests were processed successfully, but the wallet balances are inconsistent. Expecting the balance to be ```12000 - (1200 x 8) = 2400```, but the actual balance was ```6456.00```.

"hey" app output:
```
Summary:
  Total:	39.2708 secs
  Slowest:	0.8401 secs
  Fastest:	0.3026 secs
  Average:	0.3269 secs
  Requests/sec:	30.5570
  

Response time histogram:
  0.303 [1]	|
  0.356 [1188]	|■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.410 [1]	|
  0.464 [0]	|
  0.518 [0]	|
  0.571 [0]	|
  0.625 [0]	|
  0.679 [0]	|
  0.733 [0]	|
  0.786 [0]	|
  0.840 [10]	|


Latency distribution:
  10% in 0.3131 secs
  25% in 0.3170 secs
  50% in 0.3215 secs
  75% in 0.3269 secs
  90% in 0.3342 secs
  95% in 0.3404 secs
  99% in 0.3555 secs

Details (average, fastest, slowest):
  DNS+dialup:	0.0000 secs, 0.3026 secs, 0.8401 secs
  DNS-lookup:	0.0000 secs, 0.0000 secs, 0.0017 secs
  req write:	0.0000 secs, 0.0000 secs, 0.0007 secs
  resp wait:	0.3266 secs, 0.3025 secs, 0.8329 secs
  resp read:	0.0002 secs, 0.0000 secs, 0.0046 secs

Status code distribution:
  [201]	1200 responses
```

## Running Unit Tests

To run tests:

```bash
./mvnw test
```

## License

This project is licensed under the GNU General Public License v3.0 - see the LICENSE file for details.
