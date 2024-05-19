package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        state = 0;
        this.period = period;
    }

    @Override
    public double next() {
        state = state + 1;
        int weirdState = state & (state >> 7) % period;
        return normalize(weirdState);
    }

    /** Returns the normalized state with the input state.
     *  Helper function to convert values between 0 and period - 1
     *  to values between -1.0 and 1.0.
     */
    private double normalize(int originalState) {
        double normalizedState = 2.0 * originalState / (period - 1) - 1.0;
        return normalizedState;
    }
}
