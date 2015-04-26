/*
 * Simple swarming behavior
 */

package org.lightcycle.alife.finfin;

import processing.core.PApplet;

public class FinFin extends PApplet {
	public static void main(String args[])
	{
		PApplet.main(new String[] { "org.lightcycle.alife.finfin.FinFin" });
	}
	
	float a = 0;
	int worldsize = 190;
	int num = 200;
	float level = 85;
	int foodsize = 40 * 40 * 40;
	int oid = 0;
	float bounce = 0.5f;

	Creature[] c;
	Splashes s;
	Food f;

	public void setup() {
		size(350, 350, P3D);
		noFill();

		c = new Creature[num];
		seed();
		s = new Splashes(100);
		f = new Food(random(worldsize) - worldsize / 2, random(worldsize)
				- worldsize / 2, random(worldsize) - worldsize / 2, foodsize);

		smooth();
		ellipseMode(CENTER);
		rectMode(CENTER);
	}

	void seed() {
		for (int i = 0; i < num; i++)
			c[i] = new Creature((int) random(worldsize) - worldsize / 2,
					(int) random(worldsize) - worldsize / 2,
					(int) random(worldsize) - worldsize / 2);
	}

	public void mousePressed() {
		seed();
	}

	public void draw() {
		a += 0.01f;
		background(255);
		translate(width / 2, height / 2);
		rotateY(a);
		stroke(64, 64, 64);
		noFill();
		pushMatrix();
		translate(0, worldsize / 2, 0);
		rotateX(PI / 2);
		rect(0, 0, worldsize, worldsize);
		popMatrix();
		stroke(128, 128, 172);
		fill(128, 128, 172, 64);
		pushMatrix();
		translate(0, level, 0);
		rotateX(PI / 2);
		rect(0, 0, worldsize, worldsize);
		popMatrix();
		stroke(0);
		for (int i = 0; i < num; i++) {
			c[i].step();
			c[i].draw();
		}
		stroke(128, 128, 172, 128);
		noFill();
		s.step();
		s.draw();
		if (f.amount < 1000)
			f = new Food(random(worldsize) - worldsize / 2, random(worldsize)
					- worldsize / 2, random(worldsize) - worldsize / 2,
					foodsize);
		f.draw();
	}

	class Creature {
		float goalx, goaly, goalz;
		int goalvalid;
		int id;
		float ang1, ang2;
		Vector3 p, v, a;
		int time_to_next_splash;

		Creature(float x, float y, float z) {
			goalvalid = 0;

			p = new Vector3(x, y, z);
			v = new Vector3(0, 0, 0);
			a = new Vector3(0, 0.2f, 0);

			ang1 = random(TWO_PI);
			ang2 = random(TWO_PI);

			this.id = oid;
			oid++;
		}

		void step() {
			// steer towards goal
			if (goalvalid > 0) {
				a.x = (-p.x + goalx);
				a.y = (-p.y + goaly);
				a.z = (-p.z + goalz);
				float d = sqrt(sq(a.x) + sq(a.y) + sq(a.z));
				if (d > 0) {
					a.x = a.x / d / 4;
					a.y = a.y / d / 4;
					a.z = a.z / d / 4;
				}
				goalvalid--;
			} else {
				turnpitch(random(PI / 10.0f) - PI / 20.0f);
				turnrot(random(PI / 10.0f) - PI / 20.0f);
				impulse(0.15f);
			}

			// gravity & bouyency
			Vector3 env = new Vector3(0, 0, 0);
			if (p.y < level)
				env.y = 0.1f;
			else
				env.y = -0.1f;

			a = add(a, env);

			// update velocity and position
			v = add(v, a);
			p = add(p, v);

			// water drag
			if (p.y > level) {
				v.x *= 0.96f;
				v.y *= 0.96f;
				v.z *= 0.96f;
			}

			// food drag
			if (f.amount > 0
					&& (abs(p.x - f.x) < pow(f.amount, 1.0f / 3.0f) / 2)
					&& (abs(p.y - f.y) < pow(f.amount, 1.0f / 3.0f) / 2)
					&& (abs(p.z - f.z) < pow(f.amount, 1.0f / 3.0f) / 2)) {
				v.x *= 0.5f;
				v.y *= 0.5f;
				v.z *= 0.5f;
				f.amount -= 10;

				for (int i = 0; i < 4; i++) {
					int j = min((int) random(num), num - 1);
					c[j].goalvalid = 50;
					c[j].goalx = p.x;
					c[j].goaly = p.y;
					c[j].goalz = p.z;
				}
			}

			a.x = 0;
			a.y = 0;
			a.z = 0;

			// bounce off of walls
			if (abs(p.x) >= worldsize / 2)
				v.x = -v.x * bounce;
			if (abs(p.y) >= worldsize / 2)
				v.y = -v.y * bounce;
			if (abs(p.z) >= worldsize / 2)
				v.z = -v.z * bounce;

			// bounded position
			if (p.x < -worldsize / 2)
				p.x = -worldsize / 2;
			if (p.x > worldsize / 2)
				p.x = worldsize / 2;
			if (p.y < -worldsize / 2)
				p.y = -worldsize / 2;
			if (p.y > worldsize / 2)
				p.y = worldsize / 2;
			if (p.z < -worldsize / 2)
				p.z = -worldsize / 2;
			if (p.z > worldsize / 2)
				p.z = worldsize / 2;

			// generate splashes
			if (time_to_next_splash > 0)
				time_to_next_splash--;
			if (abs(p.y - level) < abs(v.y) && time_to_next_splash == 0) {
				time_to_next_splash = 5;
				s.addSplash(p.x, p.z);
			}
		}

