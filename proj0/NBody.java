/** Simulate a universe specified in one of the data files */
public class NBody {

    /** Read the data file and return the radius of the universe in the data file.
     *
     * @param String filePath
     * @return double equals to the radius of the universe
     */
    public static double readRadius(String filePath) {
        In in = new In(filePath);
        int numOfPlanets = in.readInt();
        double radiusOfUniverse = in.readDouble();
        return radiusOfUniverse;
    }

    /** Read the data file and return an array of Planets corresponding to the planets in the file.
     *
     * @param String filePath
     * @return Array that contains the planets in the data file
     */
    public static Planet[] readPlanets(String filePath) {
        In in = new In(filePath);
        int numOfPlanets = in.readInt();
        double radiusOfUniverse = in.readDouble();
        Planet[] allPlanets = new Planet[numOfPlanets];
        for (int i = 0; i < numOfPlanets; i += 1) {
            double xP = in.readDouble();
            double yP = in.readDouble();
            double xV = in.readDouble();
            double yV = in.readDouble();
            double m = in.readDouble();
            String img = in.readString();
            allPlanets[i] = new Planet(xP, yP, xV, yV, m, img);
        }
        return allPlanets;
    }

    public static void main(String[] args) {
        // Collecting All Needed Input
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);

        StdDraw.enableDoubleBuffering();

        // Drawing the Background
        String BackgroungImageToDraw = "/images/starfield.jpg";
        StdDraw.setScale(-radius, radius);
        StdDraw.picture(0, 0, BackgroungImageToDraw);

        // Drawing All the Planets
        for (Planet planet : planets) {
            planet.draw();
        }

        // Creating an Animation
        for (double time = 0; time < T; time += dt) {
            int numOfPlanets = planets.length;
            double[] xForces = new double[numOfPlanets];
            double[] yForces = new double[numOfPlanets];
            for (int i = 0; i < numOfPlanets; i += 1) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for (int i = 0; i < numOfPlanets; i += 1) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }

            StdDraw.picture(0, 0, BackgroungImageToDraw);
            for (Planet planet : planets) {
                planet.draw();
            }

            StdDraw.show();
            int timeInterval = 10; // pause the animation for 10 milliseconds
            StdDraw.pause(timeInterval);
        }

        // Printing the final state of the Universe when you’ve reached time T
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
