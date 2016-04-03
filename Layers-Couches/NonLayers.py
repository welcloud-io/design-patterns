# Adaptatation de l'example du livre "Pattern-Oriented Software Architecture, Volume 1 - A System Of Patterns"

class L3Provider:
	def L3Service():
		pass
	
class Session (L3Provider):
	def L3Service(self, data):
		print "L3Service starting its job" 
		data = "<" + data + ">"
		print "L2Service starting its job"
		data = "<" + data + ">"
		print "L1Service is doing its job"
		print data
		print "L2Service finishing its job"
		print "L3Service finishing its job"

session	= Session();

session.L3Service('x')