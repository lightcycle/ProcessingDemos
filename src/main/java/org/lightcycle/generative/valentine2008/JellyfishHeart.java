package org.lightcycle.generative.valentine2008;

import processing.core.PApplet;
import static processing.core.PApplet.*;

public class JellyfishHeart extends Jellyfish
{
	public JellyfishHeart(Vec3D position)
	{
		super(position);
	}
	
	public void draw(PApplet context)
	{
		float px1, py1, px2, py2, px3, py3;
		
		context.stroke(255, 0, 0, 128);
		context.noFill();
		
		context.pushMatrix();
				
		context.translate(p.x, p.y, p.z);
		context.scale(5);
		
		px1 = (float)Math.sin(propell_step + PI * 0.6F) * 6.0F + 16.0F;
		py1 = 2;
		px2 = (float)Math.sin(propell_step + PI * 0.6F) * 2.0F + 15.0F;
		py2 = (float)Math.sin(propell_step + PI * 0.6F) * 2.0F + 7.0F;
		px3 = -(float)Math.sin(propell_step + PI * 0.6F) * 2.0F + 15.0F;
		py3 = (float)Math.cos(propell_step + PI * 0.6F) * 2.0F + 10.0F;

		for (int i = 0; i < slices; i++)
		{
			float slice_angle = TWO_PI * (float)i / (float)slices;
			float t = (slice_angle < PI)?(slice_angle / PI):(1 - (slice_angle - PI) / PI);			
			float d = context.bezierPoint(50F, 0F, 150F, 50F, t) / (float)150;
			
			context.bezier(0F, 0F, 0F, px1 * d, py1, 0F, px2 * d, py2, 0F, px3 * d, py3, 0F);
			
			context.rotateY(TWO_PI / (float)slices);
		}
		
		context.beginShape();
		for (int i = 0; i < slices; i++)
		{
			float slice_angle = TWO_PI * (float)i / (float)slices;
			float t = (slice_angle < PI)?(slice_angle / PI):(1 - (slice_angle - PI) / PI);			
			float d = context.bezierPoint(50F, 0F, 150F, 50F, t) / (float)150;
			
			context.vertex(px3 * d * cos(slice_angle), py3, px3 * d * sin(slice_angle));
		}
		context.endShape();
		
		context.popMatrix();
		
		for (Tentacle t : tentacles)
		{
			t.draw(context);
		}
	}
}