		void impulse(float i) {
			Vector3 imp = new Vector3(0, i, 0);
			imp = rotY(rotX(imp, ang1), ang2);
			a = add(a, imp);
		}

		void turnpitch(float da) {
			ang1 += da;
		}

		void turnrot(float da) {
			ang2 += da;
		}

		void draw() {
			Vector3 tail = add(minus(v), p);
			beginShape(LINE_STRIP);
			vertex(p.x, p.y, p.z);
			vertex(tail.x, tail.y, tail.z);
			endShape();
		}
	}

	class Vector3 {
		float x, y, z;

		Vector3(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	Vector3 add(Vector3 a, Vector3 b) {
		Vector3 r = new Vector3(a.x, a.y, a.z);
		r.x += b.x;
		r.y += b.y;
		r.z += b.z;
		return r;
	}

	Vector3 minus(Vector3 a) {
		Vector3 r = new Vector3(-a.x, -a.y, -a.z);
		return r;
	}

	class Splash {
		int age;
		float x, y;

		Splash(float x, float y) {
			this.x = x;
			this.y = y;
			age = 0;
		}

		void step() {
			age += 15;
		}

		void draw() {
			ellipse(x, y, age, age);
		}
	}

	class Splashes {
		Splash[] s;
		int sn;

		Splashes(int n) {
			sn = n;
			s = new Splash[sn];
			for (int i = 0; i < sn; i++) {
				s[i] = null;
			}
		}

		void addSplash(float x, float y) {
			int slot;
			for (slot = 0; slot < sn && s[slot] != null; slot++) {
			}
			if (slot == sn)
				return;
			s[slot] = new Splash(x, y);
		}

		void step() {
			for (int i = 0; i < sn; i++) {
				if (s[i] != null) {
					s[i].age++;
					if (s[i].age > 30)
						s[i] = null;
				}
			}
		}

		void draw() {
			pushMatrix();

			rotateZ(PI);
			rotateY(PI);
			rotateX(-PI / 2);
			translate(0, 0, -level);
			for (int i = 0; i < sn; i++)
				if (s[i] != null)
					s[i].draw();
			popMatrix();
		}
	}

	Vector3 rotX(Vector3 v, float a) {
		Vector3 r = new Vector3(v.x, v.y * cos(a) - v.z * sin(a), v.y * sin(a)
				+ v.z * cos(a));
		return r;
	}

	Vector3 rotY(Vector3 v, float a) {
		Vector3 r = new Vector3(v.z * sin(a) + v.x * cos(a), v.y, v.z * cos(a)
				- v.x * sin(a));
		return r;
	}

	Vector3 rotZ(Vector3 v, float a) {
		Vector3 r = new Vector3(v.x * cos(a) - v.y * sin(a), v.x * sin(a) + v.y
				* cos(a), v.z);
		return r;
	}

	private class Food {
		float x, y, z;
		int amount;

		Food(float x, float y, float z, int amt) {
			amount = amt;
			this.x = max(min(x, worldsize / 2 - pow(amount, 1.0f / 3.0f)),
					-worldsize / 2 + pow(amount, 1.0f / 3.0f));
			this.y = max(min(y, worldsize / 2 - pow(amount, 1.0f / 3.0f)),
					-worldsize / 2 + pow(amount, 1.0f / 3.0f));
			this.z = max(min(z, worldsize / 2 - pow(amount, 1.0f / 3.0f)),
					-worldsize / 2 + pow(amount, 1.0f / 3.0f));
		}

		void draw() {
			stroke(0, 172, 0);
			fill(0, 172, 0, 50);
			pushMatrix();
			translate(x, y, z);
			box(pow(amount, 1.0f / 3.0f), pow(amount, 1.0f / 3.0f),
					pow(amount, 1.0f / 3.0f));
			popMatrix();
		}
	}
}