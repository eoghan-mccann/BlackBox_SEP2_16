package com.badlogic.game;

/**
 * A class containing methods for use in Hexagon.
 */
public class HexUtil {

    /**
     * Calculates the coordinates of each of the corners of a Hexagon, and stores them to the Hexagon's hexPoints array.
     * @param hex The Hexagon whose points are being calculated
     * @param x The starting x coordinate
     * @param y The starting y coordinate
     */
    public void setHexagonPos(Hexagon hex, float x, float y) {
        hex.setCenter(x, y);

        float[] xPoints = this.calculateXpoints(hex, x);
        float[] yPoints = this.calculateYpoints(hex, y);
        float[] flattenedPoints = new float[12];

        // Flattens array to work with polygon [x1,y1,x2,y2,...]
        for (int i = 0; i < xPoints.length; i++) {
            flattenedPoints[2 * i] = xPoints[i];
            flattenedPoints[2 * i + 1] = yPoints[i];
        }

        hex.hexPoints = flattenedPoints;
    }


    /**
     * Helper method of {@code setHexagonPos}. Generates the x coordinates of each corner of the Hexagon
     * @param hex Hexagon whose points being calculated
     * @param x Starting x coordinate
     * @return Array of 6 float coordinates
     */
    public float[] calculateXpoints(Hexagon hex, float x) {
        float[] tempX = new float[6];
        for (int i = 0; i < 6; i++) {
            tempX[i] = (float)(x + (hex.getRadius() * Math.sin(hex.getAngle() * i)));
        }
        return tempX;
    }

    /**
     * Helper method of {@code setHexagonPos}. Generates the y coordinates of each corner of the Hexagon
     * @param hex Hexagon whose points being calculated
     * @param y Starting y coordinate
     * @return Array of 6 float coordinates
     */
    public float[] calculateYpoints(Hexagon hex, float y) {
        float[] tempY = new float[6];
        for (int i = 0; i < 6; i++) {
            tempY[i] = (float)(y + (hex.getRadius() * Math.cos(hex.getAngle() * i)));
        }
        return tempY;
    }



    /**
     * Determines whether a point (x, y) is inside the given Hexagon, using the point in polygon algorithm.
     * @param hex The Hexagon being checked.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return {@code true} if inside, {@code false} otherwise.
     */
    public boolean isInside(Hexagon hex, float x, float y) {
        int i, j;
        boolean isInside = false;
        float[] vertices = hex.getCoordinates();

        for (i = 0, j = vertices.length - 2; i < vertices.length; j = i, i += 2) {
            if ((vertices[i + 1] > y) != (vertices[j + 1] > y) && (x < (vertices[j] - vertices[i]) * (y - vertices[i + 1]) / (vertices[j + 1] - vertices[i + 1]) + vertices[i])) {
                isInside = !isInside;
            }
        }

        return isInside;

    }

}
