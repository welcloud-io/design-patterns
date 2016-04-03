#encoding: cp1252
# Issu de design pattern in Ruby

# Problématique sans l'utilisation de factory
puts ">>> Sans Factory"

class Duck
	def initialize(name)
		@name = name
	end
	def eat
		puts("Duck #{@name} is eating.")
	end
	def speak
		puts("Duck #{@name} says Quack!")
	end
	def sleep
		puts("Duck #{@name} sleeps quietly.")
	end
end

class Pond
	def initialize(number_ducks)
		@ducks = []
		number_ducks.times do |i|
			duck = Duck.new("Duck#{i}")
			@ducks << duck
		end
	end
	def simulate_one_day
		@ducks.each {|duck| duck.speak}
		@ducks.each {|duck| duck.eat}
		@ducks.each {|duck| duck.sleep}
	end
end

pond = Pond.new(3)
pond.simulate_one_day

# -------- Utilisation du pattern factory '2 type d'animaux avec la même interface à créer : Duck + Frog'
puts ">>> Avec Factory"

class Frog
	def initialize(name)
		@name = name
	end
	def eat
		puts("Frog #{@name} is eating.")
	end
	def speak
		puts("Frog #{@name} says Crooooaaaak!")
	end
	def sleep
		puts("Frog #{@name} doesn't sleep; he croaks all night!")
	end
end

class Pond
	def initialize(number_animals)
		@animals = []
		number_animals.times do |i|
			animal = new_animal("Animal#{i}")
			@animals << animal
		end
	end
	def simulate_one_day
		@animals.each {|animal| animal.speak}
		@animals.each {|animal| animal.eat}
		@animals.each {|animal| animal.sleep}
	end
end

class DuckPond < Pond
	def new_animal(name)
		Duck.new(name)
	end
end

class FrogPond < Pond
	def new_animal(name)
		Frog.new(name)
	end
end

pond = FrogPond.new(3)
pond.simulate_one_day

# Ajout d'un vegetaux : l'inerface n'est pas commune avec les animaux
puts ">>> Avec 2 factories"

class Algae
	def initialize(name)
		@name = name
	end
	def grow
		puts("The Algae #{@name} soaks up the sun and grows")
	end
end
class WaterLily
	def initialize(name)
		@name = name
	end
	def grow
		puts("The water lily #{@name} floats, soaks up the sun, and grows")
	end
end

class Pond
	def initialize(number_animals, number_plants)
		@animals = []
		number_animals.times do |i|
			animal = new_animal("Animal#{i}")
			@animals << animal
		end
		@plants = []
		number_plants.times do |i|
			plant = new_plant("Plant#{i}")
			@plants << plant
		end
	end
	def simulate_one_day
		@plants.each {|plant| plant.grow }
		@animals.each {|animal| animal.speak}
		@animals.each {|animal| animal.eat}
		@animals.each {|animal| animal.sleep}
	end
end

class DuckWaterLilyPond < Pond
	def new_animal(name)
		Duck.new(name)
	end
	def new_plant(name)
		WaterLily.new(name)
	end
end

class FrogAlgaePond < Pond
	def new_animal(name)
		Frog.new(name)
	end
	def new_plant(name)
		Algae.new(name)
	end
end

pond = DuckWaterLilyPond.new(3, 2)
pond.simulate_one_day

# Factory paramétrée
puts ">>> Avec factory paramétrée"
class Pond
	def initialize(number_animals, number_plants)
		@animals = []
		number_animals.times do |i|
			animal = new_organism(:animal, "Animal#{i}")
			# Remplace le constructeur précédent : animal = new_animal("Animal#{i}")
			@animals << animal
		end
		@plants = []
		number_plants.times do |i|
			plant = new_organism(:plant, "Plant#{i}")
			# Remplace le constructeur précédent : plant = new_plant("Plant#{i}")
			@plants << plant
		end
	end
	def simulate_one_day
		@plants.each {|plant| plant.grow }
		@animals.each {|animal| animal.speak}
		@animals.each {|animal| animal.eat}
		@animals.each {|animal| animal.sleep}
	end
