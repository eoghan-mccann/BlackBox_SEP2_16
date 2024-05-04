package com.badlogic.game;

import java.util.ArrayList;
import java.util.List;

/*
    Utilities class for Ray. Holds more complex Ray methods.
 */
public class RayUtil {


    // Changes ray's direction accordingly when it hits an atom aura
    public void setCurrDirection(Hexagon hex, Ray ray)
    {
        // The current head line of the ray is added to lines once it is finished (i.e. when the ray enters atom aura)

        // ---- store current line ----
        int numLines = ray.lines.size();
        ray.lines.add(new ArrayList<>());

        ray.lines.get(numLines).add(ray.getEnterPos()[0]);
        ray.lines.get(numLines).add(ray.getEnterPos()[1]);
        ray.lines.get(numLines).add(ray.getHeadPos()[0]);
        ray.lines.get(numLines).add(ray.getHeadPos()[1]);


        // ---- initialise new line ----
        // new line now starts at head, curr head is also there
        ray.setEnterPos(ray.headPos);


        // Depending on neighbour count, change ray direction accordingly
        if(ray.currHex.neighbCount == 2)
        {
            multAtomDeflect(ray);
        }
        else if (ray.currHex.neighbCount > 2)
        {
            reflect(ray);
        }
        else // one atom deflect - accounting for all possible directions
        {
            // set direction of new line
            singleAtomDeflect(ray, hex);

        }

    }

    // Finds neighbouring pairs of neighbouring hexagons of a given hexagon. Returns an int depending on pattern
    public int findHexPair(Hexagon hex)
    {
        int x = hex.grid.findHex(hex)[0]; // x index of hexagon
        int y = hex.grid.findHex(hex)[1]; // y index

        List<List<Hexagon>> board = hex.grid.hexBoard;

        Hexagon[] neighbs = new Hexagon[] {null, null, null, null, null, null}; // list of neighbours
        int[] xIndices = new int[] {x+1, x, x-1, x-1, x, x+1}; // what has to be added to x to get a given neighbour
        int[] yIndices = new int[] {y, y+1, y, y-1, y-1, y-1};

        int tempX, tempY;


        for(int i=0;i<6;i++) {

            tempX = xIndices[i];
            tempY = yIndices[i];

            if(tempX >= 0 && tempX < 9) { // If index is inside grid
                if(tempY >= 0 && tempY < board.get(tempX).size()) { // Index in grid

                    // Due to storing of hexagons, we must slightly alter certain hexagon indices
                    if(x < 4){
                        yIndices[0] += 1;
                        yIndices[5] = y;
                        if(i == 0) {
                            tempY = yIndices[0];
                        }
                    }

                    if(x > 4) {
                        yIndices[2] = y+1;
                        yIndices[3] = y;
                    }

                    if(x == 4) {
                        yIndices[0] = y+1;
                        yIndices[5] = y;
                        if(i == 5) {
                            tempY = yIndices[5] -1;
                        }
                    }

                    neighbs[i] = board.get(tempX).get(tempY);
                }
            }

        }

        // check for pair - if two atoms directly neighbouring each other (a side is touching)
        // if yes, return the index of the first one, giving indices 0-5 to be dealt with in mulAtomDeflect()

        for(int i=0;i<6;i++) {
            if(i == 5) {
                if(neighbs[i] != null && neighbs[0] != null) {
                    if(neighbs[i].atom !=null && neighbs[0].atom != null) {
                        return i;
                    }
                }
            }
            else if(neighbs[i] != null & neighbs[i+1] != null) {
                if(neighbs[i].atom != null && neighbs[i+1].atom != null) {
                    return i;
                }
            }
        }

        return 10;
    }

    // Deflects a ray which is inside >1 atom auras
    public void multAtomDeflect(Ray ray)
    {
        switch(findHexPair(ray.currHex))
        {
            case 0:
                if(ray.currDirection == Ray.Direction.W) {
                    ray.currDirection = Ray.Direction.NE;}
                else if(ray.currDirection == Ray.Direction.SE) {
                    ray.currDirection = Ray.Direction.E;}
                break;
            case 1:
                if(ray.currDirection == Ray.Direction.W) {
                    ray.currDirection = Ray.Direction.SE;}
                else if (ray.currDirection == Ray.Direction.NW) {
                    ray.currDirection = Ray.Direction.E;}
                break;
            case 2:
                if(ray.currDirection == Ray.Direction.NW) {
                    ray.currDirection = Ray.Direction.SW;}
                else if(ray.currDirection == Ray.Direction.NE) {
                    ray.currDirection = Ray.Direction.SE;}
                break;
            case 3:
                if(ray.currDirection == Ray.Direction.E) {
                    ray.currDirection = Ray.Direction.SW;}
                else if(ray.currDirection == Ray.Direction.NE) {
                    ray.currDirection = Ray.Direction.W;}
                break;
            case 4:
                if(ray.currDirection == Ray.Direction.E) {
                    ray.currDirection = Ray.Direction.NW;}
                else if(ray.currDirection == Ray.Direction.SE) {
                    ray.currDirection = Ray.Direction.W;}
                break;
            case 5:
                if(ray.currDirection == Ray.Direction.SE) {
                    ray.currDirection = Ray.Direction.NE;}
                else if(ray.currDirection == Ray.Direction.SW) {
                    ray.currDirection = Ray.Direction.NW;}
                break;
            default:
                reflect(ray); // reflect if any other
                break;

        }
    }

