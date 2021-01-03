package com.kodilla.basketball;

public class ComputerPlayer {

    private Ball ball;
    private Basket basket;
    private int level;
    private double velocity;
    private double angle;

    public ComputerPlayer(int level) {
        this.level = level;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void throwBall() {

    }
}
