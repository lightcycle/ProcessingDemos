package org.lightcycle.generative.valentine2008;

import processing.core.PApplet;

public class Tentacle
{
	private int num_points;
	
	private float segment_length;
	
	private static final float SPRING = 0.35F;
	
	private static final float FRICTION = 0.1F;
	
	private Vec3D[] p;
	
	private Vec3D[] v;
	
	public Tentacle(Vec3D position, int n, float seg_length)
	{
		num_points = n;
		segment_length = seg_length;
		
		p = new Vec3D[num_points];
		v = new Vec3D[num_points];
		
		for (int i = 0; i < num_points; i++)
		{
			p[i] = new Vec3D(position.x, position.y + (seg_length * i), position.z);
			v[i] = new Vec3D(0, 0, 0);
		}
	}
	
	public void setHeadPosition(Vec3D position)
	{
		p[0] = position;
	}
	
	public void setTailPosition(Vec3D position)
	{
		p[p.length - 1] = position;
	}
	
	public void step()
	{
		for (int i = 1; i < num_points; i++)
		{
			if (segment_length - p[i].distanceTo(p[i - 1]) < 0)
			{
				v[i].addSelf(p[i].sub(p[i - 1]).normalize().scale(SPRING * (segment_length - p[i].distanceTo(p[i - 1]))));
			}
			p[i].addSelf(v[i]);
			v[i].scaleSelf(FRICTION);
			
			// jitter
			p[i].addSelf(new Vec3D(((float)Math.random() - 0.5F) * 0.8F, ((float)Math.random() - 0.5F) * 0.8F, ((float)Math.random() - 0.5F) * 0.8F));
		}
		
		for (int i = 0; i < num_points - 1; i++)
		{
			if (segment_length - p[i].distanceTo(p[i + 1]) < 0)
			{
				v[i].addSelf(p[i].sub(p[i + 1]).normalize().scale(SPRING * (segment_length - p[i].distanceTo(p[i + 1]))));
			}
			p[i].addSelf(v[i]);
			v[i].scaleSelf(FRICTION);
		}
	}
	
	public void draw(PApplet context)
	{
		context.noFill();
		
		context.beginShape();
		context.curveVertex(p[0].x, p[0].y, p[0].z);
		for (int i = 0; i < num_points; i++)
		{
			context.curveVertex(p[i].x, p[i].y, p[i].z);
		}
		context.endShape();
	}
}
