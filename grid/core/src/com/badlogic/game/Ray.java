package com.badlogic.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class Ray implements Entities, Clickable{
    public boolean visible = false;

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

        public float getAngleOfDirection() {
            double xSpeed = getXSpeed();
            double ySpeed = getYSpeed();

            double angle = Math.atan2(ySpeed, xSpeed); // Calculate the angle in radians

            return (float) Math.toDegrees(angle);
        }
    }

    Direction currDirection;
    Direction startDirection;

    float[] startPos; // coords of the ray's start

    float[] enterPos; // coords of start current line
    float[] headPos; // coords of the head of the current line

    float markerRadius;

    boolean isInside; // true if ray is inside an atom
    boolean hitAtom;
    List<List<Float>> lines; // list of coordinates of each line making up the ray
    HexagonGrid grid;


    Hexagon currHex; // if ray is inside the grid, this is the hexagon it is currently inside

    RayMarker[] rayMarkers;

    public Ray(float x1, float y1, Direction dir, HexagonGrid gr) {
        enterPos = new float[]{x1,y1};
        startPos = enterPos;
        headPos = new float[]{x1,y1};
        currDirection = dir;
        startDirection = dir;
        lines = new ArrayList<>();
        markerRadius = 10;

        grid = gr;

    }
    /*
    Ray moving idea:
    <List<List>> where each row is one line making up the ray path, with 4 vals; start point, head point
    when !isInside, that line must be done. Add current line to list, set direction, move forward.

    If the ray is going to hit the atom (dictated by ray + neighbour direction), let it pass through
     */
    public void setCurrDirection(Hexagon hex)
    {// called when ray hits atom aura - changes ray direction and continues moving
        /* rays are made up of lines. the current line (i.e. the very front line) isn't added to the list of lines
        *  until it has finished (hit an atom).
       */
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
        else // one atom deflect - accounting for all possible directions
        {
            // set direction of new line
            switch(hex.neighbDir)
            {

                case NorE: // top right hexagon
                    if(currDirection == Direction.E) {
                        currDirection = Direction.SE;}
                    else if(currDirection == Direction.SE) {
                        currDirection = Direction.SW;}
                    else if(currDirection == Direction.SW) {
                        currDirection = Direction.NE;}
                    else if(currDirection == Direction.W) {
                        currDirection = Direction.SW;}
                    else if(currDirection == Direction.NW) {
                        currDirection = Direction.W;}
                    break;


                case East: // right hexagon
                    if(currDirection == Direction.NE) {
                        currDirection = Direction.NW;}
                    else if(currDirection == Direction.SE) {
                        currDirection = Direction.SW;}
                    else if(currDirection == Direction.SW) {
                        currDirection = Direction.W;}
                    else if(currDirection == Direction.W) {
                        currDirection = Direction.E;}
                    else if(currDirection == Direction.NW) {
                        currDirection = Direction.W;}
                    break;

                case SouE: // bot right hexagon
                    if(currDirection == Direction.NE) {
                        currDirection = Direction.NW;}
                    else if(currDirection == Direction.E) {
                        currDirection = Direction.NE;}
                    else if(currDirection == Direction.SW) {
                        currDirection = Direction.W;}
                    else if(currDirection == Direction.W) {
                        currDirection = Direction.NW;}
                    else if(currDirection == Direction.NW) {
                        currDirection = Direction.SE;}
                    break;

                case SouW: // TOP LEFT hexagon
                    if(currDirection == Direction.NE) {
                        currDirection = Direction.E;}
                    else if(currDirection == Direction.E) {
                        currDirection = Direction.SE;}
                    else if(currDirection == Direction.SE) {
                        currDirection = Direction.NW;}
                    else if(currDirection == Direction.SW) {
                        currDirection = Direction.SE;}
                    else if(currDirection == Direction.W) {
                        currDirection = Direction.SW;}
                    break;

                case West: // left hexagon
                    if(currDirection == Direction.NE) {
                        currDirection = Direction.E;}
                    else if(currDirection == Direction.E) {
                        currDirection = Direction.W;}
                    else if(currDirection == Direction.SE) {
                        currDirection = Direction.E;}
                    else if(currDirection == Direction.SW) {
                        currDirection = Direction.SE;}
                    else if(currDirection == Direction.NW) {
                        currDirection = Direction.NE;}
                    break;

                case NorW: // BOT LEFT hexagon

                    if(currDirection == Direction.NE) {
                        currDirection = Direction.SW;}
                    else if(currDirection == Direction.E) {
                        currDirection = Direction.NE;}
                    else if(currDirection == Direction.SE) {
                        currDirection = Direction.E;}
                    else if(currDirection == Direction.W) {
                        currDirection = Direction.NW;}
                    else if(currDirection == Direction.NW) {
                        currDirection = Direction.NE;}
                    break;

            }
        }

    }

    public void multAtomDeflect()
    {
        switch(getSurroundingHexagons(currHex))
        {
            case 0:
                if(currDirection == Direction.W) {
                    currDirection = Direction.NE;}
                else if(currDirection == Direction.SE) {
                    currDirection = Direction.E;}
                break;
            case 1:
                if(currDirection == Direction.W) {
                    currDirection = Direction.SE;}
                else if (currDirection == Direction.NW) {
                    currDirection = Direction.E;}
                break;
            case 2:
                if(currDirection == Direction.NW) {
                    currDirection = Direction.SW;}
                else if(currDirection == Direction.NE) {
                    currDirection = Direction.SE;}
                break;
            case 3:
                if(currDirection == Direction.E) {
                    currDirection = Direction.SW;}
                else if(currDirection == Direction.NE) {
                    currDirection = Direction.W;}
                break;
            case 4:
                if(currDirection == Direction.E) {
                    currDirection = Direction.NW;}
                else if(currDirection == Direction.SE) {
                    currDirection = Direction.W;}
                break;
            case 5:
                if(currDirection == Direction.SE) {
                    currDirection = Direction.NE;}
                else if(currDirection == Direction.SW) {
                    currDirection = Direction.NW;}
                break;
            default:
                reflect(); // reflect if any other
                break;

        }
    }

    public void reflect()
    {
        switch(currDirection)
        {
            case NE:
                currDirection = Direction.SW;
                break;
            case E:
                currDirection = Direction.W;
                break;
            case SE:
                currDirection = Direction.NW;
                break;
            case SW:
                currDirection = Direction.NE;
                break;
            case W:
                currDirection = Direction.E;
                break;
            case NW:
                currDirection = Direction.SE;
                break;

        }
    }

    private boolean isMoving() {
        return !(hitAtom || !isInside);
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



                }
            }

        }

        // check for pair - if two atoms directly neighbouring each other (a side is touching)
        // if yes, return the index of the first one, giving indices 0-5 to be dealt with in mulAtomDeflect()

        for(int i=0;i<6;i++)
        {
            if(i == 5)
            {
                if(neighbs[i] != null && neighbs[0] != null)
                {
                    if(neighbs[i].atom !=null && neighbs[0].atom != null)
                    {
                        return i;
                    }
                }

            }
            else if(neighbs[i] != null & neighbs[i+1] != null)
            {
                if(neighbs[i].atom != null && neighbs[i+1].atom != null)
                {
                    return i;
                }
            }



        }


        return 10;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void Draw(ShapeRenderer shape)
    {
        shape.begin(ShapeRenderer.ShapeType.Line);

        if(visible) {
            shape.setColor(Color.GREEN);


            if(!lines.isEmpty()) // if ray has reflected
            {
                // draw all lines
                for (List<Float> line : lines) {
                    shape.line(line.get(0), line.get(1), line.get(2), line.get(3));
                }
            }
            // draw current line

            shape.line(enterPos[0], enterPos[1], headPos[0], headPos[1]);


        }

        shape.end();

        if (rayMarkers != null) {
            for (RayMarker rayMarker : rayMarkers) {
                rayMarker.Draw(shape);
            }
        }
    }

    private void spawnRayMarker() {
        RayMarker startMarker;
        RayMarker endMarker;
        RayMarker.Result result;

        int productOffset = 7;

        float[] startMarkerPos;
        float[] endMarkerPos;

        if(currHex.isBorder && hitAtom) // if hit border atom
        {
            startMarkerPos = new float[] {
                    this.startPos[0] - startDirection.getXSpeed() * productOffset,
                    this.startPos[1] - startDirection.getYSpeed() * productOffset
            };
            endMarkerPos = null;


            result = RayMarker.Result.HIT;
        }
        // If the ray is only 1 line = MISS
        else if (lines.isEmpty()) {
            startMarkerPos = new float[] {
                    this.startPos[0] - startDirection.getXSpeed() * productOffset,
                    this.startPos[1] - startDirection.getYSpeed() * productOffset
            };
            endMarkerPos = new float[] {
                    this.headPos[0] + currDirection.getXSpeed() * productOffset,
                    this.headPos[1] + currDirection.getYSpeed() * productOffset
            };

            result = RayMarker.Result.MISS;
        // If the ray is still inside the hexagon, maybe has reflections but got swallowed = HIT
        } else if (isInside) {
            startMarkerPos = new float[] {
                    lines.get(0).get(0) - startDirection.getXSpeed() * productOffset,
                    lines.get(0).get(1) - startDirection.getYSpeed() * productOffset
            };

            endMarkerPos = startMarkerPos;

            result = RayMarker.Result.HIT;
        }
        else { // Deflected rays, no swallowed = REFLECTION
            startMarkerPos = new float[]{
                    lines.get(0).get(0) - startDirection.getXSpeed() * productOffset,
                    lines.get(0).get(1) - startDirection.getYSpeed() * productOffset
            };
            endMarkerPos = new float[]{
                    headPos[0] + currDirection.getXSpeed() * productOffset,
                    headPos[1] + currDirection.getYSpeed() * productOffset
            };

            result = RayMarker.Result.REFLECTION;
        }

        startMarker = new RayMarker(startMarkerPos, markerRadius, result);
        if(endMarkerPos == null)
        {
            endMarker = startMarker;
        }
        else
        {
            endMarker = new RayMarker(endMarkerPos, markerRadius, result);
        }


        rayMarkers = new RayMarker[] {startMarker, endMarker};
    }

    @Override
    public void update() {
            grid.rayCheck(this);

            if(currHex.atom != null)
            {
                hitAtom = true;
            }

            if(!isInside)
            {

            }
            else if(isInside && !hitAtom && !currHex.isNeighbour) // if in the grid and hasn't hit atom update
            {
                headPos[0] += currDirection.getXSpeed();
                headPos[1] += currDirection.getYSpeed();
            }
            else if(currHex.isNeighbour && currHex.atom == null) // if currHex is a neighbour and DOESN'T have an atom in it
            {// move back, set direction, set isinside back
                headPos[0] = currHex.getCenterX(); // move line to centre of current hexagon for consistency
                headPos[1] = currHex.getCenterY();
                setCurrDirection(currHex);
                headPos[0] += currDirection.getXSpeed()*5;
                headPos[1] += currDirection.getYSpeed()*5;
                grid.rayCheck(this);

            }



            if (!isMoving()) {
                spawnRayMarker();
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
