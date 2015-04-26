package org.lightcycle.generative.valentine2008;

import processing.core.PApplet;
import static processing.core.PApplet.*;

public class JellyfishBezier extends Jellyfish
{
	private int opacity = 128;
	
	boolean fading = false;
	
	public JellyfishBezier(Vec3D position)
	{
		super(position);
		slices = 15;
	}
	
	public void step()
	{
		super.step();
		if (fading && opacity > 0)
		{
			opacity--;
		}
	}
	
	public void startFading()
	{
		fading = true;
	}
	
	public boolean isDead()
	{
		return opacity == 0;
	}
	
	public void draw(PApplet context)
	{
		float px1, py1, px2, py2, px3, py3;
		
		context.stroke(255, 255, 255, opacity);
		context.noFill();
		
		context.pushMatrix();
				
		context.translate(p.x, p.y, p.z);
		context.scale(2);
		
		px1 = (float)Math.sin(propell_step + PI * 0.6F) * 6.0F + 16.0F;
		py1 = 2;
		px2 = (float)Math.sin(propell_step + PI * 0.6F) * 2.0F + 15.0F;
		py2 = (float)Math.sin(propell_step + PI * 0.6F) * 2.0F + 7.0F;
		px3 = -(float)Math.sin(propell_step + PI * 0.6F) * 2.0F + 15.0F;
		py3 = (float)Math.cos(propell_step + PI * 0.6F) * 2.0F + 10.0F;
		
		for (int i = 0; i < slices; i++)
		{
			context.bezier(0F, 0F, 0F, px1, py1, 0F, px2, py2, 0F, px3, py3, 0F);
			context.rotateY(TWO_PI / (float)slices);
		}
		
		context.rotateX(HALF_PI);
		
		context.ellipseMode(RADIUS);
		context.translate(0F, 0F, -py3);
		context.ellipse(0F, 0F, px3, px3);
		
		context.popMatrix();
		
		context.stroke(255, 255, 255, opacity);
		
		for (Tentacle t : tentacles)
		{
			t.draw(context);
		}
	}
}
