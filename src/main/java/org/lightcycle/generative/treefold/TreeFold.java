/*  Program: TreeFold

     Author: Mike Davis
             mike@lightcycle.org
             http://www.lightcycle.org

   Synopsis: This program illustrates a method of describing recursive
             structures with strings of simple commands.
             
             Drawing is performed by a "turtle" -- a cursor that leaves lines
             and curves as it moves.  How it moves is defined by its current
             state, and the turtle also has a stack of remembered states.
             
             The commands (with turtle state variables in quotes):
             
             f   Move "length" units in direction "angle"
             F   Move "length" units in direction "angle" and leave a line
             -   Turn "turn" degrees left
             +   Turn "turn" degrees right
             C   Move "length" units in direction "angle", turn "turn2" degrees,
                 move "length2" units in direction "angle", and leave a bezier
                 curve that passes through these points.
             [   Push turtle's current state
             ]   Pop turtle's previous state
             R   Recur.  Push the current string position onto a stack and jump
                 back to the beginning of the string.  If the stack is larger than
                 the iter parameter, ignore the R.  At the end of the string, if
                 there are positions left on the stack, pop one and go to that
                 position.
 */

package org.lightcycle.generative.treefold;

import java.util.Stack;

import processing.core.PApplet;

public class TreeFold extends PApplet {
	public static void main(String args[]) {
		PApplet.main(new String[] { "org.lightcycle.generative.treefold.TreeFold" });
	}

	int num = 4; // number of example structures
	int cur = 0; // index of the currently displayed example

	String[] l_string = new String[num];
	int[] l_iter = new int[num];
	State current = new State();
	Stack<State> states = new Stack<State>();

	// Parameters for interaction with the structure
	float speed = 0.25F; // controls responsiveness of controls, must be between 0
						// and 1
	float ta = 0, tb = 0;
	float a = 0, b = 0;

	boolean changePending = false;

	public void setup() {
		size(400, 300);
		stroke(170, 255, 128);
		noFill();

		// The definitions of example structures
		l_string[0] = new String("[-R]RCC[+R]");
		l_iter[0] = 6;
		l_string[1] = new String("FFFF[-CR][+RFFFF]");
		l_iter[1] = 8;
		l_string[2] = new String("FCR[-FR]RF");
		l_iter[2] = 5;
		l_string[3] = new String("CRC[+R]");
		l_iter[3] = 8;
	}

	public void draw() {
		background(64);
		
		// Find target parameter settings
		ta = (float) mouseX / width;
		tb = (float) mouseY / height;

		// Interpolate towards target parameters (for smooth transitions)
		a += speed * (ta - a);
		b += speed * (tb - b);

		// Setup initial drawing state
		current.x = width / 2;
		current.y = height / 2;
		current.angle = 0;
		current.length = 3;
		current.turn = a * 360;
		current.length2 = 3;
		current.turn2 = b * 360 + 180;

		// Process string into a structure (see docs for details)
		Place place = new Place(0);
		Stack<Place> places = new Stack<Place>();
		boolean done = false;

		while (!done) {
			switch (l_string[cur].charAt(place.item)) {
			case '[':
				State item = new State(current);
				states.push(item);
				break;
			case ']':
				if (!states.empty())
					current = new State((State) states.pop());
				break;
			case '-':
				current.angle -= current.turn;
				break;
			case '+':
				current.angle += current.turn;
				break;
			case 'f':
				current.x += current.length * sin(radians(current.angle));
				current.y += current.length * cos(radians(current.angle));
				break;
			case 'F':
				int ox = (int) current.x;
				int oy = (int) current.y;
				current.x += current.length * sin(radians(current.angle));
				current.y += current.length * cos(radians(current.angle));
				line(ox, oy, (int) current.x, (int) current.y);
				break;
			case 'C':
				ox = (int) current.x;
				oy = (int) current.y;
				current.x += current.length * sin(radians(current.angle));
				current.y += current.length * cos(radians(current.angle));
				int ox2 = (int) current.x;
				int oy2 = (int) current.y;
				current.angle += current.turn2;
				current.x += current.length2 * sin(radians(current.angle));
				current.y += current.length2 * cos(radians(current.angle));
				bezier(ox, oy, ox2, oy2, ox2, oy2, (int) current.x, (int) current.y);
				break;
			case 'R':
				if (places.size() < l_iter[cur]) {
					places.push(place);
					place = new Place(-1);
				}
				break;
			}
			place.item++; // Move to the next command
			if (place.item >= l_string[cur].length()) // We're at the end of the
														// string
			{
				if (places.empty()) // If the places stack is empty, we're
									// finished drawing
					done = true;
				else // Otherwise, pop from places and jump to that place
				{
					place = places.pop();
					place.item++;
				}
			}
		}

		// Mouse was clicked at some point, now it's safe to change to the next
		// structure
		if (changePending) {
			cur = (cur + 1) % num;
			changePending = false;
		}
	}

	class State {
		State() {
		}

		State(State s) {
			this.x = s.x;
			this.y = s.y;
			this.angle = s.angle;
			this.length = s.length;
			this.turn = s.turn;
			this.length2 = s.length2;
			this.turn2 = s.turn2;
		}

		float x, y, angle, length, turn, length2, turn2;
	}

	class Place {
		Place(int a) {
			item = a;
		}

		int item;
	}

	public void mousePressed() {
		changePending = true;
	}
}