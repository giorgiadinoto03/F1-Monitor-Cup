package its.fullstack.biblioteca_proj.models;

public enum Ruolo {
    ADMIN("ADMIN"),
    CLIENT("CLIENT");

    private final String ruolo;

    Ruolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public String getRuolo() {
        return ruolo;
    }

    /*
     * Converte una stringa in enum Ruolo
     * @param ruoloString la stringa da convertire
     * @return l'enum corrispondente o CLIENT di default
     */
    public static Ruolo fromString(String ruoloString) {
        if (ruoloString == null) {
            return CLIENT;
        }

        for (Ruolo ruolo : Ruolo.values()) {
            if (ruolo.ruolo.equalsIgnoreCase(ruoloString)) {
                return ruolo;
            }
        }
        return CLIENT; // Default
    }

    /*
     * Verifica se l'utente ha privilegi di amministratore
     * @return true se è ADMIN
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /*
     * Verifica se l'utente è un client normale
     * @return true se è CLIENT
     */
    public boolean isClient() {
        return this == CLIENT;
    }

    @Override
    public String toString() {
        return ruolo;
    }
}