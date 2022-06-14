package com.example.rg_d2.objects;

import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.Translate;

public class Mud extends Terrain{


    public Mud(double edge, Translate position, ImagePattern background) {
        super(edge, position, 0.05, background);
    }

    @Override
    public double getSpeedModifier() {
        return 1 - super.getSpeedModifier();
    }
}
