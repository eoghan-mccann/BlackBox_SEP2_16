package com.badlogic.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.game.Main.*;

public class HexagonGrid {
    List<List<Hexagon>> hexBoard = new ArrayList<>(); // list of rows of hexagons where the rows are lists of hexagons
    int minNoHex = 5;
    int maxNoHex = 9;
    private void addHexRow(int n, float y, boolean isOffset) {
        List<Hexagon> hexRow = new ArrayList<>();

        float centreX = (float) windowWidth / 2;
        Hexagon centreHex = new Hexagon(centreX, y, hexRadius);
        float hexDist = centreHex.getWidth();
        float startingX = (float) (centreX - ((n / 2) * hexDist) - (hexDist * 0.5));

        float offset = isOffset ? hexDist / 2 : 0;

        for (int i = 0; i < n; i++) {
            hexRow.add(new Hexagon(startingX + (i * hexDist) + offset, y, hexRadius));
        }

        hexBoard.add(hexRow);
    }

    public void buildHexBoard() {
        Hexagon tempHex = new Hexagon(0,0, hexRadius);
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

                if (hexagon.isClickToggled) {
                    hexagon.atom.Draw(shape);
                }

            }
        }
    }

    public void update() {
        for (List<Hexagon> hexRow : hexBoard) {
            for (Hexagon hexagon : hexRow) {
                hexagon.update();
            }
        }
    }

    public List<Hexagon> getRow(int i) {
        return hexBoard.get(i);
    }
}
