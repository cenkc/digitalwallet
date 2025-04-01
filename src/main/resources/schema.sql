-- for auth
-- for testing purposes, passwords are nothing but BCrypt hashed usernames (via https://bcrypt-generator.com/)
INSERT INTO users (id, first_name, last_name, tckn, username, email, password, deleted)
VALUES
(1, 'Ahmet', 'Yılmaz', '12345678901', 'ayilmaz', 'ayilmaz@examle.com', '$2a$12$fJtDvx6pEDOpW6r/VNQOueyuhDlgkGJvW0NvdjokyWhLV7v2PNFhm', false),
(2, 'Ayşe', 'Kara', '23456789012', 'akara', 'akara@examle.com', '$2a$12$UM1HEpo6hH7ieGQx6Z/aj.c.suAY6mKws8/pofX5EiH9d6RKnaflK', false),
(3, 'Mehmet', 'Demir', '34567890123', 'mdemir', 'mdemir@examle.com', '$2a$12$jWiFNdTOS14xxLEXx4L3G.7zNP9N8aSAbgk.UnajS.PHwybS/oi5y', false),
(4, 'Fatma', 'Çelik', '45678901234', 'fcelik', 'fcelik@examle.com', '$2a$12$R2GHiFufHaXY991ZrGN3HOcsTxvUDiP1lYU2wfFpFsEwme3bcC53K', false),
(5, 'Ali', 'Öztürk', '56789012345', 'aozturk', 'aozturk@examle.com', '$2a$12$44CtQUs64.sTB3LmTudoVungx8n1RQFs1gtKiQ0jV8Ws/Tih9g/ze', false);

INSERT INTO roles VALUES (1,'ROLE_USER'),(2,'ROLE_ADMIN');

INSERT INTO user_roles (user_id, role_id) VALUES
(1,1), -- Ahmet Yılmaz  ROLE_ADMIN
(1,2), -- Ahmet Yılmaz  ROLE_USER
(2,1), -- Ayşe Kara     ROLE_USER
(3,1), -- Mehmet Demir  ROLE_USER
(4,1), -- Fatma Çelik   ROLE_USER
(5,1); -- Ali Öztürk    ROLE_USER


-- Disable foreign key constraints temporarily for H2
SET REFERENTIAL_INTEGRITY FALSE;

-- Insert test wallets
INSERT INTO wallets (id, user_id, wallet_name, currency_type, active_for_shopping, active_for_withdraw, balance, usable_balance, deleted) VALUES
(1, 1, 'Primary TL', 'TRY', true, true, 5000.00, 4800.00, false),
(2, 1, 'Euro Savings', 'EUR', false, true, 1000.00, 1000.00, false),
(3, 2, 'Shopping', 'TRY', true, false, 2500.00, 2500.00, false),
(4, 2, 'USD Account', 'USD', false, true, 750.00, 750.00, false),
(5, 3, 'Main Wallet', 'TRY', true, true, 12000.00, 11500.00, false),
(6, 4, 'Business', 'USD', true, true, 3500.00, 3500.00, false),
(7, 4, 'Personal', 'TRY', true, true, 1800.00, 1800.00, false),
(8, 5, 'Travel', 'EUR', true, false, 500.00, 500.00, false),
(9, 5, 'Savings', 'TRY', false, true, 15000.00, 15000.00, false);

-- Insert test transactions
INSERT INTO transactions (id, wallet_id, amount, type, opposite_party_type, opposite_party_id, status, deleted) VALUES
(1, 1, 1000.00, 'DEPOSIT', 'PAYMENT', NULL, 'APPROVED', false),
(2, 1, 200.00, 'WITHDRAW', 'PAYMENT', NULL, 'APPROVED', false),
(3, 2, 500.00, 'DEPOSIT', 'IBAN', NULL, 'APPROVED', false),
(4, 3, 1500.00, 'DEPOSIT', 'PAYMENT', NULL, 'APPROVED', false),
(5, 3, 300.00, 'WITHDRAW', 'PAYMENT', NULL, 'APPROVED', false),
(6, 4, 250.00, 'DEPOSIT', 'IBAN', NULL, 'APPROVED', false),
(7, 5, 2000.00, 'DEPOSIT', 'PAYMENT', NULL, 'APPROVED', false),
(8, 5, 500.00, 'WITHDRAW', 'PAYMENT', NULL, 'APPROVED', false),
(9, 6, 1000.00, 'DEPOSIT', 'IBAN', NULL, 'APPROVED', false),
(10, 7, 800.00, 'DEPOSIT', 'PAYMENT', NULL, 'APPROVED', false),
(11, 8, 200.00, 'DEPOSIT', 'IBAN', NULL, 'APPROVED', false),
(12, 9, 5000.00, 'DEPOSIT', 'PAYMENT', NULL, 'APPROVED', false),
(13, 1, 300.00, 'WITHDRAW', 'PAYMENT', NULL, 'PENDING', false),
(14, 5, 750.00, 'DEPOSIT', 'IBAN', NULL, 'PENDING', false),
(15, 6, 500.00, 'WITHDRAW', 'PAYMENT', NULL, 'DENIED', false);

-- Enable foreign key constraints again for H2
SET REFERENTIAL_INTEGRITY TRUE;



--
--INSERT INTO customers (id, name, surname, tckn, deleted)
--VALUES (1, 'John', 'Doe', '12345678901', false);
--INSERT INTO customers (id, name, surname, tckn, deleted)
--VALUES (2, 'Jane', 'Smith', '10987654321', false);
--INSERT INTO customers (id, name, surname, tckn, deleted)
--VALUES (3, 'Alice', 'Brown', '11122233344', false);
--
----
--INSERT INTO wallets (id, customer_id, walletName, currencyType, activeForShopping, activeForWithdraw, balance, usableBalance, deleted)
--VALUES (1, 1, 'John''s Personal Wallet', 'USD', true, true, 1000.00, 800.00, false);
--INSERT INTO wallets (id, customer_id, walletName, currencyType, activeForShopping, activeForWithdraw, balance, usableBalance, deleted)
--VALUES (2, 1, 'John''s Business Wallet', 'USD', true, true, 1000.00, 800.00, false);
--INSERT INTO wallets (id, customer_id, walletName, currencyType, activeForShopping, activeForWithdraw, balance, usableBalance, deleted)
--VALUES (3, 2, 'Jane''s Business Wallet', 'EUR', true, false, 2000.50, 1500.50, false);
--INSERT INTO wallets (id, customer_id, walletName, currencyType, activeForShopping, activeForWithdraw, balance, usableBalance, deleted)
--VALUES (4, 3, 'Alice''s Savings Wallet', 'TRY', false, true, 500.00, 500.00, false);
--
----
--INSERT INTO transactions (id, wallet_id, amount, type, oppositePartyType, opposite_party_id, status, deleted)
--VALUES (1, 1, 100.00, 'DEPOSIT', 'PAYMENT', 2, 'SUCCESS', false); --John's Personal Wallet --> John''s Business Wallet (transfer between one's own wallets)
--INSERT INTO transactions (id, wallet_id, amount, type, oppositePartyType, opposite_party_id, status, deleted)
--VALUES (2, 4, 50.00, 'WITHDRAW', 'PAYMENT', NULL, 'PENDING', false); --John''s Business Wallet
--INSERT INTO transactions (id, wallet_id, amount, type, oppositePartyType, opposite_party_id, status, deleted)
--VALUES (3, 3, 200.00, 'DEPOSIT', 'IBAN', NULL, 'SUCCESS', false);