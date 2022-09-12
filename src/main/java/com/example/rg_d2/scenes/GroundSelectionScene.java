package com.example.rg_d2.scenes;

import com.example.rg_d2.Main;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class GroundSelectionScene extends PickScene {
    private static final double GRASS_DAMP_FACTOR = 0.995;
    private static final double SAND_DAMP_FACTOR = 0.993;
    private static final double PARQUET_DAMP_FACTOR = 0.997;

    private ImagePattern grass;
    private ImagePattern sand;
    private ImagePattern parquet;


    public GroundSelectionScene() {
        super();
        Image grassImage = new Image(Main.class.getClassLoader().getResourceAsStream("grass.jpg"));
        Image sandImage = new Image(Main.class.getClassLoader().getResourceAsStream("sand.jpg"));
        Image parquetImage = new Image(Main.class.getClassLoader().getResourceAsStream("parquet.jpg"));
        ImagePattern grass = new ImagePattern(grassImage);
        ImagePattern sand = new ImagePattern(sandImage);
        ImagePattern parquet = new ImagePattern(parquetImage);
        super.selectionGroup[0].setFill(sand);
        super.selectionGroup[1].setFill(grass);
        super.selectionGroup[2].setFill(parquet);
    }

    public ImagePattern getSelectedBackground() {
        return (ImagePattern) super.selectionGroup[super.active].getFill();
    }

    public double getSelectedDampFactor() {
        double res;
        switch (super.active) {
            case 0:
                res = SAND_DAMP_FACTOR;
                break;
            case 1:
                res = GRASS_DAMP_FACTOR;
                break;
            case 2:
                res = PARQUET_DAMP_FACTOR;
                break;
            default:
                res = 1;
        }
        return res;
    }
}
