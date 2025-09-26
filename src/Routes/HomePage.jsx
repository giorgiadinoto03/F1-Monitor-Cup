import React from "react";
import SideImage from "../components/SideImage";
import Header from "../components/Header";

export default function HomePage() {
    return (
        <div className="homepage-container">
            {/* Immagine a sinistra */}
            <SideImage
                src="https://static.vecteezy.com/ti/vettori-gratis/p1/16168385-da-corsa-e-rally-auto-scacchi-vettore-bandiera-vettoriale.jpg"
                alt="Bandiera a scacchi sinistra"
                position="left"
            />

            {/* Contenuto centrale */}
            <div className="main-content">
                <h1>Benvenuto in F1 Monitor Cup</h1>
                <h2>Applicazione web per monitorare le gare della Formula 1</h2>
                <p>
                    Qui potrai trovare tutte le informazioni sulla stagione di Formula 1
                    2025, incluse classifiche, piloti, scuderie, GP e statistiche.<br />
                    Usa la barra di navigazione in alto per esplorare le diverse sezioni
                    dell'app.
                </p>
                <p>Buona navigazione!</p>

                <br />
                <br />
                <h2>Il prossimo GP è</h2>
                <h3>{/* Qui inserirai i dati dinamici */}</h3>
            </div>

            {/* Immagine a destra */}
            <SideImage
                src="https://static.vecteezy.com/ti/vettori-gratis/p1/16168385-da-corsa-e-rally-auto-scacchi-vettore-bandiera-vettoriale.jpg"
                alt="Bandiera a scacchi destra"
                position="right"
            />
        </div>
    );
}
