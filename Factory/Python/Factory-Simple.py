#!/usr/bin/env python
# -*- coding: cp1252 -*-

# issu de Thinking in python p86-87
#: c05:shapefact1:ShapeFactory1.py
# A simple static factory method.
from __future__ import generators
import random

class Shape(object):
	# Create based on class name:
	def factory(type):
		if type == "Circle": return Circle()
		if type == "Square": return Square()
		assert 1, "Bad shape creation: " + type
	factory = staticmethod(factory) # Si on met en commentaire cette ligne, on obtient une erreur (pourquoi?) , 
						     # ceci est du au fait que l'on a mis cette  méthode comme une méthode de classe et pas une méthode d'instance
						     # si on veut supprimer cela, il faut faire appel (plus bas) à Shape().factory et pas Shape.factory, 
						     # il faut mettre self dans la signature de factory => factory(self, type)
	
class Circle(Shape):
	def draw(self): print "Circle.draw"
	def erase(self): print "Circle.erase"
		
class Square(Shape):
	def draw(self): print "Square.draw"
	def erase(self): print "Square.erase"

# Generate shape name strings:
def shapeNameGen(n):
	types = Shape.__subclasses__()
	for i in range(n):
		yield random.choice(types).__name__

shapes = [ Shape.factory(i) for i in shapeNameGen(4)]
for shape in shapes:
	shape.draw()
	shape.erase()