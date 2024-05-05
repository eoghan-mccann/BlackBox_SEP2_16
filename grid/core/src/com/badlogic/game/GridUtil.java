package com.badlogic.game;

import com.badlogic.gdx.Gdx;

import java.util.List;

/**
 * Utility class for Grid. Holds methods for grid interaction with user, including management of {@code Atoms}, {@code Rays}.
 */
public class GridUtil extends GridInit {

    /**
     * Changes the status of all neighbouring Hexagons of the given hexagon to having a neighbour Hexagon with an Atom if one has been placed, or to have one less neighbouring Atom Hexagon if one has been removed.
     * @param hex The hexagon with the atom.
     * @param grid The grid the hexagon is in.
     */
    public void toggleNeighbours(Hexagon hex, HexagonGrid grid) {
        int x = findHex(hex, grid)[1]; // gets column index of hexagon with atom in it
        int y = findHex(hex, grid)[0]; // get the row
        int[] xNeighbs = new int[]{x, x+1, x, x-1, x-1, x-1}; // array of all x indexes of neighbours (column#)
        int[] yNeighbs = new int[]{y+1, y, y-1, y-1, y, y+1}; // array of all y indexes of neighbours (row#)

        Hexagon neighbour; // current neighbour hexagon

        // toggle all neighbours
        for(int i=0;i<6;i++) // for all hexagon neighbours
        {
            if(!(yNeighbs[i] > 8 || yNeighbs[i] < 0) // Check that index is inside grid
                    && !(0 > xNeighbs[i] || xNeighbs[i] > grid.hexBoard.get(yNeighbs[i]).size()-1))
            {
                /*
                The upper neighbour x indices and lower neighbour x indices
                need to be shifted slightly due to indexing in the grid array.
                 */
                if(y < 4){
                    xNeighbs[0] += 1;
                    xNeighbs[5] = x;
                }
                if(y > 4)
                {
                    xNeighbs[2] = x+1;
                    xNeighbs[3] = x;
                }


                neighbour = grid.hexBoard.get(yNeighbs[i]).get(xNeighbs[i]); // set neighbour to correct hexagon
                if(hex.atom == null) // if atom just removed, RESET
                {
                    neighbour.neighbDir = null;
                    neighbour.isNeighbour = false;
                    neighbour.neighbCount--;
                }
                else
                {
                    neighbour.neighbDir = Hexagon.NeighbourPosition.values()[i]; // set neighbour direction
                    neighbour.isNeighbour = true;
                    neighbour.neighbCount++;
                }
            }
        }

    }


    /**
     * Finds the index of the given hexagon in the {@code HexagonGrid}.
     * @param hex The Hexagon whose index is to be returned.
     * @param grid The grid the Hexagon is present in.
     * @return Index as a float array, where index 0 is the row number, index 1 the column number.
     */
    public int[] findHex(Hexagon hex, HexagonGrid grid) {
        for(int i=0;i<9;i++)
        {
            for(int j=0;j<grid.hexBoard.get(i).size();j++)
            {
                if(grid.hexBoard.get(i).get(j) == hex)
                {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalArgumentException("Couldn't find hexagon in array");
    }

    /**
     * Method to check whether a ray is inside the HexagonGrid, and if so update the ray accordingly.
     * @param ray The ray being checked.
     */
    public void rayCheck(Ray ray) {
        for(List<Hexagon> hexList: ray.grid.hexBoard) {
            for(Hexagon hex: hexList) { // For each Hexagon in the board
                if(hex.isInside(ray.headPos[0], ray.headPos[1])) { // If ray is inside
                    ray.isInside = true;
                    ray.currHex = hex;
                    return;
                }

            }
        }
        ray.isInside = false; // Ray is otherwise outside the grid

    }

    /**
     * Method to check whether a given Ray object has come in contact with an atom (i.e. has been absorbed).
     * @param ray The Ray object to be checked.
     */
    public void rayAtomCheck(Ray ray) {
        for (Atom atom : ray.grid.atoms) {
            if (atom.isInside(ray.getCoordinates())) {
                ray.hitAtom = true;
                return;
            }
        }
        ray.hitAtom = false;
    }


    /**
     * Moves the next available free atom (if any) onto the HexagonGrid at the given Hexagon's position
     * @param hex The Hexagon of the HexagonGrid for the Atom to be placed into.
     */
    public void moveAtom(Hexagon hex) {
        for (int i = 0; i < 5; i++) { // For each atom in grid.atoms[]
            if (!hex.grid.atoms[i].isPlaced) { // Find first unplaced atom in array
                hex.grid.atoms[i].setAtomPoints(hex.getCenterX(), hex.getCenterY());
                hex.grid.atoms[i].isPlaced = true;
                hex.setAtom(hex.grid.atoms[i]);

                toggleNeighbours(hex, hex.grid); // Must also set neighbouring atoms accordingly
                break;
            }
        }

    }

    /**
     * Reset the Atom in Hexagon hex of the HexagonGrid, if it exists. Moves the Atom back to its default position.
     * @param hex The hexagon of the Grid containing the atom (if any).
     */
    public void resetAtom(Hexagon hex) {
        for(int i =0;i<5;i++) {
            if(hex.grid.atoms[i].equals(hex.getAtom())) {
                hex.grid.atoms[i].setAtomPoints((Gdx.graphics.getWidth() -100), (Gdx.graphics.getHeight() - 100 - (100*i)));
                hex.grid.atoms[i].isPlaced = false;
                hex.setAtom(null);

                toggleNeighbours(hex, hex.grid); // Must also reset neighbouring atoms accordingly

            }
        }
    }


}
