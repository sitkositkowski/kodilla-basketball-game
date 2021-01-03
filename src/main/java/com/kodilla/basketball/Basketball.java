package com.kodilla.basketball;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Basketball extends Application {

    private Image imageback = new Image("file:src/main/resources/basketballCourt2.png");
    private Image imageBall = new Image("file:src/main/resources/ball.png",100,100,true,true);
    private Image imageBasket = new Image("file:src/main/resources/basket.png",300,300,true,true);
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        Ball ball = new Ball(1,0,0);
        Basket basket = new Basket(90,0,110,0);

        ScrollBar scrollBarVelocity = new ScrollBar();
        scrollBarVelocity.setOrientation(Orientation.HORIZONTAL);

        ScrollBar scrollBarAngle = new ScrollBar();
        scrollBarAngle.setOrientation(Orientation.HORIZONTAL);


        Button drawbtn = new Button();
        drawbtn.setText("Throw!");
        drawbtn.setOnAction((e) -> {
            System.out.println("Throw parameter: velocity: " + scrollBarVelocity.getValue() + ", angle: " + scrollBarAngle.getValue()/100*90 + "," + scrollBarAngle.getValue()*Math.PI/200);
            ball.setVelocity(scrollBarVelocity.getValue());
            ball.setAngle(scrollBarAngle.getValue()*Math.PI/200);
            boolean isHit = ball.throwBall(basket);
            System.out.println("Is hit? " + isHit);
        });




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
    }
}
