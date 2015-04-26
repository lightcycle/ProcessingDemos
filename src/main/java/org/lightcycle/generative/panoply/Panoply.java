/*
 * Draws a growing branch of tendrils and fruit.  Attempt at Flash-style, object-based generative graphics.
 */

package org.lightcycle.generative.panoply;

import processing.core.PApplet;

public class Panoply extends PApplet {
	public static void main(String args[])
	{
		PApplet.main(new String[] { "org.lightcycle.generative.panoply.Panoply" });
	}
	
	Array arcs;
	float decay = 0.9F;

	public void setup() {
		size(400, 400);
		rectMode(CENTER);
		ellipseMode(CENTER);

		arcs = new Array(100);

		smooth();
		
		mousePressed();
	}

	public void draw() {
		pushMatrix();
		// Run Arcs
		for (arcs.pointer_reset(); !arcs.pointer_atend(); arcs.pointer_next()) {
			Arc item = (Arc) arcs.pointer_get();
			if (item != null) {
				if (item.die())
					arcs.pointer_delete();
				else {
					item.step();
					item.draw();
					item.draw2();
				}
			}
		}
		popMatrix();
	}

	public void mousePressed() {
		background(64, 64, 64);
		arcs.insert(new Arc(mouseX, mouseY, 50, random(TWO_PI), (int) pow(-1,
				(int) random(2)), 75));
	}

	class Arc {
		int time, maxtime, growth;
		boolean dead, done;
		float px, py, radius, angle, direction;

		Arc(float px, float py, float radius, float angle, int direction,
				int maxtime) {
			this.px = px;
			this.py = py;
			this.radius = radius;
			this.angle = angle;
			this.direction = direction;
			time = 0;
			this.maxtime = maxtime;
			dead = false;
			done = false;
			growth = 0;
		}

		boolean die() {
			return dead;
		}

		void draw() {
			float step = TWO_PI / (float) maxtime * (float) direction;
			float trans;
			if (!done)
				trans = 255;
			else
				trans = (255 * sin(PI
						/ 2
						+ (PI * ((float) time - (float) growth) / ((float) maxtime - (float) growth))));
			pushMatrix();
			stroke(255, 255, 255, trans);
			noFill();
			translate(px, py);
			rotate(angle);

			for (int i = 0; i < growth; i++) {
				rotate(step);
				point(0, radius - 1);
				point(0, radius + 1);
			}

			popMatrix();
		}

		void draw2() {
			float trans = 96 * sin(PI * (float) time / (float) maxtime);
			pushMatrix();
			noStroke();
			fill(196, 196, 255, (int) trans);
			translate(px, py);
			ellipse(0, 0, radius, radius);
			popMatrix();
		}

		void step() {
			time++;
			if (!done)
				growth++;
			if (time > 3 * maxtime / 4)
				done = true;
			if (random(17) < 1 && !done) {
				float cur_angle = angle
						+ ((float) time * TWO_PI / (float) maxtime * (float) direction);
				float newradius = radius * decay;
				int newmaxtime = (int) ((float) maxtime * decay);
				float nx = px + ((radius + newradius) * sin(-cur_angle));
				float ny = py + ((radius + newradius) * cos(-cur_angle));
				if (newradius > 5) {
					arcs.insert(new Arc(nx, ny, newradius, cur_angle + PI,
							(int) -direction, newmaxtime));
					if (random(3) < 1)
						done = true;
				}
			}
			if (time > maxtime)
				dead = true;
		}
	}

	class Array {
		int n, pointer;
		Object[] array;
		boolean atend;

		Array(int num) {
			n = num;
			array = new Object[num];
			for (int i = 0; i < num; i++)
				array[i] = null;
			pointer_reset();
		}

		boolean insert(Object item) {
			for (int i = 0; i < n; i++)
				if (array[i] == null) {
					array[i] = item;
					return true;
				}
			return false;
		}

		void pointer_reset() {
			pointer = 0;
			atend = false;
		}

		void pointer_next() {
			if (!atend) {
				pointer++;
				while (pointer < n && array[pointer] == null)
					pointer++;
				if (pointer == n)
					atend = true;
			}
		}

		boolean pointer_atend() {
			return atend;
		}

		Object pointer_get() {
			if (!atend)
				return array[pointer];
			else
				return null;
		}

		void pointer_delete() {
			if (!atend)
				array[pointer] = null;
		}
	}
}
