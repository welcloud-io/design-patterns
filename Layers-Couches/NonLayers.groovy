// Adaptatation de l'example du livre "Pattern-Oriented Software Architecture, Volume 1 - A System Of Patterns"

class L3Provider {
	def L3Service(data) {}
}

class Session  extends L3Provider {
	def L3Service(data) {
		println "L3Service starting its job" 
		data = "<" + data + ">"
		println "L2Service starting its job"
		data = "<" + data + ">"
		println "L1Service is doing its job"
		print data
		println "L2Service finishing its job"
		println "L3Service finishing its job"
	}
}

session = new Session();
session.L3Service('x')