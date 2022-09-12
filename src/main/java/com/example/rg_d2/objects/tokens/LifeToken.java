package com.example.rg_d2.objects.tokens;

import com.example.rg_d2.objects.MenuBar;

public class LifeToken extends Token {
    public LifeToken(double minX, double minY, double maxX, double maxY, double radius, MenuBar menu) {
        super(minX, minY, maxX, maxY, radius, menu);
        this.points = 1;
    }

    @Override
    public void doAction() {
        this.menu.addLife();

        System.out.println("Got life!");
    }
}