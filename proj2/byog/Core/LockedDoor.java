package byog.Core;

import java.io.Serializable;

public class LockedDoor implements Serializable {
    private static final long serialVersionUID = 123123123123123L;
    Position pos;

    LockedDoor(Position doorPos) {
        pos = doorPos;
    }
}
