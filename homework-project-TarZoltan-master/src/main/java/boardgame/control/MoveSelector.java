package boardgame.control;

import boardgame.model.GameModel;
import boardgame.model.Position;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.tinylog.Logger;


/**
 * A két kattintással történő lépés kivlasztást megvalósító osztály.
 *
 */

public class MoveSelector {

    /**
     * A lépés kiválasztás fázisának lehetséges értékeit tartalmazó enum osztály.
     *
     */
    public enum Phase {
        /**
         * A lépés kiválasztás "honnnan" fázisában vagyunk. Azt a mezőt kell kiválasztanunk, ahonnal ellépünk
         */
        SELECT_FROM,

        /**
         * A lépés kiválasztás "hová" fázisában vagyunk. Azt a mezőt kell kiválasztanunk, ahová szertnénk lépni
         */
        SELECT_TO,

        /**
         * A lépés kiválasztás "lépésre készen" fázisában vagyunk. Ekkor hajtható végre a konkrét lépés
         */
        READY_TO_MOVE

    }

    private GameModel model;
    private ReadOnlyObjectWrapper<Phase> phase = new ReadOnlyObjectWrapper<>(Phase.SELECT_FROM);
    private boolean invalidSelection = false;
    private Position from;
    private Position to;

    /**
     * Konstruktor, példányosítja az osztályt.
     *
     * @param model GameModel típusú objektum, a játék egy állása
     */
    public MoveSelector(GameModel model) {
        this.model = model;
    }

    /**
     * Visszaadja a lépésválasztás aktuális fázisát.
     *
     * @return az aktuális fázis
     */
    public Phase getPhase() {
        return phase.get();
    }

    /**
     * Visszaadja a phase csak olvasható részleteit.
     *
     * @return visszaadja a phase részleteit
     */
    public ReadOnlyObjectProperty<Phase> phaseProperty() {
        return phase.getReadOnlyProperty();
    }

    /**
     * Ellenőrzi, hogy készen állunk-e a lépésre.
     *
     * @return true, ha a READY_TO_MOVE fázisban vagyunk, egyébként flase
     */
    public boolean isReadyToMove() {
        return phase.get() == Phase.READY_TO_MOVE;
    }


    /**
     * Meghívja a megfelelő kiválasztó metódust.
     *
     * @param position Position típusú objektum. lépés fázistól függően innek vagy ide akarunk lépni
     */
    public void select(Position position) {
        switch (phase.get()) {
            case SELECT_FROM -> selectFrom(position);
            case SELECT_TO -> selectTo(position);
            case READY_TO_MOVE -> throw new IllegalStateException();
        }
    }

    /**
     * Kiválasztja a pozíziót ahonan el akarunk lépni.
     *
     * @param position Position típusú objektum. Innen akarunk ellépni
     */

    private void selectFrom(Position position) {
        if (!model.isEmpty(position) && !model.isBlack(position) && model.yourTurn(position)) {
            from = position;
            phase.set(Phase.SELECT_TO);
            invalidSelection = false;
        } else {
            invalidSelection = true;
            Logger.info("Nem érvényes kiválasztás");
        }
    }

    /**
     * Kiválasztja a pozíziót ahová akarunk lépni.
     *
     * @param position Position típusú objektum. Ide akarunk lépni
     */
    private void selectTo(Position position) {

            if(position.equals(from)){
                to = position;
                phase.set(Phase.READY_TO_MOVE);
                invalidSelection = false;
                GameModel.PLAYER = -1 * GameModel.PLAYER;
                Logger.info("Kiválasztvás megszüntetve!");
            }else{
                if (model.canMove(from, position)) {
                    to = position;
                    phase.set(Phase.READY_TO_MOVE);
                    invalidSelection = false;
                } else {
                    invalidSelection = true;
                    Logger.info("Nem érvényes kiválasztás!");
                }
            }
    }

    /**
     * Visszaadja a pozíciót ahonnan lépünk.
     *
     * @return azt a Position típusú objektumot, ahonnan indul a lépés
     */
    public Position getFrom() {
        if (phase.get() == Phase.SELECT_FROM) {
            throw new IllegalStateException();
        }
        return from;
    }

    /**
     * Visszaadja a pozíciót ahová lépünk.
     *
     * @return azt a Position típusú objektumot, ahová tart a lépés
     */

    public Position getTo() {
        if (phase.get() != Phase.READY_TO_MOVE) {
            throw new IllegalStateException();
        }
        return to;
    }

    /**
     * Eldönti, hogy egy kiválasztás érvénytelen-e.
     *
     * @return true, ha a kiválasztás érvénytelen, egyébként false
     */
    public boolean isInvalidSelection() {
        return invalidSelection;
    }

    /**
     * Ez a metódus teszi meg konkrétan a lépést, aszerint, ahogy azt a modell leíja.
     *
     */
    public void makeMove() {
        if (phase.get() != Phase.READY_TO_MOVE) {
            throw new IllegalStateException();
        }
        model.step(from, to);
        reset();
    }

    /**
     * Befejezi a kiválasztási fázis ciklust, és elindítja újra.
     * Itt a ciklus végén történik az ellenőrzése, véget ért-e a játék
     * Ha igen, ez a metódus hívja meg a GameOver metódust
     *
     */
    public void reset() {
        from = null;
        to = null;
        phase.set(Phase.SELECT_FROM);
        invalidSelection = false;
        if (model.isGameOver()){
            GameController.GameOver();
            Logger.info("Vége a játéknak!");
        }
    }

}
