-- Creazione del database (se non esiste già)
CREATE DATABASE IF NOT EXISTS biblioteca_proj;
USE biblioteca_proj;

-- Tabella utenti
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    age INT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'CLIENT'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabella libri
CREATE TABLE IF NOT EXISTS book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    year INT,
    disponibile BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabella prestiti
CREATE TABLE IF NOT EXISTS prestiti (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    data_prestito DATE NOT NULL,
    data_restituzione DATE,
    restituito BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES book(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- Inserimento dei dati di esempio per `book`
INSERT INTO book (name, author, category, year, disponibile) VALUES
('Innamorata di un Angelo', 'Federica Bosco', 'Romanzo', 2005, TRUE),
('La caduta dei tre regni', 'Morgan Rice', 'Fantasy', 2014, TRUE),
('Il Signore degli Anelli', 'J.R.R. Tolkien', 'Fantasy', 1954, TRUE),
('Io uccido', 'Giorgio Faletti', 'Thriller', 2002, TRUE),
('Harry Potter e la Pietra Filosofale', 'J.K. Rowling', 'Fantasy', 1997, TRUE),
('Cronache del mondo emerso', 'Licia Troisi', 'Fantasy', 2004, TRUE),
('Il nome della rosa', 'Umberto Eco', 'Storico', 1980, TRUE);

-- Inserimento dei dati di esempio per `users` (SEMPLIFICATO)
-- Le password sono "password" per tutti, criptate con BCrypt
INSERT INTO users (name, surname, age, email, password, role) VALUES
('Admin', 'Admin', 30, 'admin@biblioteca.it', '$2a$12$43Kb.ypjZYPh5f6PJS0GR.2C1BiFA4G3TmlSkD.QDOACHE8LUqla2', 'ADMIN'),
('Mario', 'Rossi', 25, 'mario.rossi@example.com', '$2a$12$79pQdOoxtV7Vv72U70az0u9S05FD8C3aWCGXiOOvQdMMexLcmrH5q', 'CLIENT'),
('Giorgia', 'Bianchi', 28, 'giorgia.bianchi@biblioteca.it', '$2a$12$OGMGwiJ4h8bTSafzkDP5Xev7GuOAzx0aUAhNpxo.PE71q0iBLWUoq', 'ADMIN'),
('Francesco', 'Verdi', 19, 'francesco.verdi@example.com', '$2a$12$JOZ0hUanFldlhb9hukFk5uaPyhbAPj0DjwoMsxG6UyhqYKNO8a3Ua', 'CLIENT');


-- Inserimento prestiti di esempio
INSERT INTO prestiti (user_id, book_id, data_prestito, data_restituzione, restituito) VALUES
(2, 5, '2025-06-01', '2025-07-01', FALSE),
(4, 3, '2025-07-10', '2025-07-30', FALSE);

UPDATE book SET disponibile = FALSE WHERE id IN (5, 3);
