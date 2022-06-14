package com.example.rg_d2.objects;

import com.example.rg_d2.objects.shapes.Heart;
import javafx.scene.Group;
import javafx.scene.control.Label;
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
            life.getTransforms().add(new Translate(max_width - i * width - (i > 0 ? (i - 1) * width / 3 : 0), 0));
            hearts.add(life);
        }

        hearts.toArray(this.hearts);

        for (Heart h : this.hearts) {
            super.getChildren().add(h);
        }
        this.score = new Label(this.points.toString());
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

    public void newAttempt() {
        this.hearts[--this.lives].setVisible(false);
    }

    public boolean gameOver() {
        return this.lives == 0;
    }
}
