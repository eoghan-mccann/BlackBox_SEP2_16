package com.badlogic.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Tests {

    @Test
    void testAtomMethods(){
        try {
            Atom.class.getDeclaredMethod("setCenterX", float.class);
        } catch (NoSuchMethodException ex) {
            fail("setCenterX() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("setCenterY", float.class);
        } catch (NoSuchMethodException ex) {
            fail("setCenterY() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("setAtomPoints", float.class, float.class);
        } catch (NoSuchMethodException ex) {
            fail("setAtomPoints() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("getCentre");
        } catch (NoSuchMethodException ex) {
            fail("getCentre() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("getCoordinates");
        } catch (NoSuchMethodException ex) {
            fail("getCoordinates() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("getPosition");
        } catch (NoSuchMethodException ex) {
            fail("getPosition() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("getCollision");
        } catch (NoSuchMethodException ex) {
            fail("getCollision() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("Draw", ShapeRenderer.class);
        } catch (NoSuchMethodException ex) {
            fail("Draw() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("update");
        } catch (NoSuchMethodException ex) {
            fail("update() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("getCentreRadius");
        } catch (NoSuchMethodException ex) {
            fail("getCentreRadius() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("getLayerRadius");
        } catch (NoSuchMethodException ex) {
            fail("getLayerRadius() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("atomRow", int.class, ShapeRenderer.class);
        } catch (NoSuchMethodException ex) {
            fail("atomRow() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("onClick");
        } catch (NoSuchMethodException ex) {
            fail("onClick() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("isClicked");
        } catch (NoSuchMethodException ex) {
            fail("isClicked() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("isHoveredOver");
        } catch (NoSuchMethodException ex) {
            fail("isHoveredOver() method signature incorrect");
        }
    }

    @Test
    void testRayMethods(){
        try {
            Ray.class.getDeclaredMethod("getCentre");
        } catch (NoSuchMethodException ex) {
            fail("getCentre() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("getCoordinates");
        } catch (NoSuchMethodException ex) {
            fail("getCoordinates() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("getPosition");
        } catch (NoSuchMethodException ex) {
            fail("getPosition() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("getCollision");
        } catch (NoSuchMethodException ex) {
            fail("getCollision() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("Draw", ShapeRenderer.class);
        } catch (NoSuchMethodException ex) {
            fail("Draw() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("update");
        } catch (NoSuchMethodException ex) {
            fail("update() method signature incorrect");
        }
    }

    @Test
    void testHexagonGridMethods(){
        try {
            HexagonGrid.class.getDeclaredMethod("initAtoms");
        } catch (NoSuchMethodException ex) {
            fail("initAtoms() method signature incorrect");
        }
        try {
            HexagonGrid.class.getDeclaredMethod("moveAtom", Hexagon.class);
        } catch (NoSuchMethodException ex) {
            fail("moveAtom() method signature incorrect");
        }
        try {
            HexagonGrid.class.getDeclaredMethod("resetAtom", Hexagon.class);
        } catch (NoSuchMethodException ex) {
            fail("resetAtom() method signature incorrect");
        }
        try {
            HexagonGrid.class.getDeclaredMethod("addHexRow", int.class, float.class, boolean.class);
        } catch (NoSuchMethodException ex) {
            fail("addHexRow() method signature incorrect");
        }
        try {
            HexagonGrid.class.getDeclaredMethod("buildHexBoard");
        } catch (NoSuchMethodException ex) {
            fail("buildHexBoard() method signature incorrect");
        }
        try {
            HexagonGrid.class.getDeclaredMethod("getBorderHexagons");
        } catch (NoSuchMethodException ex) {
            fail("getBorderHexagons() method signature incorrect");
        }
        try {
            HexagonGrid.class.getDeclaredMethod("isBorderHexagon", Hexagon.class);
        } catch (NoSuchMethodException ex) {
            fail("isBorderHexagon() method signature incorrect");
        }
        try {
            HexagonGrid.class.getDeclaredMethod("Draw", ShapeRenderer.class);
        } catch (NoSuchMethodException ex) {
            fail("Draw() method signature incorrect");
        }
        try {
            HexagonGrid.class.getDeclaredMethod("update");
        } catch (NoSuchMethodException ex) {
            fail("update() method signature incorrect");
        }
        try {
            HexagonGrid.class.getDeclaredMethod("getRow", int.class);
        } catch (NoSuchMethodException ex) {
            fail("getRow() method signature incorrect");
        }
        try {
            HexagonGrid.class.getDeclaredMethod("onClick");
        } catch (NoSuchMethodException ex) {
            fail("onClick() method signature incorrect");
        }
        try {
            HexagonGrid.class.getDeclaredMethod("isClicked");
        } catch (NoSuchMethodException ex) {
            fail("isClicked() method signature incorrect");
        }
        try {
            HexagonGrid.class.getDeclaredMethod("isHoveredOver");
        } catch (NoSuchMethodException ex) {
            fail("isHoveredOver() method signature incorrect");
        }
    }

    @Test
    void testHexagonMethods(){
        try {
            Hexagon.class.getDeclaredMethod("calculateXpoints", float.class);
        } catch (NoSuchMethodException ex) {
            fail("calculateXPoints() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("calculateYpoints", float.class);
        } catch (NoSuchMethodException ex) {
            fail("calculateYPoints() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("setHexagonPos", float.class, float.class);
        } catch (NoSuchMethodException ex) {
            fail("setHexagonPos() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("getWidth");
        } catch (NoSuchMethodException ex) {
            fail("getWidth() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("getHeight");
        } catch (NoSuchMethodException ex) {
            fail("getHeight() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("onClick");
        } catch (NoSuchMethodException ex) {
            fail("onClick() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("isClicked");
        } catch (NoSuchMethodException ex) {
            fail("isClicked() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("isHoveredOver");
        } catch (NoSuchMethodException ex) {
            fail("isHoveredOver() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("contains", float.class, float.class);
        } catch (NoSuchMethodException ex) {
            fail("contains() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("getCenterX");
        } catch (NoSuchMethodException ex) {
            fail("getCenterX() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("getCenterY");
        } catch (NoSuchMethodException ex) {
            fail("getCenterY() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("getCentre");
        } catch (NoSuchMethodException ex) {
            fail("getCentre() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("getCoordinates");
        } catch (NoSuchMethodException ex) {
            fail("getCoordinates() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("getPosition");
        } catch (NoSuchMethodException ex) {
            fail("getPosition() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("getCollision");
        } catch (NoSuchMethodException ex) {
            fail("getCollision() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("Draw", ShapeRenderer.class);
        } catch (NoSuchMethodException ex) {
            fail("Draw() method signature incorrect");
        }
        try {
            Hexagon.class.getDeclaredMethod("update");
        } catch (NoSuchMethodException ex) {
            fail("update() method signature incorrect");
        }
    }
}
