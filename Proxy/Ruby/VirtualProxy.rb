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
		s = subject				# renvoie � la cr�ation du compte, il est cr�� s'il n'exsite pas encore.
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
		@subject || (@subject = @creation_block.call) # l'objet est cr�� si ce dernier n'existe pas encore
	end
end

proxy = VirtualAccountProxy.new { BankAccount.new(10) } # Le compte devient acc�ssible par le proxy, mais l'objet n'est pas encore cr��
proxy.deposit(50) # l'objet vient d'�tre cr�� lors de l'appel � cette m�thode
proxy.withdraw(10) # l'objet n'est pas cr�� un deuxi�me fois (voir expression logique de subject)
puts "proxy account : " + proxy.balance.to_s