package com.example.rg_d2.objects;

import com.example.rg_d2.objects.shapes.Heart;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    private final Label remaining;
    private final Rectangle remainingLine;
    private final double max_time;
    private double remaining_time;
    private boolean started;


    public MenuBar(int max_lives, double max_width, double max_time) {
        super();
        this.max_lives = max_lives;
        this.lives = max_lives;
        this.max_time = max_time;
        this.points = 0;
        this.hearts = new Heart[max_lives];
        this.remaining_time = this.max_time;
        this.started = false;
        //TODO dodati liniju za vreme koja se smanjuje u update() i label koji prikazuje preostalo vreme

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

        this.remaining = new Label(Double.toString(this.max_time));
        this.remaining.setTextAlignment(TextAlignment.RIGHT);
        this.remaining.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
        this.remaining.getTransforms().add(new Translate(400, 2));

        this.remainingLine = new Rectangle(50, 5, Color.RED);
        this.remainingLine.setX(400);
        this.remainingLine.setY(12);

        super.getChildren().addAll(this.remaining, this.remainingLine);

    }

    public void init() {
        this.points = 0;
        this.lives = this.max_lives;
        this.remaining_time = this.max_time;
        for (Heart h : this.hearts) {
            h.setVisible(true);
        }
        this.score.setText(this.points.toString());
    }

    public void addPoints(int points) {
        System.out.println("Points before: " + this.points);
        this.points += points;
        this.score.setText(this.points.toString());
        System.out.println("Points after: " + this.points);
    }

    public int getPoints() {
        return this.points;
    }

    public void newAttempt() {
        this.hearts[--this.lives].setVisible(false);
    }

    public boolean gameOver() {
        if (this.lives == 0 || this.remaining_time == 0) {
            this.started = false;
            return true;
        }
        return false;
    }

    public boolean notStarted() {
        return this.lives == this.max_lives;
    }

    public void start() {
        this.started = true;
    }

    public void endGame() {
        for (Heart h : this.hearts) {
            h.setVisible(false);
        }
        this.lives = 0;
    }

    public boolean update(double ds) {
        if (this.started) {
            this.remaining_time -= ds;
            if (this.remaining_time < 0) this.remaining_time = 0;
            this.remaining.setText(Double.toString(Math.round(this.remaining_time)));
            this.remainingLine.setWidth(50 * this.remaining_time / this.max_time);
        }
        return this.remaining_time == 0 && this.started;
    }

    public void addTime(int points) {
        System.out.println("Time before: " + this.remaining_time);
        this.remaining_time += points;
        this.remaining.setText(Double.toString(Math.round(this.remaining_time)));
        this.remainingLine.setWidth(50 * this.remaining_time / this.max_time);
        System.out.println("Time after: " + this.remaining_time);
    }

    public void addLife() {
        System.out.println("Lives before: " + this.lives);

        this.hearts[this.lives++].setVisible(true);
        System.out.println("Lives after: " + this.lives);
    }
}
