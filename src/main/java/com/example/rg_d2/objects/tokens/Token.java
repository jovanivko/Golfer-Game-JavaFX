package com.example.rg_d2.objects.tokens;

import com.example.rg_d2.objects.*;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

public abstract class Token extends Circle {
    protected int points;
    private final double radius;
    protected MenuBar menu;
    private double duration;

    private static final List<Shape> objects = new ArrayList<>();

    public Token(double minX, double minY, double maxX, double maxY, double radius, MenuBar menu) {
        super(radius, Color.YELLOW);
        boolean invalid = false;
        this.radius = radius;
        this.menu = menu;
        this.duration = 5;
        do {
            double x = Math.random() * (maxX - minX - 2 * radius) + minX + radius;
            double y = Math.random() * (maxY - minY - 2 * radius) + minY + radius;
            this.setCenterX(x);
            this.setCenterY(y);
            for (Shape object : objects) {
                /*if (object instanceof Barrier) {
                    if (((Barrier) object).handleCollision(this) != 0) invalid = true;
                    break;
                }
                if (object instanceof Hole) {
                    if (((Hole) object).handleCollision(this)) invalid = true;
                    break;
                }
                if (object instanceof Terrain) {
                    if (((Terrain) object).handleCollision(this)) invalid = true;
                    break;
                }
                if (object instanceof TeleportationField) {
                    if (((TeleportationField) object).handleCollision(this)) invalid = true;
                    break;
                }*/
                if (((Path) Shape.intersect(this, object)).getElements().size() > 0) {
                    invalid = true;
                    break;
                }
                if (x >= 250 && x <= 350 && y < 700 && y >= 600) {
                    invalid = true;
                    break;
                }
            }
        } while (invalid);
    }

    public static void addNode(Shape node) {
        Token.objects.add(node);
    }

    public static void removeNode(Shape node) {
        Token.objects.remove(node);
    }

    public abstract void doAction();

    public boolean handleCollision(Circle ball) {
        Bounds ballBounds = ball.getBoundsInParent();

        double ballX = ballBounds.getCenterX();
        double ballY = ballBounds.getCenterY();
        double ballRadius = ball.getRadius();

        Bounds bounds = this.getBoundsInParent();
        double x = bounds.getCenterX();
        double y = bounds.getCenterY();

        if (Math.sqrt(Math.pow(ballX - x, 2) + Math.pow(ballY - y, 2)) <= ballRadius + this.radius) {
            this.doAction();
            return true;
        }
        return false;
    }

    public boolean update(double ns) {
        return (this.duration -= ns) <= 0;
    }
}
