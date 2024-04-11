package byog.Core;

public class MapGenerationParameters {
    int width;
    int height;
    long seed;

    MapGenerationParameters(int mapWidth, int mapHeight, long mapSeed) {
        width = mapWidth;
        height = mapHeight;
        seed = mapSeed;
    }

    /** Generates a default map for MapVisualTest.java
     *  just hard-core the map seed, with the map size equals (Game.WIDTH * Game.HEIGHT)
     */
    public static MapGenerationParameters defaultParameters() {
        long seedDefault = 1234; // hard core the seed to 1234, rather than read from user's input
        return new MapGenerationParameters(Game.WIDTH, Game.HEIGHT, seedDefault);
    }
}
