#encoding: cp1252
require 'etc'

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

class BankAccountProtectionProxy
	def initialize(real_account, owner_name)
		@subject = real_account
		@owner_name = owner_name
	end
	def deposit(amount)
		check_access
		return @subject.deposit(amount)
	end
	def withdraw(amount)
		check_access
		return @subject.withdraw(amount)
	end
	def balance
		check_access
		return @subject.balance
	end
	def check_access
		if Etc.getlogin != @owner_name
			raise "Illegal access: #{Etc.getlogin} cannot access account : #{@owner_name}."
		end
	end
end

account = BankAccount.new(100)
begin
	compteAAcceder = "CompteSecret"
	puts "Essai avec le nom : " + compteAAcceder
	proxy = BankAccountProtectionProxy.new(account, compteAAcceder) # Le compte devient accéccible par le proxy
	proxy.deposit(50)
	proxy.withdraw(10)
	puts "#{Etc.getlogin} peut accéder au compte : #{CompteAAcceder}. Solde=" + proxy.balance.to_s
rescue
	puts $!
end

begin
	compteAAcceder = "Olivier"
	puts "Essai avec le nom : " + compteAAcceder
	proxy = BankAccountProtectionProxy.new(account, compteAAcceder) # Le compte devient accéccible par le proxy
	proxy.deposit(50)
	proxy.withdraw(10)
	puts "#{Etc.getlogin} peut accéder au compte : #{compteAAcceder}. Le Solde est de " + proxy.balance.to_s
rescue
	puts $!
end

