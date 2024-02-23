package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.game.Game.*;

public class HexagonGrid {
    List<List<Hexagon>> hexBoard = new ArrayList<>(); // list of rows of hexagons where the rows are lists of hexagons
    int minNoHex = 5;
    int maxNoHex = 9;

    Atom[] atoms = new Atom[5];


      /*
    rayCheck method -> takes a ray, iterates through hexgrid
                    -> if ray head is somewhere inside the grid, update
                    -> otherwise, dont update

                    method in hexgrid -> called in game
     */
    public void rayCheck(Ray2 ray)
    {
        for(List<Hexagon> hexList: hexBoard)
        {
            for(Hexagon hex: hexList)
            {
                if(hex.contains(ray.headPos[0], ray.headPos[1]))
                {
                    ray.isInside = true;
                    return;
                }
            }
        }
        ray.isInside = false;
    }

    public void toggleAtom()
    {
        for(Atom at: atoms)
        {
            at.toggled = !at.toggled;
        }
    }

    public void initAtoms() // initial placement of 5 atoms on the right side
    {
        float startEx = Gdx.graphics.getWidth() - 100;
        float startWhy = Gdx.graphics.getHeight() - 100;
        for(int i=0;i<5;i++){
            atoms[i] = new Atom(startEx, startWhy, .75f*hexRadius, 1.5f*hexRadius);
            startWhy -= 100;
        }

    }

    public void moveAtom(Hexagon hex) // when a hexagon is clicked, move next free atom to hexagon
    {
        for (int i = 0; i < 5; i++) // loop through array of atoms
        {
            if (!atoms[i].isPlaced) // find first unplaced atom in array
            {
                atoms[i].setAtomPoints(hex.getCenterX(), hex.getCenterY());
                atoms[i].isPlaced = true;
                hex.setAtom(atoms[i]);
                break;
            }
            if (i == 4 && atoms[i].isPlaced) // if all placed
            {
                continue;
                // do nothinG? or move the first placed one
            }
        }

    }

    public void resetAtom(Hexagon hex)
    {
        for(int i =0;i<5;i++)
        {
            if(atoms[i].equals(hex.getAtom()))
            {
                atoms[i].setAtomPoints((Gdx.graphics.getWidth() -100), (Gdx.graphics.getHeight() - 100 - (100*i)));
                atoms[i].isPlaced = false;
                hex.setAtom(null);
            }
        }
    }

    private void buildHexRow(int n, float y, boolean isOffset) { // Builds row of Hexagons with n of hexagons & y position
        List<Hexagon> hexRow = new ArrayList<>();

        // places first hexagon (n - 1) / 2 hexagon lengths to the left of x center point to build across.
        float centreX = (float) windowWidth / 2;
        Hexagon centreHex = new Hexagon(centreX, y, hexRadius, this);
        float hexDist = centreHex.getWidth();
        float startingX = (float) (centreX - ((n / 2) * hexDist) - (hexDist * 0.5));

        float offset = isOffset ? hexDist / 2 : 0; // offsets row distance to fit seamlessly with row.

        // Builds row of hexagons, adding hexagon length each time.
        for (int i = 0; i < n; i++) {
            hexRow.add(new Hexagon(startingX + (i * hexDist) + offset, y, hexRadius, this));
        }

        hexBoard.add(hexRow); // adds row of hexagons to hexBoard list
    }

    public void buildHexBoard() { // Builds hexBoard from bottom row up
        Hexagon tempHex = new Hexagon(0,0, hexRadius, this);
        float heightDist = (float) (tempHex.getHeight() * 0.75); // gets height distance to increase y value

        float startY = (windowHeight) - (heightDist * (minNoHex * 2));

        // Initially hexagon n is increasing and it starts dead center on x axis.
        boolean descending = false;
        boolean offset = false;

        int currNoHex = minNoHex;

        for (int i = 0; i < maxNoHex; i++) {

            buildHexRow(currNoHex, startY + (i * heightDist), offset); // build row, increasing y level each time
            offset = !offset; // switch between offset & no offset so the rows fit together

            if (descending) {
                currNoHex--;
                continue; // once descending toggled, keep it descending until loop concludes.
            }
            else {
                currNoHex++;
            }

            descending = currNoHex >= maxNoHex;
        }
    }

