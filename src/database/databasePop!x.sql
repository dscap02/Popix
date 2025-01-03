-- Creazione del database popix
DROP DATABASE IF EXISTS Popix;
CREATE DATABASE Popix;
USE Popix;

-- Table: UtenteRegistrato
CREATE TABLE UtenteRegistrato (
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) PRIMARY KEY,
    role ENUM('User', 'Admin', 'Gestore') NOT NULL
);

-- Table: Admin
CREATE TABLE Admin (
    utente_registrato_email VARCHAR(100) PRIMARY KEY,
    FOREIGN KEY (utente_registrato_email) REFERENCES UtenteRegistrato(email)
);

-- Table: Cliente
CREATE TABLE Cliente (
    utente_registrato_email VARCHAR(100) PRIMARY KEY,
    FOREIGN KEY (utente_registrato_email) REFERENCES UtenteRegistrato(email)
);

-- Table: InfoPersonali
CREATE TABLE InfoPersonali (
    name VARCHAR(50),
    surname VARCHAR(50),
    country VARCHAR(50),
    city VARCHAR(50),
    address VARCHAR(100),
    zip VARCHAR(10),
    birthday_date DATE,
    cell VARCHAR(15),
    cliente_email VARCHAR(100) PRIMARY KEY,
    FOREIGN KEY (cliente_email) REFERENCES Cliente(utente_registrato_email)
);
-- Table: MetodoPagamento
CREATE TABLE MetodoPagamento (
    id VARCHAR(5) PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL,
    cvc INT NOT NULL,
    owner VARCHAR(100),
    expiration DATE NOT NULL,
    cliente_email VARCHAR(100),
    FOREIGN KEY (cliente_email) REFERENCES Cliente(utente_registrato_email)
);

-- Table: Carrello
CREATE TABLE Carrello (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_email VARCHAR(100) UNIQUE, -- Vincolo UNIQUE
    FOREIGN KEY (cliente_email) REFERENCES Cliente(utente_registrato_email)
);


CREATE TABLE Prodotto (
    id VARCHAR(5) PRIMARY KEY,
    name VARCHAR(100),
    description TEXT,
    cost FLOAT NOT NULL,
    pieces_in_stock INT,
    img MEDIUMBLOB,
    brand varchar(100),
    figure varchar(100)
);

-- Table: ProdottoCarrello
CREATE TABLE ProdottoCarrello (
    carrello_id INT,
    prodotto_id VARCHAR(5),
    quantity INT NOT NULL,
    unitary_cost FLOAT NOT NULL,
    PRIMARY KEY (carrello_id, prodotto_id), -- Vincolo PRIMARY KEY
    FOREIGN KEY (carrello_id) REFERENCES Carrello(id),
    FOREIGN KEY (prodotto_id) REFERENCES Prodotto(id)
);



-- Table: Ordine
CREATE TABLE Ordine (
    id VARCHAR(5) PRIMARY KEY,
    subtotal FLOAT NOT NULL,
    customer_email VARCHAR(100),
    status VARCHAR(20),
    data_ordine DATE,
    FOREIGN KEY (customer_email) REFERENCES Cliente(utente_registrato_email)
);

-- Table: RigaOrdine
CREATE TABLE RigaOrdine (
    id VARCHAR(5) PRIMARY KEY,
    ordine_id VARCHAR(5),
    prodotto_id VARCHAR(5),
    quantity INT NOT NULL,
    unitary_cost FLOAT NOT NULL,
    FOREIGN KEY (ordine_id) REFERENCES Ordine(id),
    FOREIGN KEY (prodotto_id) REFERENCES Prodotto(id)
);


-- Table: GestoreOrdine
CREATE TABLE GestoreOrdine (
    utente_registrato_email VARCHAR(100) PRIMARY KEY,
    FOREIGN KEY (utente_registrato_email) REFERENCES UtenteRegistrato(email)
);
