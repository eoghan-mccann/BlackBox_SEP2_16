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
            Atom.class.getDeclaredMethod("setAtomPoints");
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
            Atom.class.getDeclaredMethod("display");
        } catch (NoSuchMethodException ex) {
            fail("display() method signature incorrect");
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
            Atom.class.getDeclaredMethod("atomToggle", ShapeRenderer.class);
        } catch (NoSuchMethodException ex) {
            fail("atomToggle() method signature incorrect");
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
            Atom.class.getDeclaredMethod("display");
        } catch (NoSuchMethodException ex) {
            fail("display() method signature incorrect");
        }
        try {
            Atom.class.getDeclaredMethod("update");
        } catch (NoSuchMethodException ex) {
            fail("update() method signature incorrect");
        }
    }
}
