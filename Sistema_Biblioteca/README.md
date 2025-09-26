# 📚 Sistema Biblioteca - Backend

Un progetto di Backend sviluppato in Java con Spring Boot.
Contiene:
codice sorgente del backend
file di configurazione per Docker
test automatici
una relazione PDF che documenta il progetto
un README dedicato nella cartella del progetto

Backend per la gestione di una biblioteca con autenticazione JWT e ruoli (Admin/Client), sviluppato con Spring Boot per il Project Work UF13.

## 📌 Indice
- [Panoramica](#-panoramica)
- [Tecnologie Utilizzate](#-tecnologie-utilizzate)
- [Architettura](#-architettura)
- [Installazione](#-installazione)
- [Servizi disponibili](#-servizi-disponibili)
- [Credenziali di accesso](#-credenziali-di-accesso)
- [API Endpoints](#-api-endpoints)
- [Permessi speciali Admin](#-permessi-speciali-admin)
- [Database](#-database)
- [Crediti e Miglioramenti](#-crediti-e-miglioramenti)

## 🌐 Panoramica

Sistema completo per la gestione di:
- **Catalogo libri** con ricerca avanzata
- **Prestiti** con scadenze e restituzioni
- **Utenti** con autenticazione JWT e ruoli (ADMIN/CLIENT)

### ✅ Requisiti Soddisfatti
- **Spring Boot MVC** con architettura a strati
- **Spring Data JPA** con relazioni 1:N e N:M
- **Spring Security** con JWT e controllo ruoli
- **Testing** con copertura >35%
- **Docker** con container per app e database
- **Validazione input** e gestione errori

## 🛠 Tecnologie Utilizzate

### Backend
- Java 21
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA
- Maven 3.9+

### Database
- MySQL 8.0
- phpMyAdmin

### Container
- Docker 24+
- Docker Compose 2.0+

## 🏗 Architettura

    ┌─────────────────┐
    │   Controllers   │ ← Gestione API REST
    ├─────────────────┤
    │   Services      │ ← Logica di business
    ├─────────────────┤
    │   Repositories  │ ← Accesso al database
    ├─────────────────┤
    │    Models       │ ← Entità JPA
    └─────────────────┘

## 🚀 Installazione

1. **Prerequisiti**:
    - Docker installato
    - Docker Compose installato
    - Java 21 installato
    - Maven (opzionale, già incluso nel progetto)
    - Postman per testare le richieste dell'API REST
    - IDE per sviluppo (IntelliJ IDEA)

2. **Avvio**:
  ```bash
  cd <directory che contiene docker-compose.yml>
  cd ..\Sistema_Biblioteca\biblioteca_proj\docker
  docker-compose up --build -d
  ```


#### Altri comandi utili:
1. **Verifica Stato Container (Opzionale)**
```bash
    docker-compose ps
 ```

2. **Visualizzazione Log (Opzionale)**
```bash
docker-compose logs -f
```

3. **Arresto Servizi**
```bash
docker-compose down
```

## 🔎 Servizi disponibili:

- Applicazione: http://localhost:8081 o http://localhost:8082
- ‼️ **NOTA: La porta può variare in base alla configurazione di Docker Compose o a conflitti locali. Controllare i log per sapere quale porta è attiva.**
- PhpMyAdmin: http://localhost:8080 (user: root, password: root)
- Database MySQL: porta 3306

### 🔑 Credenziali di accesso
**Utenti predefiniti (password per tutti: password):**

- Admin: admin@biblioteca.it
- Client: mario.rossi@example.com
- Altro admin: giorgia.bianchi@biblioteca.it
- Altro client: francesco.verdi@example.com

## 📡 API Endpoints
### Root (/):
- GET / --> Home
Questo endpoint restituisce un messaggio di benvenuto.

- ❗❗️ **NOTA: Questo endpoint serve per verificare a quale porta l'applicazione è in ascolto.** ❗❗️
http://localhost:8081 o http://localhost:8082, quindi modificare la variabile su postman {{base_url}} con la porta corretta.


### Autenticazione (/api/auth):
- POST /api/auth/login --> Login (restituisce JWT token).

❗️❗️ **NOTA: nella collection, inserire il token ottenuto nelle variabili alla voce "jwt_token" della collection.** ❗️❗️
```bash
{
  "email": "admin@biblioteca.it",
  "password": "password"
}
```
### Libri (/api/books):
- GET /api/books/all --> Lista tutti i libri
- GET /api/books/available --> Libri disponibili
- GET /api/books/{id} --> Dettaglio libro
- POST /api/books/search/title --> Cerca per titolo

```bash 
{"title": "Harry Potter"}
```

### Prestiti (/api/prestiti):
- POST /api/prestiti/nuovo --> Crea prestito
- POST /api/prestiti/restituisci --> Restituisci libro
```bash
{
"userId": 2,
"bookId": 1
}
```

### Utenti (/api/users):
- POST /api/users/register - Registra nuovo utente

```bash
{
"name": "Nome",
"surname": "Cognome",
"email": "nuovo@email.com",
"password": "password",
"age": 22
}
```
## 🔒 Permessi speciali Admin
**Solo gli admin possono:**
- Creare/modificare/eliminare libri
- Vedere tutti i prestiti
- Eliminare utenti

## 📊 Database
**Il sistema include già:**

- 7 libri di esempio
- 4 utenti (2 admin e 2 client)
- 2 prestiti attivi

## Crediti e Miglioramenti:
Progetto sviluppato da Giorgia Di Noto, per il Project Work UF13 - per il corso di Full Stack Developer.

### Miglioramenti futuri:
- Implementazione con un frontend adeguato.
- Implementazione di un sistema per comprare libri.
- Implementazione di un sistema di pagamenti per i libri acquistati.
- Inserimento di microservizi.
