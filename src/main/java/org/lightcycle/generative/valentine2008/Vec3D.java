package org.lightcycle.generative.valentine2008;

import processing.core.PApplet;

public class Vec3D {
	public float x, y, z;
	
	public Vec3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void addSelf(Vec3D v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}

	public void scaleSelf(float scale) {
		x *= scale;
		y *= scale;
		z *= scale;
	}

	public Vec3D scale(float scale) {
		return new Vec3D(x * scale, y * scale, z * scale);
	}

	public float distanceTo(Vec3D v) {
		return (float)Math.sqrt((x - v.x) * (x - v.x) + (y - v.y) * (y - v.y) + (z - v.z) * (z - v.z));
	}

	public Vec3D sub(Vec3D v) {
		return new Vec3D(x - v.x, y - v.y, z - v.z);
	}

	public Vec3D normalize() {
		float magnitude = (float)Math.sqrt(x * x + y * y + z * z);
		return this.scale(1.0F/magnitude);
	}
}
