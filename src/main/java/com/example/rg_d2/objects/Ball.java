package com.example.rg_d2.objects;

import com.example.rg_d2.Utilities;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Ball extends Circle {
    private Translate position;
    private Point2D speed;
    private ParallelTransition fade;
    private boolean stopped;

    public Ball(double radius, Translate position, Point2D speed) {
        super(radius, Color.RED);

        this.position = position;
        this.speed = speed;
        this.stopped = false;

        super.getTransforms().addAll(this.position);

    }

    public double getSpeed() {
        return this.speed.magnitude();
    }

    public boolean notStopped() {
        return this.stopped;
    }

    public void playAnimation(Method method, Object obj, Hole hole) {
        double x = hole.getX();
        double y = hole.getY();

        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), this);
        tt.setToX(x);
        tt.setToY(y);
        tt.setCycleCount(1);

        ScaleTransition st = new ScaleTransition(Duration.seconds(2), this);
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(0);
        st.setToY(0);
        st.setCycleCount(1);

        this.fade = new ParallelTransition(tt, st);
        this.fade.setOnFinished(e -> {
            try {
                method.invoke(obj, null);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.fade.play();
        this.stopped = true;
    }

    public boolean update(double ds, double left, double right, double top, double bottom, double dampFactor, double minBallSpeed) {
        boolean result = false;
        if (this.stopped) {

            return result;
        }
        double newX = this.position.getX() + this.speed.getX() * ds;
        double newY = this.position.getY() + this.speed.getY() * ds;

        double radius = super.getRadius();

        double minX = left + radius;
        double maxX = right - radius;
        double minY = top + radius;
        double maxY = bottom - radius;

        this.position.setX(Utilities.clamp(newX, minX, maxX));
        this.position.setY(Utilities.clamp(newY, minY, maxY));

        if (newX < minX || newX > maxX) {
            this.speed = new Point2D(-this.speed.getX(), this.speed.getY());
        }

        if (newY < minY || newY > maxY) {
            this.speed = new Point2D(this.speed.getX(), -this.speed.getY());
        }

        this.speed = this.speed.multiply(dampFactor);

        double ballSpeed = this.speed.magnitude();

        if (ballSpeed < minBallSpeed) {
            result = true;
        }

        return result;
    }

    public void switchHorizontal(){
        this.speed = new Point2D(-this.speed.getX(), this.speed.getY());
    }
    public void switchVertical(){
        this.speed = new Point2D(this.speed.getX(), -this.speed.getY());
    }
}
