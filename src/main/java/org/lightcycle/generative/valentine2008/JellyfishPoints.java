package org.lightcycle.generative.valentine2008;

import processing.core.PApplet;
import static processing.core.PApplet.*;

public class JellyfishPoints extends Jellyfish
{
	public JellyfishPoints(Vec3D position)
	{
		super(position);
	
		slices = 30;
		vslices = 20;
	}

	public void draw(PApplet context)
	{
		float px1, py1, px2, py2, px3, py3;
		
		context.stroke(255, 255, 255, 128);
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
		
		context.beginShape(POINTS);
		
		for (int i = 0; i < slices; i++)
		{			
			for (int j = 0; j < vslices; j++)
			{
				float t = (float) j/ (float)(vslices - 1);
				float d = context.bezierPoint(0F, px1, px2, px3, t);
				float y = context.bezierPoint(0F, py1, py2, py3, t);
				
				context.point(d * sin((float)i * PApplet.TWO_PI / (float)slices), y, d * cos((float)i * PApplet.TWO_PI / (float)slices));
			}
		}
		context.endShape();
		
		context.popMatrix();
		
		context.stroke(255, 255, 255, 128);
		
		for (Tentacle t : tentacles)
		{
			t.draw(context);
		}
	}
}
