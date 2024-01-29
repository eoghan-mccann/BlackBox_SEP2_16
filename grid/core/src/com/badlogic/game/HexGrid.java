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
                for(int j=0;j<5+i;j++)
                {

                    shape.polygon(setHexPoints(startX, startY)); // make polygon
                    // change starting vals
                    //hello
                    //trying commit
                    //:(
                    startX+=86.6F;
                    startY+=50F;

                } // line drawn must change xy

            }
            else {
                startX+=((4*(i-5))*43.3F + (2*43.3F));
                for(int j=0;j<13-i;j++)
                {
                    shape.polygon(setHexPoints(startX, startY)); // make polygon
                    // change starting vals
                    startX+=86.6F;
                    startY+=50;

                } // line drawn must change xy

            }
            startX-=((11+(i*2))*43.3f);
            startY-=75;

        }
        // reset for next frame
        startX = 250F;
        startY = 750F;
    }

    public float[] setHexPoints(float X, float Y)
    {
        this.startX = X;
        this.startY = Y;
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
}
