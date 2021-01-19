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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class BasketballGame extends Application {

    private Canvas canvas;
    private int points;
    private int attempts;
    private int pointSum;
    Random random = new Random();
    double[] arrowXY = new double[]{0.0,0.0};
    Ball ball = new Ball(1, 50, 50);
    Basket basket = new Basket(400, 350);
    private final Image imageLogo = new Image("file:src/main/resources/logo2.png",120,200,true,true);
    private final Image icon = new Image("file:src/main/resources/ball.png",120,120,true,true);
    private final Path path = Paths.get("src/main/resources/ranking.txt");
    private int bestScore = checkHighScore();

    Slider sliderVelocity = new Slider();
    Slider sliderAngle= new Slider();

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        Scene scene = new Scene(makeCanvas(),900,700);
        stage.setScene(scene);
        stage.getIcons().add(icon);
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
        root.setStyle("-fx-border-width: 1px; -fx-border-color: #000000");
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
        g.strokeText("Goals: " + points + " / Attempts: " + attempts, 50, 50);
        g.strokeText("Points: " + pointSum, 50, 70);
        g.strokeText("High Score: " + bestScore, 50, 90);
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
        sliderVelocity.valueProperty().addListener((observable, oldValue, newValue) -> {
            arrowXY = new double[]{sliderVelocity.getValue(),Math.toRadians(sliderAngle.getValue())};
            paintCanvas();
        });

        sliderAngle.setMin(0);
        sliderAngle.setMax(90);
        sliderAngle.setValue(30);
        sliderAngle.setBlockIncrement(15);
        sliderAngle.setMajorTickUnit(30);
        sliderAngle.setShowTickLabels(true);
        sliderAngle.setShowTickMarks(true);

        sliderAngle.valueProperty().addListener((observable, oldValue, newValue) -> {
            arrowXY = new double[]{sliderVelocity.getValue(),Math.toRadians(sliderAngle.getValue())};
            paintCanvas();
        });


        Button drawbtn = new Button();
        drawbtn.setText("Throw!");
        drawbtn.setPrefSize(120,30);
        drawbtn.setOnAction((e) -> {

            System.out.println("Throw parameter: velocity: " + sliderVelocity.getValue() + ", angle: " + sliderAngle.getValue() + "," + Math.toRadians(sliderAngle.getValue()));
            ball.setVelocity(sliderVelocity.getValue()*1.1);
            ball.setAngle(Math.toRadians(sliderAngle.getValue()));
            boolean isHit = ball.throwBall(basket);
            System.out.println("Is hit? " + isHit);
            attempts += 1;
            points += (isHit) ? 1 : 0;
            pointSum = points * 2 - (attempts-points);
            throwBall();

            Alert alert;

            if (isHit) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("You did it! The ball hit the basket and you score 2 points.");
            } else{
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Unfortunately... you didn't make it this time. You lose 1 point.");
            }

            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setOnHiding((event) -> reset());
            alert.showAndWait();

        });

        HBox tools = new HBox(10);
        tools.setPadding(new Insets(15, 12, 15, 12));
        tools.getChildren().add(label);
        tools.setAlignment(Pos.CENTER);
        tools.getChildren().add(sliderVelocity);
        tools.getChildren().add(labelAngle);
        tools.getChildren().add(sliderAngle);
        tools.getChildren().add(drawbtn);

        return tools;
    }

    public VBox addVBox() {

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        Button resetbtn = new Button();
        resetbtn.setText("Start/Reset");
        resetbtn.setPrefSize(120,30);
        resetbtn.setOnAction((e) -> {
            points = 0;
            pointSum = 0;
            attempts = 0;
            reset();
        });

        TextInputDialog dialog = new TextInputDialog("Player Name");
        dialog.setTitle("Save");
        dialog.setHeaderText("Save your score.");
        dialog.setContentText("Please enter your name:");

        Button savebtn = new Button();
        savebtn.setText("Save");
        savebtn.setPrefSize(120,30);
        savebtn.setOnAction((e) -> {

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(this::saveResult);
        });

        final ImageView selectedImage = new ImageView();
        selectedImage.setImage(imageLogo);
        vbox.getChildren().addAll(selectedImage);
        vbox.getChildren().add(resetbtn);
        vbox.getChildren().add(savebtn);
        vbox.setSpacing(50);

        return vbox;
    }

    public void reset (){
        ball.setPosX(50);
        ball.setPosY(50);
        basket.setPosXa(random.nextInt(200)+300);
        basket.setPosYa(random.nextInt(200)+250);
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

            List<Double> point = new ArrayList<>();
            point.add(x0+ball.getVelocity()*t*Math.cos(ball.getAngle()));
            point.add(-(y0+ball.getVelocity()*t*Math.sin(ball.getAngle()) - 5 * Math.pow(t,2))+600);
            ball.getTrajectory().add(point);

            ball.setPosX(x0+ball.getVelocity()*t*Math.cos(ball.getAngle()));
            ball.setPosY(y0+ball.getVelocity()*t*Math.sin(ball.getAngle()) - 5 * Math.pow(t,2));

            paintCanvas();
        }
    }

    void drawArrow(GraphicsContext gc, double x1, double y1, double velocity,double angle) {
        velocity = velocity*2;

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/ranking.txt",true)))
        {
            double accuracy = (double) points / attempts;
            writer.write(name + ";" + pointSum + ";" + accuracy);
            writer.newLine();

            bestScore = checkHighScore();

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public int checkHighScore(){
        try (Stream<String> stream = Files.lines(path)) {

            int score = stream.map(x->x.split(";")).map(x-> x[1]).map(Integer::parseInt).mapToInt(v -> v)
                    .max().orElseThrow(NoSuchElementException::new);

            System.out.println(score);
            return score;

        } catch (IOException e) {
            System.out.println("Error: " + e);
            return 0;
        }
    }

}
