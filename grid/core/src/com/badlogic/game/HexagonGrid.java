package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.game.Main.*;

public class HexagonGrid implements Clickable{
    List<List<Hexagon>> hexBoard = new ArrayList<>(); // list of rows of hexagons where the rows are lists of hexagons
    int minNoHex = 5;
    int maxNoHex = 9;

    Atom[] atoms = new Atom[5];

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
                hex.atom = atoms[i];
                break;
            }
            if (i == 4 && atoms[i].isPlaced) // if all placed
            {
                // do nothinG? or move the first placed one
                atoms[0].setAtomPoints(hex.getCenterX(), hex.getCenterY());
                Atom temp = atoms[0];

                for(int j=1;j<4;j++)
                {
                    atoms[j-1] = atoms[j];

                }
                atoms[3] = atoms[4];
                atoms[4] = temp;
            }
        }

    }


    private void addHexRow(int n, float y, boolean isOffset) {
        List<Hexagon> hexRow = new ArrayList<>();

        float centreX = (float) windowWidth / 2;
        Hexagon centreHex = new Hexagon(centreX, y, hexRadius, this);
        float hexDist = centreHex.getWidth();
        float startingX = (float) (centreX - ((n / 2) * hexDist) - (hexDist * 0.5));

        float offset = isOffset ? hexDist / 2 : 0;

        for (int i = 0; i < n; i++) {
            hexRow.add(new Hexagon(startingX + (i * hexDist) + offset, y, hexRadius, this));
        }

        hexBoard.add(hexRow);
    }

    public void buildHexBoard() {
        Hexagon tempHex = new Hexagon(0,0, hexRadius, this);
        float heightDist = (float) (tempHex.getHeight() * 0.75);

        float startY = (windowHeight) - (heightDist * (minNoHex * 2));

        boolean descending = false;
        boolean offset = false;

        int currNoHex = minNoHex;

        for (int i = 0; i < maxNoHex; i++) {

            addHexRow(currNoHex, startY + (i * heightDist), offset);
            offset = !offset;

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

    private List<Hexagon> getBorderHexagons() {
        List<Hexagon> returnList = new ArrayList<>();
        int hexBoardRows = hexBoard.size();

        for (int rowNo = 0; rowNo < hexBoardRows; rowNo++) {

            List<Hexagon> rowElements = getRow(rowNo);

            // If it's the last row or first row add all the elements to the border list
            if (rowNo == 0 || rowNo == hexBoardRows - 1) {
                returnList.addAll(rowElements);
            }

            // add first and last element of each row to the border list
            returnList.add(rowElements.get(0));
            returnList.add(rowElements.get(rowElements.size() - 1));
        }

        return returnList;
    }

    public boolean isBorderHexagon(Hexagon hexagon) {
        List<Hexagon> borderHexagons = getBorderHexagons();

        return borderHexagons.contains(hexagon);
    }

    public void Draw(ShapeRenderer shape) {

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

    public void update() {

        // update hexagons
        for (List<Hexagon> hexRow : hexBoard) {
            for (Hexagon hexagon : hexRow) {
                hexagon.update();
                if(hexagon.isClicked())
                {

                }
            }
        }
    }

    public List<Hexagon> getRow(int i) {
        return hexBoard.get(i);
    }






    @Override
    public void onClick() {

    }

    @Override
    public boolean isClicked() {
        return true;
    }

    @Override
    public boolean isHoveredOver() {
        return false;
    }


}
