package com.example.rg_d2.objects;

import com.example.rg_d2.objects.shapes.Heart;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

public class MenuBar extends Group {
    private Integer points;
    private int lives;
    private final int max_lives;
    private final Heart[] hearts;
    private final Label score;

    public MenuBar(int max_lives, double max_width) {
        super();
        this.max_lives = max_lives;
        this.lives = max_lives;
        this.points = 0;
        this.hearts = new Heart[max_lives];

        List<Heart> hearts = new ArrayList<>();
        for (int i = 0; i < max_lives; i++) {
            Heart life = new Heart();
            double width = life.getBoundsInParent().getWidth();
            life.getTransforms().add(new Translate(max_width - (i + 1) * width - (i > 0 ? (i) * width / 3 : 0), 0));
            hearts.add(life);
        }

        hearts.toArray(this.hearts);

        for (Heart h : this.hearts) {
            super.getChildren().add(h);
        }
        this.score = new Label(this.points.toString());
        this.score.setTextAlignment(TextAlignment.RIGHT);
        this.score.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        this.score.getTransforms().add(new Translate(20, 0));
        super.getChildren().add(this.score);

    }

    public void init() {
        this.points = 0;
        this.lives = max_lives;
        for (Heart h : this.hearts) {
            h.setVisible(true);
        }
        this.score.setText(this.points.toString());
    }

    public void addPoints(int points) {
        this.points += points;
        this.score.setText(this.points.toString());
    }

    public int getPoints() {
        return this.points;
    }

    public void newAttempt() {
        this.hearts[--this.lives].setVisible(false);
    }

    public boolean gameOver() {
        return this.lives == 0;
    }
}
