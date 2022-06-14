package com.example.rg_d2.objects;

import com.example.rg_d2.Utilities;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class PowerBar extends Rectangle {
    private double max_height;
    private Translate postion;

    public PowerBar(double width, double height, Translate position) {
        super(width, 0, Color.RED);
        this.max_height = height;

        super.getTransforms().addAll(
                position,
                new Translate(width, 0),
                new Rotate(180)
        );

    }

    public void updateBar(double perc) {
        super.setHeight(Utilities.clamp(perc * max_height, 0, max_height));
    }
}
