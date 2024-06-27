package boardgame.model;

/**
 * Egy pozíciót modellező osztály.
 *
 * @param row a pozíció x koordinátája a egy táblázatban
 * @param col a pozíció x koordinátája a egy táblázatban
 */
public record Position(int row, int col) {

    /**
     * Egy Position típusú objektumot reprezentál Stringként.
     *
     * @return Visszaadja a Position típusú objektumot sor és oszlop értékét Stringként
     */
    public String toString(){return String.format("(sor: %d, oszlop: %d)", row, col);}


    /**
     * Eldönti két pozícióról, hogy egyenlőek-e.
     *
     * @param pos egy Position típusú objektum, amivel össze akarjuk hasonlítani az aktuális objektumunkat
     * @return true, ha a két objektum sor és oszlop koordinátája megfelelően megegyezik, egyébként false
     */
    public boolean equals(Position pos) {
        if(row == pos.row && col == pos.col){
            return true;
        }else {
            return false;
        }
    }
}
