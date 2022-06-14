package com.example.rg_d2.objects;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Fence extends Group {
    private double width;
    private double max_height;
    private double max_width;

    public Fence(double width, double max_height, double max_width, ImagePattern pattern) {
        super();
        this.width = width;
        this.max_height = max_height;
        this.max_width = max_width;
        Rectangle top = new Rectangle(max_width, this.width, pattern);
        Rectangle left = new Rectangle(this.width, this.max_height, pattern);
        Rectangle bottom = new Rectangle(this.max_width, this.width, pattern);
        bottom.getTransforms().addAll(
                new Translate(0, max_height - this.width)
        );
        Rectangle right = new Rectangle(this.width, this.max_height, pattern);
        right.getTransforms().addAll(
                new Translate(max_width - this.width, 0)
        );
        super.getChildren().addAll(top, left, bottom, right);
    }
}
