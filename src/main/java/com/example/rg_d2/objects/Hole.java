package com.example.rg_d2.objects;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;

public class Hole extends Circle {
	private int points;
	private Translate position;
	public Hole ( double radius, Translate position , int points, RadialGradient gradient) {
		super ( radius);
		this.points = points;
		this.position = position;
		super.setFill(gradient);

		super.getTransforms ( ).addAll ( position );
	}
	public double getX(){
		return this.position.getX();
	}
	public double getY(){
		return this.position.getY();
	}
	public int getPoints(){
		return this.points;
	}

	public boolean handleCollision ( Circle ball ) {
		Bounds ballBounds = ball.getBoundsInParent ( );
		
		double ballX      = ballBounds.getCenterX ( );
		double ballY      = ballBounds.getCenterY ( );
		double ballRadius = ball.getRadius ( );
		
		Bounds holeBounds = super.getBoundsInParent ( );
		
		double holeX      = holeBounds.getCenterX ( );
		double holeY      = holeBounds.getCenterY ( );
		double holeRadius = super.getRadius ( );
		
		double distanceX = holeX - ballX;
		double distanceY = holeY - ballY;
		
		double distanceSquared = distanceX * distanceX + distanceY * distanceY;
		
		boolean result = distanceSquared < ( holeRadius * holeRadius );
		
		return result;
	};
}
