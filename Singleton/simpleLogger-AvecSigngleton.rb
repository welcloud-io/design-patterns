
class SimpleLogger
	attr_accessor :level
	ERROR = 1
	WARNING = 2
	INFO = 3
	
	def initialize
		@log = File.open("log.txt", "w")
		@level = WARNING
	end

	def error(msg)
		@log.puts(msg)
		puts(msg)
		@log.flush
	end

	def warning(msg)
		@log.puts(msg) if @level >= WARNING
		puts(msg) if @level >= WARNING
		@log.flush
	end

	def info(msg)
		@log.puts(msg) if @level >= INFO
		puts(msg) if @level >= INFO
		@log.flush
	end
	
	@@instance = SimpleLogger.new #Variable de classe (diff�rent de variable d'instance)

	def self.instance
		return @@instance
	end
	
	private_class_method :new 	# C'est un method de classe (diff�rent de instance), qui assure que l'on ne pourra pas instancier deux fois la classe,
						# Il faudra toujours passer par la methode self.instance

end

logger1 = SimpleLogger.instance # Returns the logger
logger2 = SimpleLogger.instance # Returns exactly the same logger

logger1.level = SimpleLogger::INFO
logger1.info("logger1")
logger2.info("logger2")

logger3 = SimpleLogger.new
