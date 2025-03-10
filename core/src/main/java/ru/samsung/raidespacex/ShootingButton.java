package ru.samsung.raidespacex;

public class ShootingButton extends Joystick{

    public ShootingButton(float diameter, boolean side) {
        super(diameter, side);
        y = height;
    }
}
