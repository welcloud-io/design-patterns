#encoding: cp1252

class Classe
	attr_accessor :command
	def initialize(command)
		@command = command
	end
	#
	# Lots of button drawing and management
	# code omitted...
	#
	def execute_commande
		@command.execute if @command
	end
end

class Commande
	def execute
		puts "Commande exécutée"
	end
end

objet = Classe.new( Commande.new )
objet.execute_commande