package com.example.rg_d2.objects;

import javafx.geometry.Bounds;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class Barrier extends Rectangle {
    public Barrier(double width, double height, Translate position, ImagePattern image) {
        super(width, height, image);
        super.getTransforms().add(position);
    }

    public int handleCollision(Circle ball) {
        Bounds ballBounds = ball.getBoundsInParent();

        double ballX = ballBounds.getCenterX();
        double ballY = ballBounds.getCenterY();
        double ballRadius = ball.getRadius();

        Bounds barrierBounds = super.getBoundsInParent();

        double minX = barrierBounds.getMinX() - ballRadius;
        double minY = barrierBounds.getMinY() - ballRadius;
        double maxX = barrierBounds.getMaxX() + ballRadius;
        double maxY = barrierBounds.getMaxY() + ballRadius;
        int result = 0;
        if (minX < ballX && ballX < maxX && ballY > minY && ballY < maxY) {
            result = -1;
            if ((ballX < minX + this.getWidth() && ballY < maxY - ballX + minX && ballY > minY + ballX - minX) ||
                    (ballX > maxX - this.getWidth() && ballY < maxY - maxX + ballX && ballY > minY + maxX - ballX)) {
                result = 1;
            }
        }
        return result;
    }
}
