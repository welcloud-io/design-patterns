class Employee
	attr_reader :name
	attr_accessor :title, :salary
	def initialize( name, title, salary )
		@name = name
		@title = title
		@salary = salary
	end
end

fred = Employee.new("Fred Flintstone", "Crane Operator", 30000.0)
puts fred.salary
# Give Fred a raise
fred.salary=35000.0
puts fred.salary