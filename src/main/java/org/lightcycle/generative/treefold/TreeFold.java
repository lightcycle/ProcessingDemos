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
             (   Begin a curve.
             .   Place a curve vertex.
             )   End the curve.
 */

package org.lightcycle.generative.treefold;

import java.util.Stack;

import processing.core.PApplet;

public class TreeFold extends PApplet {
	public static void main(String args[]) {
		PApplet.main(new String[] { "org.lightcycle.generative.treefold.TreeFold" });
	}
	
	boolean switchexample = false;
	int num = 1; // number of example structures
	int cur = 0; // index of the currently displayed example

	String[] l_string = new String[num];
	int[] l_iter = new int[num];
	State current = new State();
	Stack<State> states = new Stack<State>();

	// Parameters for interaction with the structure

	float ta = 0, tb = 0;
	float a = 0, b = 0;	

	public void setup() {
		
		background(255);

		size(600, 600);
		stroke(255, 255, 255, 172);
		noFill();

		// The definitions of example structures
		l_string[0] = new String("CF[+R][-R]");
		l_iter[0] = 8;
	}

	public void draw() {
		background(0, 0, 0);
		
		// Center
		translate(width / 2, height / 2);
		
		// Find target parameter settings
		ta = ((float) mouseX / (float) width - 0.5F) * TWO_PI;
		tb = ((float) mouseY / (float) height - 0.5F) * TWO_PI;

		// Interpolate towards target parameters (for smooth transitions)
		a += (ta - a) * 0.5;
		b += (tb - b) * 0.5;

		// Setup initial drawing state
		current.x = 0;
		current.y = 0;
		current.angle = 0;
		current.length = 15;
		current.turn = a * TWO_PI;
		current.turn2 = current.turn;

		// Process string into a structure
		Place place = new Place(0);
		Stack<Place> places = new Stack<Place>();
		boolean done = false;

		while (!done && place.item < l_string[cur].length()) {
			switch (l_string[cur].charAt(place.item)) {
			case 'o':
				current.turn += TWO_PI / 24F;
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
				current.x += current.length * sin((float)current.angle);
				current.y += current.length * cos((float)current.angle);
				break;
			case 'F':
				int ox = (int) current.x;
				int oy = (int) current.y;
				current.x += current.length * sin((float)current.angle);
				current.y += current.length * cos((float)current.angle);
				line(ox, oy, (int) current.x, (int) current.y);
				break;
			case 'C':
				ox = (int) current.x;
				oy = (int) current.y;
				
				current.x += current.length * sin((float)current.angle);
				current.y += current.length * cos((float)current.angle);
				int ox2 = (int) current.x;
				int oy2 = (int) current.y;
				
				current.angle += current.turn;
				current.x += current.length * sin((float)current.angle);
				current.y += current.length * cos((float)current.angle);
				int ox3 = (int) current.x;
				int oy3 = (int) current.y;
				
				current.angle += current.turn2;
				current.x += current.length * sin((float)current.angle);
				current.y += current.length * cos((float)current.angle);
				bezier(ox, oy, ox2, oy2, ox3, oy3, (int) current.x,
						(int) current.y);
				break;
			case '(':
				beginShape();
				break;
			case '.':
				curveVertex((float)current.x, (float)current.y);
				break;
			case ')':
				endShape();
				break;
			case 'R':
				if (places.size() < l_iter[cur]) {
					places.push(place);
					place = new Place(-1);
				}
				break;
			}
			place.item++; // Move to the next command
			if (place.item >= l_string[cur].length()) // We're at the end of
														// the string
			{
				if (places.empty()) // If the places stack is empty, we're
									// finished drawing
					done = true;
				else // Otherwise, pop from places and jump to that place
				{
					place = (Place) places.pop();
					place.item++;
				}
			}
		}
		
		if (switchexample) {
			switchexample = false;
			cur = (cur + 1) % num;
		}
	}

	public void mousePressed() {
		switchexample = true;
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
			this.turn2 = s.turn2;
		}

		double x, y, angle, length, turn, turn2;
	}
}

class Place {
	Place(int a) {
		item = a;
	}

	int item;
}