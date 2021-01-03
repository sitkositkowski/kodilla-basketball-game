package com.kodilla.basketball;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Basketball2 extends Application {

    private Canvas canvas;
    private int points;
    private int attempts;
    Ball ball = new Ball(1, 50, 50);
    Basket basket = new Basket(500, 400, 550, 400);


    public static void main(String[] args) {
        launch(args);
    }

    /*@Override
    public void start(Stage primaryStage) throws Exception {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);




        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        grid.setHgap(5.5);
        grid.setVgap(5.5);


        ImageView imgBall = new ImageView(imageBall);
        ImageView imgBasket = new ImageView(imageBasket);
        grid.add(imgBall,0,0);
        grid.add(imgBasket,10,0);
        grid.add(drawbtn, 0, 5);
        grid.add(scrollBarVelocity, 0, 10);
        grid.add(scrollBarAngle, 0, 15);
        grid.setBackground(background);



        Scene scene = new Scene(grid, 1600, 900, Color.BLACK);

        primaryStage.setTitle("Basketball Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }*/

    public void start(Stage stage) {
        canvas = makeCanvas();
        paintCanvas();
        StackPane canvasHolder = new StackPane(canvas);
        canvasHolder.setStyle("-fx-border-width: 2px; -fx-border-color: #444");
        BorderPane root = new BorderPane(canvasHolder);
        root.setStyle("-fx-border-width: 1px; -fx-border-color: black");
        root.setBottom(makeToolPanel());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Basketball game");
        stage.setResizable(false);
        stage.show();
    }

    private Canvas makeCanvas() {
        return new Canvas(800,600);
    }

    private void paintCanvas() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.WHITE); // Fill with white background.
        g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        ball.draw(g);
        basket.draw(g);
        g.setFill(Color.BLACK);
        for (List<Double> point1: ball.getTrajectory()) {
            if (point1 != null) {
                g.fillOval(point1.get(0), point1.get(1), 1,1);
            }
        }
        g.strokeText(points + "/" + attempts, 50, 50);
    }

    public HBox makeToolPanel() {

        ScrollBar scrollBarVelocity = new ScrollBar();
        scrollBarVelocity.setOrientation(Orientation.HORIZONTAL);

        ScrollBar scrollBarAngle = new ScrollBar();
        scrollBarAngle.setOrientation(Orientation.HORIZONTAL);

        Button resetbtn = new Button();
        resetbtn.setText("Start/Reset");
        resetbtn.setOnAction((e) -> {
            ball.setPosX(50);
            ball.setPosY(50);
            ball.setTrajectory(new ArrayList<>(100));
            //trajectory = new ArrayList<>(100);
            paintCanvas();
        });

        Button drawbtn = new Button();
        drawbtn.setText("Throw!");
        drawbtn.setOnAction((e) -> {
            System.out.println("Throw parameter: velocity: " + scrollBarVelocity.getValue() + ", angle: " + scrollBarAngle.getValue()/100*90 + "," + scrollBarAngle.getValue()*Math.PI/200);
            ball.setVelocity(scrollBarVelocity.getValue()*1.2);
            ball.setAngle(scrollBarAngle.getValue()*Math.PI/200);
            boolean isHit = ball.throwBall(basket);
            System.out.println("Is hit? " + isHit);
            attempts += 1;
            points += (isHit) ? 1 : 0;
            throwBall();
        });

        HBox tools = new HBox(30);
        tools.getChildren().add(resetbtn);
        tools.getChildren().add(drawbtn);
        tools.getChildren().add(scrollBarVelocity);
        tools.getChildren().add(scrollBarAngle);

        return tools;
    }

    public void throwBall (){
        double time = (int) Math.ceil(2*ball.getVelocity()*Math.sin(ball.getAngle())/10);

        double x0 = ball.getPosX();
        double y0 = ball.getPosY();
        double t;
        for (int i = 0; i<100; i++){
            t = time/100 *i;
            //System.out.println(time);
            List<Double> point = new ArrayList<>();
            point.add(x0+ball.getVelocity()*t*Math.cos(ball.getAngle()));
            point.add(-(y0+ball.getVelocity()*t*Math.sin(ball.getAngle()) - 5 * Math.pow(t,2))+600);
            ball.getTrajectory().add(point);
            //trajectory.add(point);
            ball.setPosX(x0+ball.getVelocity()*t*Math.cos(ball.getAngle()));
            ball.setPosY(y0+ball.getVelocity()*t*Math.sin(ball.getAngle()) - 5 * Math.pow(t,2));
            //System.out.println(point);
            paintCanvas();
        }
    }
}
