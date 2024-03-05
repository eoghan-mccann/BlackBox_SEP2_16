package com.badlogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.game.Game.*;

public class HexagonGrid {
    List<List<Hexagon>> hexBoard; // list of rows of hexagons where the rows are lists of hexagons
    int minNoHex;
    int maxNoHex;

    Atom[] atoms;
    List<Ray2> rays;

    public HexagonGrid()
    {
        hexBoard = new ArrayList<>(); // list of rows of hexagons where the rows are lists of hexagons
        minNoHex = 5;
        maxNoHex = 9;

        atoms = new Atom[5];
        rays = new ArrayList<>();
    }

    /*
        How to make borders clickable
        -> sideBorders array in each hexagon -> stores which sides are borders
        -> activateBorders() - iterate through getBorderHexagons().
            \-> For hexagon, create a new "border" thing for each 1 in sideborders
                \-> update this in hexagon.update()

        -> "border" thing - class?
            \-> store its two points
            \-> if clicked, new ray at midpoint of border going in direction specified

     */

    public void activateBorders()
    {
        List<Hexagon> bordering = getBorderHexagons();
        for(Hexagon hex: bordering)
        {
            for(int i=0;i<6;i++)
            {
                if(hex.sideBorders[i] == 1) // if side is a border, add border to list
                {
                    if(i == 5) // if last
                    {
                        hex.borders.add(new Border(hex.hexPoints[i*2], hex.hexPoints[(i*2)+1], hex.hexPoints[0], hex.hexPoints[1], Ray2.Direction.values()[i], this));
                    }
                    else
                    {
                        hex.borders.add(new Border(hex.hexPoints[i*2], hex.hexPoints[(i*2)+1], hex.hexPoints[(i*2)+2], hex.hexPoints[(i*2)+3], Ray2.Direction.values()[i], this));
                    }
                }
            }
        }

    }

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
                if(hex.isInside(ray.headPos[0], ray.headPos[1]))
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
        for (Ray2 ray : rays) {
            ray.toggle = !ray.toggle;
        }
    }

    public void addRay(float x, float y, Ray2.Direction direction) {
        rays.add(new Ray2(x,y,direction));
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

        for (int rowNo = 0; rowNo < hexBoardRows; rowNo++) { // for all rows bottom to top

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

                returnList.addAll(rowElements);

            }
            else if(rowNo == hexBoardRows - 1) // top row
            {
                returnList.addAll(rowElements);

                for(Hexagon hex: rowElements)
                {
                    hex.isBorder = true;
                    hex.sideBorders[0] = 1;
                    hex.sideBorders[5] = 1;
                }
                rowElements.get(0).sideBorders[4] = 1; // first hex gets
                rowElements.get(rowElements.size()-1).sideBorders[1] = 1;

                returnList.addAll(rowElements);
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

                    returnList.addAll(rowElements);
                }
                if(rowNo > 3) // row 4 is counted twice since it is the middle (this is already too long)
                {
                    rowElements.get(0).sideBorders[4] = 1;
                    rowElements.get(0).sideBorders[5] = 1;
                    rowElements.get(0).isBorder = true;


                    rowElements.get(rowElements.size()-1).sideBorders[0] = 1;
                    rowElements.get(rowElements.size()-1).sideBorders[1] = 1;
                    rowElements.get(rowElements.size()-1).isBorder = true;

                    returnList.addAll(rowElements);
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

    public List<float[]> getOutsideVertices() {
        List<float[]> outerVertices = new ArrayList<>();

        List<Hexagon> borderHexagons = getBorderHexagons();

        for (Hexagon borderHexagon : borderHexagons) {
            float[] coordinates = borderHexagon.getCoordinates();

            for (int i = 0; i < coordinates.length; i += 2) {
                float[] currentVertex = new float[]{coordinates[i], coordinates[i + 1]};
                int sharedPoints = 0;

                for (Hexagon hexagon : getHexBoard()) {
                    if (hexagon == borderHexagon) { continue; }
                    if (hexagon.containsVertex(currentVertex)) {
                        sharedPoints++;
                    }
                }

                if (sharedPoints <= 1) {
                    outerVertices.add(currentVertex);
                }
            }
        }

        return outerVertices;

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

        for(Ray2 ray: rays) {
            ray.Draw(shape);
        }
    }

    // loop through all elements stored in board and call it's update function
    public void update() {
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

        for(Ray2 ray: rays) {
            rayCheck(ray);
            ray.update();
        }

    }


    public List<Hexagon> getHexBoard() { // Accessor method for hexBoard list
        List<Hexagon> flattenedHexList = new ArrayList<>();
        hexBoard.forEach(flattenedHexList::addAll);
        return flattenedHexList;
    }
    public List<Hexagon> getRow(int i) {
        return hexBoard.get(i);
    } // Accessor method for hexBoard row
}
