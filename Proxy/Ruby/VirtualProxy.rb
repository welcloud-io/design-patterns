class BankAccount
	attr_reader :balance
	def initialize(starting_balance=0)
		@balance = starting_balance
	end
	def deposit(amount)
		@balance += amount
	end
	def withdraw(amount)
		@balance -= amount
	end
end

class VirtualAccountProxy
	def initialize(&creation_block)
		@creation_block = creation_block
	end
	def deposit(amount)
		s = subject				# renvoie à la création du compte, il est créé s'il n'exsite pas encore.
		return s.deposit(amount)
	end
	def withdraw(amount)
		s = subject
		return s.withdraw(amount)
	end
	def balance
		s = subject
		return s.balance
	end
	def subject
		@subject || (@subject = @creation_block.call) # l'objet est créé si ce dernier n'existe pas encore
	end
end

proxy = VirtualAccountProxy.new { BankAccount.new(10) } # Le compte devient accéssible par le proxy, mais l'objet n'est pas encore créé
proxy.deposit(50) # l'objet vient d'être créé lors de l'appel à cette méthode
proxy.withdraw(10) # l'objet n'est pas créé un deuxième fois (voir expression logique de subject)
puts "proxy account : " + proxy.balance.to_s