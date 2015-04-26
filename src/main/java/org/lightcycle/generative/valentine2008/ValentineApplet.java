package org.lightcycle.generative.valentine2008;

import java.util.LinkedList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

public class ValentineApplet extends PApplet
{
	public static void main(String args[])
	{
		PApplet.main(new String[]{"org.lightcycle.generative.valentine2008.ValentineApplet"});
	}
	
	private List<JellyfishBezier> jellies;
	
	private JellyfishHeartNormal jelly_heart;
	
	private List<Grass> plants;
	
//	private PImage title1, title2, title3, title4;
	
	private final int SCHOOL_SIZE = 15;
	
	private final int NUM_PLANTS = 100;
	
	private float view_angle;
	
	private float view_spin;
	
	private float hand_holding;
	
	private int title_opacity;
	
	private enum DemoState
	{
		INIT,
		JELLY_SCHOOL,
		TRANSITION1,
		JELLY_HEART,
		TRANSITION2,
		FINAL;
	}
	
	DemoState current_state;

	public void setup()
	{
		size(500, 400, OPENGL);
		
//		title1 = loadImage("title1.png");
//		title2 = loadImage("title2.png");
//		title3 = loadImage("title3.png");
//		title4 = loadImage("title4.png");
				
		current_state = DemoState.INIT;
	}
	
	public void draw()
	{
		background(0, 0, 74);
		
		if (current_state == DemoState.INIT)
		{
			jellies = new LinkedList<JellyfishBezier>();
			for (int i = 0; i < SCHOOL_SIZE; i++)
			{
				jellies.add(new JellyfishBezier(new Vec3D(random(500) - 250.0F, random(500) - 150.0F, random(500) - 250.0F)));
			}
			plants = new LinkedList<Grass>();
			for (int i = 0; i < NUM_PLANTS; i++)
			{
				plants.add(new Grass(new Vec3D(random(500) - 250F, 300F, 0F), 6, random(20) + 50));
			}
			jelly_heart = new JellyfishHeartNormal(new Vec3D(0F, 140F, 0F));
			view_angle = 0F;
			view_spin = 0F;
			hand_holding = 0F;
			current_state = DemoState.JELLY_SCHOOL;
			title_opacity = 0;
		}
		
		if (current_state == DemoState.JELLY_SCHOOL || current_state == DemoState.TRANSITION1)
		{
			if (current_state == DemoState.JELLY_SCHOOL && title_opacity < 255)
			{
				title_opacity += 2;
			}
			else if (current_state == DemoState.TRANSITION1 && title_opacity > 0)
			{
				title_opacity -= 2;
			}
			tint(255, title_opacity);
//			image(title1, width/2 - title1.width/2, height - title1.height - 10);
			noTint();
		}
		
		if (current_state == DemoState.JELLY_HEART || current_state == DemoState.TRANSITION2)
		{
			if (current_state == DemoState.JELLY_HEART && title_opacity < 255)
			{
				title_opacity += 2;
			}
			else if (current_state == DemoState.TRANSITION2 && title_opacity > 0)
			{
				title_opacity -= 2;
			}
			tint(255, title_opacity);
//			image(title2, width/2 - title2.width/2, height - title2.height - 10);
			noTint();
		}
		
		if (current_state == DemoState.FINAL)
		{
			if (title_opacity < 255)
			{
				title_opacity += 2;
			}
			tint(255, title_opacity);
//			image(title3, width/2 - title3.width/2, height/2 - title3.height/2 - 15);
//			image(title4, width/2 - title4.width/2 + 150, height/2 - title4.height/2 + 10);
			noTint();
		}
		
		pointLight(255, 255, 255, 0, 0, 0);
		
		if (current_state == DemoState.JELLY_SCHOOL || current_state == DemoState.TRANSITION1)
		{
			translate(width/2, height/2);
			
			rotateZ(HALF_PI / 2F);
			
			if (current_state == DemoState.JELLY_SCHOOL)
			{
				while (jellies.size() < SCHOOL_SIZE)
				{
					jellies.add(new JellyfishBezier(new Vec3D(random(500) - 250.0F, 500F, random(500) - 250.0F)));
				}
			}
			
			for (Jellyfish jelly : jellies)
			{
				if (random(1) < 0.02)
				{
					jelly.propell();
				}
				
				jelly.step();
				jelly.draw(this);			

				if (jelly.getPosition().y < -400F)
				{
					((JellyfishBezier)jelly).startFading();
				}
				
			}
			
			for (int i = 0; i < jellies.size(); i++)
			{
				JellyfishBezier jelly = jellies.get(i);
				
				if (jelly.isDead())
				{
					jellies.remove(i);
					i--;
				}
			}
		}
		
		if (current_state == DemoState.TRANSITION1 && jellies.size() == 0)
		{
			current_state = DemoState.JELLY_HEART;
		}
		
		if (current_state == DemoState.JELLY_HEART || current_state == DemoState.TRANSITION2)
		{
			translate(width/2, height/2);
						
			scale(2F);
			
			rotateX(view_angle);
			rotateY(HALF_PI + view_spin);
			
			jelly_heart.propell();
			
			if (jelly_heart.getPosition().y < -35)
			{
				jelly_heart.getPosition().y = -35;
				view_spin += 0.01;
				if (view_angle > -HALF_PI * 0.5)
				{
					view_angle -= 0.005;
				}
				else
				{
					if (hand_holding < 1)
					{
						hand_holding += 0.003;
					}
					jelly_heart.startHoldingTentacles(hand_holding);
				}
			}
			
			jelly_heart.step();
			
			pushMatrix();
			translate(50, 0);
			jelly_heart.draw(this);
			popMatrix();
			
			pushMatrix();
			rotateY(PI);
			translate(50, 0);
			jelly_heart.draw(this);
			popMatrix();
		}
		
		if (current_state == DemoState.TRANSITION2 && jelly_heart.isDead())
		{
			current_state = DemoState.FINAL;
		}
		
		if (current_state == DemoState.FINAL)
		{
			translate(width/2, height/2);
			
			for (Grass grass : plants)
			{
				grass.step();
				grass.draw(this);
			}
		}
	}
	
	public void mouseClicked()
	{
		if (current_state == DemoState.JELLY_SCHOOL)
		{
			for (JellyfishBezier jelly : jellies)
			{
				jelly.startFading();
			}
			current_state = DemoState.TRANSITION1;
		}
		else if (current_state == DemoState.JELLY_HEART)
		{
			jelly_heart.startFading();
			current_state = DemoState.TRANSITION2;
		}
		else if (current_state == DemoState.FINAL)
		{
			current_state = DemoState.INIT;
		}
	}
}
