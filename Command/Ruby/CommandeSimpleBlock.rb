class Classe
	attr_accessor :command
	
	def initialize(&block)
		@command = block
	end
	#
	# Lots of button drawing and management
	# code omitted...
	#
	def execute_commande
		@command.call if @command  #Le "commande.execute" devient "commande.call"
	end
end

objet = Classe.new do
	puts "Commande exécutée"
end

objet.execute_commande