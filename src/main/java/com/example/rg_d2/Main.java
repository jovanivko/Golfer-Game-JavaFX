package com.example.rg_d2;

import com.example.rg_d2.objects.*;
import com.example.rg_d2.objects.tokens.LifeToken;
import com.example.rg_d2.objects.tokens.PointsToken;
import com.example.rg_d2.objects.tokens.TimeToken;
import com.example.rg_d2.objects.tokens.Token;
import com.example.rg_d2.scenes.CannonSelectionScene;
import com.example.rg_d2.scenes.GroundSelectionScene;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.*;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private static final double BALL_RADIUS = Main.PLAYER_WIDTH / 4;
    private static final double MIN_BALL_SPEED = 5;
    private static final double MAX_BALL_SPEED = 900;
    private static final double MAX_TIME = 30;

    private static final double HOLE_RADIUS = 3 * BALL_RADIUS;
    private static final double FENCE_WIDTH = 20;
    private static final double POWERBAR_HEIGHT = WINDOW_HEIGHT - FENCE_WIDTH;
    private static final double POWERBAR_WIDTH = 15;
    private static final double TERRAIN_WIDTH = 35;
    private static final double TELEPORT_WIDTH = 25;
    private static final double BARRIER_WIDTH = 10;
    private static final double BARRIER_HEIGHT = 0.2 * WINDOW_WIDTH;
    private static final int MAX_LIVES = 5;
    private static final double TOKEN_RADIUS = Main.PLAYER_WIDTH / 2;

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
    private Barrier[] barriers;
    private TeleportationField[] teleports;
    private double dampFactor;
    private ImagePattern background;
    private double max_ball_speed;
    private List<Token> tokens;
    private double tokenTime = 4;

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

    private void addBarriers(ImagePattern cob) {
        Translate t0 = new Translate(Main.WINDOW_WIDTH / 2 - Main.BARRIER_WIDTH / 2, Main.WINDOW_HEIGHT * 0.15);
        Barrier barrier0 = new Barrier(Main.BARRIER_WIDTH, Main.BARRIER_HEIGHT, t0, cob);
        this.root.getChildren().add(barrier0);

        Translate t1 = new Translate(Main.WINDOW_WIDTH / 3 - Main.BARRIER_HEIGHT / 2, Main.WINDOW_HEIGHT * 0.5);
        Barrier barrier1 = new Barrier(Main.BARRIER_HEIGHT, Main.BARRIER_WIDTH, t1, cob);
        this.root.getChildren().add(barrier1);

        Translate t2 = new Translate(Main.WINDOW_WIDTH * 2 / 3 - Main.BARRIER_HEIGHT / 2, Main.WINDOW_HEIGHT * 0.5);
        Barrier barrier2 = new Barrier(Main.BARRIER_HEIGHT, Main.BARRIER_WIDTH, t2, cob);
        this.root.getChildren().add(barrier2);
        this.barriers = new Barrier[]{barrier0, barrier1, barrier2};
    }

    private void addTeleports() {
        Translate t1 = new Translate(Main.WINDOW_WIDTH * 0.2, Main.WINDOW_HEIGHT * 0.15);
        Translate t2 = new Translate(Main.WINDOW_WIDTH * 0.8 - Main.TELEPORT_WIDTH, Main.WINDOW_HEIGHT * 0.7);
        TeleportationField teleport1 = new TeleportationField(Main.TELEPORT_WIDTH, t1, t2, Color.ALICEBLUE);
        this.root.getChildren().add(teleport1);

        Translate t3 = new Translate(Main.WINDOW_WIDTH * 0.8 - Main.TELEPORT_WIDTH, Main.WINDOW_HEIGHT * 0.15);
        Translate t4 = new Translate(Main.WINDOW_WIDTH * 0.2, Main.WINDOW_HEIGHT * 0.7);
        TeleportationField teleport2 = new TeleportationField(Main.TELEPORT_WIDTH, t3, t4, Color.CORAL);
        this.root.getChildren().add(teleport2);

        this.teleports = new TeleportationField[]{teleport1, teleport2};
    }

    public void endAttempt() {
        this.root.getChildren().remove(this.ball);
        for (Terrain t : this.terrains) {
            t.toBack();
            t.toFront();
        }
        this.ball = null;
        this.gameover = this.menu.gameOver();
        if (this.menu.gameOver()) {
            System.out.println("Game over your total points are: " + this.menu.getPoints());
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Golfer");
        stage.setResizable(false);

        Stage selectionStage = new Stage(StageStyle.UNDECORATED);
        selectionStage.initModality(Modality.APPLICATION_MODAL);
        selectionStage.initOwner(stage);
        GroundSelectionScene pickGround = new GroundSelectionScene();
        selectionStage.setScene(pickGround);
        selectionStage.showAndWait();
        this.dampFactor = pickGround.getSelectedDampFactor();
        this.background = pickGround.getSelectedBackground();

        CannonSelectionScene pickCannon = new CannonSelectionScene(Main.PLAYER_WIDTH, Main.PLAYER_HEIGHT);
        selectionStage.setScene(pickCannon);
        selectionStage.showAndWait();

        this.root = new Group();

        Image fenceImage = new Image(Main.class.getClassLoader().getResourceAsStream("fence.jpg"));
        Image iceImage = new Image(Main.class.getClassLoader().getResourceAsStream("ice.jpg"));
        Image mudImage = new Image(Main.class.getClassLoader().getResourceAsStream("mud.jpg"));
        Image cobblestoneImage = new Image(Main.class.getClassLoader().getResourceAsStream("cobblestone.jpg"));

        ImagePattern fence_fill = new ImagePattern(fenceImage);
        ImagePattern ice_fill = new ImagePattern(iceImage);
        ImagePattern mud_fill = new ImagePattern(mudImage);
        ImagePattern cobblestone_fill = new ImagePattern(cobblestoneImage);

        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT, this.background);
        Translate playerPosition = new Translate(
                Main.WINDOW_WIDTH / 2 - Main.PLAYER_WIDTH / 2,
                Main.WINDOW_HEIGHT - FENCE_WIDTH - Main.PLAYER_HEIGHT
        );
        int cannon = pickCannon.getSelectedCannon();
        this.player = new Player(Main.PLAYER_WIDTH, Main.PLAYER_HEIGHT, playerPosition, cannon);
        this.max_ball_speed = pickCannon.getSelectedMaximumSpeed();
        //if (cannon == 2) this.player.correctPivot(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);

        this.fence = new Fence(Main.FENCE_WIDTH, Main.WINDOW_HEIGHT, Main.WINDOW_WIDTH, fence_fill);

        Translate powerBarPosition = new Translate(0, Main.WINDOW_HEIGHT);

        this.powerBar = new PowerBar(POWERBAR_WIDTH, POWERBAR_HEIGHT, powerBarPosition);

        this.menu = new MenuBar(MAX_LIVES, WINDOW_WIDTH, MAX_TIME);

        this.root.getChildren().addAll(this.fence, this.player, this.powerBar, this.menu);

        this.addHoles();
        this.addTerrain(ice_fill, mud_fill);
        this.addBarriers(cobblestone_fill);
        this.addTeleports();

        for (Node e : this.root.getChildren()) {
            Token.addNode(e);
        }
        this.tokens = new ArrayList<>();

        scene.addEventHandler(
                MouseEvent.MOUSE_MOVED,
                mouseEvent -> this.player.handleMouseMoved(
                        mouseEvent,
                        Main.PLAYER_MIN_ANGLE_OFFSET,
                        Main.PLAYER_MAX_ANGLE_OFFSET
                )
        );
        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> this.handleKeyPressed(keyEvent));

        scene.addEventHandler(MouseEvent.ANY, this);

        Timer timer = new Timer(
                deltaNanoseconds -> {
                    double deltaSeconds = (double) deltaNanoseconds / Main.NS_IN_S;
                    if (this.menu.update(deltaSeconds)) {
                        this.endAttempt();
                    }

                    if (!this.gameover && (tokenTime -= deltaSeconds) <= 0) {
                        tokenTime = 4;
                        Token t;
                        double rnd = Math.random();
                        if (rnd < 1 / 3) {
                            t = new TimeToken(FENCE_WIDTH, FENCE_WIDTH, WINDOW_WIDTH - FENCE_WIDTH, WINDOW_HEIGHT - FENCE_WIDTH, TOKEN_RADIUS, this.menu);
                        } else if (rnd >= 2 / 3) {
                            t = new LifeToken(FENCE_WIDTH, FENCE_WIDTH, WINDOW_WIDTH - FENCE_WIDTH, WINDOW_HEIGHT - FENCE_WIDTH, TOKEN_RADIUS, this.menu);
                        } else {
                            t = new PointsToken(FENCE_WIDTH, FENCE_WIDTH, WINDOW_WIDTH - FENCE_WIDTH, WINDOW_HEIGHT - FENCE_WIDTH, TOKEN_RADIUS, this.menu);
                        }
                        this.root.getChildren().add(t);
                        this.tokens.add(t);
                        Token.addNode(t);
                    }

                    for (int i = 0; i < this.tokens.size(); i++) {
                        Token t = tokens.get(i);
                        if (t.update(deltaSeconds)) {
                            this.tokens.remove(t);
                            Token.removeNode(t);
                            this.root.getChildren().remove(t);
                            t = null;
                            i--;
                        }
                    }

                    if (this.ball != null) {
                        double damp = this.dampFactor;
                        for (Terrain t : terrains) {
                            if (t.handleCollision(this.ball)) {
                                damp = damp * t.getSpeedModifier();
                            }
                        }

                        for (int i = 0; i < this.tokens.size(); i++) {
                            Token t = tokens.get(i);
                            if (t.handleCollision(this.ball)) {
                                this.tokens.remove(t);
                                Token.removeNode(t);
                                this.root.getChildren().remove(t);
                                t = null;
                                i--;
                            }
                        }

                        for (TeleportationField t : this.teleports) {
                            t.handleCollision(this.ball);
                        }

                        for (Barrier b : this.barriers) {
                            switch (b.handleCollision(this.ball)) {
                                case 1:
                                    this.ball.switchHorizontal();
                                    break;
                                case -1:
                                    this.ball.switchVertical();
                                    break;
                                case 0:
                                    continue;
                                default:
                                    continue;
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
                                }
                            }
                        }
                        //TODO generisati tokene i letece objekte
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
                if (this.menu.notStarted()) {
                    this.menu.init();
                    this.menu.start();
                }
                double value = (System.currentTimeMillis() - this.time) / Main.MS_IN_S;
                double deltaSeconds = Utilities.clamp(value, 0, Main.MAXIMUM_HOLD_IN_S);

                double ballSpeedFactor = deltaSeconds / Main.MAXIMUM_HOLD_IN_S * this.max_ball_speed;

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

    public void handleKeyPressed(KeyEvent keyEvent) {
        if (this.ball != null && keyEvent.getCode() == KeyCode.SPACE) {
            this.endAttempt();
        }
    }
}