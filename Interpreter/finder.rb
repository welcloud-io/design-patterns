require 'find'

class Expression
	# Common expression code will go here soon...
end

class All < Expression
	def evaluate(dir)
		results= []
		Find.find(dir) do |p|
		next unless File.file?(p)
			results << p
		end
		results
	end
end

expr_all = All.new
files = expr_all.evaluate('test_dir')
puts "all files:", files

class FileName < Expression
	def initialize(pattern)
		@pattern = pattern
	end
	
	def evaluate(dir)
		results= []
		Find.find(dir) do |p|
		next unless File.file?(p)
			name = File.basename(p)
			results << p if File.fnmatch(@pattern, name)
		end
		results
	end
end

expr = FileName.new('*.mp3')
puts "*.mp3 :", expr.evaluate('test_dir')

class Bigger < Expression
	def initialize(size)
		@size = size
	end
	def evaluate(dir)
		results = []
		Find.find(dir) do |p|
		next unless File.file?(p)
			results << p if( File.size(p) > @size)
		end
		results
	end
end

expr = Bigger.new(2)
puts "bigger:", expr.evaluate('test_dir')

class Writable < Expression
	def evaluate(dir)
		results = []
		Find.find(dir) do |p|
		next unless File.file?(p)
			results << p if( File.writable?(p) )
		end
		results
	end
end

expr = Writable.new
puts "writable: ", expr.evaluate('test_dir')


class Not < Expression
	def initialize(expression)
		@expression = expression
	end
	def evaluate(dir)
		All.new.evaluate(dir) - @expression.evaluate(dir)
	end
end

expr = Not.new( FileName.new('*.mp3') )
puts "not mp3:", expr.evaluate('test_dir')

class Or < Expression
	def initialize(expression1, expression2)
		@expression1 = expression1
		@expression2 = expression2
	end
	def evaluate(dir)
		result1 = @expression1.evaluate(dir)
		result2 = @expression2.evaluate(dir)
		(result1 + result2).sort.uniq
	end
end

expr = Or.new( Bigger.new(1), FileName.new('*.mp3') )
puts "big or mp3:", expr.evaluate('test_dir')

class And < Expression
	def initialize(expression1, expression2)
		@expression1 = expression1
		@expression2 = expression2
	end
	def evaluate(dir)
		result1 = @expression1.evaluate(dir)
		result2 = @expression2.evaluate(dir)
		(result1 & result2)
	end
end

expr =And.new( Bigger.new(1), FileName.new('*.mp3') )
puts "big and mp3:", expr.evaluate('test_dir')

# bigger than 1 and writable and not mp3
complex_expression = And.new(And.new(Bigger.new(1),Writable.new),Not.new( FileName.new('*.mp3') ))
files = complex_expression.evaluate('test_dir')
puts 'complexe expression:', files

# -------------------------------------------------------------------------------------------

class Parser
	def initialize(text)
		@tokens = text.scan(/\(|\)|[\w\.\*]+/)
	end
	def next_token
		@tokens.shift
	end
	def expression
		token = next_token
		if token == nil
			return nil
		elsif token == '('
			result = expression
			raise 'Expected )' unless next_token == ')'
			return result
		elsif token == 'all'
			return All.new
		elsif token == 'writable'
			return Writable.new
		elsif token == 'bigger'
			return Bigger.new(next_token.to_i)
		elsif token == 'filename'
			return FileName.new(next_token)
		elsif token == 'not'
			return Not.new(expression)
		elsif token == 'and'
			return And.new(expression, expression)
		elsif token == 'or'
			return Or.new(expression, expression)
		else
			raise "Unexpected token: #{token}"
		end
	end
end

parser = Parser.new "and (and(bigger 1)(filename *.txt)) writable"
puts "Parser result: ", parser.expression.evaluate('test_dir')

class Expression
	def |(other)
		Or.new(self, other)
	end
	def &(other)
		And.new(self, other)
	end
end

puts "Expression with | and &:", ((Bigger.new(2) & Not.new(Writable.new)) | FileName.new("*.txt")).evaluate('test_dir')

def all
	All.new
end
def bigger(size)
	Bigger.new(size)
end
def file_name(pattern)
	FileName.new(pattern)
end
def except(expression)
	Not.new(expression)
end
def writable
	Writable.new
end

puts "Simplified expression: ", ((bigger(2000) & except(writable) ) | file_name('*.txt')).evaluate('test_dir')
