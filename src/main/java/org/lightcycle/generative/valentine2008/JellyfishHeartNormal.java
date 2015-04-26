package org.lightcycle.generative.valentine2008;

import processing.core.PApplet;
import static processing.core.PApplet.*;

public class JellyfishHeartNormal extends Jellyfish
{
	private float opacity;
	
	boolean fading = false;
	
	public JellyfishHeartNormal(Vec3D position)
	{
		super(position);
		tentacles_offsety = 5;
		slices = 40;
		
		opacity = 128;
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
	
	public void startHoldingTentacles(float progress)
	{
		for (Tentacle tentacle : tentacles)
		{
			tentacle.setTailPosition(new Vec3D(-75F * progress, 55F, 0F));
		}
	}
	
	public void draw(PApplet context)
	{
		float px1, py1, px2, py2, px3, py3;
		
		context.noStroke();
		context.fill(255, 0, 0, opacity);
		
		context.pushMatrix();
				
		context.translate(p.x, p.y, p.z);
		context.scale(5);
		context.rotateY(HALF_PI);
		
		px1 = (float)Math.sin(propell_step + PI * 0.6F) * 6.0F + 16.0F;
		//py1 = 2;
		py1 = 1;
		px2 = (float)Math.sin(propell_step + PI * 0.6F) * 2.0F + 15.0F;
		//py2 = (float)Math.sin(propell_step + PI * 0.6F) * 2.0F + 7.0F;
		py2 = (float)Math.sin(propell_step + PI * 0.6F) * 1.0F + 3.5F;
		px3 = -(float)Math.sin(propell_step + PI * 0.6F) * 2.0F + 15.0F;
		//py3 = (float)Math.cos(propell_step + PI * 0.6F) * 2.0F + 10.0F;
		py3 = (float)Math.cos(propell_step + PI * 0.6F) * 1.0F + 5.0F;

		context.beginShape(TRIANGLES);
		for (int j = 0; j < vslices - 1; j++)
		{
			float ty1 = (float)j/(float)vslices;
			float ty2 = (float)(j + 1)/(float)vslices;
			
			for (int i = 0; i < slices; i++)
			{
				float slice_angle1 = TWO_PI * (float)i / (float)slices;
				float slice_angle2 = TWO_PI * (float)((i + 1) % slices) / (float)slices;
				float tr1 = (slice_angle1 < PI)?(slice_angle1 / PI):(1 - (slice_angle1 - PI) / PI);			
				float tr2 = (slice_angle2 < PI)?(slice_angle2 / PI):(1 - (slice_angle2 - PI) / PI);			
				float d1 = context.bezierPoint(50F, 0F, 150F, 50F, tr1) / (float)150;
				float d2 = context.bezierPoint(50F, 0F, 150F, 50F, tr2) / (float)150;
				float r1 = context.bezierPoint(0, px1, px2, px3, ty1);
				float r2 = context.bezierPoint(0, px1, px2, px3, ty2);
				float y1 = context.bezierPoint(0, py1, py2, py3, ty1);
				float y2 = context.bezierPoint(0, py1, py2, py3, ty2);
				
				context.vertex(r1 * d1 * cos(slice_angle1), y1, r1 * d1 * sin(slice_angle1));
				context.vertex(r1 * d2 * cos(slice_angle2), y1, r1 * d2 * sin(slice_angle2));
				context.vertex(r2 * d1 * cos(slice_angle1), y2, r2 * d1 * sin(slice_angle1));
				
				context.vertex(r1 * d2 * cos(slice_angle2), y1, r1 * d2 * sin(slice_angle2));
				context.vertex(r2 * d2 * cos(slice_angle2), y2, r2 * d2 * sin(slice_angle2));
				context.vertex(r2 * d1 * cos(slice_angle1), y2, r2 * d1 * sin(slice_angle1));
				
//				context.rotateY(TWO_PI / (float)slices);
			}
		}
		context.endShape();
		
		context.popMatrix();
		
		context.stroke(255, 0, 0, opacity);
		
		for (Tentacle t : tentacles)
		{
			t.draw(context);
		}
	}
}
