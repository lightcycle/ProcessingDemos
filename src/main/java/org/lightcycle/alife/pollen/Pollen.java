package org.lightcycle.alife.pollen;

import java.util.Random;

import processing.core.PApplet;

public class Pollen extends PApplet
{
	public static void main(String args[])
	{
		PApplet.main(new String[] { "org.lightcycle.alife.pollen.Pollen" });
	}
	
	private static final int NUMCELLS = 10000;
	private static final int STEPS_PER_LOOP = 3;
	private int SPORE_COLOR = color(172, 255, 128);
	private int EMPTY_COLOR = color(0, 0, 0);
	private Cell[] cells = new Cell[NUMCELLS];
	private Random random = new Random();

	public void setup()
	{
		size(300, 600);
		stroke(255);
		clearscr();
		seed();
	}

	private void seed()
	{
		// Add a bunch of cells at random places 
		for (int i = 0; i < NUMCELLS;)
		{
			int cX = random.nextInt(width);
			int cY = random.nextInt(height);
			if (getpix(cX, cY) == EMPTY_COLOR)
			{
				setpix(cX, cY, SPORE_COLOR);
				cells[i] = new Cell(cX, cY);
				i++;
			}
		}
	}

	public void draw()
	{
		for (int i = 0; i < STEPS_PER_LOOP; i++)
		{
			for (int j = 0; j < NUMCELLS; j++)
			{
				cells[j].run();				
			}
		} 
	}

	private void clearscr()
	{
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				set(x, y, EMPTY_COLOR);
			}
		}
	}

	public void mousePressed()
	{
		setup();
	}

	class Cell
	{
		int x, y;

		Cell(int xin, int yin)
		{
			x = xin;
			y = yin;
		}

		// Perform action based on surroundings 
		void run()
		{
			// Fix cell coordinates 
			while (x < 0)
				x += width;
			while (x > width - 1)
				x -= width;
			while (y < 0)
				y += height;
			while (y > height - 1)
				y -= height;

			// Cell instructions

			int[] actions = new int[100];
			int current_action = 0;

			if (getpix(x - 1, y + 1) == EMPTY_COLOR
					&& getpix(x + 1, y + 1) == EMPTY_COLOR
					&& getpix(x, y + 1) == EMPTY_COLOR)
			{
				actions[current_action] = 4;
				current_action++;
			}
			
			if (getpix(x - 1, y - 1) == EMPTY_COLOR
					&& getpix(x + 1, y - 1) == EMPTY_COLOR
					&& getpix(x, y - 1) == EMPTY_COLOR)
			{
				actions[current_action] = 5;
				current_action++;
				actions[current_action] = 7;
				current_action++;
			}

			if (current_action != 0)
			{
				int action = actions[min(random.nextInt(current_action),
						current_action - 1)];
				switch (action)
				{
				case 0:
					move(0, -1);
					break;
				case 1:
					move(1, -1);
					break;
				case 2:
					move(1, 0);
					break;
				case 3:
					move(1, 1);
					break;
				case 4:
					move(0, 1);
					break;
				case 5:
					move(-1, 1);
					break;
				case 6:
					move(-1, 0);
					break;
				case 7:
					move(-1, -1);
					break;
				}
			}
		}

		// Will move the cell (dx, dy) units if that space is empty 
		void move(int dx, int dy)
		{
			if (getpix(x + dx, y + dy) == EMPTY_COLOR)
			{
				setpix(x + dx, y + dy, getpix(x, y));
				setpix(x, y, EMPTY_COLOR);
				x += dx;
				y += dy;
			}
		}
	}

	void setpix(int x, int y, int c)
	{
		while (x < 0)
			x += width;
		while (x > width - 1)
			x -= width;
		while (y < 0)
			y += height;
		while (y > height - 1)
			y -= height;
		set(x, y, c);
	}

	int getpix(int x, int y)
	{
		while (x < 0)
			x += width;
		while (x > width - 1)
			x -= width;
		while (y < 0)
			y += height;
		while (y > height - 1)
			y -= height;
		return get(x, y);
	}
}
