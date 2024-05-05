package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * A grid made of {@code Hexagons}. The game board.
 */
public class HexagonGrid extends GridUtil{
    List<List<Hexagon>> hexBoard; // List representation of the Hexagon Grid.
    int minNoHex; // The minimum number of Hexagons in a row. Also, the side length.
    int maxNoHex;
    float hexRadius;

    Atom[] atoms;
    List<Ray> rays;

    public HexagonGrid(float hexRadius)
    {
        hexBoard = new ArrayList<>();
        minNoHex = 5;
        maxNoHex = 9;
        this.hexRadius = hexRadius;

        atoms = new Atom[5];
        rays = new ArrayList<>();
    }


    public void Draw(ShapeRenderer shape) {
        for (Hexagon hexagon : getHexBoard()) {
            hexagon.Draw(shape);
        }
        for(Atom at: atoms) {
            at.Draw(shape);
        }
        for(Ray ray: rays) {
            ray.Draw(shape);
        }
    }

    public void update() {
        for (List<Hexagon> hexRow : hexBoard) {
            for (Hexagon hexagon : hexRow) {
                hexagon.update();
            }
        }
        for(Atom at: atoms) {
            at.update();
        }
        for(Ray ray: rays) {
            rayCheck(ray);
            rayAtomCheck(ray);
            ray.update();
        }
    }

    /**
     * Factory method to create a new Ray object.
     * @param x Horizontal coordinate.
     * @param y Vertical coordinate.
     * @param direction Ray direction.
     * @param hex The Hexagon being shot from.
     */
    public void addRay(float x, float y, Ray.Direction direction, Hexagon hex) {
            Ray newRay = new Ray(x,y,direction, this);
            newRay.startHex = hex;
            newRay.currHex = hex;

            rays.add(newRay);
    }

    /**
     * Method to reset all Atom objects to their default position.
     */
    public void resetAllAtoms() {
        for (Hexagon hex : getHexBoard()) {
            resetAtom(hex);
        }
    }

    /**
     * Method to delete all Ray objects from the Grid.
     */
    public void resetAllRays() {
        rays.clear();

        for (Hexagon hex : getHexBoard()) {
            for (Border border : hex.borders) {
                border.hasRay = false;
            }
        }
    }

    /**
     * Checks if all atoms have been placed.
     * @return {@code true} if all atoms have been placed, {@code false} otherwise.
     */
    public boolean allAtomsPlaced() {
        for (Atom atom : atoms) {
            if (!atom.isPlaced) { return false; }
        }
        return true;
    }

    //  ----    Setter / Getter methods     ----
    /**
     * Sets the status of the {@code visible} boolean in each Atom of the Grid.
     * @param visible The value to be set.
     */
    public void setAtomsVisible(boolean visible) {
        for (Atom at : atoms) {
            at.setVisible(visible);
        }
    }

    /**
     * Set the status of the {@code visible} boolean in each Ray of the Grid.
     * @param visible The value to be set.
     */
    public void setRayVisible(boolean visible) {
        for (Ray ray : rays) {
            ray.setVisible(visible);
        }
    }

    /**
     * Set the status of the {@code clickable} boolean in each Hexagon of the Grid.
     * @param clickable
     */
    public void setBorderClickable(boolean clickable) {
        for (Hexagon hexagon : getHexBoard()) {
            for (Border border : hexagon.borders) {
                border.setClickable(clickable);
            }
        }
    }

    /**
     * Set the visibility of each Border's bounding box.
     * @param visible The value to be set.
     */
    public void setBorderBoundingBoxVisible(boolean visible) {
        for (Hexagon hexagon : getHexBoard()) {
            for (Border border : hexagon.borders) {
                border.setBoundingBoxVisible(visible);
            }
        }
    }

    /**
     * Set the state of each Hexagon in the Grid.
     * @param state The state to be set to.
     */
    public void setHexState(Hexagon.State state) {
        for (Hexagon hex : getHexBoard()) {
            hex.setState(state);
        }
    }

    /**
     * Method to get the Hexagon Grid as a List.
     * @return A List of Hexagons, making up the Grid.
     */
    public List<Hexagon> getHexBoard() {
        List<Hexagon> flattenedHexList = new ArrayList<>();
        hexBoard.forEach(flattenedHexList::addAll);
        return flattenedHexList;
    }

    /**
     * Gets the row of the Hexagon Grid at index i.
     * @param i Index of the row.
     * @return The row of Hexagons at index i as a List.
     */
    public List<Hexagon> getRow(int i) {
        return hexBoard.get(i);
    }
}
