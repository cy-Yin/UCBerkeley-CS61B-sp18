public class Planet {
    public double xxPos; // its current x position
    public double yyPos; // its current y position
    public double xxVel; // its current velocity in the x direction
    public double yyVel; // its current velocity in the y direction
    public double mass;  // its mass
    public String imgFileName; // file name that corresponds to the image depicting the planet

    private static final double G = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    /** Take in a Planet object and initialize an identical Planet object. */
    public Planet(Planet p) {
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    /** Calculate the distance between two Planets.
     *
     * @param Planet p
     * @return a double equal to the distance between the supplied planet and the planet
     */
    public double calcDistance(Planet p) {
        double xDistance = p.xxPos - this.xxPos;
        double yDistance = p.yyPos - this.yyPos;
        double distance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);
        return distance;
    }

    /** Calculate the force exerted on this planet by the given planet.
     * @param Planet p
     * @return a double equal to the force
     */
    public double calcForceExertedBy(Planet p) {
        double distance = this.calcDistance(p);
        double force = G * this.mass * p.mass / (distance * distance);
        return force;
    }

    /** Calculate the force in the x direction exerted on this planet by the given planet.
     * @param Planet p
     * @return a double equal to the force in the x direction
     */
    public double calcForceExertedByX(Planet p) {
        double force = this.calcForceExertedBy(p);
        double distance = this.calcDistance(p);
        double dx = p.xxPos - this.xxPos;
        double forceX = force * dx / distance;
        return forceX;
    }

    /** Calculate the force in the y direction exerted on this planet by the given planet.
     * @param Planet p
     * @return a double equal to the force in the y direction
     */
    public double calcForceExertedByY(Planet p) {
        double force = this.calcForceExertedBy(p);
        double distance = this.calcDistance(p);
        double dy = p.yyPos - this.yyPos;
        double forceY = force * dy / distance;
        return forceY;
    }

    /** Calculate the net force in the x direction
     * exerted on this planet by the whole given planets.
     * @param Planet[] allPlanets
     * @return a double equal to the net force in the x direction
     */
    public double calcNetForceExertedByX(Planet[] allPlanets) {
        double netForceX = 0;
        for (Planet planet : allPlanets) {
            if (this.equals(planet)) {
                continue;
            }
            netForceX += this.calcForceExertedByX(planet);
        }
        return netForceX;
    }

    /** Calculate the net force in the y direction
     * exerted on this planet by the whole given planets.
     * @param Planet allPlanets
     * @return a double equal to the net force in the y direction
     */
    public double calcNetForceExertedByY(Planet[] allPlanets) {
        double netForceY = 0;
        for (Planet planet : allPlanets) {
            if (this.equals(planet)) {
                continue;
            }
            netForceY += this.calcForceExertedByY(planet);
        }
        return netForceY;
    }

    /** Calculate the change in the planet’s velocity and position in a small period of time
     *  with the net forces exerted on the planet.
     *
     * @param period of time dt, the net force in x direction, the net force in y direction
     */
    public void update(double dt, double fX, double fY) {
        double accelerationX = fX / this.mass;
        double accelerationY = fY / this.mass;
        // Calculate the change of the planet's velocity
        this.xxVel = this.xxVel + dt * accelerationX;
        this.yyVel = this.yyVel + dt * accelerationY;
        // Calculate the change of the planet's position
        // Here the velocity in the x, y direction has been updated before update the position.
        this.xxPos = this.xxPos + dt * this.xxVel;
        this.yyPos = this.yyPos + dt * this.yyVel;
    }

    /** Use the StdDraw API to draw the Planet’s image at the Planet’s position */
    public void draw() {
        String imagePath = "./images/" + this.imgFileName;
        StdDraw.picture(this.xxPos, this.yyPos, imagePath);
    }
}