end

class DuckWaterLilyPond < Pond
	def new_organism(type, name)
		if type == :animal
			Duck.new(name)
		elsif type == :plant
			WaterLily.new(name)
		else
			raise "Unknown organism type: #{type}"
		end
	end
end

pond = DuckWaterLilyPond.new(3, 2)
pond.simulate_one_day

# Abstract Factory paramétrée
puts ">>> Abstract Factory Phase 1"

class Habitat
	def initialize(number_animals, animal_class, number_plants, plant_class)
		@animal_class = animal_class
		@plant_class = plant_class
		@animals = []
		number_animals.times do |i|
			animal = new_organism(:animal, "Animal#{i}")
			@animals << animal
		end
		@plants = []
		number_plants.times do |i|
			plant = new_organism(:plant, "Plant#{i}")
			@plants << plant
		end
	end
	
	def simulate_one_day
		@plants.each {|plant| plant.grow}
		@animals.each {|animal| animal.speak}
		@animals.each {|animal| animal.eat}
		@animals.each {|animal| animal.sleep}
	end
	
	def new_organism(type, name)
		if type == :animal
			@animal_class.new(name)
		elsif type == :plant
			@plant_class.new(name)
		else
			raise "Unknown organism type: #{type}"
		end
	end
end

class Tree
	def initialize(name)
		@name = name
	end
	def grow
		puts("The tree #{@name} grows tall")
	end
end
class Tiger
	def initialize(name)
		@name = name
	end
	def eat
		puts("Tiger #{@name} eats anything it wants.")
	end
	def speak
		puts("Tiger #{@name} Roars!")
	end
	def sleep
		puts("Tiger #{@name} sleeps anywhere it wants.")
	end
end

jungle = Habitat.new(1, Tiger, 4, Tree)
jungle.simulate_one_day
pond = Habitat.new( 2, Duck, 4, WaterLily)
pond.simulate_one_day

# Abstract Factory paramétrée
puts ">>> Abstract Factory Phase 2"

class PondOrganismFactory
	def new_animal(name)
		Frog.new(name)
	end
	def new_plant(name)
		Algae.new(name)
	end
end

class JungleOrganismFactory
	def new_animal(name)
		Tiger.new(name)
	end
	def new_plant(name)
		Tree.new(name)
	end
end

class Habitat
	def initialize(number_animals, number_plants, organism_factory)
		@organism_factory = organism_factory
		@animals = []
		number_animals.times do |i|
			animal = @organism_factory.new_animal("Animal#{i}")
			@animals << animal
		end
		@plants = []
		number_plants.times do |i|
			plant = @organism_factory.new_plant("Plant#{i}")
			@plants << plant
		end
	end

	def simulate_one_day
		@plants.each {|plant| plant.grow}
		@animals.each {|animal| animal.speak}
		@animals.each {|animal| animal.eat}
		@animals.each {|animal| animal.sleep}
	end
	
	def new_organism(type, name)
		if type == :animal
			@animal_class.new(name)
		elsif type == :plant
			@plant_class.new(name)
		else
			raise "Unknown organism type: #{type}"
		end
	end
end

jungle = Habitat.new(number_animals=1, number_plants=4, JungleOrganismFactory.new)
jungle.simulate_one_day
pond = Habitat.new(number_animals=2, number_plants=4, PondOrganismFactory.new)
pond.simulate_one_day

# Classe are Objects (Again)
puts ">>> Class are objects again"

class OrganismFactory
	def initialize(plant_class, animal_class)
		@plant_class = plant_class
		@animal_class = animal_class
	end
	def new_animal(name)
		@animal_class.new(name)
	end
	def new_plant(name)
		@plant_class.new(name)
	end
end

jungle_organism_factory = OrganismFactory.new(Tree, Tiger)
pond_organism_factory = OrganismFactory.new(WaterLily, Frog)
jungle = Habitat.new(1, 4, jungle_organism_factory)
jungle.simulate_one_day
pond = Habitat.new( 2, 4, pond_organism_factory)
pond.simulate_one_day