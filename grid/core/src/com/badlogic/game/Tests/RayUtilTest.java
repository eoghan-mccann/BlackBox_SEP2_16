package com.badlogic.game.Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.badlogic.game.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayUtilTest {
    HexagonGrid hexagonGrid;

    @BeforeEach
    void setUp() {
        hexagonGrid = new HexagonGrid(50);
    }

    @Test
    void setCurrDirection_singleAtomDeflect() {
        // Create a Hexagon with only 1 neighbor
        Hexagon hex = new Hexagon(0, 0, 50, hexagonGrid);
        hex.neighbCount = 1;
        hex.setNeighbDir(Hexagon.NeighbourPosition.West);

        // Create a Ray with initial direction E
        Ray ray = new Ray(0, 0, Ray.Direction.E, hexagonGrid);
        ray.setCurrHex(hex);

        // Set current direction
        ray.setCurrDirection(hex, ray);

        // The new direction after deflecting should be W
        assertEquals(Ray.Direction.W, ray.getCurrDirection());
    }
    @Test
    void reflectTest() {
        // Create a new Ray travelling NE direction
        Ray rayNE = new Ray(0, 0, Ray.Direction.NE, hexagonGrid);

        // Reflect it back the way it came
        rayNE.reflect(rayNE);

        // The new direction after reflecting should be SW
        assertEquals(Ray.Direction.SW, rayNE.getCurrDirection());
    }


    @Test
    void getReflectionDirection() {
        // Test all reflection directions
        assertEquals(Ray.Direction.SW, RayUtil.getReflectionDirection(Ray.Direction.NE));
        assertEquals(Ray.Direction.W, RayUtil.getReflectionDirection(Ray.Direction.E));
        assertEquals(Ray.Direction.NW, RayUtil.getReflectionDirection(Ray.Direction.SE));
        assertEquals(Ray.Direction.NE, RayUtil.getReflectionDirection(Ray.Direction.SW));
        assertEquals(Ray.Direction.E, RayUtil.getReflectionDirection(Ray.Direction.W));
        assertEquals(Ray.Direction.SE, RayUtil.getReflectionDirection(Ray.Direction.NW));
    }
}