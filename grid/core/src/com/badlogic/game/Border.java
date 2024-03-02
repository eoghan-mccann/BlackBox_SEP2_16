package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Border implements Clickable, Entities{
    /*
    This is a square hitbox around the given side, i think?
     */

    float x1, y1;
    float x2, y2;
    float[] midPoint;
    float[] revMid;
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
    public boolean isHoveredOver() {
        float curX = Gdx.input.getX();
        float curY = Gdx.graphics.getHeight() - Gdx.input.getY();

        if((curX > x1  && curX < x2 )  &&  (curY > y1 &&  curY < y2)) {
            color = Color.GREEN;
            return true;
        }
        return false;
    }

    @Override
    public float[] getCentre() {
        return new float[0];
    }

    @Override
    public float[] getCoordinates() {
        return new float[0];
    }

    @Override
    public void getCollision() {

    }

    @Override
    public void Draw(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(color);
        shape.line(midPoint[0], midPoint[1], revMid[0], revMid[1]);
        shape.end();
    }

    @Override
    public void update() {

    }

    public float[] midPoint(float x1, float y1, float x2, float y2)
    {
        float midX = (x1 + x2)/2;
        float midY = (y1 + y2)/2;

        return new float[]{midX, midY};
    }
}
