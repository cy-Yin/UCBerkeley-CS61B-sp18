package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int period;
    private int state;

    public SawToothGenerator(int period) {
        state = 0;
        this.period = period;
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        return normalize(state);
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
