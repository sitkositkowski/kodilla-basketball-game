package com.kodilla.basketball;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Basket {

    private double posXa;
    private double posYa;
    private double posXb;
    private double posYb;
    private Image imageBasket = new Image("file:src/main/resources/basket.png",300,300,true,true);

    public Basket(double posXa, double posYa, double posXb, double posYb) {
        this.posXa = posXa;
        this.posYa = posYa;
        this.posXb = posXb;
        this.posYb = posYb;
    }

    public void draw (GraphicsContext g) {
        g.drawImage(imageBasket, this.posXa-150,-this.posYa-150+600);
    }

    public double getPosXa() {
        return posXa;
    }

    public void setPosXa(double posXa) {
        this.posXa = posXa;
    }

    public double getPosYa() {
        return posYa;
    }

    public void setPosYa(double posYa) {
        this.posYa = posYa;
    }

    public double getPosXb() {
        return posXb;
    }

    public void setPosXb(double posXb) {
        this.posXb = posXb;
    }

    public double getPosYb() {
        return posYb;
    }

    public void setPosYb(double posYb) {
        this.posYb = posYb;
    }

    @Override
    public String toString() {
        return "Basket{" +
                "posXa=" + posXa +
                ", posYa=" + posYa +
                ", posXb=" + posXb +
                ", posYb=" + posYb +
                '}';
    }
}
