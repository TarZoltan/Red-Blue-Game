package boardgame.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * A játék alapvető logikáját, működését leíró osztály.
 */
public class GameModel {

    /**
     * A játéktábla sorainak száma.
     */
    public static final int BOARD_ROW = 6;

    /**
     * A játéktábla oszlopainak száma.
     */
    public static final int BOARD_COL = 7;

    /**
     * A soron következő játékost azonosító változó.
     */
    public static int PLAYER = 1;


    private static String Blue_Player;

    private static String Red_Player;

    /**
     * A kék játékos nevének lekérése.
     *
     * @return a kék játékos neve
     */
    public static String getBlue_Player() {
        return Blue_Player;
    }

    /**
     * A kék játékos nevének beálítás.
     *
     * @param blue_Player a kék játékos neve String-ben
     */
    public static void setBlue_Player(String blue_Player) {
        Blue_Player = blue_Player;
    }

    /**
     * A piros játékos nevének lekérése.
     *
     * @return a piros játékos neve
     */
    public static String getRed_Player() {
        return Red_Player;
    }

    /**
     * A piros játékos nevének beálítás.
     *
     * @param red_Player a piros játékos neve String-ben
     */
    public static void setRed_Player(String red_Player) {
        Red_Player = red_Player;
    }


    /**
     * A tiltott mező beállítás.
     * Ezen a helyen tároljuk ideiglenesen annak a mezőnek a bábuját, ahová lépni akar a játokos
     */
    public static final Position tilt = new Position(3,2);


    private ReadOnlyObjectWrapper<Disk>[][] board = new ReadOnlyObjectWrapper[BOARD_ROW][BOARD_COL];


    /**
     * Konstruktor, létrehozza a játokot a fent definiált feltételekkel.
     */
    public GameModel(){
        
        for (int i = 0; i < BOARD_ROW; i++){
            for (int j = 0; j < BOARD_COL; j++){
                
                if(i == 0){
                    board[i][j] = new ReadOnlyObjectWrapper<Disk>(Disk.RED);
                } else if (i == BOARD_ROW -1) {
                    board[i][j] = new ReadOnlyObjectWrapper<Disk>(Disk.BLUE);
                } else if ((i == 3 && j ==2) || (i == 2 && j == 4)) {
                    board[i][j] = new ReadOnlyObjectWrapper<Disk>(Disk.BLACK);
                }else {
                    board[i][j] = new ReadOnlyObjectWrapper<Disk>(Disk.NONE);
                }
            }
        }

    }


    /**
     * Egy adott pozíció elhelyezkedő bábu színét adja vissza. Illetve azt is, ha a mező üres, vagy fekete
     *
     * @param p a pozíció amire kíváncsiak vagyunk
     * @return a bábu színe
     */
    public Disk getDisk(Position p) {
        return board[p.row()][p.col()].get();
    }

    /**
     * Beálíthajuk vele egy pozíción állapotát (kék bábu/piros bábu/fekete/üres).
     *
     * @param p a pozíció amit kezelni akarunk
     * @param disk az állapot amilyenre változtatni akarjuk a pizíció állapotát
     */
    public void setDisk(Position p, Disk disk) {
        board[p.row()][p.col()].set(disk);
    }


    /**
     * Visszaadja a csak olvasható tulajdonságait a megfelelő objektumnak.
     *
     * @param i a táblázat hányadik sorában van az elem ami érdekel
     * @param j a táblázat hányadik oszlopában van az elem, ami érdekel
     * @return a Disk.java egy "állapota" (NONE/BLUE/RED/BLACK)
     */
    public ReadOnlyObjectProperty<Disk> diskProperty(int i, int j)
    {
        return board[i][j].getReadOnlyProperty();
    }

    /**
     * Egy lépés logikáját írja le, használatával léptetjük a bábukat.
     *
     * @param from egy Position típusú változó, ahonnan lépni kívánunk
     * @param to egy Position típusú változó, ahová lépni kívánunk
     */

    public void step(Position from, Position to){
        if(isStroke(from, to)){
            setDisk(to, getDisk(from));
            setDisk(from, Disk.NONE);
            PLAYER = -1*PLAYER;
        }else{
            setDisk(tilt, getDisk(to));
            setDisk(to, getDisk(from));
            setDisk(from, getDisk(tilt));
            setDisk(tilt, Disk.NONE);
            PLAYER = -1*PLAYER;
        }
    }

