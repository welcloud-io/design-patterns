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

end

logger = SimpleLogger.new
logger.level = SimpleLogger::INFO
logger.info('Doing the first thing')
# Do the first thing...
logger.info('Now doing the second thing')
# Do the second thing...