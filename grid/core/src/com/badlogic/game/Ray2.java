package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class Ray2 implements Entities, Clickable{
    public boolean debug = false;

    public enum Direction  { // enum of ray's directions - "direction the ray is coming from" - a NE ray is travelling SW
        NE(new float[]{-5.2F, -9}), // NE -> SW
        E(new float[]{-10,0}), // E -> W
        SE(new float[]{-5.2F, 9}), // SE -> NW
        SW(new float[]{5.2F,9}), // SW -> NE
        W(new float[]{10,0}), // W -> E
        NW(new float[]{5.2F,-9}); // NW -> SE

        public final float[] direction;

        public float getXSpeed() {return direction[0];}
        public float getYSpeed() {return direction[1];}

        Direction(float[] direction) {
            this.direction = direction;
        }
    }

    Direction direction;

    float[] startPos; // coords of the ray's start

    float[] enterPos; // coords of start current line
    float[] headPos; // coords of the head of the current line

    boolean isInside; // true if ray is inside an atom
    boolean hitAtom;
    List<List<Float>> lines; // list of coordinates of each line making up the ray


    Hexagon currHex; // if ray is inside the grid, this is the hexagon it is currently inside

    public Ray2(float x1, float y1, Direction dir) {
        enterPos = new float[]{x1,y1};
        startPos = enterPos;
        headPos = new float[]{x1,y1};
        direction = dir;
        lines = new ArrayList<>();
    }
    /*
    Ray moving idea:
    <List<List>> where each row is one line making up the ray path, with 4 vals; start point, head point
    when !isInside, that line must be done. Add current line to list, set direction, move forward.

    If the ray is going to hit the atom (dictated by ray + neighbour direction), let it pass through
     */
    public void setDirection(Hexagon hex)
    {// called when ray hits atom aura - changes ray direction and continues moving
        int numLines = lines.size();
        lines.add(new ArrayList<>());

        // add curr line to lines
        lines.get(numLines).add(getEnterPos()[0]);
        lines.get(numLines).add(getEnterPos()[1]);
        lines.get(numLines).add(getHeadPos()[0]);
        lines.get(numLines).add(getHeadPos()[1]);


        // ---- initialise new line ----
        // new line now starts at head, curr head is also there
        setEnterPos(headPos);

        if(currHex.neighbCount == 2)
        {
            multAtomDeflect(); // deflect accordingly
        }
        else if (currHex.neighbCount > 2)
        {
            reflect();
        }
        else
        {
            // set direction of new line
            switch(hex.neighbDir)
            {

                case NorE: // top right hexagon
                    if(direction == Direction.E) {direction = Direction.SE;}
                    else if(direction == Direction.SE) {direction = Direction.SW;}
                    else if(direction == Direction.SW) {direction = Direction.NE;}
                    else if(direction == Direction.W) {direction = Direction.SW;}
                    else if(direction == Direction.NW) {direction = Direction.W;}
                    break;


                case East: // right hexagon
                    if(direction == Direction.NE) {direction = Direction.NW;}
                    else if(direction == Direction.SE) {direction = Direction.SW;}
                    else if(direction == Direction.SW) {direction = Direction.W;}
                    else if(direction == Direction.W) {direction = Direction.E;}
                    else if(direction == Direction.NW) {direction = Direction.W;}
                    break;

                case SouE: // bot right hexagon
                    if(direction == Direction.NE) {direction = Direction.NW;}
                    else if(direction == Direction.E) {direction = Direction.NE;}
                    else if(direction == Direction.SW) {direction = Direction.W;}
                    else if(direction == Direction.W) {direction = Direction.NW;}
                    else if(direction == Direction.NW) {direction = Direction.SE;}
                    break;

                case SouW: // TOP LEFT hexagon
                    if(direction == Direction.NE) {direction = Direction.E;}
                    else if(direction == Direction.E) {direction = Direction.SE;}
                    else if(direction == Direction.SE) {direction = Direction.NW;}
                    else if(direction == Direction.SW) {direction = Direction.SE;}
                    else if(direction == Direction.W) {direction = Direction.SW;}
                    break;

                case West: // left hexagon
                    if(direction == Direction.NE) {direction = Direction.E;}
                    else if(direction == Direction.E) {direction = Direction.W;}
                    else if(direction == Direction.SE) {direction = Direction.E;}
                    else if(direction == Direction.SW) {direction = Direction.SE;}
                    else if(direction == Direction.NW) {direction = Direction.NE;}
                    break;

                case NorW: // BOT LEFT hexagon

                    if(direction == Direction.NE) {direction = Direction.SW;}
                    else if(direction == Direction.E) {direction = Direction.NE;}
                    else if(direction == Direction.SE) {direction = Direction.E;}
                    else if(direction == Direction.W) {direction = Direction.NW;}
                    else if(direction == Direction.NW) {direction = Direction.NE;}
                    break;

            }
        }

    }

    public void multAtomDeflect()
    {
        switch(getSurroundingHexagons(currHex))
        {
            case 0:
                if(direction == Direction.W) {direction = Direction.NE;}
                else if(direction == Direction.SE) {direction = Direction.E;}
                break;
            case 1:
                if(direction == Direction.W) { direction = Direction.SE;}
                else if (direction == Direction.NW) { direction = Direction.E;}
                break;
            case 2:
                if(direction == Direction.NW) {direction = Direction.SW;}
                else if(direction == Direction.E) {direction = Direction.SE;}
                break;
            case 3:
                if(direction == Direction.E) {direction = Direction.SW;}
                else if(direction == Direction.NE) {direction = Direction.W;}
                break;
            case 4:
                if(direction == Direction.E) {direction = Direction.NW;}
                else if(direction == Direction.SE) {direction = Direction.W;}
                break;
            case 5:
                if(direction == Direction.SE) {direction = Direction.NE;}
                else if(direction == Direction.SW) {direction = Direction.NW;}
                break;
            default:
                reflect(); // reflect if any other
                break;

        }
    }

    public void reflect()
    {
        switch(direction)
        {
            case NE:
                direction = Direction.SW;
                break;
            case E:
                direction = Direction.W;
                break;
            case SE:
                direction = Direction.NW;
                break;
            case SW:
                direction = Direction.NE;
                break;
            case W:
                direction = Direction.E;
                break;
            case NW:
                direction = Direction.SE;
                break;

        }
    }

    public int getSurroundingHexagons(Hexagon hex)
    {
        int x = hex.grid.findHex(hex)[0]; // x index of hexagon
        int y = hex.grid.findHex(hex)[1]; // y index

        List<List<Hexagon>> board = hex.grid.hexBoard;

        Hexagon[] neighbs = new Hexagon[] {null, null, null, null, null, null}; // list of neighbours
        int[] xIndices = new int[] {x+1, x, x-1, x-1, x, x+1}; // what has to be added to x to get a given neighbour
        int[] yIndices = new int[] {y, y+1, y, y-1, y-1, y-1};

        int tempX, tempY;



        for(int i=0;i<6;i++)
        {
            tempX = xIndices[i];
            tempY = yIndices[i];

//            if(tempX < 4 && i == 0)
//            {
//                tempY +=1;
//            }



            if(tempX >= 0 && tempX < 9) // validation check
            {
                if(tempY >= 0 && tempY < board.get(tempX).size()) // validation check
                {

                    if(x < 4){
                        yIndices[0] += 1;
                        if(i == 0)
                        {
                            tempY = yIndices[0];
                        }

                        yIndices[5] = y;
                    }

                    if(x > 4)
                    {
                        yIndices[2] = y+1;
                        yIndices[3] = y;
                    }

                    if(x == 4)
                    {
                        yIndices[0] = y+1;
                        yIndices[5] = y;
                        if(i == 5)
                        {
                            tempY = yIndices[5] -1;
                        }

                    }

                    neighbs[i] = board.get(tempX).get(tempY);
                    neighbs[i].color = Color.VIOLET;


                }
            }

        }

        // check for pair

        for(int i=0;i<5;i++)
        {
            if(neighbs[i] != null & neighbs[i+1] != null)
            {
                if(neighbs[i].atom != null && neighbs[i+1].atom != null)
                {
                    return i;
                }
            }

        }

        return 10;



    }

    @Override
    public void Draw(ShapeRenderer shape)
    {
        shape.begin(ShapeRenderer.ShapeType.Line);
        if(debug) {
            shape.setColor(Color.GREEN);



            if(!lines.isEmpty()) // if ray has reflected
            {
                for(int i=0;i< lines.size();i++) // draw all lines
                {
                    shape.line(lines.get(i).get(0), lines.get(i).get(1), lines.get(i).get(2), lines.get(i).get(3));
                }
            }
            // draw current line

            shape.line(enterPos[0], enterPos[1], headPos[0], headPos[1]);


        }
        shape.end();
    }

    @Override
    public void update() {

            if(isInside && !hitAtom)
            {
                headPos[0] += direction.getXSpeed();
                headPos[1] += direction.getYSpeed();
            }
            if(currHex.isNeighbour) // move back, set direction, set isinside back
            {
                headPos[0] = currHex.getCenterX(); // move line to centre of current hexagon for consistency
                headPos[1] = currHex.getCenterY();
                setDirection(currHex);
                headPos[0] += direction.getXSpeed()*5;
                headPos[1] += direction.getYSpeed()*5;


            }


    }

    // accessor methods
    @Override
    public float[] getCoordinates() {
        return headPos;
    }
    public float[] getEnterPos(){ return enterPos;}
    public float[] getStartPos() { return startPos;}
    public float[] getHeadPos() { return headPos;}

    public void setEnterPos(float[] arr) {
        enterPos[0] = arr[0];
        enterPos[1] = arr[1];
    }
    public void setHeadPos(float[] arr) {
        headPos[0] = arr[0];
        headPos[1] = arr[1];
    }

    @Override
    public void onClick() {

    }

    @Override
    public boolean isClicked() {
        return false;
    }

    @Override
    public boolean isHoveredOver() {
        return false;
    }



    @Override
    public float[] getCentre() {
        return new float[0];
    }

    @Override
    public void getCollision() {

    }
}
