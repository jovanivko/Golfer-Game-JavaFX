package com.example.rg_d2.objects;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class TeleportationField {
    private Field field1;
    private Field field2;

    protected class Field extends Rectangle {
        public Field(double size, Color color) {
            super(size, size, color);
        }

        public boolean handleCollision(Circle ball){
            Bounds ballBounds = ball.getBoundsInParent();

            double ballX = ballBounds.getCenterX();
            double ballY = ballBounds.getCenterY();

            Bounds terrainBounds = super.getBoundsInParent();

            double minX = terrainBounds.getMinX();
            double minY = terrainBounds.getMinY();
            double maxX = terrainBounds.getMaxX();
            double maxY = terrainBounds.getMaxY();

            boolean result = (ballX < maxX && ballX > minX) && (ballY > minY && ballY < maxY);

            return result;
        }
    }

    public TeleportationField(double size, Translate position1, Translate position2, Color color) {
        this.field1 = new Field(size, color);
        this.field1.getTransforms().add(position1);

        this.field2 = new Field(size, color);
        this.field2.getTransforms().add(position2);
    }

    public boolean handleCollision(Circle ball){
        return false;
    }

}
