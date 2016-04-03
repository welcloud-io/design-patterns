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


class BankAccountProxy
	def initialize(real_object)
		@real_object = real_object
	end
	def balance
		@real_object.balance
	end
	def deposit(amount)
		@real_object.deposit(amount)
	end
	def withdraw(amount)
		@real_object.withdraw(amount)
	end
end

account = BankAccount.new(100)
account.deposit(50)
account.withdraw(10)
puts "account : " + account.balance.to_s

proxy = BankAccountProxy.new(account) # Le compte devient accéccible par le proxy
proxy.deposit(50)
proxy.withdraw(10)
puts "proxy account : " + proxy.balance.to_s