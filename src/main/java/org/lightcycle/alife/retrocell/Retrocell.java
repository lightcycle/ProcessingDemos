/*
 * Click and drag to spawn random cell types.
 * 
 * Each cell genome defines the directions of reproduction, movement, and
 * killing and occur on each step.  The graph on the right shows a comparison
 * of species populations.
 */

package org.lightcycle.alife.retrocell;

import processing.core.PApplet;

public class Retrocell extends PApplet {
	public static void main(String args[])
	{
		PApplet.main(new String[] { "org.lightcycle.alife.retrocell.Retrocell" });
	}
	
	int world[][];
	int stats[];
	int blocksize = 5;
	int sx, sy;
	int graphy = 0;
	int graphx;
	int cellmax;

	int graph_width = 100;

	public void setup() {
		size(499, 399);

		sx = (width - graph_width) / blocksize;
		sy = height / blocksize;

		graphx = width - graph_width;

		world = new int[sx][sy];
		clear_world();

		stats = new int[64];

		noStroke();

		background(255);
	}

	void clear_world() {
		for (int i = 0; i < sx; i++)
			for (int j = 0; j < sy; j++)
				world[i][j] = -1;
	}

	void collect_stats() {
		cellmax = 0;

		for (int i = 0; i < 64; i++)
			stats[i] = 0;

		for (int x = 0; x < sx; x++)
			for (int y = 0; y < sy; y++)
				if (world[x][y] != -1)
					stats[world[x][y]]++;

		for (int i = 0; i < 64; i++)
			if (stats[i] > cellmax)
				cellmax = stats[i];
	}

	void draw_stats() {
		for (int i = 0; i < graph_width; i++)
			set(width - i, graphy, color(255, 255, 255));

		for (int i = 0; i < 64; i++) {
			set(width
					- (int) ((float) stats[i] * (float) graph_width / (float) cellmax),
					graphy, getcolor(i));
		}

		graphy = (graphy + 1) % height;
	}

	public void draw() {
		for (int i = 0; i < 1000; i++)
			process((int) random(sx) % sx, (int) random(sy) % sy);

		for (int x = 0; x < sx; x++)
			for (int y = 0; y < sy; y++)
				if (world[x][y] != -1) {
					fill(getcolor(world[x][y]));
					rect(x * blocksize, y * blocksize, blocksize - 1,
							blocksize - 1);
				} else {
					fill(255);
					rect(x * blocksize, y * blocksize, blocksize - 1,
							blocksize - 1);
				}

		collect_stats();
		draw_stats();
	}

	public void keyPressed() {
		clear_world();
		background(255);
	}

	public void mouseDragged() {
		if (mouseX < width - graph_width) {
			int x, y;
			x = ((mouseX / blocksize) + sx) % sx;
			y = ((mouseY / blocksize) + sy) % sy;

			world[x][y] = (int) random(64) % 64;
		}
	}

	private int getcolor(int a) {
		return color((a % 4) * 70, ((a / 4) % 4) * 70, (a / 16) * 70);
	}

	void process(int x, int y) {
		// toridal coordinates
		x = (x + sx) % sx;
		y = (y + sy) % sy;

		if (world[x][y] == -1)
			return; // if no cell here, return

		// get directions
		int value, move_dir, kill_dir, birth_dir;
		value = world[x][y];
		move_dir = value % 4;
		kill_dir = (value / 4) % 4;
		birth_dir = value / 16;

		int ax = x, ay = y;

		// kill
		switch (kill_dir) {
		case 0:
			ax = x + 1;
			ay = y;
			break;
		case 1:
			ax = x;
			ay = y + 1;
			break;
		case 2:
			ax = x - 1;
			ay = y;
			break;
		case 3:
			ax = x;
			ay = y - 1;
			break;
		}

		ax = (ax + sx) % sx;
		ay = (ay + sy) % sy;

		if (world[ax][ay] != world[x][y])
			world[ax][ay] = -1;

		// move
		switch (move_dir) {
		case 0:
			ax = x + 1;
			ay = y;
			break;
		case 1:
			ax = x;
			ay = y + 1;
			break;
		case 2:
			ax = x - 1;
			ay = y;
			break;
		case 3:
			ax = x;
			ay = y - 1;
			break;
		}

		ax = (ax + sx) % sx;
		ay = (ay + sy) % sy;

		if (world[ax][ay] == -1) {
			world[ax][ay] = world[x][y];
			world[x][y] = -1;
		}

		x = ax;
		y = ay;

		// birth
		switch (birth_dir) {
		case 0:
			ax = x + 1;
			ay = y;
			break;
		case 1:
			ax = x;
			ay = y + 1;
			break;
		case 2:
			ax = x - 1;
			ay = y;
			break;
		case 3:
			ax = x;
			ay = y - 1;
			break;
		}

		ax = (ax + sx) % sx;
		ay = (ay + sy) % sy;

		world[ax][ay] = world[x][y];

	}
}
