package ru.samsung.raidespacex;

import static ru.samsung.raidespacex.Main.SCR_HEIGHT;
import static ru.samsung.raidespacex.Main.SCR_WIDTH;

public class Shot extends SpaceObject {
    public Shot(float x, float y) {
        super(x, y);
        width = 50;
        height = 150;
        vy = 15;
    }

    public boolean outOfScreen(){
        return y>SCR_HEIGHT+height/2 || y<-height/2 || x>SCR_WIDTH+width/2 || x<-width/2;
    }
}
