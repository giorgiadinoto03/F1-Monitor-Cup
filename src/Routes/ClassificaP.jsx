import React, { useState } from 'react';
import { BrowserRouter, Route, Routes, Link } from 'react-router-dom';
import DriversData from '../data/piloti.json';
import PilotiCard from '../components/PilotiCard';

// Fetching data from the API
export function ClassificaPiloti() {
    // Ordina subito i dati in base a season_position
    const [drivers] = useState(
        [...DriversData].sort((a, b) => a.season_position - b.season_position)
    );

/*
    meeting_key=latest è il key dell weekend 
    session_key=latest è il key dell evento (pratiche, qualifiche o gara)
*/

    return (
        <>
        <div className="Piloti">
            <h1>Benvenuto nella sezione Piloti</h1>
            <h2>Informazioni sui piloti di Formula 1</h2>
            <p>Qui potrai trovare tutte le informazioni sulla stagione di Formula 1 2025 </p>
            <p> Usa la barra di navigazione in alto per esplorare le diverse sezioni dell'app.</p>
            <p>Buona navigazione!</p>

        {/*Bottone per vedere in base al numero del pilota*/}
                <Link to="/piloti">
                    <button className='Filtro'>Filtra per Numero Piloti</button>
                </Link>

            <table>
                <thead>
                    <tr>
                        <th>Classifica Piloti</th>
                        <th>  </th>
                        <th>Acronimo</th>
                        <th>Nome Completo</th>
                        <th>Team</th>
                        <th>Numero del pilota</th>
                        <th>Nazione</th>
                        <th>Punti Stagione 2025</th>
                    </tr>
                </thead>
                <tbody>
                    {drivers.map((driver) => (
                        <tr key={driver.broadcast_name}>
                            <td>{driver.season_position}</td>
                            <td>
                                <div
                                    className="PilotiBackground"
                                    style={{ backgroundColor: driver.team_colour }}
                                >
                                    <img
                                        className="PilotiImg"
                                        src={driver.headshot_url}
                                        alt={driver.full_name}
                                    />
                                </div>
                            </td>
                            <td>{driver.name_acronym}</td>
                            <td className='PilotiName' >{driver.first_name } {driver.last_name}
                                <br></br>
                                <PilotiCard driver={driver} /></td>                            <td>{driver.team_name}</td>
                            <td>{driver.driver_number}</td>
                            <td>{driver.country_code}</td>
                            <td>{driver.season_point}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
        </>
    );
}

export default ClassificaPiloti;
