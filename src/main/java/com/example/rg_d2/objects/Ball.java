package com.example.rg_d2.objects;

import com.example.rg_d2.Utilities;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

public class Ball extends Circle {
	private Translate position;
	private Point2D speed;
	
	public Ball ( double radius, Translate position, Point2D speed ) {
		super ( radius, Color.RED );
		
		this.position = position;
		this.speed = speed;

		super.getTransforms ( ).addAll ( this.position );
	}

	public double getSpeed(){
		return this.speed.magnitude();
	}

	public boolean update ( double ds, double left, double right, double top, double bottom, double dampFactor, double minBallSpeed ) {
		boolean result = false;
		
		double newX = this.position.getX ( ) + this.speed.getX ( ) * ds;
		double newY = this.position.getY ( ) + this.speed.getY ( ) * ds;
		
		double radius = super.getRadius ( );
		
		double minX = left + radius;
		double maxX = right - radius;
		double minY = top + radius;
		double maxY = bottom - radius;
		
		this.position.setX ( Utilities.clamp ( newX, minX, maxX ) );
		this.position.setY ( Utilities.clamp ( newY, minY, maxY ) );
	
		if ( newX < minX || newX > maxX ) {
			this.speed = new Point2D ( -this.speed.getX ( ), this.speed.getY ( ) );
		}
		
		if ( newY < minY || newY > maxY ) {
			this.speed = new Point2D ( this.speed.getX ( ), -this.speed.getY ( ) );
		}
		
		this.speed = this.speed.multiply ( dampFactor );
		
		double ballSpeed = this.speed.magnitude ( );
		
		if ( ballSpeed < minBallSpeed ) {
			result = true;
		}
		
		return result;
	}
}