    public List<Hexagon> getBorderHexagons() { // Returns a list of all the hexagons on the outside of the hexBoard
        List<Hexagon> returnList = new ArrayList<>();
        int hexBoardRows = hexBoard.size();

        for (int rowNo = 0; rowNo < hexBoardRows; rowNo++) {

            List<Hexagon> rowElements = getRow(rowNo);

            // If it's the last row or first row add all the elements to the border list
            if (rowNo == 0) { // remember, from bottom to top
                returnList.addAll(rowElements);

                // set borders
                for(Hexagon hex: rowElements)
                {
                    hex.isBorder = true;
                    hex.sideBorders[2] = 1;
                    hex.sideBorders[3] = 1;
                }
                rowElements.get(0).sideBorders[4] = 1;
                rowElements.get(rowElements.size()-1).sideBorders[1] = 1;

            }
            else if(rowNo == hexBoardRows - 1) // top row
            {
                for(Hexagon hex: rowElements)
                {
                    hex.isBorder = true;
                    hex.sideBorders[0] = 1;
                    hex.sideBorders[5] = 1;
                }
                rowElements.get(0).sideBorders[4] = 1;
                rowElements.get(rowElements.size()-1).sideBorders[1] = 1;
            }
            else
            {
                if(rowNo < 5) // if ascending in count
                {
                    rowElements.get(0).sideBorders[3] = 1;
                    rowElements.get(0).sideBorders[4] = 1;
                    rowElements.get(0).isBorder = true;


                    rowElements.get(rowElements.size()-1).sideBorders[1] = 1;
                    rowElements.get(rowElements.size()-1).sideBorders[2] = 1;
                    rowElements.get(rowElements.size()-1).isBorder = true;
                }
                if(rowNo > 3) // row 4 is counted twice since it is the middle (this is already too long)
                {
                    rowElements.get(0).sideBorders[4] = 1;
                    rowElements.get(0).sideBorders[5] = 1;
                    rowElements.get(0).isBorder = true;


                    rowElements.get(rowElements.size()-1).sideBorders[0] = 1;
                    rowElements.get(rowElements.size()-1).sideBorders[1] = 1;
                    rowElements.get(rowElements.size()-1).isBorder = true;
                }


                // add first and last element of each row to the border list
                returnList.add(rowElements.get(0));
                returnList.add(rowElements.get(rowElements.size() - 1));
            }
        }

        return returnList;
    }

    public boolean isBorderHexagon(Hexagon hexagon) { // returns whether given hexagon is on the border.
        List<Hexagon> borderHexagons = getBorderHexagons();

        return borderHexagons.contains(hexagon);
    }

    public void Draw(ShapeRenderer shape) { // loop through all elements stored in board and call it's draw function

        for (List<Hexagon> hexRow : hexBoard) {
            for (Hexagon hexagon : hexRow) {

                hexagon.Draw(shape);

            }
        }

        for(Atom at: atoms)
        {
            at.Draw(shape);
        }
    }

    public void update() {  // loop through all elements stored in board and call it's update function

        // update hexagons
        for (List<Hexagon> hexRow : hexBoard) {
            for (Hexagon hexagon : hexRow) {
                hexagon.update();
            }
        }

        for(Atom at: atoms)
        {
            at.update();
        }
    }

    public List<List<Hexagon>> getHexBoard() { // Accessor method for hexBoard list
        return hexBoard;
    }
    public List<Hexagon> getRow(int i) {
        return hexBoard.get(i);
    } // Accessor method for hexBoard row
}
