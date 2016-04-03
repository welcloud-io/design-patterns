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

class AccountProxy
	def initialize(real_account)
		@subject = real_account
	end
	def method_missing(name, *args) 	#cette m�thode est une m�thode surcharg�e h�rit� de object. 
								# Quand une methode est appel�e sur le proxy est qu'elle est inexistante, c'est celle ci qui est appel�e
		puts("Delegating #{name} message to subject.")
		@subject.send(name, *args)
	end
end

ap = AccountProxy.new( BankAccount.new(100) )
ap.deposit(25)
ap.withdraw(50)
puts("account balance is now: #{ap.balance}")