package ru.samsung.raidespacex;

import static ru.samsung.raidespacex.Main.LEFT;
import static ru.samsung.raidespacex.Main.SCR_WIDTH;

public class ShootingButton extends Joystick{

    public ShootingButton(float diameter, boolean side) {
        super(diameter, side);
        y = height;
    }

    @Override
    public void setSide(boolean side){
        this.side = side;
        x = side == LEFT ? width : SCR_WIDTH-width;
    }
}
