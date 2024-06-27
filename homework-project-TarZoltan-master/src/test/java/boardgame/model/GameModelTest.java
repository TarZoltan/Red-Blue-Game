package boardgame.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static boardgame.model.GameModel.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {

    @Test
    void testisEmpty(){

        GameModel game = new GameModel();
        Position p = new Position(2, 3);

        game.setDisk(p, Disk.BLUE);
        assertFalse(game.isEmpty(p));

        game.setDisk(p, Disk.NONE);
        assertTrue(game.isEmpty(p));
    }

    @Test
    void testSetDisk(){

        GameModel game = new GameModel();
        Position p = new Position(2, 3);

        game.setDisk(p, Disk.BLUE);
        assertEquals(game.getDisk(p), Disk.BLUE);
        assertNotEquals(game.getDisk(p), Disk.NONE);


    }

    @Test
    void testIsYourTurn(){

        GameModel game = new GameModel();
        Position p = new Position(2, 3);
        PLAYER = 1;

        game.setDisk(p, Disk.RED);
        assertFalse(game.yourTurn(p));

        game.setDisk(p, Disk.BLUE);
        assertTrue(game.yourTurn(p));

        game.setDisk(p, Disk.BLACK);
        assertFalse(game.yourTurn(p));

    }

    @Test
    void testIsPawnMove(){

        Position p1 = new Position(2, 3);
        Position p2 = new Position(1, 3);
        Position p3 = new Position(4, 1);
        Position p4 = new Position(1, 2);

        assertTrue(isPawnMove(p1, p2));
        assertFalse(isPawnMove(p1, p3));
        assertTrue(isPawnMove(p1, p4));

    }

    @Test
    void testStep(){

        GameModel game = new GameModel();
        Position p1 = new Position(2, 3);
        Position p2 = new Position(3, 4);

        game.setDisk(p1, Disk.BLUE);
        game.setDisk(p2, Disk.NONE);
        game.step(p1, p2);

        assertEquals(game.getDisk(p2), Disk.BLUE);
        assertNotEquals(game.getDisk(p2), Disk.NONE);

        game.setDisk(p1, Disk.RED);
        game.setDisk(p2, Disk.BLUE);
        game.step(p1, p2);
        assertEquals(game.getDisk(p2), Disk.RED);
        assertEquals(game.getDisk(p1), Disk.NONE);

    }

    @Test
    void testIsUnallowed(){

        GameModel game = new GameModel();
        Position p1 = new Position(2, 3);
        Position p2 = new Position(3, 2);

        assertFalse(game.isUnallowed(p1));
        assertTrue(game.isUnallowed(p2));

    }

    @Test
    void testIsBlack(){

        GameModel game = new GameModel();
        Position p1 = new Position(2, 1);
        Position p2 = new Position(5, 2);

        game.setDisk(p1, Disk.NONE);
        game.setDisk(p2, Disk.BLACK);

        assertFalse(game.isBlack(p1));
        assertTrue(game.isBlack(p2));

        game.setDisk(p1, Disk.BLUE);
        game.setDisk(p2, Disk.RED);

        assertFalse(game.isBlack(p1));
        assertFalse(game.isBlack(p2));

    }

    @Test
    void testIsOnBoard(){

        Position p1 = new Position(2, 1);
        Position p2 = new Position(100, 11);
        Position p3 = new Position(-3, 9);
        Position p4 = new Position(6, 7);

        assertTrue(isOnBoard(p1));
        assertFalse(isOnBoard(p2));
        assertFalse(isOnBoard(p3));
        assertFalse(isOnBoard(p4));

    }

    @Test
    void testIsStroke(){

        GameModel game = new GameModel();
        Position p1 = new Position(2, 1);
        Position p2 = new Position(3,2);
        Position p3 = new Position(2,2);


        game.setDisk(p1, Disk.BLUE);
        game.setDisk(p2, Disk.RED);
        game.setDisk(p3, Disk.RED);

        assertTrue(game.isStroke(p1, p2));
        assertFalse(game.isStroke(p1, p3));

        game.setDisk(p2, Disk.BLUE);
        assertFalse(game.isStroke(p1, p2));

        game.setDisk(p2, Disk.BLACK);
        assertFalse(game.isStroke(p1, p2));

        game.setDisk(p2, Disk.NONE);
        assertFalse(game.isStroke(p1, p2));

    }

    @Test
    void testIsForward(){

        GameModel game = new GameModel();
        Position p1 = new Position(2, 1);
        Position p2 = new Position(3,2);
        PLAYER = 1;

        assertFalse(game.isForward(p1, p2));

        PLAYER = -1;

        assertTrue(game.isForward(p1, p2));

    }

    @Test
    void testLegalSteps(){

        GameModel game = new GameModel();
        Position p1 = new Position(2, 3);
        Position p2 = new Position(1, 3);
        PLAYER = 1;
        game.setDisk(p1,Disk.BLUE);
        game.setDisk(p2, Disk.NONE);

        assertTrue(game.legalSteps(p1));
    }

    @Test
    void testCanMove(){

        GameModel game = new GameModel();
        Position p1 = new Position(2, 3);
        Position p2 = new Position(1, 3);
        PLAYER = 1;
        game.setDisk(p1,Disk.BLUE);
        game.setDisk(p2, Disk.NONE);

        assertTrue(game.canMove(p1, p2));

        PLAYER = -1;
        assertFalse(game.canMove(p1, p2));

        PLAYER = 1;
        game.setDisk(p2, Disk.BLACK);
        assertFalse(game.canMove(p1, p2));

        game.setDisk(p2, Disk.BLUE);
        assertFalse(game.canMove(p1, p2));

        game.setDisk(p2, Disk.NONE);
        Position p3 = new Position(1, 5);
        assertFalse(game.canMove(p1, p3));

        Position p4 = new Position(1, 12);
        assertFalse(game.canMove(p1, p4));

        game.setDisk(p1, Disk.NONE);
        assertFalse(game.canMove(p1, p2));

    }

}
