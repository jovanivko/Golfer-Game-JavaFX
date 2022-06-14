package com.example.rg_d2.objects.shapes;

import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class Heart extends SVGPath {
    public Heart() {
        super();
        super.setContent("M11.8 0c-1.7 0-3.15 1.35-3.8 2.8C7.35 1.35 5.9 0 4.2 0 1.9 0 0 1.9 0 4.2c0 4.7 4.75 5.95 8 10.6 3.05-4.65 8-6.05 8-10.6C16 1.9 14.1 0 11.8 0z");
        super.setFill(Color.RED);
    }
}
