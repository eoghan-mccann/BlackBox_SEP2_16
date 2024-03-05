package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Arrays;

public class Border implements Clickable, Entities{

    public boolean debug = false;

    float x1, y1;
    float x2, y2;
    float[] midPoint;
    float[] revMid;
    float[] boundingBox;
    Color color;
    int direction;

    public Border(float x1, float y1, float x2, float y2, int dir)
    {
        this.x1 = x1;
        this.y1 = y1;

        this.x2 = x2;
        this.y2 = y2;
        color = Color.WHITE;
        midPoint = midPoint(x1, y1, x2, y2); //


        //direction mirrors ray's direction and hexagon's sideBorders[] - 0 is coming from NW, 1 W, etc
        this.direction = dir; // use to print line, get revMid
        setRevMid();
        initBoundingBox();

    }

    public void setRevMid()
    { /* this method sets the second point of the line that is drawn at each side
        this val can't be calc'ed generally for any side, so it has to be done like this
        the direction indicates which side is being handled
        */
        switch(direction)
        {
            case 0:
                revMid = new float[]{x2-2, y1+12};
                break;
            case 1:
                revMid = new float[]{midPoint[0] + 30, midPoint[1]};
                break;
            case 2:
                revMid = new float[]{x1-2, y2-12};
                break;
            case 3:
                revMid = new float[]{x2+2, y1-12};
                break;
            case 4:
                revMid = new float[]{midPoint[0] - 30, midPoint[1]};
                break;
            case 5:
                revMid = new float[]{x1+2, y2+12};
                break;
            default:
                break;
        }
    }

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
    public void onClick() {

    }

    @Override
    public boolean isClicked() {
        if(isHoveredOver() && Gdx.input.justTouched())
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean isHoveredOver()
    {
        float curX = Gdx.input.getX();
        float curY = Gdx.graphics.getHeight() - Gdx.input.getY();

        return contains(curX, curY);
    }
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

    @Override
    public float[] getCentre() {
        return new float[0];
    }

    @Override
    public float[] getCoordinates() {
        return new float[]{midPoint[0], midPoint[1], revMid[0], revMid[1]};
    }

    @Override
    public void getCollision() {

    }

    @Override
    public void Draw(ShapeRenderer shape) {

        color = isHoveredOver() ? Color.ROYAL : Color.WHITE;

        shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(color);
            shape.line(midPoint[0], midPoint[1], revMid[0], revMid[1]);
        shape.end();

        if (debug) { // draw bounding box
            shape.begin(ShapeRenderer.ShapeType.Line);
                shape.setColor(Color.GREEN);
                shape.polygon(boundingBox);
            shape.end();
        }
    }

    @Override
    public void update() {
        if (isClicked()) {
            System.out.println("CLICKED");
        }
    }

    public float[] midPoint(float x1, float y1, float x2, float y2)
    {
        float midX = (x1 + x2)/2;
        float midY = (y1 + y2)/2;

        return new float[]{midX, midY};
    }

}
