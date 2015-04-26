/*
 * Pollen Blocks
 * 
 * The probability of a block moving is inversely proportional to how many
 * neighbors it has.
 * 
 * (The only interesting thing here is the Blocks class, which does edge
 * removal on the cubes)
 * 
 * Michael Davis mike [at] lightcycle [dot] org
 */

package org.lightcycle.generative.pollenblocks;

import processing.core.PApplet;

public class PollenBlocks extends PApplet {
	public static void main(String args[])
	{
		PApplet.main(new String[] { "org.lightcycle.generative.pollenblocks.PollenBlocks" });
	}
	
	Blocks b;
	float a = 0, b1 = 0;
	Coord[] cells;
	int n = 250;
	float zoom = 30;

	public void setup() {
		size(600, 600, P3D);
		noFill();
		strokeWeight(2.0F/zoom);
		initialize();
	}

	void initialize() {
		b = new Blocks(10, 10, 10, true);
		cells = new Coord[n];
		for (int i = 0; i < n; i++) {
			int x = (int) random(b.sx);
			int y = (int) random(b.sy);
			int z = (int) random(b.sz);
			cells[i] = new Coord(x, y, z);
			b.set(x, y, z, 1);
		}
	}

	public void mousePressed() {
		initialize();
	}

	public void draw() {
		background(255);
		a += ((float) mouseX / width * PI - PI / 2) * 0.1;
		b1 += ((float) mouseY / height * PI - PI / 2) * 0.1;
		translate(width / 2, height / 2);
		rotateX(b1);
		rotateY(-a);
		scale(zoom);

		for (int h = 0; h < 1000; h++) {
			int i = min(n - 1, (int) random(n));
			Coord c = cells[i];

			int count = 0;
			for (int dz = -1; dz <= 1; dz++)
				for (int dy = -1; dy <= 1; dy++)
					for (int dx = -1; dx <= 1; dx++)
						if ((dx != 0 && dy == 0 && dz == 0)
								|| (dx == 0 && dy != 0 && dz == 0)
								|| (dx == 0 && dy == 0 && dz != 0)) // limiter
							if (b.get(c.x + dx, c.y + dy, c.z + dz) != 0)
								count++;
			if (random(1) > (float) (2 * count) / 6)
				b.move(c, (int) random(3) - 1, (int) random(3) - 1,
						(int) random(3) - 1);
		}

		b.draw();
	}

	class Blocks {
		int sx, sy, sz;
		int[][][] grid;
		boolean wraparound;

		Blocks(int x, int y, int z, boolean wrap) {
			sx = x;
			sy = y;
			sz = z;
			grid = new int[sx][sy][sz];
			wraparound = wrap;
		}

		boolean move(Coord p, int dx, int dy, int dz) {
			int tx = p.x + dx, ty = p.y + dy, tz = p.z + dz;
			if (wraparound) {
				if (p.x < 0)
					p.x = sx - (abs(p.x) % sx);
				else if (p.x > sx - 1)
					p.x = p.x % sx;
				if (p.y < 0)
					p.y = sy - (abs(p.y) % sy);
				else if (p.y > sy - 1)
					p.y = p.y % sy;
				if (p.z < 0)
					p.z = sz - (abs(p.z) % sz);
				else if (p.z > sz - 1)
					p.z = p.z % sz;
				if (tx < 0)
					tx = sx - (abs(tx) % sx);
				else if (tx > sx - 1)
					tx = tx % sx;
				if (ty < 0)
					ty = sy - (abs(ty) % sy);
				else if (ty > sy - 1)
					ty = ty % sy;
				if (tz < 0)
					tz = sz - (abs(tz) % sz);
				else if (tz > sz - 1)
					tz = tz % sz;
			} else {
				if (p.x < 0 || p.x >= sx || p.y < 0 || p.y >= sy || p.z < 0
						|| p.z >= sz || tx < 0 || tx >= sx || ty < 0
						|| ty >= sy || tz < 0 || tz >= sz)
					return false;
			}
			if (get(tx, ty, tz) != 0)
				return false;
			set(tx, ty, tz, get(p.x, p.y, p.z));
			set(p.x, p.y, p.z, 0);
			p.x += dx;
			p.y += dy;
			p.z += dz;
			return true;
		}

		int get(int x, int y, int z) {
			if (wraparound) {
				if (x < 0)
					x = sx - (abs(x) % sx);
				else if (x > sx - 1)
					x = x % sx;
				if (y < 0)
					y = sy - (abs(y) % sy);
				else if (y > sy - 1)
					y = y % sy;
				if (z < 0)
					z = sz - (abs(z) % sz);
				else if (z > sz - 1)
					z = z % sz;
				return grid[x][y][z];
			} else {
				if (x < 0 || x >= sx || y < 0 || y >= sy || z < 0 || z >= sz)
					return 0;
				else
					return grid[x][y][z];
			}
		}