    // Deflects a ray inside 1 atom aura
    public void singleAtomDeflect(Ray ray, Hexagon hex)
    {
        switch(hex.neighbDir)
        {

            case NorE: // top right hexagon
                if(ray.currDirection == Ray.Direction.E) {
                    ray.currDirection = Ray.Direction.SE;}
                else if(ray.currDirection == Ray.Direction.SE) {
                    ray.currDirection = Ray.Direction.SW;}
                else if(ray.currDirection == Ray.Direction.SW) {
                    ray.currDirection = Ray.Direction.NE;}
                else if(ray.currDirection == Ray.Direction.W) {
                    ray.currDirection = Ray.Direction.SW;}
                else if(ray.currDirection == Ray.Direction.NW) {
                    ray.currDirection = Ray.Direction.W;}
                break;


            case East: // right hexagon
                if(ray.currDirection == Ray.Direction.NE) {
                    ray.currDirection = Ray.Direction.NW;}
                else if(ray.currDirection == Ray.Direction.SE) {
                    ray.currDirection = Ray.Direction.SW;}
                else if(ray.currDirection == Ray.Direction.SW) {
                    ray.currDirection = Ray.Direction.W;}
                else if(ray.currDirection == Ray.Direction.W) {
                    ray.currDirection = Ray.Direction.E;}
                else if(ray.currDirection == Ray.Direction.NW) {
                    ray.currDirection = Ray.Direction.W;}
                break;

            case SouE: // bot right hexagon
                if(ray.currDirection == Ray.Direction.NE) {
                    ray.currDirection = Ray.Direction.NW;}
                else if(ray.currDirection == Ray.Direction.E) {
                    ray.currDirection = Ray.Direction.NE;}
                else if(ray.currDirection == Ray.Direction.SW) {
                    ray.currDirection = Ray.Direction.W;}
                else if(ray.currDirection == Ray.Direction.W) {
                    ray.currDirection = Ray.Direction.NW;}
                else if(ray.currDirection == Ray.Direction.NW) {
                    ray.currDirection = Ray.Direction.SE;}
                break;

            case SouW: // TOP LEFT hexagon
                if(ray.currDirection == Ray.Direction.NE) {
                    ray.currDirection = Ray.Direction.E;}
                else if(ray.currDirection == Ray.Direction.E) {
                    ray.currDirection = Ray.Direction.SE;}
                else if(ray.currDirection == Ray.Direction.SE) {
                    ray.currDirection = Ray.Direction.NW;}
                else if(ray.currDirection == Ray.Direction.SW) {
                    ray.currDirection = Ray.Direction.SE;}
                else if(ray.currDirection == Ray.Direction.W) {
                    ray.currDirection = Ray.Direction.SW;}
                break;

            case West: // left hexagon
                if(ray.currDirection == Ray.Direction.NE) {
                    ray.currDirection = Ray.Direction.E;}
                else if(ray.currDirection == Ray.Direction.E) {
                    ray.currDirection = Ray.Direction.W;}
                else if(ray.currDirection == Ray.Direction.SE) {
                    ray.currDirection = Ray.Direction.E;}
                else if(ray.currDirection == Ray.Direction.SW) {
                    ray.currDirection = Ray.Direction.SE;}
                else if(ray.currDirection == Ray.Direction.NW) {
                    ray.currDirection = Ray.Direction.NE;}
                break;

            case NorW: // BOT LEFT hexagon

                if(ray.currDirection == Ray.Direction.NE) {
                    ray.currDirection = Ray.Direction.SW;}
                else if(ray.currDirection == Ray.Direction.E) {
                    ray.currDirection = Ray.Direction.NE;}
                else if(ray.currDirection == Ray.Direction.SE) {
                    ray.currDirection = Ray.Direction.E;}
                else if(ray.currDirection == Ray.Direction.W) {
                    ray.currDirection = Ray.Direction.NW;}
                else if(ray.currDirection == Ray.Direction.NW) {
                    ray.currDirection = Ray.Direction.NE;}
                break;

        }
    }

    // Reflect ray (invert its direction)
    public static void reflect(Ray ray)
    {
        switch(ray.currDirection)
        {
            case NE:
                ray.currDirection = Ray.Direction.SW;
                break;
            case E:
                ray.currDirection = Ray.Direction.W;
                break;
            case SE:
                ray.currDirection = Ray.Direction.NW;
                break;
            case SW:
                ray.currDirection = Ray.Direction.NE;
                break;
            case W:
                ray.currDirection = Ray.Direction.E;
                break;
            case NW:
                ray.currDirection = Ray.Direction.SE;
                break;

        }
    }




}
