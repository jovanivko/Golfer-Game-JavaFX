package com.example.rg_d2.scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class PickScene extends Scene {
    private final Button select;
    protected Rectangle[] selectionGroup;
    protected int active = 1;
    protected Group root;

    public PickScene() {
        super(new Group(), 800, 650, Color.ORANGE);

        this.root = new Group();

        this.select = new Button("Select");
        this.select.setFont(Font.font(20));
        this.select.setPrefSize(150, 50);
        this.select.addEventHandler(ActionEvent.ACTION, actionEvent -> {
            ((Stage) this.getWindow()).close();
        });
        this.select.getTransforms().add(new Translate(325, 550));
        root.getChildren().add(this.select);

        Rectangle box1 = new Rectangle(50, 100, 200, 400);
        box1.setFill(Color.WHITE);
        box1.setStroke(Color.BLACK);
        box1.setStrokeWidth(3);
        Rectangle box2 = new Rectangle(300, 100, 200, 400);
        box2.setFill(Color.WHITE);
        box2.setStroke(Color.RED);
        box2.setStrokeWidth(3);
        Rectangle box3 = new Rectangle(550, 100, 200, 400);
        box3.setFill(Color.WHITE);
        box3.setStroke(Color.BLACK);
        box3.setStrokeWidth(3);
        this.selectionGroup = new Rectangle[]{box1, box2, box3};
        this.root.getChildren().addAll(box1, box2, box3);

        super.setRoot(this.root);

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            double x = e.getX();
            double y = e.getY();
            int former = this.active;
            if (y > 100 && y < 500) {
                if (x > 50 && x < 250) {
                    this.active = 0;
                } else if (x > 300 && x < 500) {
                    this.active = 1;
                }
                if (x > 550 && x < 750) {
                    this.active = 2;
                }

                this.selectionGroup[former].setStroke(Color.BLACK);

                this.selectionGroup[this.active].setStroke(Color.RED);
            }
        });
    }

}
