/*
 * Each agent has a genome that dictates its movements, which may mutate during
 * reproduction.  Natural selection favors agents that can collect food
 * regularly without depleting the local area.
 */

package org.lightcycle.alife.gridbug;

import java.util.*;

import processing.core.PApplet;

public class GridBug extends PApplet {
	public static void main(String args[]) {
		PApplet.main(new String[] { "org.lightcycle.alife.gridbug.GridBug" });
	}

	int p_length = 12;
	int i_count = 4;
	int init_pop = 100;
	int max_pop = 2000;
	int init_energy = 100;
	int max_age = 2000;
	float squeeze = 2;
	int selected = -1;
	int plant_type = 0;
	int plant_type_count = 5;
	float mutate_prob = 0.001f;
	int size = 4;
	int worldx = 100;
	int worldy = 70;
	int birth_thresh = 500;
	int birth_cost = 200;
	private Monera current;
	boolean[][] plant;
	int init_plants = 1000;
	float plant_prob = 1;
	int plantenergy = 50;
	Vector<Monera> population;

	public void setup()
	{
		size(400, 280);
		background(0);
		stroke(255);
		noFill();

		population = new Vector<Monera>(max_pop);
		plant = new boolean[worldx][worldy];

		populate();
		plant();
	}

	public void draw()
	{
		background(0);

		if (population.size() == 0)
			populate();

		if (random(1) < plant_prob)
			addplant(plant_type);

		stroke(0, 128, 0);

		for (int x = 0; x < worldx; x++)
			for (int y = 0; y < worldy; y++)
				if (plant[x][y])
				{
					line(x * size, y * size, x * size + 2, y * size + 2);
					line(x * size, y * size + 2, x * size + 2, y * size);
				}

		stroke(255);

		for (int i = 0; i < population.size(); i++)
		{
			current = (Monera) population.elementAt(i);
			if (mousePressed && abs(current.x - mouseX / size) < 3
					&& abs(current.y - mouseY / size) < 3)
				selected = i;

			if (i == selected)
				print_Code(current);

			if (current.e <= 0 || current.age > max_age)
			{
				population.remove(i);

				if (selected == i)
					selected = -1;

				else if (selected > i && selected != -1)
					selected--;

				i--;
			}

			else
			{
				if (random(1) < mutate_prob)
					current.mutate();

				rect(current.x * size, current.y * size, 2, 2);

				if (plant[current.x][current.y])
				{
					plant[current.x][current.y] = false;
					current.e += plantenergy;
				}

				if (current.e > birth_thresh
						&& population.size() < population.capacity())
				{
					current.e -= birth_cost;
					Monera a = new Monera();
					a.x = (current.x + (int) random(3) - 1 + worldx) % worldx;
					a.y = (current.y + (int) random(3) - 1 + worldy) % worldy;
					a.d = current.d;
					a.p_counter = current.p_counter;
					for (int p = 0; p < p_length; p++)
						a.program[p] = current.program[p];

					population.insertElementAt(a, 0);

					if (selected != -1)
						selected++;

					i++;
				}

				current.step();
			}
		}
	}

	class Monera
	{
		int p_counter, x, y, d, e, age;
		int[] program;

		Monera()
		{
			x = (int) random(worldx);
			y = (int) random(worldy);
			program = new int[p_length];

			for (int i = 0; i < p_length; i++)
				program[i] = (int) random(i_count);

			p_counter = 0;
			e = init_energy;
			age = 0;
		}

		void step()
		{
			e--;
			age++;
			switch (program[p_counter])
			{
			case 0:
				break;
			case 1:
				d = (d + 1) % 8;
				break;
			case 2:
				d = (d + 7) % 8;
				break;
			case 3:
				switch (d)
				{
				case 0:
					x = (x + 1) % worldx;
					break;
				case 1:
					x = (x + 1) % worldx;
					y = (y + 1) % worldy;
					break;
				case 2:
					y = (y + 1) % worldy;
					break;
				case 3:
					y = (y + 1) % worldy;
					x = (x - 1 + worldx) % worldx;
					break;
				case 4:
					x = (x - 1 + worldx) % worldx;
					break;
				case 5:
					x = (x - 1 + worldx) % worldx;
					y = (y - 1 + worldy) % worldy;
					break;
				case 6:
					y = (y - 1 + worldy) % worldy;
					break;
				case 7:
					x = (x + 1) % worldx;
					y = (y - 1 + worldy) % worldy;
					break;

				}

				break;

			}
			p_counter = (p_counter + 1) % p_length;
		}

		void mutate()
		{
			int i_mutate = (int) random(p_length);
			program[i_mutate] = (int) random(i_count);
		}
	}

	void addplant(int type)
	{
		int px = 0, py = 0;
		int giveup = 100;
		do
		{
			giveup--;
			switch (type)
			{
			case 0:
				px = (int) random(worldx - worldx / squeeze)
						+ (int) (worldx / (squeeze * 2));
				py = (int) random(worldy);
				break;
			case 1:
				if (random(1) < 0.5f)
				{
					px = (int) random(worldx - worldx / squeeze)
							+ (int) (worldx / (squeeze * 2));
					py = (int) random(worldy);
				}
				else
				{
					px = (int) random(worldx);
					py = (int) random(worldy - worldy / squeeze)
							+ (int) (worldy / (squeeze * 2));
				}
				break;
			case 2:
				px = (int) random(worldx - worldx / squeeze)
						+ (int) (worldx / (squeeze * 2));
				py = (int) random(worldy - worldy / squeeze)
						+ (int) (worldy / (squeeze * 2));
				break;
			case 3:
				px = (int) random(worldx);
				py = (int) random(worldy / 4) * 4;
				break;
			default:
				px = (int) random(worldx);
				py = (int) random(worldy);
				break;
			}
		} while (plant[px][py] && giveup > 0);

		plant[px][py] = true;
	}

	void populate()

	{
		population.clear();
		Monera a;
		for (int i = 0; i < init_pop; i++)
		{
			a = new Monera();
			population.add(a);
		}
	}

	void plant()
	{
		for (int x = 0; x < worldx; x++)
			for (int y = 0; y < worldy; y++)
				plant[x][y] = false;
		for (int p = 0; p < init_plants; p++)
			addplant(plant_type);
	}

	public void keyPressed()
	{
		switch (key)
		{
		case ' ':
			populate();
			plant();
			break;
		case 't':
			plant_type = (plant_type + 1) % plant_type_count;
			plant();
			break;
		case '+':
			squeeze += 0.1f;
			plant();
			break;
		case '-':
			if (squeeze > 1.0f)
				squeeze -= 0.1f;
			plant();
			break;
		}
	}

	void print_Code(Monera a)
	{
		StringBuffer code = new StringBuffer(p_length);
		for (int i = 0; i < p_length; i++)
			code.append(a.program[i]);
		pushMatrix();
		scale(1.5f);
		text(code.toString(), a.x * size / 1.5f, a.y * size / 1.5f);
		text(code.toString(), (a.x - worldx) * size / 1.5f, a.y * size / 1.5f);
		text(code.toString(), a.x * size / 1.5f, (a.y + worldy) * size / 1.5f);
		popMatrix();
	}
}