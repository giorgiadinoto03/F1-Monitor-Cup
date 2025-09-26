import React, { useEffect, useState } from "react";
import SessionCard from "../components/SessionCard";

export function Gp2025() {
    const [GranPremio, setGranPremio] = useState([]); // Stato per i meeting
    const [Sessioni, setSessioni] = useState({}); // Stato per le sessioni (oggetto con chiavi meeting_key)

    useEffect(() => {
        const fetchGranPremio = async () => {
            try {
                const response = await fetch('https://api.openf1.org/v1/meetings?year=2025');
                const data = await response.json();
                setGranPremio(data);

                // Dopo aver recuperato i meeting, recupera le sessioni
                fetchSessioni(data);
            } catch (error) {
                console.error("Errore durante il fetch dei dati dei meeting:", error);
            }
        };
        const fetchSessioni = async () => {
            try {
                const responseGp = await fetch('https://api.openf1.org/v1/sessions?year=2025');
                const data = await responseGp.json();

                // Raggruppa le sessioni per meeting_key
                const sessioniPerMeeting = {};
                data.forEach((session) => {
                    const key = session.meeting_key;
                    if (!sessioniPerMeeting[key]) {
                        sessioniPerMeeting[key] = { practice: [], qualifying: [], race: [] };
                    }

                    // Classifica le sessioni in base al tipo
                    if (session.session_type === "Practice") {
                        sessioniPerMeeting[key].practice.push(session);
                    } else if (session.session_type === "Qualifying") {
                        sessioniPerMeeting[key].qualifying.push(session);
                    } else if (session.session_type === "Race") {
                        sessioniPerMeeting[key].race.push(session);
                    }
                });

                setSessioni(sessioniPerMeeting); // Aggiorna lo stato con le sessioni
            } catch (error) {
                console.error("Errore durante il fetch dei dati delle sessioni:", error);
            }
        };

        fetchGranPremio();
    }, []);

    return (
        <>
            <div className="Gp2025">
                <h1>GP eseguiti nel 2025</h1>
                <h2>Elenco dei Gran Premi</h2>
                <p>Qui potrai trovare tutte le informazioni sulla stagione di Formula 1 2025.</p>
                <p>Seleziona un Gran Premio per vedere i dettagli delle sessioni.</p>
                <h3>L'elenco di GP 2025 mostra SOLO i GP di Formula 1 GIA' PASSATI</h3>
                
                <table className="gp-table">
                    <thead>
                        <tr>
                            <th>Paese</th>
                            <th>Nome Ufficiale</th>
                            <th>Location</th>
                            <th>Prove Libere</th>
                            <th>Qualifiche</th>
                            <th>Gara</th>
                        </tr>
                    </thead>
                    <tbody>
                        {GranPremio.map((gp, index) => (
                            <tr key={index}>
                                {/* Colonna Paese */}
                                <td>
                                    <strong style={{ fontSize: "1.2em" }}>{gp.country_name}</strong>
                                </td>

                                {/* Colonna Nome Ufficiale */}
                                <td>
                                    <div>
                                        <span style={{ fontWeight: "bold" }}>{gp.meeting_name}</span>
                                        <br />
                                        <span style={{ fontStyle: "italic" }}>{gp.meeting_official_name}</span>
                                    </div>
                                </td>

                                {/* Colonna Location */}
                                <td>{gp.location}</td>

                                {/* Colonna Prove Libere */}
                                <td>
                                    {Sessioni[gp.meeting_key]?.practice.length > 0 ? (
                                        Sessioni[gp.meeting_key].practice.map((session, i) => (
                                            <div key={i} className="session-item">
                                                <SessionCard
                                                sessionKey={session.session_key}
                                                label={`FP${i + 1}`}
                                                meetingName={gp.meeting_name}
                                                dateStart={session.date_start}
                                                /> 
                                            </div>
                                            
                                        ))
                                    ) : (
                                        "N/A"
                                    )}
                                    
                                </td>

                                {/* Colonna Qualifiche */}
                                <td>
                                    {Sessioni[gp.meeting_key]?.qualifying.length > 0 ? (
                                        Sessioni[gp.meeting_key].qualifying.map((session, i) => (
                                            <div key={i} className="session-item">
                                                <SessionCard
                                                sessionKey={session.session_key}
                                                label="Qualifying"
                                                meetingName={gp.meeting_name}
                                                dateStart={session.date_start}
                                                />
                                            </div>
                                        ))
                                    ) : (
                                        "N/A"
                                    )}
                                </td>

                                {/* Colonna Gara */}
                                <td>
                                    {Sessioni[gp.meeting_key]?.race.length > 0 ? (
                                        Sessioni[gp.meeting_key].race.map((session, i) => (
                                            <div key={i} className="session-item">
                                                <SessionCard
                                                sessionKey={session.session_key}
                                                label="Race"
                                                meetingName={gp.meeting_name}
                                                dateStart={session.date_start}
                                                />
                                                
                                            </div>
                                        ))
                                    ) : (
                                        "N/A"
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </>
    );
};

export default Gp2025;