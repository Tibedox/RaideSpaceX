package ru.samsung.raidespacex;

public class SpaceObject {
    public float x, y;
    public float width, height;
    public float vx, vy;
    public int type;

    public SpaceObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public SpaceObject() {
    }

    public void move(){
        x += vx;
        y += vy;
    }

    public float scrX(){
        return x-width/2;
    }

    public float scrY(){
        return y-height/2;
    }

    public boolean overlap(SpaceObject o){
        return Math.abs(x-o.x) < width/2+o.width/2 && Math.abs(y-o.y) < height/4+o.height/4;
    }
}
