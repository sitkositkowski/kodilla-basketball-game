package com.kodilla.basketball;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Basketball2 extends Application {

    private Canvas canvas;
    private int points;
    private int attempts;
    double[] arrowXY = new double[]{0.0,0.0};
    Ball ball = new Ball(1, 50, 50);
    Basket basket = new Basket(400, 350, 550, 400);
    private Image imageback = new Image("file:src/main/resources/basketballCourt2.png");
    private Image imageLogo = new Image("file:src/main/resources/logo2.png",120,120,true,true);

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
        Scene scene = new Scene(makeCanvas(),900,700);
        stage.setScene(scene);
        stage.setTitle("Basketball game");
        stage.setResizable(false);
        stage.show();

    }

    public void start2(Stage stage) {
        BorderPane root = makeCanvas();
        root.setBottom(makeToolPanel());

        Scene scene = new Scene(root,800,600);
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

    private GridPane makeGrid(Stage stage) {
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
        drawbtn.setPrefSize(100,20);
        drawbtn.setOnAction((e) -> {
            start2(stage);
        });

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        grid.setHgap(5.5);
        grid.setVgap(5.5);

        grid.add(drawbtn, 0, 5);
        grid.add(scrollBarVelocity, 0, 10);
        grid.add(scrollBarAngle, 0, 15);
        grid.setBackground(background);
        return grid;
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
        drawArrow(g,0,0,arrowXY[0], arrowXY[1]);
        for (List<Double> point1: ball.getTrajectory()) {
            if (point1 != null) {
                g.fillOval(point1.get(0), point1.get(1), 1,1);
            }
        }

    }

    public HBox makeToolPanel() {

        ScrollBar scrollBarVelocity = new ScrollBar();
        ScrollBar scrollBarAngle = new ScrollBar();

        scrollBarVelocity.setOrientation(Orientation.HORIZONTAL);
        scrollBarVelocity.setOnDragDetected((event -> {
            System.out.println("v");
            arrowXY = new double[]{50+scrollBarVelocity.getValue()*1.2,550-scrollBarAngle.getValue()};
            paintCanvas();
        }));


        scrollBarAngle.setOrientation(Orientation.HORIZONTAL);
        scrollBarAngle.setOnDragDetected((event -> {
            System.out.println("a");
            arrowXY = new double[]{50+scrollBarVelocity.getValue()*1.2,550-scrollBarAngle.getValue()};

            paintCanvas();
        }));

        Label label = new Label("Velocity:");
        Slider sliderVelocity = new Slider();

        Label labelAngle = new Label("Angle:");
        Slider sliderAngle= new Slider();

        sliderVelocity.setMin(0);
        sliderVelocity.setMax(100);
        sliderVelocity.setValue(80);

        sliderVelocity.setShowTickLabels(true);
        sliderVelocity.setShowTickMarks(true);

        sliderVelocity.setBlockIncrement(10);

        // Adding Listener to value property.
        sliderVelocity.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {

                //System.out.println("New value: " + newValue);
                arrowXY = new double[]{sliderVelocity.getValue(),Math.toRadians(sliderAngle.getValue())};
                System.out.println(sliderVelocity.getValue()+","+Math.toRadians(sliderAngle.getValue())+","+sliderAngle.getValue());
                paintCanvas();
            }
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
        sliderAngle.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {

                //System.out.println("New value: " + newValue);
                arrowXY = new double[]{sliderVelocity.getValue(),Math.toRadians(sliderAngle.getValue())};
                System.out.println(sliderVelocity.getValue()+","+Math.toRadians(sliderAngle.getValue())+","+sliderAngle.getValue());
                paintCanvas();
            }
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
        resetbtn.setOnAction((e) -> reset());

        Hyperlink options[] = new Hyperlink[] {
                new Hyperlink("Sales"),
                new Hyperlink("Marketing"),
                new Hyperlink("Distribution"),
                new Hyperlink("Costs")};
        final ImageView selectedImage = new ImageView();
        selectedImage.setImage(imageLogo);


        vbox.getChildren().addAll(selectedImage);
        vbox.getChildren().add(resetbtn);
        for (int i=0; i<4; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }

        return vbox;
    }

    public void reset (){
        ball.setPosX(50);
        ball.setPosY(50);
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

    void drawArrow(GraphicsContext gc, int x1, int y1, double velocity,double angle) {
        velocity = velocity*2;
        double a = 1 + Math.pow(Math.tan(angle),2);
        double b = -2*x1-2*x1*Math.pow(Math.tan(angle),2);
        double c = Math.pow(x1,2) + Math.pow(x1,2)*Math.tan(angle)-Math.pow(velocity,2);

        double delta = Math.pow(b,2)-4*a*c;
        double root = (-b+Math.sqrt(delta))/(2*a);
        double y = Math.tan(angle)*(root - x1) -y1;

        System.out.println("("+ (Math.cos(angle))+","+(Math.sin(angle))+")");
        double beta = Math.asin(0.15/1.8);
        double K= 0.9 * velocity;


        gc.strokeLine(50, 550, 50 + velocity*Math.cos(angle), 550-velocity*Math.sin(angle));
        gc.strokeLine(50 + velocity*Math.cos(angle), 550-velocity*Math.sin(angle),50+K*Math.cos(angle-beta),550-K*Math.sin(angle-beta));
        gc.strokeLine(50 + velocity*Math.cos(angle), 550-velocity*Math.sin(angle),50+K*Math.cos(angle+beta),550-K*Math.sin(angle+beta));
    }
}
