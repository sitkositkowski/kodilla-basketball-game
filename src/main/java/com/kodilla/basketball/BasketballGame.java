package com.kodilla.basketball;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class BasketballGame extends Application {

    private Canvas canvas;
    private int points;
    private int attempts;
    private int pointSum;
    private double accuracy;
    double[] arrowXY = new double[]{0.0,0.0};
    Ball ball = new Ball(1, 50, 50);
    Basket basket = new Basket(400, 350, 550, 400);
    private final Image imageLogo = new Image("file:src/main/resources/logo2.png",120,120,true,true);
    private Path path = Paths.get("C:\\Users\\Karol\\Development\\Java\\kodilla-basketball-game\\src\\main\\resources\\ranking.txt");
    private int bestScore;

    Slider sliderVelocity = new Slider();
    Slider sliderAngle= new Slider();

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        Scene scene = new Scene(makeCanvas(),900,700);
        stage.setScene(scene);
        stage.setTitle("Basketball game");
        stage.setResizable(false);
        stage.show();

    }

    private BorderPane makeCanvas() {
        canvas = new Canvas(800,600);
        paintCanvas();
        StackPane canvasHolder = new StackPane(canvas);
        canvasHolder.setStyle("-fx-border-width: 2px; -fx-border-color: #444");
        BorderPane root = new BorderPane(canvasHolder);
        root.setStyle("-fx-border-width: 1px; -fx-border-color: black");
        root.setBottom(makeToolPanel());
        root.setLeft(addVBox());
        return root;
    }

    private void paintCanvas() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.WHITE); // Fill with white background.
        g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        g.setFill(Color.RED);
        ball.draw(g);
        basket.draw2(g);
        basket.draw(g);
        g.strokeText(points + "/" + attempts, 50, 50);
        g.strokeText("points: " + pointSum, 50, 70);
        drawArrow(g,ball.getPosX(),ball.getPosY(),arrowXY[0], arrowXY[1]);
        for (List<Double> point1: ball.getTrajectory()) {
            if (point1 != null) {
                g.fillOval(point1.get(0), point1.get(1), 1,1);
            }
        }

    }

    public HBox makeToolPanel() {
        Label label = new Label("Velocity:");
        Label labelAngle = new Label("Angle:");

        sliderVelocity.setMin(0);
        sliderVelocity.setMax(100);
        sliderVelocity.setValue(80);

        sliderVelocity.setShowTickLabels(true);
        sliderVelocity.setShowTickMarks(true);

        sliderVelocity.setBlockIncrement(10);

        // Adding Listener to value property.
        //
        sliderVelocity.valueProperty().addListener((observable, oldValue, newValue) -> {

            //System.out.println("New value: " + newValue);
            arrowXY = new double[]{sliderVelocity.getValue(),Math.toRadians(sliderAngle.getValue())};
            //System.out.println(sliderVelocity.getValue()+","+Math.toRadians(sliderAngle.getValue())+","+sliderAngle.getValue());
            paintCanvas();
        });



        sliderAngle.setMin(0);
        sliderAngle.setMax(90);
        sliderAngle.setValue(30);
        sliderAngle.setBlockIncrement(15);
        sliderAngle.setMajorTickUnit(30);
        //sliderAngle.setMinorTickCount(6);
        sliderAngle.setShowTickLabels(true);
        sliderAngle.setShowTickMarks(true);


        // Adding Listener to value property.
        //
        sliderAngle.valueProperty().addListener((observable, oldValue, newValue) -> {

            //System.out.println("New value: " + newValue);
            arrowXY = new double[]{sliderVelocity.getValue(),Math.toRadians(sliderAngle.getValue())};
            //System.out.println(sliderVelocity.getValue()+","+Math.toRadians(sliderAngle.getValue())+","+sliderAngle.getValue());
            paintCanvas();
        });



        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setOnHiding((e) -> reset());

        Button drawbtn = new Button();
        drawbtn.setText("Throw!");
        drawbtn.setPrefSize(100,50);
        drawbtn.setOnAction((e) -> {
            System.out.println("Throw parameter: velocity: " + sliderVelocity.getValue() + ", angle: " + sliderAngle.getValue() + "," + Math.toRadians(sliderAngle.getValue()));
            ball.setVelocity(sliderVelocity.getValue());
            ball.setAngle(Math.toRadians(sliderAngle.getValue()));
            boolean isHit = ball.throwBall(basket);
            System.out.println("Is hit? " + isHit);
            attempts += 1;
            points += (isHit) ? 1 : 0;
            pointSum = points * 2 - (attempts-points);
            throwBall();
            alert.setContentText("Is hit? " + isHit);
            alert.showAndWait();
        });

        HBox tools = new HBox(10);
        tools.setPadding(new Insets(15, 12, 15, 12));
        //tools.getChildren().add(resetbtn);
        tools.getChildren().add(drawbtn);
        //tools.getChildren().add(scrollBarVelocity);
        //tools.getChildren().add(scrollBarAngle);
        tools.getChildren().add(label);
        tools.setAlignment(Pos.CENTER);
        tools.getChildren().add(sliderVelocity);
        tools.getChildren().add(labelAngle);
        tools.getChildren().add(sliderAngle);


        return tools;
    }

    public VBox addVBox() {

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Button resetbtn = new Button();
        resetbtn.setText("Start/Reset");
        resetbtn.setOnAction((e) -> {
            points = 0;
            pointSum = 0;
            attempts = 0;
            reset();
        });

        TextInputDialog dialog = new TextInputDialog("walter");
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText("Look, a Text Input Dialog");
        dialog.setContentText("Please enter your name:");

        Button savebtn = new Button();
        savebtn.setText("Save");
        savebtn.setOnAction((e) -> {

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> saveResult(name));
        });

        final ImageView selectedImage = new ImageView();
        selectedImage.setImage(imageLogo);

        vbox.getChildren().addAll(selectedImage);
        vbox.getChildren().add(resetbtn);
        vbox.getChildren().add(savebtn);

        return vbox;
    }

    public void reset (){
        ball.setPosX(50);
        ball.setPosY(50);
        sliderAngle.setValue(30);
        sliderVelocity.setValue(30);
        ball.setTrajectory(new ArrayList<>(100));
        //trajectory = new ArrayList<>(100);
        paintCanvas();
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

    void drawArrow(GraphicsContext gc, double x1, double y1, double velocity,double angle) {
        velocity = velocity*2;

        System.out.println("("+ (Math.cos(angle))+","+(Math.sin(angle))+")");
        double beta = Math.asin(0.15/1.8);
        double K= 0.9 * velocity;

        y1 = 600-y1;
        double x2 = x1 + velocity*Math.cos(angle);
        double y2 = y1 - velocity*Math.sin(angle);

        gc.strokeLine(x1, y1, x2, y2);
        gc.strokeLine(x2, y2,x1+K*Math.cos(angle-beta),y1-K*Math.sin(angle-beta));
        gc.strokeLine(x2, y2,x1+K*Math.cos(angle+beta),y1-K*Math.sin(angle+beta));
    }

    public void saveResult(String name){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Karol\\Development\\Java\\kodilla-basketball-game\\src\\main\\resources\\ranking.txt",true)))
        {   accuracy = (double) points / attempts;
            writer.write(name + " | points: " + pointSum + ", accuracy: " +  accuracy);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("wystąpił błąd: " + e);
        }

        try
        {
            File myObj = new File("C:\\Users\\Karol\\Development\\Java\\kodilla-basketball-game\\src\\main\\resources\\bestscores.txt");
            Scanner myReader = new Scanner(myObj);
            String data = myReader.nextLine();
            bestScore = Integer.parseInt(data);
            if (bestScore< pointSum) {
                try (BufferedWriter writer2 = new BufferedWriter(new FileWriter("C:\\Users\\Karol\\Development\\Java\\kodilla-basketball-game\\src\\main\\resources\\ranking.txt",true)))
                {   accuracy = (double) points / attempts;
                    writer2.write(name + " | points: " + pointSum + ", accuracy: " +  accuracy);
                } catch (IOException e) {
                    System.out.println("wystąpił błąd: " + e);
                }
            }
        } catch (IOException e) {
            System.out.println("wystąpił błąd: " + e);
        }
    }
}
