require 'observer'

class Employee
	include Observable
	
	attr_reader :name, :address
	attr_reader :salary
	
	def initialize( name, title, salary)
		@name = name
		@title = title
		@salary = salary
	end
	
	def salary=(new_salary)
		@salary = new_salary
		changed # Met a vrai le fait qu'il y a eu un changement, évite de notifier 2 fois le changement
		notify_observers(self)
	end
	
end

