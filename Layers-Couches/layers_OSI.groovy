// Adaptatation de l'example du livre "Pattern-Oriented Software Architecture, Volume 1 - A System Of Patterns"

class L3Provider {
	def level2
	
	def L3Service(data) {}
	
	def setLowerLayer(L2Provider) {
		this.level2 = L2Provider
	}
}

class L2Provider {
	def level1
	
	def L2Service(data) {}
		
	def setLowerLayer(L1Provider) {
		this.level1 = L1Provider
	}
}

class L1Provider {
	def L1Service(data) {}
}


class Session extends L3Provider {
	def L3Service(data) {
		println "L3Service starting its job" 
		data = "<" + data + ">"
		this.level2.L2Service(data);
		println "L3Service finishing its job"
	}
}


class Transport extends L2Provider {
	def L2Service(data) {
		println "L2Service starting its job";
		data = "<" + data + ">"
		this.level1.L1Service(data);
		println "L2Service finishing its job"
	}
}


class DataLink  extends L1Provider {
	def L1Service(data) {
		println "L1Service is doing its job"
		println data
	}
}

session	= new Session()
transport	= new Transport()
datalink	= new DataLink()


session.setLowerLayer(transport)
transport.setLowerLayer(datalink)

session.L3Service('x')
