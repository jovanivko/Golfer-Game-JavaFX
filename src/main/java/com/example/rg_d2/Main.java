package com.example.rg_d2;

import com.example.rg_d2.objects.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class Main extends Application implements EventHandler<MouseEvent> {
    private static final double WINDOW_WIDTH = 600;
    private static final double WINDOW_HEIGHT = 700;

    private static final double PLAYER_WIDTH = 20;
    private static final double PLAYER_HEIGHT = 80;
    private static final double PLAYER_MAX_ANGLE_OFFSET = 60;
    private static final double PLAYER_MIN_ANGLE_OFFSET = -60;

    private static final double MS_IN_S = 1e3;
    private static final double NS_IN_S = 1e9;
    private static final double MAXIMUM_HOLD_IN_S = 3;
    private static final double MAXIMUM_BALL_SPEED = 1500;
    private static final double BALL_RADIUS = Main.PLAYER_WIDTH / 4;
    private static final double BALL_DAMP_FACTOR = 0.995;
    private static final double MIN_BALL_SPEED = 5;
    private static final double MAX_BALL_SPEED = 900;

    private static final double HOLE_RADIUS = 3 * BALL_RADIUS;
    private static final double FENCE_WIDTH = 20;
    private static final double POWERBAR_HEIGHT = WINDOW_HEIGHT - FENCE_WIDTH;
    private static final double POWERBAR_WIDTH = 15;
    private static final double TERRAIN_WIDTH = 75;
    private static final int MAX_LIVES = 5;

    private Group root;
    private Player player;
    private Ball ball;
    private long time;
    private Hole[] holes;
    private PowerBar powerBar;
    private boolean mouse_hold;
    private Fence fence;
    private Terrain[] terrains;
    private MenuBar menu;
    private boolean gameover = false;

    private void addHoles() {
        Translate hole0Position = new Translate(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT * 0.1
        );
        Stop stops0[] = {
                new Stop(0, Color.BLACK),
                new Stop(1, Color.YELLOW)
        };

        RadialGradient gradient0 = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops0);
        Hole hole0 = new Hole(Main.HOLE_RADIUS, hole0Position, 20, gradient0);
        this.root.getChildren().addAll(hole0);

        Stop stops1[] = {
                new Stop(0, Color.BLACK),
                new Stop(1, Color.GREEN)
        };
        RadialGradient gradient1 = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops1);
        Translate hole1Position = new Translate(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT * 0.4
        );
        Hole hole1 = new Hole(Main.HOLE_RADIUS, hole1Position, 5, gradient1);
        this.root.getChildren().addAll(hole1);

        Stop stops2[] = {
                new Stop(0, Color.BLACK),
                new Stop(1, Color.BLUE)
        };
        RadialGradient gradient2 = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, stops2);


        Translate hole2Position = new Translate(
                Main.WINDOW_WIDTH / 3,
                Main.WINDOW_HEIGHT * 0.25
        );
        Hole hole2 = new Hole(Main.HOLE_RADIUS, hole2Position, 10, gradient2);
        this.root.getChildren().addAll(hole2);

        Translate hole3Position = new Translate(
                Main.WINDOW_WIDTH * 2 / 3,
                Main.WINDOW_HEIGHT * 0.25
        );
        Hole hole3 = new Hole(Main.HOLE_RADIUS, hole3Position, 10, gradient2);
        this.root.getChildren().addAll(hole3);

        this.holes = new Hole[]{
                hole0,
                hole1,
                hole2,
                hole3,
        };
    }

    private void addTerrain(ImagePattern ice, ImagePattern mud) {
        Translate pos0 = new Translate(WINDOW_WIDTH * 0.1, WINDOW_HEIGHT * 0.20);
        Terrain ter0 = new Ice(TERRAIN_WIDTH, pos0, ice);
        this.root.getChildren().addAll(ter0);

        Translate pos1 = new Translate(WINDOW_WIDTH * 0.9 - TERRAIN_WIDTH, WINDOW_HEIGHT * 0.20);
        Terrain ter1 = new Mud(TERRAIN_WIDTH, pos1, mud);
        this.root.getChildren().addAll(ter1);

        Translate pos2 = new Translate(WINDOW_WIDTH * 0.1, WINDOW_HEIGHT * 0.6);
        Terrain ter2 = new Mud(TERRAIN_WIDTH, pos2, mud);
        this.root.getChildren().addAll(ter2);

        Translate pos3 = new Translate(WINDOW_WIDTH * 0.9 - TERRAIN_WIDTH, WINDOW_HEIGHT * 0.6);
        Terrain ter3 = new Ice(TERRAIN_WIDTH, pos3, ice);
        this.root.getChildren().addAll(ter3);
        this.terrains = new Terrain[]{ter0, ter1, ter2, ter3};
    }

    public void endAttempt() {
        this.root.getChildren().remove(this.ball);
        for (Terrain t : this.terrains) {
            t.toBack();
            t.toFront();
        }
        this.ball = null;
        if (this.menu.gameOver()) {
            System.out.println("Game over your total points are: " + this.menu.getPoints());
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.root = new Group();

        Image backgroundImage = new Image(Main.class.getClassLoader().getResourceAsStream("grass.jpg"));
        Image fenceImage = new Image(Main.class.getClassLoader().getResourceAsStream("fence.jpg"));
        Image iceImage = new Image(Main.class.getClassLoader().getResourceAsStream("ice.jpg"));
        Image mudImage = new Image(Main.class.getClassLoader().getResourceAsStream("mud.jpg"));

        ImagePattern background = new ImagePattern(backgroundImage);
        ImagePattern fence_fill = new ImagePattern(fenceImage);
        ImagePattern ice_fill = new ImagePattern(iceImage);
        ImagePattern mud_fill = new ImagePattern(mudImage);

        Scene scene = new Scene(this.root, Main.WINDOW_WIDTH, WINDOW_HEIGHT, background);

        Translate playerPosition = new Translate(
                Main.WINDOW_WIDTH / 2 - Main.PLAYER_WIDTH / 2,
                Main.WINDOW_HEIGHT - FENCE_WIDTH - Main.PLAYER_HEIGHT
        );

        this.player = new Player(
                Main.PLAYER_WIDTH,
                Main.PLAYER_HEIGHT,
                playerPosition
        );

        this.fence = new Fence(Main.FENCE_WIDTH, Main.WINDOW_HEIGHT, Main.WINDOW_WIDTH, fence_fill);

        Translate powerBarPosition = new Translate(0, Main.WINDOW_HEIGHT);

        this.powerBar = new PowerBar(POWERBAR_WIDTH, POWERBAR_HEIGHT, powerBarPosition);

        this.menu = new MenuBar(MAX_LIVES, WINDOW_WIDTH);

        this.root.getChildren().addAll(this.fence, this.player, this.powerBar, this.menu);

        this.addHoles();
        this.addTerrain(ice_fill, mud_fill);
//        this.addTerrain(null, null);

        scene.addEventHandler(
                MouseEvent.MOUSE_MOVED,
                mouseEvent -> this.player.handleMouseMoved(
                        mouseEvent,
                        Main.PLAYER_MIN_ANGLE_OFFSET,
                        Main.PLAYER_MAX_ANGLE_OFFSET
                )
        );

        scene.addEventHandler(MouseEvent.ANY, this);

        Timer timer = new Timer(
                deltaNanoseconds -> {
                    double deltaSeconds = (double) deltaNanoseconds / Main.NS_IN_S;
                    if (this.ball != null) {
                        double damp = BALL_DAMP_FACTOR;
                        for (Terrain t : terrains) {
                            if (t.handleCollision(this.ball)) {
                                damp = damp * t.getSpeedModifier();
                            }
                        }
                        boolean stopped = this.ball.update(
                                deltaSeconds,
                                FENCE_WIDTH,
                                Main.WINDOW_WIDTH - FENCE_WIDTH,
                                FENCE_WIDTH,
                                Main.WINDOW_HEIGHT - FENCE_WIDTH,
                                damp,
                                Main.MIN_BALL_SPEED
                        );

                        boolean isInHole = false;
                        boolean toFast = this.ball.getSpeed() > MAX_BALL_SPEED;

                        for (Hole h : this.holes) {
                            if (h.handleCollision(this.ball)) {
                                isInHole = true;
                                if (!toFast && !this.ball.notStopped()) {
                                    this.menu.addPoints(h.getPoints());

                                    try {
                                        this.ball.playAnimation(Main.class.getMethod("endAttempt", null), this, h);
                                    } catch (NoSuchMethodException e) {
                                        throw new RuntimeException(e);
                                    }

                                    this.gameover = this.menu.gameOver();
                                }
                            }
                        }

                        if (stopped && ball != null) {
                            endAttempt();
                        }
                    } else if (this.mouse_hold) {
                        double value = (System.currentTimeMillis() - this.time) / Main.MS_IN_S;
                        double perc = value / Main.MAXIMUM_HOLD_IN_S;
                        this.powerBar.updateBar(perc);
                    }
                }

        );
        timer.start();

        scene.setCursor(Cursor.NONE);

        stage.setTitle("Golfer");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        this.mouse_hold = false;
        if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_PRESSED) && mouseEvent.isPrimaryButtonDown() & !this.gameover) {
            this.time = System.currentTimeMillis();
            this.mouse_hold = true;
        } else if (mouseEvent.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
            if (this.time != -1 && this.ball == null) {
                double value = (System.currentTimeMillis() - this.time) / Main.MS_IN_S;
                double deltaSeconds = Utilities.clamp(value, 0, Main.MAXIMUM_HOLD_IN_S);

                double ballSpeedFactor = deltaSeconds / Main.MAXIMUM_HOLD_IN_S * Main.MAXIMUM_BALL_SPEED;

                Translate ballPosition = this.player.getBallPosition();
                Point2D ballSpeed = this.player.getSpeed().multiply(ballSpeedFactor);
                this.menu.newAttempt();
                this.ball = new Ball(Main.BALL_RADIUS, ballPosition, ballSpeed);
                this.root.getChildren().addAll(this.ball);
            }
            this.time = -1;
            this.mouse_hold = false;
            this.powerBar.updateBar(0);
        }
    }
}