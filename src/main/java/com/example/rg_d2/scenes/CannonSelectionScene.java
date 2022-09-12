package com.example.rg_d2.scenes;

import com.example.rg_d2.objects.Player;
import javafx.scene.transform.Translate;

public class CannonSelectionScene extends PickScene {
    private static final double MAXIMUM_BALL_SPEED1 = 800;
    private static final double MAXIMUM_BALL_SPEED2 = 1200;
    private static final double MAXIMUM_BALL_SPEED3 = 1600;

    private final Player cannon1;
    private final Player cannon2;
    private final Player cannon3;

    public CannonSelectionScene(double width, double heigth) {
        super();
        this.cannon1 = new Player(width, heigth, new Translate(150 - width / 2, 200 + heigth / 2), 0);
        this.cannon2 = new Player(width, heigth, new Translate(400 - width / 2, 200 + heigth / 2), 1);
        this.cannon3 = new Player(width, heigth, new Translate(650 - width / 2, 200 + heigth / 2), 2);
        super.root.getChildren().addAll(cannon1, cannon2, cannon3);
    }

    public double getSelectedMaximumSpeed() {
        double res;
        switch (super.active) {
            case 0:
                res = MAXIMUM_BALL_SPEED1;
                break;
            case 1:
                res = MAXIMUM_BALL_SPEED2;
                break;
            case 2:
                res = MAXIMUM_BALL_SPEED3;
                break;
            default:
                res = 1;
        }
        return res;
    }

    public int getSelectedCannon() {
        return super.active;
    }
}
