package com.kodilla.basketball;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Basket {

    private double posXa;
    private double posYa;
    private double posXb;
    private double posYb;
    private final Image imageBasket = new Image("file:src/main/resources/basket.png",300,300,true,true);

    public Basket(double posXa, double posYa, double posXb, double posYb) {
        this.posXa = posXa;
        this.posYa = posYa;
        this.posXb = posXa+110;
        this.posYb = posYa;
    }

    public void draw2 (GraphicsContext g) {
        g.drawImage(imageBasket, this.posXa-50,-this.posYa-220+600);
    }

    public void draw( GraphicsContext g){
        g.fillRect(this.posXa+50,-this.posYa+600-50,110,2);
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
