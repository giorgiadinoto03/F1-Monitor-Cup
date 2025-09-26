# 🏎️ F1 Monitor Cup

[![React](https://img.shields.io/badge/React-18.2.0-blue)](https://reactjs.org/)
[![Vite](https://img.shields.io/badge/Vite-5.x-purple)](https://vitejs.dev/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

Applicazione web per monitorare le classifiche e le statistiche del campionato di Formula 1 2025 in tempo reale.

## 📋 Descrizione

F1 Monitor Cup è un'applicazione web frontend sviluppata con React e Vite che permette di visualizzare in modo semplice e intuitivo:
- **Classifiche** piloti e scuderie aggiornate
- **Risultati completi** dei GP con posizioni finali e migliori giri
- **Dettagli piloti e scuderie** con statistiche della stagione 2025
- **Informazioni sui prossimi eventi** e sessioni (libere, qualifiche, gara)

## 🎯 Target

Rivolto principalmente ad appassionati di Formula 1, inizialmente sviluppato per un gruppo ristretto di amici con possibilità di espansione a una community più vasta. Ideale sia per chi cerca informazioni rapide che per chi desidera analisi dettagliate.

## 🚀 Mission

Offrire un'esperienza utente semplice, veloce e visivamente efficace per l'accesso a classifiche e statistiche di gara, superando la complessità dei siti ufficiali tradizionali.

## ✨ Funzionalità

### Versione 1.0 (Attuale)
- ✅ **Classifiche** piloti e scuderie aggiornate.
- ✅ **Risultati GP** con posizioni finali e migliori giri.
- ✅ **Dashboard piloti** con statistiche stagione 2025.
- ✅ **Integrazione parziale API** OpenF1 per dati in tempo reale.
- ✅ **Design ** con Material UI.

### Roadmap (Versione 2.0)
- 🔄 **Integrazione completa API** ufficiali
- 🔄 **Backend Django** per gestione dati avanzata
- 🔄 **Aggiornamenti automatici** in tempo reale
- 🔄 **Notifiche** sessioni e risultati

## 🏗️ Architettura Tecnica

### Frontend
- **React 18** con Hooks e functional components
- **Vite** come build tool e dev server
- **SCSS** per styling modulare e variabili CSS
- **React Router** per la navigazione

### Struttura Navigazione
Home → Prossimo GP
↓
Piloti
↓
Scuderie
↓
Risultati GP 2025


### Dati
- **API OpenF1** per dati in tempo reale
- **JSON locale** per dati mockup e integrazioni
- **Future integrazioni** con API ufficiali F1

## 🛠️ Installazione e Utilizzo

### Prerequisiti
- Node.js 16+ e npm

### Setup
```bash
# Clona il repository
git clone https://github.com/tuo-username/f1-monitor-cup.git

# Entra nella cartella del progetto
cd f1-monitor-cup

# Installa le dipendenze
npm install

# Avvia il server di sviluppo
npm run dev
```

### 🎨 Design e UX
Interfaccia ottimizzata per:

- Visualizzazione rapida delle informazioni principali
- Navigazione intuitiva tra le diverse sezioni
- Design moderno ispirato al mondo della Formula 1
- Experience mobile-first e responsive


#### *Autore*
Giorgia Di Noto - giorgiadinoto03@gmail.com
