# ProcessingDemos
Generative graphics and artificial demos made using Processing.

Most of these were written long ago in Processing's integrated IDE, and have now been modified to work as part of a Maven project.  The code could use some serious cleanup, but there are some fun little demos in here.

## Pollen

Each cell follows the same rules, attempting to move in a direction determined by which neighboring spaces are occupied.  Cells run in random order to prevent preference.  This example shows shifting aggregation patterns in a toroidal grid.  Changes to the rules often yield interesting new behaviors.

![Alt text](http://lightcycle.github.io/screenshots/Pollen.png "Pollen Screenshot")

## Valentine 2008

Generative animation for Valentine's Day 2008.  Click to cycle through three scenes: swimming wireframe jellyfish, two solid heart-shaped jellyfish that join tentacles, and jiggling sea grass.  Poem deleted.

![Alt text](http://lightcycle.github.io/screenshots/Valentine2008.png "Valentine 2008 Screenshot")

## FinFin

Simple swarming behavior towards a food source, with gratuitous water ripple animations.

![Alt text](http://lightcycle.github.io/screenshots/FinFin.png "FinFin Screenshot")

## PollenBlocks

Cells move randomly, sticking to one another to form an aggregation.  Internal edges of the aggregation are identified and excluded from rendering.

![Alt text](http://lightcycle.github.io/screenshots/PollenBlocks.png "PollenBlocks Screenshot")

## TreeFold

Sort of a psuedo Lindenmayer system, where a recursively read rule string is rendered using a graphics turtle.

![Alt text](http://lightcycle.github.io/screenshots/TreeFold.png "TreeFold Screenshot")

## Retrocell

Each agent moves, kills, and reproduces in a set direction.  These three directions are the genome of each agent, and also determine its color.

![Alt text](http://lightcycle.github.io/screenshots/Retrocell.png "Retrocell Screenshot")

## GridBug

Agents move according to the rules that make up their genome.  Those that fail to collect enough food die out, while those that collect enough food reproduce.  Offspring are sometimes mutated.  Natural selection favors agents that can regularly collect food without depleting their local area. 

![Alt text](http://lightcycle.github.io/screenshots/GridBug.png "GridBug Screenshot")

## Panoply

Little demo of Flash-style animation with objects that change and propagate themselves.

![Alt text](http://lightcycle.github.io/screenshots/Panoply.png "Panoply Screenshot")
