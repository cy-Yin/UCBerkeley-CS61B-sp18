import synthesizer.GuitarString;

public class GuitarHero {
    private static final double CONCERT_A = 440.0;

    private static String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static void main(String[] args) {
        GuitarString[] strings = new GuitarString[37];
        for (int i = 0; i < 37; i++) {
            double frequency = CONCERT_A *  Math.pow(2, (double) (i - 24) / 12);
            strings[i] = new GuitarString(frequency);
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index == -1) {
                    continue;
                }
                strings[index].pluck();

            }

            /* compute the superposition of samples */
            double sample = 0;
            for (GuitarString guitarString : strings) {
                sample += guitarString.sample();
            }


            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (GuitarString guitarString : strings) {
                guitarString.tic();
            }
        }
    }
}
