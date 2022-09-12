package com.example.rg_d2.objects.tokens;

import com.example.rg_d2.objects.MenuBar;

public class PointsToken extends Token {
    public PointsToken(double minX, double minY, double maxX, double maxY, double radius, MenuBar menu) {
        super(minX, minY, maxX, maxY, radius, menu);
        this.points = 5;
    }

    @Override
    public void doAction() {
        this.menu.addPoints(points);

        System.out.println("Got points!");
    }
}