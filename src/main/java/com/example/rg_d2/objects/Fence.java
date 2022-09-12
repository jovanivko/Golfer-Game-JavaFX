package com.example.rg_d2.objects;

import javafx.scene.Group;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;


public class Fence extends Group {

    public Fence(double width, double max_height, double max_width, ImagePattern pattern) {
        super();
        Rectangle top = new Rectangle(max_width, width, pattern);
        Rectangle left = new Rectangle(width, max_height, pattern);
        Rectangle bottom = new Rectangle(max_width, width, pattern);
        bottom.getTransforms().addAll(
                new Translate(0, max_height - width)
        );
        Rectangle right = new Rectangle(width, max_height, pattern);
        right.getTransforms().addAll(
                new Translate(max_width - width, 0)
        );
        super.getChildren().addAll(top, left, bottom, right);
    }
}