		void set(int x, int y, int z, int c) {
			if (wraparound) {
				while (x < 0)
					x += sx;
				while (x >= sx)
					x -= sx;
				while (y < 0)
					y += sy;
				while (y >= sy)
					y -= sy;
				while (z < 0)
					z += sz;
				while (z >= sz)
					z -= sz;
				grid[x][y][z] = c;
			} else if (x >= 0 && x < sx && y >= 0 && y < sy && z >= 0 && z < sz)
				grid[x][y][z] = c;
		}

		void draw() {
			translate(-(float) sx / 2, -(float) sy / 2, -(float) sz / 2);
			beginShape(LINES);
			for (int z = 0; z < sz; z++)
				for (int y = 0; y < sy; y++)
					for (int x = 0; x < sx; x++) {
						boolean[][][] n = new boolean[3][3][3];
						for (int dz = -1; dz <= 1; dz++)
							for (int dy = -1; dy <= 1; dy++)
								for (int dx = -1; dx <= 1; dx++)
									n[dx + 1][dy + 1][dz + 1] = ((x + dx) >= 0)
											&& ((y + dy) >= 0)
											&& ((z + dz) >= 0)
											&& ((x + dx) < sx)
											&& ((y + dy) < sy)
											&& ((z + dz) < sz)
											&& (grid[x + dx][y + dy][z + dz] != 0);
						if (e(n[1][1][1], n[0][1][1], n[0][0][1], n[1][0][1])) {
							vertex(x, y, z);
							vertex(x, y, z + 1);
						}
						if (e(n[1][1][1], n[1][0][1], n[1][0][0], n[1][1][0])) {
							vertex(x, y, z);
							vertex(x + 1, y, z);
						}
						if (e(n[1][1][1], n[0][1][1], n[0][1][0], n[1][1][0])) {
							vertex(x, y, z);
							vertex(x, y + 1, z);
						}
						if (x == (sx - 1)) {
							if (e(n[1][1][1], n[2][1][1], n[2][0][1],
									n[1][0][1])) {
								vertex(x + 1, y, z);
								vertex(x + 1, y, z + 1);
							}
							if (e(n[1][1][1], n[2][1][1], n[2][1][0],
									n[1][1][0])) {
								vertex(x + 1, y, z);
								vertex(x + 1, y + 1, z);
							}
						}
						if (y == (sy - 1)) {
							if (e(n[1][1][1], n[0][1][1], n[0][2][1],
									n[1][2][1])) {
								vertex(x, y + 1, z);
								vertex(x, y + 1, z + 1);
							}
							if (e(n[1][1][1], n[1][2][1], n[1][2][0],
									n[1][1][0])) {
								vertex(x, y + 1, z);
								vertex(x + 1, y + 1, z);
							}
						}
						if (z == (sz - 1)) {
							if (e(n[1][1][1], n[1][0][1], n[1][0][2],
									n[1][1][2])) {
								vertex(x, y, z + 1);
								vertex(x + 1, y, z + 1);
							}
							if (e(n[1][1][1], n[0][1][1], n[0][1][2],
									n[1][1][2])) {
								vertex(x, y, z + 1);
								vertex(x, y + 1, z + 1);
							}
						}
						if (x == (sx - 1) && y == (sy - 1)) {
							if (e(n[1][1][1], n[2][1][1], n[2][2][1],
									n[1][2][1])) {
								vertex(x + 1, y + 1, z + 1);
								vertex(x + 1, y + 1, z);
							}
						}
						if (x == (sx - 1) && z == (sz - 1)) {
							if (e(n[1][1][1], n[2][1][1], n[2][1][2],
									n[1][1][2])) {
								vertex(x + 1, y + 1, z + 1);
								vertex(x + 1, y, z + 1);
							}
						}
						if (y == (sy - 1) && z == (sz - 1)) {
							if (e(n[1][1][1], n[1][2][1], n[1][2][2],
									n[1][1][2])) {
								vertex(x + 1, y + 1, z + 1);
								vertex(x, y + 1, z + 1);
							}
						}
					}
			endShape();
		}

		boolean xor(boolean a, boolean b) {
			return (a && !b) || (!a && b);
		}

		boolean e(boolean h, boolean u, boolean d, boolean a) {
			if ((h && d) || (a && u))
				stroke(128);
			else
				stroke(0);
			return xor(h, xor(u, xor(d, a))) || (!u && (d || h) && !a)
					|| (!d && (u || a) && !h);
		}
	}

	class Coord {
		Coord(int xx, int yy, int zz) {
			x = xx;
			y = yy;
			z = zz;
		}

		int x, y, z;
	}

}
