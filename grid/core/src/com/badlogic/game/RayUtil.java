package com.badlogic.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilities class for {@code Ray}. Contains complex methods for Ray manipulation.
 *
 */
public class RayUtil {
    // Define a map to store reflection mappings based on neighbor position and input direction
    private static final Map<Hexagon.NeighbourPosition, Map<Ray.Direction, Ray.Direction>> DIRECTION_MAP = new HashMap<>();

    /*
      Initializes the direction map for reflecting ray directions based on neighbor positions.
      Map is initialized with predefined reflection mappings for each neighbor position.
     */
    static { // static initializer for directionMaps
        // Northeast neighbour position
        Map<Ray.Direction, Ray.Direction> norEMap = new HashMap<>();
        norEMap.put(Ray.Direction.E, Ray.Direction.SE);
        norEMap.put(Ray.Direction.SE, Ray.Direction.SW);
        norEMap.put(Ray.Direction.SW, Ray.Direction.NE);
        norEMap.put(Ray.Direction.W, Ray.Direction.SW);
        norEMap.put(Ray.Direction.NW, Ray.Direction.W);

        // East neighbour position
        Map<Ray.Direction, Ray.Direction> eastMap = new HashMap<>();
        eastMap.put(Ray.Direction.NE, Ray.Direction.NW);
        eastMap.put(Ray.Direction.SE, Ray.Direction.SW);
        eastMap.put(Ray.Direction.SW, Ray.Direction.W);
        eastMap.put(Ray.Direction.W, Ray.Direction.E);
        eastMap.put(Ray.Direction.NW, Ray.Direction.W);

        // Southeast neighbour position
        Map<Ray.Direction, Ray.Direction> souEMap = new HashMap<>();
        souEMap.put(Ray.Direction.NE, Ray.Direction.NW);
        souEMap.put(Ray.Direction.E, Ray.Direction.NE);
        souEMap.put(Ray.Direction.SW, Ray.Direction.W);
        souEMap.put(Ray.Direction.W, Ray.Direction.NW);
        souEMap.put(Ray.Direction.NW, Ray.Direction.SE);

        // Southwest neighbour position
        Map<Ray.Direction, Ray.Direction> souWMap = new HashMap<>();
        souWMap.put(Ray.Direction.NE, Ray.Direction.E);
        souWMap.put(Ray.Direction.E, Ray.Direction.SE);
        souWMap.put(Ray.Direction.SE, Ray.Direction.NW);
        souWMap.put(Ray.Direction.SW, Ray.Direction.SE);
        souWMap.put(Ray.Direction.W, Ray.Direction.SW);

        // West neighbour position
        Map<Ray.Direction, Ray.Direction> westMap = new HashMap<>();
        westMap.put(Ray.Direction.NE, Ray.Direction.E);
        westMap.put(Ray.Direction.E, Ray.Direction.W);
        westMap.put(Ray.Direction.SE, Ray.Direction.E);
        westMap.put(Ray.Direction.SW, Ray.Direction.SE);
        westMap.put(Ray.Direction.NW, Ray.Direction.NE);

        // NorthWest neighbour position
        Map<Ray.Direction, Ray.Direction> norWMap = new HashMap<>();
        norWMap.put(Ray.Direction.NE, Ray.Direction.SW);
        norWMap.put(Ray.Direction.E, Ray.Direction.NE);
        norWMap.put(Ray.Direction.SE, Ray.Direction.E);
        norWMap.put(Ray.Direction.W, Ray.Direction.NW);
        norWMap.put(Ray.Direction.NW, Ray.Direction.NE);

        // Put each direction map into the main direction map
        DIRECTION_MAP.put(Hexagon.NeighbourPosition.NorE, norEMap);
        DIRECTION_MAP.put(Hexagon.NeighbourPosition.East, eastMap);
        DIRECTION_MAP.put(Hexagon.NeighbourPosition.SouE, souEMap);
        DIRECTION_MAP.put(Hexagon.NeighbourPosition.SouW, souWMap);
        DIRECTION_MAP.put(Hexagon.NeighbourPosition.West, westMap);
        DIRECTION_MAP.put(Hexagon.NeighbourPosition.NorW, norWMap);
    }

    /**
     * Changes a Ray's direction, based on the given Hexagon's position, and the Ray's direction .
     *
     * @param hex the Hexagon the Ray is currently travelling through.
     * @param ray the Ray whose direction is to be changed.
     */
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
            multiAtomDeflect(ray);
        }
        else if (ray.currHex.neighbCount > 2)
        {
            reflect(ray);
        }
        else // one atom deflect - accounting for all possible directions
        {
            singleAtomDeflect(ray, hex);
        }

    }

    /**
     * Searches for a pair of Hexagons surrounding the given hexagon directly neighbouring each other that both have atoms.
     *
     * @return the {@code index} of the first hexagon in the pair, where index 0 is the top right hexagon and 1 is the right (east) Hexagon. Returns {@code -1} if none found.
     */
    public int findHexPair(Hexagon hex)
    {
        int x = hex.grid.findHex(hex, hex.grid)[0]; // x index of hexagon
        int y = hex.grid.findHex(hex, hex.grid)[1]; // y index

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

        return -1;
    }

    /**
     * Deflects a ray which is inside of multiple atom auras at the same time.
     *
     * @param ray The ray to be deflected.
     */
    public void multiAtomDeflect(Ray ray) {
        int pairIndex = findHexPair(ray.currHex);
        if (pairIndex != -1) {
            Map<Ray.Direction, Ray.Direction> directionMap = DIRECTION_MAP.get(Hexagon.NeighbourPosition.values()[pairIndex]);
            if (directionMap != null) {
                Ray.Direction newDirection = directionMap.get(ray.currDirection);
                if (newDirection != null) {
                    ray.currDirection = newDirection;
                }
            }
        } else {
            reflect(ray); // Reflect if no suitable direction found
        }
    }

    /**
     * Deflects a ray which is inside 1 atom aura.
     *
     * @param ray The ray to be deflected.
     * @param hex The Hexagon that is deflecting the Ray
     */
    public void singleAtomDeflect(Ray ray, Hexagon hex) {
        Map<Ray.Direction, Ray.Direction> directionMap = DIRECTION_MAP.get(hex.neighbDir);
        if (directionMap != null) {
            Ray.Direction newDirection = directionMap.get(ray.currDirection);
            if (newDirection != null) {
                ray.currDirection = newDirection;
            }
        }
    }

    /**
     * Inverts the direction of a Ray, reflecting it back along its course.
     *
     * @param ray The ray to be reflected.
     */
    public static void reflect(Ray ray) {
        ray.currDirection = getReflectionDirection(ray.currDirection);
    }

    /**
     * Returns the reflection direction of the inputted direction.
     *
     * @param direction direction to be reflected
     * @return reflected {@code Ray.Direction} of inputted {@code direction}
     */

    public static Ray.Direction getReflectionDirection(Ray.Direction direction) {
        switch(direction) {
            case NE:
                return Ray.Direction.SW;
            case E:
                return Ray.Direction.W;
            case SE:
                return Ray.Direction.NW;
            case SW:
                return Ray.Direction.NE;
            case W:
                return Ray.Direction.E;
            case NW:
                return Ray.Direction.SE;
            default:
                return direction;
        }
    }
}
