# Adaptatation de l'example du livre "Pattern-Oriented Software Architecture, Volume 1 - A System Of Patterns"

class L3Provider:
	def Service(data):
		pass
		
	def setLowerLayer(self, L2Provider):
		self.lowerLevel = L2Provider

class L2Provider:
	def Service(data):
		pass
		
	def setLowerLayer(self, L1Provider):
		self.lowerLevel = L1Provider

class L1Provider:
	def Service(data):
		pass
	
class Software (L3Provider):
	def Service(self, data):
		print "Software Service starting its job" 
		data = "<" + data + ">"
		self.lowerLevel.Service(data);
		print "Software Service finishing its job"

class OperatingSystem(L2Provider):
	def Service(self, data):
		print "OS Service starting its job";
		data = "<" + data + ">"
		self.lowerLevel.Service(data);
		print "OS Service finishing its job"

class HardDisk (L1Provider):
	def Service(self, data):
		print "Hardware Service is doing its job"
		print data
		
class HardDisk_2 (L1Provider):
	def Service(self, data):
		print "Hardware Service is doing its job"
		print "[" + data + "]"

software					= Software();
operatingSystem				= OperatingSystem();
hardDisk					= HardDisk();
hardDisk_new				= HardDisk_2();

software.setLowerLayer(operatingSystem)
# operatingSystem.setLowerLayer(hardDisk)
operatingSystem.setLowerLayer(hardDisk_new)

print
print '===> Software sur OS seul'
print
software.Service('x')
print
print '===> Software sur machine virtuelle'
print

softwareOnVirtualMachine		= Software();
operatingSystemOnVirtualMachine	= OperatingSystem();
operatingSystem				= OperatingSystem();
hardDisk					= HardDisk();

softwareOnVirtualMachine.setLowerLayer(operatingSystemOnVirtualMachine)
operatingSystemOnVirtualMachine.setLowerLayer(operatingSystem)
operatingSystem.setLowerLayer(hardDisk)

softwareOnVirtualMachine.Service('y')
