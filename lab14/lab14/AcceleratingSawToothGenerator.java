package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private int state;
    private double factor;

    public AcceleratingSawToothGenerator(int period, double factor) {
        state = 0;
        this.period = period;
        this.factor = factor;
    }

    @Override
    public double next() {
        state = (state + 1) % period;
        /* After resetting, the period should change
         * by a factor of the second argument, rounded down.
         */
        if (state % period == 0) {
            period = (int) Math.round(period * factor);
        }
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
