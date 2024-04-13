package byog.Core;

import java.io.Serializable;

/** Position is a class with two variables p.x and p.y */
public class Position implements Serializable {
    private static final long serialVersionUID = 123123123123123L;
    int x;
    int y;

    public Position(int xPos, int yPos) {
        x = xPos;
        y = yPos;
    }
}
