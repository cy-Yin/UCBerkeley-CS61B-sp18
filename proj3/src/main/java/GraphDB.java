import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    Map<Long, Node> nodes = new HashMap<Long, Node>();
    Map<Long, Way> ways = new HashMap<Long, Way>();
    Map<String, Long> path = new HashMap<>();

    public class Node {
        long id;
        double lon;
        double lat;
        List<Long> neighbors;
        String name;

        Node(long id, double lat, double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.neighbors = new ArrayList<>();
            this.name = null;
        }

        void setName(String name) {
            this.name = name;
        }

        /** Connecting this node A to another node B
         * means A is B's neighbor and vice versa.
         */
        void connectTo(Node other) {
            if (other.id == this.id) {
                return;
            }
            this.neighbors.add(other.id);
            other.neighbors.add(this.id);
        }
    }

    public class Way {
        long id;
        List<Long> connectNodesId;
        String name;

        Way(long id) {
            this.id = id;
            this.connectNodesId = new ArrayList<>();
            this.name = null;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // Your code here.
        Map<Long, Node> nodesAfterClean = new HashMap<Long, Node>();
        for (Node node : nodes.values()) {
            if (!node.neighbors.isEmpty()) {
                nodesAfterClean.put(node.id, node);
            }
        }
        nodes = nodesAfterClean;
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE
        return nodes.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return nodes.get(v).neighbors;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /** Returns which direction user to take according to the relative bearing.
     * * Between -15 and 15 degrees the direction should be “Continue straight”.
     * * Beyond -15 and 15 degrees but between -30 and 30 degrees
     *      the direction should be “Slight left/right”.
     * * Beyond -30 and 30 degrees but between -100 and 100 degrees
     *      the direction should be “Turn left/right”.
     * * Beyond -100 and 100 degrees the direction should be “Sharp left/right”.
     * @param prevBearing the previous bearing
     * @param bearing the current bearing
     */
    int calculateDirection(double prevBearing, double bearing) {
        double diff = bearing - prevBearing;
        
        // Guarantee diff is always ranging from -180 to 180.
        while (!(diff <= 180 && diff > -180)) {
            if (diff < -180) {
                diff += 360;
            } else if (diff > 180) {
                diff -= 360;
            }
        }
        
        if (diff >= -15.0 && diff <= 15.0) {
            return Router.NavigationDirection.STRAIGHT;
        } else if (diff >= -30.0 && diff < -15.0) {
            return Router.NavigationDirection.SLIGHT_LEFT;
        } else if (diff <= 30.0 && diff > 15.0) {
            return Router.NavigationDirection.SLIGHT_RIGHT;
        } else if (diff >= -100.0 && diff < -30.0) {
            return Router.NavigationDirection.LEFT;
        } else if (diff <= 100.0 && diff > 30.0) {
            return Router.NavigationDirection.RIGHT;
        } else {
            if (diff < -100.0) {
                return Router.NavigationDirection.SHARP_LEFT;
            } else { // diff > 100.0
                return Router.NavigationDirection.SHARP_RIGHT;
            }
        }
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long closestId = 0;
        double minDistance = Double.MAX_VALUE;
        for (long w : vertices()) {
            double distance = distance(lon, lat, lon(w), lat(w));
            if (minDistance > distance) {
                minDistance = distance;
                closestId = w;
            }
        }
        return closestId;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return nodes.get(v).lat;
    }

    /** Adds the node to the graph. */
    void addNode(long nodeId, double lon, double lat) {
        Node node = new Node(nodeId, lon, lat);
        nodes.put(nodeId, node);
    }

    /** Adds the way to the graph. */
    void addWay(long wayId, List<Long> connectNodes) {
        Way validWay = new Way(wayId);
        validWay.connectNodesId = connectNodes;
        ways.put(wayId, validWay);
        for (int i = 0; i < connectNodes.size() - 1; i += 1) {
            long nodeVId = connectNodes.get(i);
            long nodeWId = connectNodes.get(i + 1);
            nodes.get(nodeVId).connectTo(nodes.get(nodeWId));

            path.put(nodeVId + "to" + nodeWId, wayId);
            path.put(nodeWId + "to" + nodeVId, wayId);
        }
    }

    /** Returns the way's id according to the ids of two nodes. */
    Long findWayId(Long v, Long w) {
        String pathName = v + "to" + w;
        if (path.containsKey(pathName)) {
            return path.get(pathName);
        } else {
            return (long) -1;
        }
    }

    /** Returns the way's name from the input way id. */
    String getWayName(Long wayId) {
        if (!ways.containsKey(wayId) || ways.get(wayId).name == null) {
            return Router.NavigationDirection.UNKNOWN_ROAD;
        }
        return ways.get(wayId).name;
    }
}
