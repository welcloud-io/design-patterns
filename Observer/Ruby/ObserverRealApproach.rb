class Payroll
	def update( changed_employee )
		puts("Cut a new check for #{changed_employee.name}!")
		puts("His salary is now #{changed_employee.salary}!")
	end
end

class TaxMan
	def update( changed_employee )
		puts("Send #{changed_employee.name} a new tax bill!")
	end
end

class Employee
	attr_reader :name, :title
	attr_reader :salary
	def initialize( name, title, salary)
		@name = name
		@title = title
		@salary = salary
		@observers = [] #Liste des observateurs
	end
	
	def salary=(new_salary)
		@salary = new_salary
		notify_observers
	end
	
	def notify_observers
		# Appelle toute les méthodes update de chaque observateur
		@observers.each do |observer|
			observer.update(self)
		end
	end
	
	def add_observer(observer)
		@observers << observer
	end
	
	def delete_observer(observer)
		@observers.delete(observer)
	end
end

fred = Employee.new('Fred', 'Crane Operator', 30000.0)
payroll = Payroll.new
fred.add_observer( payroll )
fred.salary=35000.0

tax_man = TaxMan.new
fred.add_observer(tax_man)
fred.salary=90000.0