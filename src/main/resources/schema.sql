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
-- Insert sample transactions with various scenarios
-- 1. DEPOSIT transactions with IBAN as opposite party type
INSERT INTO transactions (id, wallet_id, amount, type, opposite_party_type, opposite_party_identifier, opposite_party_wallet_id, status, deleted) VALUES
(1, 1, 500.00, 'DEPOSIT', 'IBAN', 'TR330006100519786457841326', NULL, 'APPROVED', false),
(2, 2, 1000.00, 'DEPOSIT', 'IBAN', 'DE89370400440532013000', NULL, 'APPROVED', false),
(3, 3, 300.00, 'DEPOSIT', 'IBAN', 'FR7630006000011234567890189', NULL, 'APPROVED', false),
(4, 4, 2500.00, 'DEPOSIT', 'IBAN', 'GB29NWBK60161331926819', NULL, 'PENDING', false);

-- 2. DEPOSIT transactions with PAYMENT as opposite party type
INSERT INTO transactions (id, wallet_id, amount, type, opposite_party_type, opposite_party_identifier, opposite_party_wallet_id, status, deleted) VALUES
(5, 5, 750.00, 'DEPOSIT', 'PAYMENT', 'PAY78901234', NULL, 'APPROVED', false),
(6, 1, 450.00, 'DEPOSIT', 'PAYMENT', 'PAY12345678', NULL, 'APPROVED', false),
(7, 2, 1250.00, 'DEPOSIT', 'PAYMENT', 'PAY98765432', NULL, 'PENDING', false);

-- 3. WITHDRAW transactions with IBAN as opposite party type
INSERT INTO transactions (id, wallet_id, amount, type, opposite_party_type, opposite_party_identifier, opposite_party_wallet_id, status, deleted) VALUES
(8, 1, 200.00, 'WITHDRAW', 'IBAN', 'TR330006100519786457841326', NULL, 'APPROVED', false),
(9, 2, 350.00, 'WITHDRAW', 'IBAN', 'DE89370400440532013000', NULL, 'APPROVED', false),
(10, 3, 1500.00, 'WITHDRAW', 'IBAN', 'FR7630006000011234567890189', NULL, 'PENDING', false);

-- 4. WITHDRAW transactions with PAYMENT as opposite party type
INSERT INTO transactions (id, wallet_id, amount, type, opposite_party_type, opposite_party_identifier, opposite_party_wallet_id, status, deleted) VALUES
(11, 4, 100.00, 'WITHDRAW', 'PAYMENT', 'PAY43210987', NULL, 'APPROVED', false),
(12, 5, 75.00, 'WITHDRAW', 'PAYMENT', 'PAY13579246', NULL, 'APPROVED', false),
(13, 1, 1800.00, 'WITHDRAW', 'PAYMENT', 'PAY24681357', NULL, 'DENIED', false);

-- 5. Internal transfers between wallets (one user's wallets)
INSERT INTO transactions (id, wallet_id, amount, type, opposite_party_type, opposite_party_identifier, opposite_party_wallet_id, status, deleted) VALUES
-- Transfer from wallet 1 to wallet 2 (same user)
(14, 1, 300.00, 'WITHDRAW', 'PAYMENT', NULL, 2, 'APPROVED', false),
-- Corresponding deposit to wallet 2
(15, 2, 300.00, 'DEPOSIT', 'PAYMENT', NULL, 1, 'APPROVED', false);

-- 6. Internal transfers between different users' wallets
INSERT INTO transactions (id, wallet_id, amount, type, opposite_party_type, opposite_party_identifier, opposite_party_wallet_id, status, deleted) VALUES
-- Transfer from wallet 3 to wallet 5 (different users)
(16, 3, 250.00, 'WITHDRAW', 'PAYMENT', NULL, 5, 'APPROVED', false),
-- Corresponding deposit to wallet 5
(17, 5, 250.00, 'DEPOSIT', 'PAYMENT', NULL, 3, 'APPROVED', false);

-- 7. Pending internal transfers
INSERT INTO transactions (id, wallet_id, amount, type, opposite_party_type, opposite_party_identifier, opposite_party_wallet_id, status, deleted) VALUES
-- Pending transfer from wallet 4 to wallet 1
(18, 4, 1200.00, 'WITHDRAW', 'PAYMENT', NULL, 1, 'PENDING', false),
-- Corresponding pending deposit to wallet 1
(19, 1, 1200.00, 'DEPOSIT', 'PAYMENT', NULL, 4, 'PENDING', false);

-- 8. Transactions with deleted flag set to true
INSERT INTO transactions (id, wallet_id, amount, type, opposite_party_type, opposite_party_identifier, opposite_party_wallet_id, status, deleted) VALUES
(20, 2, 50.00, 'WITHDRAW', 'PAYMENT', 'PAY99887766', NULL, 'APPROVED', true),
(21, 3, 150.00, 'DEPOSIT', 'IBAN', 'GB29NWBK60161331926819', NULL, 'APPROVED', true);

-- Enable foreign key constraints again for H2
SET REFERENTIAL_INTEGRITY TRUE;
