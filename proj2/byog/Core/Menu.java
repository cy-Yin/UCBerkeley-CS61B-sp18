package byog.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

public class Menu {
    int width;
    int height;

    public Menu(int menuWidth, int menuHeight) {
        width = menuWidth;
        height = menuHeight;
    }

    /** When the user chooses to play with the keyboard, firstly generate the menu for user. */
    public void generateMenu() {
        int midWidth = width / 2;
        int midHeight = height / 2;

        StdDraw.setCanvasSize(width * 16, height * 16);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear();
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.WHITE);

        Font fontTitle = new Font("Monaco", Font.BOLD, 35);
        StdDraw.setFont(fontTitle);
        StdDraw.text(midWidth, midHeight + 5, "CS61B: THE GAME");

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(midWidth, midHeight, "New Game (N)");
        StdDraw.text(midWidth, midHeight - 2, "Load Game (L)");
        StdDraw.text(midWidth, midHeight - 4, "Quit (Q)");
        StdDraw.show();
    }

    /** Generates the menu to ask for the seed of the new world and returns the long-type seed.
     *  Show the up-to-date seed on the menu.
     *  @return
     */
    public long generateSeedMenu() {
        StdDraw.clear();
        StdDraw.clear(Color.black);
        Font gameInfoFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(gameInfoFont);
        StdDraw.setPenColor(Color.white);
        int midWidth = width / 2;
        int midHeight = height / 2;
        StdDraw.text(midWidth, midHeight + 4,
                "Please input the seed, then press \"s\" to start new game");
        StdDraw.show();
        String seedString = "";
        while (true) {
            StdDraw.clear(Color.black);
            StdDraw.setFont(gameInfoFont);
            StdDraw.text(midWidth, midHeight + 4,
                    "Please input the seed, then press \"s\" to start new game");

            char digit;
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            digit = StdDraw.nextKeyTyped();
            if (digit != 's' && digit != 'S') {
                if (!Character.isDigit(digit)) {
                    continue;
                }
                seedString += digit;
                StdDraw.setFont(new Font("Monaco", Font.PLAIN, 30));
                StdDraw.text(midWidth, midHeight - 2, seedString);
                StdDraw.show();
            } else {
                break;
            }
        }

        long seed = Long.parseLong(seedString);
        return seed;
    }
}
