package com.example.rg_d2.objects;

import javafx.geometry.Bounds;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public abstract class Terrain extends Rectangle {
    private final double speedModifier;

    protected Terrain(double edge, Translate position, double modifier, ImagePattern background) {
        super(edge, edge, background);
        super.getTransforms().add(position);
        this.speedModifier = modifier;
    }

    public boolean handleCollision(Circle ball) {
        Bounds ballBounds = ball.getBoundsInParent();

        double ballX = ballBounds.getCenterX();
        double ballY = ballBounds.getCenterY();

        Bounds terrainBounds = super.getBoundsInParent();

        double minX = terrainBounds.getMinX();
        double minY = terrainBounds.getMinY();
        double maxX = terrainBounds.getMaxX();
        double maxY = terrainBounds.getMaxY();

        return (ballX < maxX && ballX > minX) && (ballY > minY && ballY < maxY);
    }

    public double getSpeedModifier() {
        return this.speedModifier;
    }
}
