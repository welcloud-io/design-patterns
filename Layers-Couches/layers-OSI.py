# Adaptatation de l'example du livre "Pattern-Oriented Software Architecture, Volume 1 - A System Of Patterns"

class L3Provider:
	def L3Service(data):
		pass
		
	def setLowerLayer(self, L2Provider):
		self.level2 = L2Provider

class L2Provider:
	def L2Service(data):
		pass
		
	def setLowerLayer(self, L1Provider):
		self.level1 = L1Provider

class L1Provider:
	def L1Service(data):
		pass
	
class Session (L3Provider):
	def L3Service(self, data):
		print "L3Service starting its job" 
		data = "<" + data + ">"
		self.level2.L2Service(data);
		print "L3Service finishing its job"

class Transport(L2Provider):
	def L2Service(self, data):
		print "L2Service starting its job";
		data = "<" + data + ">"
		self.level1.L1Service(data);
		print "L2Service finishing its job"

class DataLink (L1Provider):
	def L1Service(self, data):
		print "L1Service is doing its job"
		print data

session	= Session();
transport	= Transport();
datalink	= DataLink();

session.setLowerLayer(transport)
transport.setLowerLayer(datalink)

session.L3Service('x')