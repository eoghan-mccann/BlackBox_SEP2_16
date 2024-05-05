package com.badlogic.game.Tests;

import com.badlogic.game.HexUtil;
import com.badlogic.game.Hexagon;
import com.badlogic.game.HexagonGrid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HexUtilTest {

    @Test
    void calculateXpoints() {
        HexUtil hexUtil = new HexUtil();
        Hexagon hex = new Hexagon(30, 40, 1, new HexagonGrid(1));

        float[] expectedXPoints = {0.0f, 0.8660254f, 0.8660254f, 0.0f, -0.8660254f, -0.8660254f};
        float[] actualXPoints = hexUtil.calculateXpoints(hex, 0);
        assertArrayEquals(expectedXPoints, actualXPoints, 0.0001f);

    }

    @Test
    void calculateYpoints() {
        HexUtil hexUtil = new HexUtil();
        Hexagon hex = new Hexagon(30, 40, 1, new HexagonGrid(1));

        float[] expectedYPoints = {1.0f, 0.5f, -0.5f, -1.0f, -0.5f, 0.5f};
        float[] actualYPoints = hexUtil.calculateYpoints(hex, 0);
        assertArrayEquals(expectedYPoints, actualYPoints, 0.0001f);
    }

    @Test
    void testIsInside() {
        HexUtil hexUtil = new HexUtil();
        Hexagon hex = new Hexagon(100, 100, 50, new HexagonGrid(50));

        // Test case 1: Point inside the hexagon
        assertTrue(hexUtil.isInside(hex, 80, 90));

        // Test case 2: Point outside the hexagon
        assertFalse(hexUtil.isInside(hex, 0, 0));

        // Test case 3: Point on edge of Hexagon
        assertTrue(hexUtil.isInside(hex, 100, 100));

    }

    @Test
    void testSetHexagonPos() {
        HexUtil hexUtil = new HexUtil();
        Hexagon hex = new Hexagon(30, 40, 1, new HexagonGrid(1));

        // Set the position of the hexagon
        hexUtil.setHexagonPos(hex, 50, 60);

        // Expected coordinates after setting the position
        float[] expectedXPoints = {50.0f, 50.8660254f, 50.8660254f, 50.0f, 49.133975f, 49.133975f};
        float[] expectedYPoints = {61.0f, 60.5f, 59.5f, 59.0f, 59.5f, 60.5f};

        // Get the actual coordinates of the hexagon's corners
        int countX = 0;
        int countY = 0;
        float[] actualXPoints = new float[6];
        float[] actualYPoints = new float[6];
        for(int i=0;i<12;i++)
        {
            if(i % 2 == 0)
            {
                actualXPoints[countX] = hex.hexPoints[i];
                countX++;
            }
            else
            {
                actualYPoints[countY] = hex.hexPoints[i];
                countY++;
            }
        }


        // Verify that the actual coordinates match the expected coordinates
        assertArrayEquals(expectedXPoints, actualXPoints, 0.0001f);
        assertArrayEquals(expectedYPoints, actualYPoints, 0.0001f);

        assertNotSame(expectedXPoints, expectedYPoints);
    }



}