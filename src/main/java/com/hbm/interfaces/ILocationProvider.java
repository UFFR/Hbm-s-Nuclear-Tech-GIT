package com.hbm.interfaces;

public interface ILocationProvider
{
	public double getX();
	public double getY();
	public double getZ();
	public double[] getCoordDouble();
	public int[] getCoordInt();
}
