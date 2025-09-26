import React from "react";
import DriversData from "../data/piloti.json";
import TeamData from "../data/scuderie.json";

export function Scuderie() {
    
    return (
    <div className="Scuderie" >
        <h1>Benvenuto nella sezione Scuderie</h1>
        <h2>Informazioni sui team di Formula 1</h2>
        <p>Qui potrai trovare tutte le informazioni sulla stagione di Formula 1 2025 </p>
        <p> Usa la barra di navigazione in alto per esplorare le diverse sezioni dell'app.</p>
        <p>Buona navigazione!</p>

        
        <table>
            <thead>
                <tr>
                    <th>Classifica Scuderia</th>
                    <th>Scuderia</th>
                    <th>Logo</th>
                    <th>Piloti </th>
                    <th>Punti Stagione 2025</th>
                </tr>
            </thead>

            <tbody>
                {TeamData.map((team, index) => (
                <tr key={index}>
                    <td>{team.season_position}</td>
                    <td>{team.team_name}</td>
                    <td>
                        <div
                            className='LogoBackground'
                            style={{backgroundColor: team.team_colour}}
                        >
                            <img 
                            className="TeamLogo"
                                src={team.team_logo}
                                alt={team.team_name}
                            />
                        </div>
                    </td>
                    <td> 
                        <ul className="PilotiList">
                            {team.drivers.map((driver, index) => (
                                <li key={index} className="Pilota">
                                    <img
                                    className='PilotiImgTeam'
                                        src={driver.headshot_url}
                                        alt={driver.full_name}
                                />
                                <span>{driver.full_name}</span>
                                </li>
                            ))}
                        </ul>
                    </td>
                    <td>{team.season_point}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    </div>
    );
};

export default Scuderie;