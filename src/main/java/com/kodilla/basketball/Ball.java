package com.kodilla.basketball;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class Ball {

    private double radius;
    private double posX;
    private double posY;
    private double angle;
    private double velocity;
    private List<List<Double>> trajectory;
    private final Image imageBall = new Image("file:src/main/resources/ball.png",100,100,true,true);



    public Ball(double radius, double posX, double posY) {
        this.radius = radius;
        this.posX = posX;
        this.posY = posY;
        this.angle = 0;
        this.velocity = 0;
        this.trajectory = new ArrayList<>(100);
    }

    public boolean throwBall(Basket basket) {
        double h = basket.getPosYa();
        double x;
        double g = 10.0;
        double maxH = Math.pow(this.velocity,2)*Math.pow(Math.sin(this.angle),2)/(2*g);
        if (maxH>basket.getPosYa()) {
            x = Math.pow(Math.pow(this.velocity, 2) * Math.sin(this.angle) * Math.cos(this.angle), 2)
                    - 2 * g * h * Math.pow(this.velocity * Math.cos(this.angle), 2);
            System.out.println(x);
            x = Math.pow(this.velocity, 2) * Math.sin(this.angle) * Math.cos(this.angle)
                    + Math.sqrt(x);
            x = x / g;
            System.out.println(x + "," + basket.getPosXa());
            return (x < basket.getPosXb()) && (x > basket.getPosXa());
        }else{
            return false;
        }
    }

    public void draw(GraphicsContext g) {
        g.drawImage(imageBall, this.posX-50,-this.posY-50+600);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public List<List<Double>> getTrajectory() {
        return trajectory;
    }

    public void setTrajectory(List<List<Double>> trajectory) {
        this.trajectory = trajectory;
    }

    @Override
    public String toString() {
        return "Ball{" +
                "radius=" + radius +
                ", posX=" + posX +
                ", posY=" + posY +
                ", angle=" + angle +
                ", velocity=" + velocity +
                '}';
    }
}
