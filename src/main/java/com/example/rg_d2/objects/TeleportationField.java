package com.example.rg_d2.objects;

import com.example.rg_d2.Timer;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class TeleportationField extends Group {
    private static final double NS_IN_S = 1e9;

    private final Field field1;
    private final Field field2;
    private double timeElapsed1;

    public Field getField1() {
        return field1;
    }

    public Field getField2() {
        return field2;
    }

    private double timeElapsed2;
    private boolean teleported1;
    private boolean teleported2;

    protected static class Field extends Rectangle {
        public Field(double size, Color color) {
            super(size, size, color);
        }

        public boolean handleCollision(Circle ball) {
            Bounds ballBounds = ball.getBoundsInParent();

            double ballX = ballBounds.getCenterX();
            double ballY = ballBounds.getCenterY();

            Bounds fieldBounds = super.getBoundsInParent();

            double minX = fieldBounds.getMinX();
            double minY = fieldBounds.getMinY();
            double maxX = fieldBounds.getMaxX();
            double maxY = fieldBounds.getMaxY();

            return (ballX < maxX && ballX > minX) && (ballY > minY && ballY < maxY);
        }
    }

    public TeleportationField(double size, Translate position1, Translate position2, Color color) {
        super();
        this.teleported1 = false;
        this.teleported2 = false;
        this.timeElapsed1 = 0;
        this.timeElapsed2 = 0;

        this.field1 = new Field(size, color);
        this.field1.getTransforms().add(position1);

        this.field2 = new Field(size, color);
        this.field2.getTransforms().add(position2);

        super.getChildren().addAll(this.field1, this.field2);
        Timer timer = new Timer(dns -> {
            double deltaSeconds = (double) dns / TeleportationField.NS_IN_S;
            if (teleported1) timeElapsed1 += deltaSeconds;
            if (teleported2) timeElapsed2 += deltaSeconds;
            if (timeElapsed1 > 1) teleported1 = false;
            if (timeElapsed2 > 1) teleported2 = false;
        });
        timer.start();
    }

    public void handleCollision(Ball ball) {
        if (this.field1.handleCollision(ball) && (!this.teleported2)) {
            this.teleported1 = true;
            this.timeElapsed1 = 0;
            ball.translate(this.field2.getBoundsInParent().getCenterX(), this.field2.getBoundsInParent().getCenterY());
        } else if (this.field2.handleCollision(ball) && !this.teleported1) {
            this.teleported2 = true;
            this.timeElapsed2 = 0;
            ball.translate(this.field1.getBoundsInParent().getCenterX(), this.field1.getBoundsInParent().getCenterY());
        }
    }

    public boolean handleCollision(Circle circle) {
        return this.field2.handleCollision(circle) || this.field1.handleCollision(circle);
    }

}





