package com.example.rg_d2.objects;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Enemy extends Path {
    private final Point2D speed;
    private final Translate position;
    private final double height;
    private final double maxX;
    private static final double[] directionAngle = new double[]{
            135, -135, -45, 45
    };
    private static final double maxAngle = 22.5;

    public Enemy(double width, double height, double maxX, double maxY, double speed) {
        super();
        this.height = height;
        this.maxX = maxX;
        super.getElements().addAll(
                new MoveTo(width / 2, 0),
                new LineTo(0, height),
                new HLineTo(width),
                new ClosePath()
        );
        super.setFill(Color.BROWN);
        double rnd = Math.random();
        int sign = rnd < 0.5 ? 1 : -1;

        double angle = Math.random() * maxAngle;

        rnd = Math.random();
        int direction;
        if (rnd < 0.25) {
            direction = 0;
            position = new Translate(0, 0);
        } else if (rnd < 0.5) {
            direction = 1;
            position = new Translate(maxX, 0);
        } else if (rnd < 0.75) {
            direction = 2;
            position = new Translate(maxX, maxY);
        } else {
            direction = 3;
            position = new Translate(0, maxY);
        }

        angle = directionAngle[direction] + sign * angle;

        this.speed = new Point2D(Math.cos(2 * Math.PI * (angle - 90) / 360) * speed, Math.sin(2 * Math.PI * (angle - 90) / 360) * speed);
        //Odredi ugao i direction
        super.getTransforms().addAll(
                position,
                new Rotate(angle),
                new Translate(-width / 2, 0)
        );
    }

    public boolean handleCollision(Circle ball) {
        return ((Path) Shape.intersect(this, ball)).getElements().size() > 0;
    }

    public boolean update(double ds) {
        this.position.setX(this.position.getX() + this.speed.getX() * ds);
        this.position.setY(this.position.getY() + this.speed.getY() * ds);
        return this.position.getX() >= maxX + height || this.position.getX() <= -height;
    }
}
