import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private final int maxDepth = 7;

    public Rasterer() {

    }

    /** Returns true if the params is valid, false otherwise.
     *  "valid" means:
     *  1. the params must contain all the information for query,
     *      including the upper-left and lower-left longitudes/latitudes of the query box
     *      and the width and height of user's viewpoint.
     *  2. the query box must have a valid area, which means the upper-left corner must be
     *      at the northwest direction of the lower-left one.
     *  3. the query box is not allowed to be completely outside the root longitude/latitudes,
     *      which means it must contain part of the root map at least.
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *              the user viewport width and height.
     */
    private boolean isValidParams(Map<String, Double> params) {
        if (!(params.containsKey("lrlon") && params.containsKey("ullon")
            && params.containsKey("w") && params.containsKey("h")
            && params.containsKey("ullat") && params.containsKey("lrlat"))) {
            return false;
        }

        /* For a valid query box
         *
         * ulLat -> ***********
         *          *         *
         *          *         *
         * lrLat -> ***********
         *          ^         ^
         *        ulLon     lrLon
         *
         * ulLat > lrLat and ulLon < lrLon
         */
        double lrLon = params.get("lrlon");
        double ulLon = params.get("ullon");
        double lrLat = params.get("lrlat");
        double ulLat = params.get("ullat");

        return lrLon > ulLon && lrLat < ulLat // query box with a valid area
                // query box is not allowed to be completely outside the root longitude/latitudes
                && ulLat > MapServer.ROOT_LRLAT && lrLat < MapServer.ROOT_ULLAT
                && ulLon < MapServer.ROOT_LRLON && lrLon > MapServer.ROOT_ULLON;
    }

    /** Returns the appropriate depth of images.
     * the map with an appropriate depth has a resolution
     * which is just equal to or greater than the demanded one.
     *
     * @param demandLonDPP the demanded LonDPP calculated through the user's query params
     */
    private int getDepth(double demandLonDPP) {
        int depth = 0;
        // initialize LonDPP to d0.
        double depthLonDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
        while (depth < maxDepth && depthLonDPP > demandLonDPP) {
            depthLonDPP = depthLonDPP / 2;
            depth = depth + 1;
        }
        return depth;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();

        if (!isValidParams(params)) {
            results.put("render_grid", null);
            results.put("raster_ul_lon", null);
            results.put("raster_ul_lat", null);
            results.put("raster_lr_lon", null);
            results.put("raster_lr_lat", null);
            results.put("depth", null);
            results.put("query_success", false);
            return results;
        }

        double lrLon = params.get("lrlon");
        double ulLon = params.get("ullon");
        double width = params.get("w");
        double height = params.get("h");
        double ulLat = params.get("ullat");
        double lrLat = params.get("lrlat");

        double demandLonDPP = (lrLon - ulLon) / width;
        int depth = getDepth(demandLonDPP);

        int[] corners = getRenderGridCorners(depth, lrLon, ulLon, ulLat, lrLat);
        int startX = corners[0];
        int endX = corners[1];
        int startY = corners[2];
        int endY = corners[3];

        String[][] renderGrid = new String[endY - startY + 1][endX - startX + 1];
        for (int i = startX; i <= endX; i += 1) {
            for (int j = startY; j <= endY; j += 1) {
                renderGrid[j - startY][i - startX] = "d" + depth
                                    + "_" + "x" + i + "_" + "y" + j + ".png";
            }
        }

        double rasterULLon = MapServer.ROOT_ULLON
                + startX * (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        double rasterULLat = MapServer.ROOT_ULLAT
                - startY * (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
        double rasterLRLon = MapServer.ROOT_ULLON
                + (endX + 1) * (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        double rasterLRLat = MapServer.ROOT_ULLAT
                - (endY + 1) * (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);

        results.put("render_grid", renderGrid);
        results.put("raster_ul_lon", rasterULLon);
        results.put("raster_ul_lat", rasterULLat);
        results.put("raster_lr_lon", rasterLRLon);
        results.put("raster_lr_lat", rasterLRLat);
        results.put("depth", depth);
        results.put("query_success", true);
        return results;
    }

    /** Returns an array containing the corners' positions of the grid.
     *  To be more specific, returns [startX, endX, startY, endY]
     *
     * @param depth the depth of the map with appropriate resolution
     * @param lrLon longitude of the lower-right corner
     * @param ulLon longitude of the upper-left corner
     * @param ulLat latitude of the upper-left corner
     * @param lrLat latitude of the lower-right corner
     */
    private int[] getRenderGridCorners(int depth, double lrLon, double ulLon,
                                                double ulLat, double lrLat) {
        double numImagesN = Math.pow(2, depth); // totally N * N images on the root map
        double stepX = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / numImagesN;
        double stepY = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / numImagesN;

        // calculates the coordinates of images need to be displayed
        int startX = 0;
        int startY = 0;
        int endX = 0;
        int endY = 0;

        final double x = MapServer.ROOT_ULLON;
        while (x + startX * stepX <= ulLon && x + startX * stepX <= MapServer.ROOT_LRLON) {
            startX += 1;
        }
        if (startX > 0) {
            startX -= 1;
        }
        while (x + endX * stepX <= lrLon && x + endX * stepX <= MapServer.ROOT_LRLON
                                                                    && endX < numImagesN) {
            endX += 1;
        }
        if (endX > 0) {
            endX -= 1;
        }

        final double y = MapServer.ROOT_ULLAT;
        while (y - startY * stepY >= ulLat && y - startY * stepY >= MapServer.ROOT_LRLAT) {
            startY += 1;
        }
        if (startY > 0) {
            startY -= 1;
        }
        while (y - endY * stepY >= lrLat && y - endY * stepY >= MapServer.ROOT_LRLAT
                                                                    && endY < numImagesN) {
            endY += 1;
        }
        if (endY > 0) {
            endY -= 1;
        }


        int[] corners = {startX, endX, startY, endY};
        return corners;
    }

}
