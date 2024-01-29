package com.badlogic.game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HexGrid extends Hexagon{

    public HexGrid(float x, float y) {
        super(x, y);
    }

    public void drawGrid(ShapeRenderer shape)
    {
        for(int i=0;i<9;i++)
        {
            if(i<5) // growing bigger
            {
                less5(i, shape);
            }
            else
            {
                more5(i, shape);
            }
            startX-=((11+(i*2))*43.3f);
            startY-=75;

        }
        // reset for next frame
        startX = 250F;
        startY = 750F;
    }

    public float[] setHexPoints(float X, float Y) {
        this.setStartX(X);
        this.setStartY(Y);
        float[] hexPoints = {
                startX, startY,
                startX+=43.3F, startY+=25,
                startX+=43.3F, startY-=25,
                startX, startY-=50F,
                startX-=43.3F, startY-=25,
                startX-=43.3F, startY+=25
        };

        return hexPoints;
    }

    private void less5(int i, ShapeRenderer shape) {
        for(int j=0;j<5+i;j++)
        {

            shape.polygon(setHexPoints(startX, startY)); // make polygon
            // change starting vals
            startX+=86.6F;
            startY+=50F;

        } // line drawn must change xy
    }

    private void more5(int i, ShapeRenderer shape){
        startX+=((4*(i-5))*43.3F + (2*43.3F));
        for(int j=0;j<13-i;j++)
        {
            shape.polygon(setHexPoints(startX, startY)); // make polygon
            // change starting vals
            startX+=86.6F;
            startY+=50;

        } // line drawn must change xy
    }

}
