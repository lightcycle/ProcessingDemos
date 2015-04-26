package org.lightcycle.generative.valentine2008;

import static processing.core.PConstants.PI;
import static processing.core.PConstants.TWO_PI;

import java.util.LinkedList;
import java.util.List;

import processing.core.PApplet;

abstract public class Jellyfish
{
	protected List<Tentacle> tentacles;
	
	protected int slices = 20;
	
	protected int vslices = 12;
	
	protected float friction = 0.98F;
	
	protected float thrust = 0.04F;
	
	protected float propell_speed = 0.2F;
	
	protected int num_tentacles = 5;
	
	protected float tentacles_rad = 12;
	
	protected float tentacles_offsety = 3;
	
	protected float propell_step = 0;
		
	protected boolean propelling = false;
	
	protected Vec3D p, v;

	public Jellyfish(Vec3D position)
	{
		p = position;
		v = new Vec3D(0, 0, 0);
		tentacles = new LinkedList<Tentacle>();
		for (int i = 0; i < num_tentacles; i++)
		{
			tentacles.add(new Tentacle(new Vec3D(tentacles_rad * (float)Math.sin((float)i / (float)num_tentacles * TWO_PI) + p.x, p.y + tentacles_offsety, tentacles_rad * (float)Math.cos((float)i / (float)num_tentacles * TWO_PI) + p.z), 8, 20));
		}
	}

	public void propell()
	{
		propelling = true;
	}
	
	public boolean isDead()
	{
		return false;
	}
	
	public Vec3D getPosition()
	{
		return p;
	}
	
	public void step()
	{
		if (propelling)
		{
			propell_step += propell_speed;
			
			if (propell_step > TWO_PI)
			{
				propelling = false;
				propell_step = 0;
			}
			
			if (propell_step > PI)
			{
				v.addSelf(new Vec3D(0, -1, 0).scale(thrust * propell_step / PI));				
			}
		}
		
		p.addSelf(v);
		
		v.scaleSelf(friction);
		
		for (int i = 0; i < num_tentacles; i++)
		{
			Tentacle t = tentacles.get(i);
			t.setHeadPosition(new Vec3D(tentacles_rad * (float)Math.sin((float)i / (float)num_tentacles * TWO_PI) + p.x, p.y + tentacles_offsety, tentacles_rad * (float)Math.cos((float)i / (float)num_tentacles * TWO_PI) + p.z));
			t.step();
		}
	}
	
	abstract public void draw(PApplet context);
}
