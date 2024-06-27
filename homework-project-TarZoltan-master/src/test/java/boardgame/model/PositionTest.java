package boardgame.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    void testequals(){
        Position position1 = new Position(4, 5);
        Position position2 = new Position(2, 3);
        Position position3 = new Position(4, 5);

        assertTrue(position1.equals(position3));
        assertFalse(position2.equals(position3));
        assertFalse(position1.equals("Hello!"));
    }

    @Test
    void testtoString(){
        Position position1 = new Position(4, 5);
        Position position2 = new Position(1, 3);

        assertEquals(position1.toString(), "(sor: 4, oszlop: 5)");
        assertNotEquals(position2.toString(), "sor:1,oszlop3");
    }

}
