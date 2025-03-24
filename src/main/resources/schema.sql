INSERT INTO users VALUES
(1,'alice@gmail.com','alice','$2a$12$efw2epOG4ZxnkKE3vda/n.P7rqJPgyvP3c4Drw9yBNkryHsn1j2Ra','alice'),
(2,'bob@gmail.com','bob','$2a$12$LJt7zrT8ruN4KIGaTYLzOuZ70iUApFWBVSSCfhaebjy8bphrkPTMe','bob');

INSERT INTO roles VALUES (1,'ROLE_USER'),(2,'ROLE_ADMIN');

INSERT INTO user_roles VALUES (1,1),(2,2);