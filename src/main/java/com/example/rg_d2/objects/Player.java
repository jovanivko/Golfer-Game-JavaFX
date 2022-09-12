package com.example.rg_d2.objects;

import com.example.rg_d2.Utilities;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Player extends Group {

    private final double width;
    private final double height;
    private Translate position;
    private Rotate rotate;

    private double baseRadius;

    public Player(double width, double height, Translate position, int type) {
        this.width = width;
        this.height = height;
        this.position = position;
        //switch u odnosu na type pa svaki drugacije;
        switch (type) {
            case 0: {
                this.baseRadius = width / 2;

                Circle base = new Circle(baseRadius, Color.ORANGE);

                base.getTransforms().add(new Translate(width / 2, height - baseRadius));

                Path cannon = new Path(
                        new MoveTo(width / 4, 0),
                        new LineTo(0, height - baseRadius),
                        new HLineTo(width),
                        new LineTo(width * 3 / 4, 0),
                        new ClosePath()
                );
                cannon.setFill(Color.LIGHTBLUE);

                super.getChildren().addAll(cannon, base);

                this.rotate = new Rotate(0);

                super.getTransforms().addAll(
                        position,
                        new Translate(width / 2, height - baseRadius),
                        rotate,
                        new Translate(-width / 2, -(height - baseRadius))
                );
                break;
            }
            case 1: {
                Rectangle rec = new Rectangle(this.position.getX() - this.width / 2, this.position.getY() + this.height - 2 * this.width, this.width * 2, 2 * this.width);
                rec.setFill(Color.ORANGE);
                this.baseRadius = width / 2;

                Path cannon = new Path(
                        new MoveTo(0, 0),
                        new VLineTo(height - baseRadius),
                        new ArcTo(this.baseRadius, this.baseRadius, 0, this.width, this.height - this.baseRadius, false, false),
                        new VLineTo(0),
                        new ClosePath()
                );
                cannon.setFill(Color.LIGHTBLUE);

                this.rotate = new Rotate();

                cannon.getTransforms().addAll(
                        position,
                        new Translate(width / 2, height - baseRadius),
                        rotate,
                        new Translate(-width / 2, -(height - baseRadius))
                );

                super.getChildren().addAll(rec, cannon);
                break;
            }
            case 2: {
                this.baseRadius = this.width / 2;
                Path base = new Path(
                        new MoveTo(this.width / 2, 0),
                        new LineTo(0, this.width),
                        new HLineTo(3 * this.width),
                        new LineTo(2.5 * this.width, 0),
                        new ClosePath()
                );
                base.setFill(Color.DARKGRAY);
                base.setStroke(null);
                base.getTransforms().add(new Translate(this.position.getX() - this.width, this.position.getY() + this.height - this.width));
                Path cannon = new Path(
                        new MoveTo(this.width / 4, 0),
                        new VLineTo(height - baseRadius),
                        new ArcTo(this.width / 4, this.width / 4, 0, 0.75 * this.width, this.height - this.baseRadius, false, false),
                        new VLineTo(0),
                        new ClosePath()
                );
                cannon.setFill(Color.DARKGRAY);
                cannon.setStroke(null);

                this.rotate = new Rotate();
                this.position = new Translate(this.position.getX(), this.position.getY());
                cannon.getTransforms().addAll(
                        this.position,
                        new Translate(width / 2, height - baseRadius),
                        rotate,
                        new Translate(-width / 2, -(height - baseRadius))
                );
                super.getChildren().addAll(base, cannon);
                break;
            }
        }
    }

    public void handleMouseMoved(MouseEvent mouseEvent, double minAngleOffset, double maxAngleOffset) {
        Bounds bounds = super.getBoundsInParent();

        double startX = bounds.getCenterX();
        double startY = bounds.getMaxY();

        double endX = mouseEvent.getX();
        double endY = mouseEvent.getY();

        Point2D direction = new Point2D(endX - startX, endY - startY).normalize();
        Point2D startPosition = new Point2D(0, -1);

        double angle = (endX > startX ? 1 : -1) * direction.angle(startPosition);

        this.rotate.setAngle(Utilities.clamp(angle, minAngleOffset, maxAngleOffset));
    }

    public Translate getBallPosition() {
        double startX = this.position.getX() + this.width / 2;
        double startY = this.position.getY() + this.height - this.baseRadius;

        double x = startX + Math.sin(Math.toRadians(this.rotate.getAngle())) * this.height;
        double y = startY - Math.cos(Math.toRadians(this.rotate.getAngle())) * this.height;

        return new Translate(x, y);
    }

    public Point2D getSpeed() {
        double startX = this.position.getX() + this.width / 2;
        double startY = this.position.getY() + this.height - this.baseRadius;

        double endX = startX + Math.sin(Math.toRadians(this.rotate.getAngle())) * this.height;
        double endY = startY - Math.cos(Math.toRadians(this.rotate.getAngle())) * this.height;

        Point2D result = new Point2D(endX - startX, endY - startY);

        return result.normalize();
    }
}