    /**
     * Megadja, hogy egy adott pozícióvól létezik-e legális lépés.
     *
     * @param from egy Position típusú változó, ahonnan ellenőrizni akarjuk van-e szabad lépés
     * @return true, ha az adott bábuval létezik szabályos lépés, egyébként false
     */
    public boolean legalSteps(Position from){
        for (int i = 0; i < BOARD_ROW; i++){
            for (int j = 0; j < BOARD_COL; j++){
                Position temp = new Position(i, j);
                if (canMove(from, temp)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Annak ellenőrzésére szolgál, véget ért-e már a játék.
     *
     * @return true, ha a soron következő játékos már nem tud lépni, egyébként false
     */
    public boolean isGameOver(){
        for (int i = 0; i < BOARD_ROW; i++){
            for(int j = 0; j < BOARD_COL; j++){
                Position temp = new Position(i, j);
                if(yourTurn(temp)){
                    if(legalSteps(temp)){
                        return false;
                    };
                }
            }
        }
        return true;
    }

    /**
     * Megmondja, hogy egy mezőről egy másikra szabad-e a lépés.
     *
     * @param from egy Position típusú változó, ahonnan lépni kívánunk
     * @param to egy Position típusú változó, ahová lépni kívánunk
     * @return true, ha a megadott lépés engedélyezett, egyébként false
     */
    public boolean canMove(Position from, Position to){
        return isOnBoard(from) && isOnBoard(to) && yourTurn(from) && isForward(from, to) && !isUnallowed(to) && !isBlack(to) && !isEmpty(from) && (isEmpty(to) || isStroke(from, to))&& isPawnMove(from, to);
    }

    /**
     * Megmondja üres-e egy mező.
     *
     * @param p Position típusú változó, a mező ami érdekel minket
     * @return  true, ha a mező igaz, egyébként false
     */
    public boolean isEmpty(Position p) {
        return getDisk(p) == Disk.NONE;
    }

    /**
     * Megadja, hogy a vizsgált mező nem-e az előre definiált, ideiglenes tárolásra használt mező-e.
     *
     * @param p Position típusú változó, a vizsgálni kívánt mező
     * @return true, ha a tiltott mezőt vizsgáljuk, egyébként false
     */
    public boolean isUnallowed(Position p){
        return p.equals(tilt);
    }

    /**
     * Megmondja, hogy egy mező fekete-e.
     *
     * @param p Position típusú változó, a vizsgálni kívánt mező
     * @return true, ha a mező fekete, egyébként false
     */
    public boolean isBlack(Position p) {
        return getDisk(p) == Disk.BLACK;
    }


    /**
     * Azzal tér vissza, hogy az adott mezőn lévő bábu tulajdonosa következik-e a lépésben.
     *
     * @param p Position típusú változó, a vizsgálni kívánt mező
     * @return true, ha a kiválasztott mezőn lévő bábu tulajdonosa következik, egyébként false
     */
    public boolean yourTurn(Position p){
        if (getDisk(p) == Disk.BLUE) {
            return PLAYER == 1;
        } else if (getDisk(p) == Disk.RED) {
            return PLAYER == -1;
        }else {
            return false;
        }
    }

    /**
     * Megmondja, hogy egy adott pozíció része-e a játéktérnek.
     *
     * @param p Position típusú változó, a vizsgálni kívánt mező
     * @return true, ha a pozíció része a táblának, egyébként false
     */

    public static boolean isOnBoard(Position p) {
        return 0 <= p.row() && p.row() < BOARD_ROW && 0 <= p.col() && p.col() < BOARD_COL;
    }

    /**
     *Eldönti, hogy egy adott lépés ütés-e.
     *
     * @param from Position típusú változó, ahonnan lépni kívánunk
     * @param to Position típusú változó, ahová lépünk
     * @return true, ha a lépés ütés, egyébként false
     */
    public boolean isStroke(Position from, Position to){
        int dx = Math.abs(to.row() - from.row());
        int dy = Math.abs(to.col() - from.col());
        boolean opposite = (getDisk(from) == Disk.BLUE && getDisk(to) ==Disk.RED) || (getDisk(from) == Disk.RED && getDisk(to) == Disk.BLUE);
        return dx * dy == 1 && opposite;
    }

    /**
     * Azt mondja meg, hogy két mező szomszédos-e (szomszédosnak számít két mező akkor is, ha csak a sarkuk érintkezik).
     *
     * @param from Position típusú változó, egyik mező
     * @param to Position típusú változó, másik mező
     * @return true, ha a két mező szomszédos, egyébként false
     */

    public static boolean isPawnMove(Position from, Position to) {
        int dx = Math.abs(to.row() - from.row());
        int dy = Math.abs(to.col() - from.col());
        return dx + dy == 1 || dx * dy == 1;
    }

    /**
     * Megmondja, hogy egy adott lépés (amit két pozíció reprezentál), előre haladásnak számít-e.
     *
     * @param from Position típusú változó, ahonnan lépni kívánunk
     * @param to Position típusú változó, ahová lépünk
     * @return true, ha a lépés előre irányul (akár átlósan is), egyébként false
     */
    public boolean isForward(Position from, Position to){
        return (from.row()- to.row()) == PLAYER;
    }

    /**
     * A tábla álását reprezentálja.
     *
     * @return a játék állását adja vissza Stringként
     */
    public String toString() {
        var sb = new StringBuilder();
        for (var i = 0; i < BOARD_ROW; i++) {
            for (var j = 0; j < BOARD_COL; j++) {
                sb.append(board[i][j].get().ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        var model = new GameModel();
        System.out.println(model);
    }


}


