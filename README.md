<<<<<<< HEAD
# F1-Monitor-Cup
Applicazione web per monitorare le classifiche del campionato di Formula 1 2025, con dati su piloti, scuderie, risultati gara e migliori giri.

1. **Titolo del progetto**
    
    **F1 Monitor Cup**
    
2. **Descrizione del progetto**
    
    Applicazione web per monitorare le classifiche del campionato di Formula 1 2025, con dati su piloti, scuderie, risultati gara e migliori giri.
    
    Struttura di navigazione:
    
    - Home → Prossimo Gp
    - Pagina Classifiche.
    - Pagina contenete i dati degli scorsi GP.
    - Pagina Piloti/Scuderia.
3. **Target**
    
    Rivolto ad appassionati di Formula 1, inizialmente a un gruppo ristretto di amici, con possibilità di ampliamento a una community più vasta, sia per chi vuole avere un colpo d’occhio veloce, sia per chi desidera confrontare statistiche in dettaglio.
    
4. **Mission**
    
    Offrire un colpo d’occhio semplice, veloce e visivo su classifiche e statistiche di gara, rendendo l’esperienza più immediata e intuitiva rispetto ai siti ufficiali, che spesso sono più complessi e dispersivi
    
5. **Funzionalità (Desiderata → Requirements)**
    
    a. Visualizzare classifiche piloti e scuderie aggiornate.
    
    b. Consultare i risultati dei GP con posizioni finali e migliori giri.
    
    c. Dashboard dedicate per piloti e scuderie con statistiche intuitive.
    
    d. Integrazione futura con API in tempo reale per aggiornamenti post-gara.
    
6. **Architettura Software**
    
    Webapp **React 18** con **Vite**, **SCSS** per la gestione degli stili.
    
    Database **MySQL/MariaDB** in container Docker con gestione tramite phpMyAdmin.
    
    Inizialmente i dati saranno mockup inseriti manualmente con un file JSON e in futuro saranno sostituiti da dati reali tramite API.
    
7. **Note**
    
    I requisiti dal punto d in poi saranno sviluppati in una **versione 2** del progetto, che includerà l’integrazione con API ufficiali e aggiornamenti automatici in tempo reale.
=======
<<<<<<< HEAD
# React + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react) uses [Babel](https://babeljs.io/) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## Expanding the ESLint configuration

If you are developing a production application, we recommend using TypeScript with type-aware lint rules enabled. Check out the [TS template](https://github.com/vitejs/vite/tree/main/packages/create-vite/template-react-ts) for information on how to integrate TypeScript and [`typescript-eslint`](https://typescript-eslint.io) in your project.

## API UTILI
**meeting_key=latest è il key dell weekend**
**session_key=latest è il key dell evento (pratiche, qualifiche o gara)**

classfica ultima gara 
https://api.openf1.org/v1/position?meeting_key=latest&session_key=latest

dati di un weekend data, orari inizio e fine (pratiche, qualifiche e gara)
https://api.openf1.org/v1/sessions?year=2025

risultati ultima gara
https://api.openf1.org/v1/position?meeting_key=latest&session_key=latest

tutti i circuiti del 2025 (svolti)
https://api.openf1.org/v1/meetings?year=2025
=======
# ProjectWork

In questa repository raccolgo alcuni progetti su cui ho lavorato durante il mio percorso di studio e pratica con **Java, HTML, CSS e Javascript**, altri linguaggi sia per il *Backend* che per il *Frontend *e lo sviluppo software.

## 📌 Progetti attuali:

- **Sistema_Biblioteca**  
  Un progetto di **Backend** sviluppato in **Java** con Spring Boot.  
  Contiene:
  - codice sorgente del backend
  - file di configurazione per Docker
  - test automatici
  - una **relazione PDF** che documenta il progetto
  - un README dedicato nella cartella del progetto

## 🎯 Obiettivo
Questa repository ha lo scopo di raccogliere e condividere i miei lavori, mostrando le competenze acquisite nello sviluppo di applicazioni da Full stack developers.

---

📂 Per maggiori dettagli sul funzionamento e sull’architettura del progetto, consulta direttamente la cartella [`Sistema_Biblioteca`](./Sistema_Biblioteca).
>>>>>>> b834dc7dc592c2762861427181d886b365a39667
>>>>>>> 9dffe0f (F1 Monitor Cup project 1.0)
