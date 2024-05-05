package com.badlogic.game;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities class for {@code HexagonGrid}. Contains methods which create the Grid.
 */
public class GridInit {

    /**
     * Builds the Hexagon Grid, from bottom up.
     * @param grid The grid object to be made.
     */
    public void buildHexBoard(HexagonGrid grid) {
        Hexagon tempHex = new Hexagon(0,0, grid.hexRadius, grid);
        float heightDist = (float) (tempHex.getHeight() * 0.75); // gets height distance to increase y value

        float startY = Game.getWindowHeight() * 0.95f - (heightDist * (grid.minNoHex * 2));

        // Initially hexagon n is increasing and it starts dead center on x axis.
        boolean descending = false;
        boolean offset = false;

        int currNoHex = grid.minNoHex;

        for (int i = 0; i < grid.maxNoHex; i++) {
            buildHexRow(currNoHex, startY + (i * heightDist), offset, grid); // build row, increasing y level each time
            offset = !offset; // switch between offset & no offset so the rows fit together

            if (descending) {
                currNoHex--;
                continue; // once descending toggled, keep it descending until loop concludes.
            }
            else {
                currNoHex++;
            }

            descending = currNoHex >= grid.maxNoHex;
        }
    }

    /**
     * Helper method for {@code buildHexBoard()}. Builds a row of Hexagons.
     * @param n Number of Hexagons in the row
     * @param y Vertical position of row
     * @param isOffset Offset boolean, to shift Hexagons
     * @param grid The Grid object being built
     */
    private void buildHexRow(int n, float y, boolean isOffset, HexagonGrid grid) {
        List<Hexagon> hexRow = new ArrayList<>();

        // places first hexagon (n - 1) / 2 hexagon lengths to the left of x center point to build across.
        float centreX = (float) Game.getWindowWidth() / 2;
        Hexagon centreHex = new Hexagon(centreX, y, grid.hexRadius, grid);
        float hexDist = centreHex.getWidth();
        float startingX = centreX - ((n / 2) * hexDist);

        float offset = isOffset ? hexDist / 2 : 0; // offsets row distance to fit seamlessly with row.

        // Builds row of hexagons, adding hexagon length each time.
        for (int i = 0; i < n; i++) {
            hexRow.add(new Hexagon(startingX + (i * hexDist) + offset, y, grid.hexRadius, grid));
        }

        grid.hexBoard.add(hexRow); // adds row of hexagons to hexBoard list
    }

    /**
     *  Returns a list of all the {@code Hexagons} on the perimeter of the hexBoard
     * @param grid HexagonGrid object
     * @return A List of border Hexagons
     */
    public List<Hexagon> getBorderHexagons(HexagonGrid grid) { // Returns a list of all the hexagons on the outside of the hexBoard
        List<Hexagon> returnList = new ArrayList<>();
        int hexBoardRows = grid.hexBoard.size();

        for (int rowNo = 0; rowNo < hexBoardRows; rowNo++) { // for all rows bottom to top
            List<Hexagon> rowElements = grid.getRow(rowNo);

            // If it's the last row or first row add all the elements to the border list
            if (rowNo == 0) { // remember, from bottom to top
                returnList.addAll(rowElements);
                // set borders
                for(Hexagon hex: rowElements) {
                    hex.isBorder = true;
                    hex.sideBorders[2] = 1;
                    hex.sideBorders[3] = 1;
                }
                rowElements.get(0).sideBorders[4] = 1;
                rowElements.get(rowElements.size()-1).sideBorders[1] = 1;

                returnList.addAll(rowElements);

            }
            else if(rowNo == hexBoardRows - 1) { // top row
                returnList.addAll(rowElements);
                for(Hexagon hex: rowElements) {
                    hex.isBorder = true;
                    hex.sideBorders[0] = 1;
                    hex.sideBorders[5] = 1;
                }
                rowElements.get(0).sideBorders[4] = 1; // first hex gets
                rowElements.get(rowElements.size()-1).sideBorders[1] = 1;

                returnList.addAll(rowElements);
            }
            else {
                if(rowNo < 5) { // if ascending in count
                    rowElements.get(0).sideBorders[3] = 1;
                    rowElements.get(0).sideBorders[4] = 1;
                    rowElements.get(0).isBorder = true;

                    rowElements.get(rowElements.size()-1).sideBorders[1] = 1;
                    rowElements.get(rowElements.size()-1).sideBorders[2] = 1;
                    rowElements.get(rowElements.size()-1).isBorder = true;

                    returnList.addAll(rowElements);
                }
                if(rowNo > 3) { // row 4 is counted twice since it is the middle (this is already too long)
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

    /**
     * Initialises the atoms array in {@code HexagonGrid}.
     * @param grid The grid being initialised.
     */
    public void initAtoms(HexagonGrid grid) {
        float startEx = Gdx.graphics.getWidth() - 100;
        float startWhy = Gdx.graphics.getHeight() - 100;
        for(int i=0;i<5;i++){
            grid.atoms[i] = new Atom(startEx, startWhy, .75f*grid.hexRadius, 1.75f*grid.hexRadius);
            startWhy -= 100;
        }
    }

    /**
     * Activates and fills the sideBorders array in each border Hexagon of the given HexagonGrid
     * @param grid The grid to be manipulated
     */
    public void activateBorders(HexagonGrid grid) {
        List<Hexagon> bordering = getBorderHexagons(grid);
        for(Hexagon hex: bordering)
        {
            for(int i=0;i<6;i++)
            {
                if(hex.sideBorders[i] == 1) // if side is a border, add border to list
                {
                    if(i == 5) // if last
                    {
                        hex.borders.add(new Border(hex.hexPoints[i*2], hex.hexPoints[(i*2)+1], hex.hexPoints[0], hex.hexPoints[1], Ray.Direction.values()[i], grid, hex));
                    }
                    else
                    {
                        hex.borders.add(new Border(hex.hexPoints[i*2], hex.hexPoints[(i*2)+1], hex.hexPoints[(i*2)+2], hex.hexPoints[(i*2)+3], Ray.Direction.values()[i], grid, hex));
                    }
                }
            }
        }
    }
}
