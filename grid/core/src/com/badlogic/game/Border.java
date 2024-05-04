package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * A Clickable Entity representing the side of a Hexagon exposed to the perimeter. Each Hexagon has at most 3 Borders.
 * Borders are represented in-game by a straight, perpendicular line coming out of the border side of the hexagon.
 */
public class Border implements Clickable, Entities{
    private boolean clickable;
    private boolean boundingBoxVisible;

    boolean hasRay = false; // Each border can hold exactly 1 ray
    HexagonGrid hexagonGrid;

    float x1, y1, x2, y2;  // Coordinates of the associated side's points

    float[] midPoint; // mid-point of the associated hexagon's associated side (where the border starts)
    float[] revMid;  // the other point of the border's line
    float[] boundingBox;
    Color color;

    Ray.Direction direction;
    Hexagon assocHex; // associated hexagon

    public Border(float x1, float y1, float x2, float y2, Ray.Direction dir, HexagonGrid hex, Hexagon assoc)
    {
        this.x1 = x1;
        this.y1 = y1;

        this.x2 = x2;
        this.y2 = y2;
        color = Color.WHITE;
        midPoint = midPoint(x1, y1, x2, y2);

        hexagonGrid = hex;


        this.direction = dir;
        setRevMid(direction);
        initBoundingBox();

        this.assocHex = assoc;

        clickable = false;
        boundingBoxVisible = false;

    }

    /**
     * Calculates the coordinates of the second point on the Border's line.
     *
     * @param direction The associated hexagon's associated side's position.

     */
    public void setRevMid(Ray.Direction direction)
    {
        // Depending on direction, set coordinates to make a perpendicular line
        switch(direction)
        {
            case NE:
                revMid = new float[]{x2-2, y1+12};
                break;
            case E:
                revMid = new float[]{midPoint[0] + 30, midPoint[1]};
                break;
            case SE:
                revMid = new float[]{x1-2, y2-12};
                break;
            case SW:
                revMid = new float[]{x2+2, y1-12};
                break;
            case W:
                revMid = new float[]{midPoint[0] - 30, midPoint[1]};
                break;
            case NW:
                revMid = new float[]{x1+2, y2+12};
                break;
            default:
                break;
        }
    }

    /**
     * Generates the box around the Border's line in which the Border can be clicked. Updates {@code boundingBox} to hold these coordinates.
     */
    private void initBoundingBox() {
        float[] linePoints = this.getCoordinates();

        float x1 = linePoints[0];
        float x2 = linePoints[2];
        float dX = x1 - x2;

        float y1 = linePoints[1];
        float y2 = linePoints[3];
        float dY = y1 - y2;

        float D = (float) Math.sqrt((dX * dX) + (dY * dY));

        dX = 10 * dX / D;
        dY = 10 * dY / D;

        boundingBox = new float[]{
                x1 -dY, y1 + dX,
                x1 + dY, y1 -dX,
                x2 + dY, y2  -dX,
                x2 -dY, y2 + dX,
        };
    }


    @Override
    public boolean isClicked() {
        return (isHoveredOver() && Gdx.input.justTouched()) && clickable;
    }

    @Override
    public boolean isHoveredOver()
    {
        float curX = Gdx.input.getX();
        float curY = Gdx.graphics.getHeight() - Gdx.input.getY();

        return contains(curX, curY);
    }

    /**
     * Determines if a given x, y coordinate is inside the Border's {@code boundingBox}.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return {@code true} if point is in the boundingBox, {@code false} otherwise.
     */
    public boolean contains(float x, float y) {
        int i, j;
        boolean isInside = false;
        float[] vertices = boundingBox;

        for (i = 0, j = vertices.length - 2; i < vertices.length; j = i, i += 2) {
            if ((vertices[i + 1] > y) != (vertices[j + 1] > y) && (x < (vertices[j] - vertices[i]) * (y - vertices[i + 1]) / (vertices[j + 1] - vertices[i + 1]) + vertices[i])) {
                isInside = !isInside;
            }
        }

        return isInside;

    }

    /**
     * Sets the clickable boolean.
     *
     * @param clickable The logical value to assign to clickable.

     */
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    @Override
    public float[] getCentre() {
        return new float[0];
    }

    @Override
    public float[] getCoordinates() {
        return new float[]{midPoint[0], midPoint[1], revMid[0], revMid[1]};
    }


    @Override
    public void Draw(ShapeRenderer shape) {

        color = isHoveredOver() && clickable ? Color.ROYAL : Color.WHITE;

        shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(color);
            shape.line(midPoint[0], midPoint[1], revMid[0], revMid[1]);
        shape.end();

        // draw bounding box
        if (boundingBoxVisible) {
            shape.begin(ShapeRenderer.ShapeType.Line);
                shape.setColor(Color.GREEN);
                shape.polygon(boundingBox);
            shape.end();
        }
    }

    @Override
    public void update() {
        if (isClicked() && !hasRay) { // Various pixel offsets due to the isInside function
            if (direction == Ray.Direction.NE || direction == Ray.Direction.NW) {
                hexagonGrid.addRay(midPoint[0]+direction.direction[0], midPoint[1] + direction.direction[1], direction, assocHex);
            } else if (direction == Ray.Direction.SE){
                hexagonGrid.addRay(midPoint[0]+direction.direction[0], midPoint[1] + direction.direction[1] , direction, assocHex);
            } else if (direction == Ray.Direction.E ) {
                hexagonGrid.addRay(midPoint[0] + direction.direction[0], midPoint[1] +direction.direction[1], direction, assocHex);
            } else {
                hexagonGrid.addRay(midPoint[0] + direction.direction[0], midPoint[1] +direction.direction[1], direction, assocHex);
            }
            hasRay = true;
        }
    }

    /**
     * Sets the visibility of the boundingBox around a Border.
     *
     * @param visible The status to set the boundingBoxVisible bool to.
     */
    public void setBoundingBoxVisible(boolean visible) {
        boundingBoxVisible = visible;
    }

    /**
     * Calculates and returns the midpoint of the two points given.
     *
     * @param x1 The first x coordinate.
     * @param y1 The first y coordinate.
     * @param x2 The second x coordinate.
     * @param y2 The second y coordinate.
     * @return The midpoint of the given line
     */
    public float[] midPoint(float x1, float y1, float x2, float y2)
    {
        float midX = (x1 + x2)/2;
        float midY = (y1 + y2)/2;

        return new float[]{midX, midY};
    }

}
