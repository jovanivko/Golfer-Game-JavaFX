package com.example.rg_d2;

public class Utilities {
	
	public static double clamp ( double value, double min, double max ) {
		double result = value;
		
		if ( result < min ) {
			result = min;
		}
		if ( result > max ) {
			result = max;
		}
		
		return result;
	}
}
