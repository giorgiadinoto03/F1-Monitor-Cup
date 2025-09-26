import React, { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Typography,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
} from "@mui/material";
import DriversData from "../data/piloti.json";

export default function SessionCard({ sessionKey, label, meetingName, dateStart }) {
  const [open, setOpen] = useState(false);
  const [positions, setPositions] = useState([]);
  const [loading, setLoading] = useState(false);

  const sessionLabels = {
    R: "Gara",
    Q: "Qualifica",
    FP1: "Prove Libere 1",
    FP2: "Prove Libere 2",
    FP3: "Prove Libere 3",
  };

  const handleOpen = async () => {
    setOpen(true);
    setLoading(true);
    try {
      // 1️⃣ Fetch posizioni
      const resPos = await fetch(
        `https://api.openf1.org/v1/position?session_key=${sessionKey}`
      );
      const posData = await resPos.json();

      // 2️⃣ Fetch risultati (duration + gap)
      const resResult = await fetch(
        `https://api.openf1.org/v1/session_result?session_key=${sessionKey}`
      );
      const resultData = await resResult.json();

      // Mappa risultati per driver_number
      const resultMap = resultData.reduce((acc, r) => {
        acc[r.driver_number] = r;
        return acc;
      }, {});

      // 3️⃣ Ultima posizione per pilota
      const latestPositions = Object.values(
        posData.reduce((acc, pos) => {
          acc[pos.driver_number] = pos;
          return acc;
        }, {})
      );

      // 4️⃣ Arricchisci con dati locali + durata/gap
      const enriched = latestPositions
        .map((pos) => {
          const driver = DriversData.find(
            (d) => d.driver_number === pos.driver_number
          );
          const result = resultMap[pos.driver_number];
          if (!driver) return null;

          return {
            ...pos,
            ...driver,
            duration: result?.duration || "-",
            gap_to_leader: result?.gap_to_leader || "-",
          };
        })
        .filter(Boolean);

      // 5️⃣ Ordina per posizione
      const ordered = enriched.sort((a, b) => a.position - b.position);
      setPositions(ordered);
    } catch (err) {
      console.error("Errore fetch griglia:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setOpen(false);
    setPositions([]);
  };

  return (
    <>
      <Button
        className="session-button"
        variant="contained"
        color="error"
        onClick={handleOpen}
      >
        {label}
      </Button>

      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="lg">
        <DialogTitle>
          Risultati {sessionLabels[label] || label} – {meetingName}
        </DialogTitle>

        <DialogContent dividers>
          {loading ? (
            <Typography>Caricamento risultati...</Typography>
          ) : positions.length > 0 ? (
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Pos</TableCell>
                  <TableCell>Pilota</TableCell>
                  <TableCell>NO</TableCell>
                  <TableCell>Team</TableCell>
                  <TableCell>Durata</TableCell>
                  <TableCell>Gap Leader</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {positions.map((p, i) => (
                  <TableRow key={i}>
                    <TableCell>{p.position} th</TableCell>
                    <TableCell>
                      <div
                        style={{
                          display: "flex",
                          alignItems: "center",
                          gap: "8px",
                        }}
                      >
                        <div
                          style={{
                            backgroundColor: p.team_colour,
                            borderRadius: "6px",
                            padding: "2px",
                            width: "1.5px",
                          }}
                        >
                          <img
                            src={p.headshot_url}
                            alt={p.full_name}
                            style={{ width: "0px", borderRadius: "4px" }}
                          />
                        </div>
                        <Typography>{p.name_acronym}</Typography>
                      </div>
                    </TableCell>
                    <TableCell>{p.driver_number}</TableCell>
                    <TableCell>{p.team_name}</TableCell>
                    <TableCell>{p.duration}</TableCell>
                    <TableCell>{p.gap_to_leader}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          ) : (
            <Typography>Nessun risultato disponibile.</Typography>
          )}
        </DialogContent>

        <DialogActions>
          <Button onClick={handleClose} variant="outlined">
            Chiudi
          </Button>
        </DialogActions>
      </Dialog>
      <div className="session-item">


  <span>
    {new Date(dateStart).toLocaleDateString("it-IT", {
      weekday: "short",
      year: "numeric",
      month: "2-digit",
      day: "2-digit"
    })}{" "}
    {new Date(dateStart).toLocaleTimeString("it-IT", {
      hour: "2-digit",
      minute: "2-digit"
    })}
  </span>
</div>
    </>
  );
}
