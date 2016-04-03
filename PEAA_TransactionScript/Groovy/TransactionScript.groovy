// Inspiré de PEAA : Transaction script
import groovy.sql.Sql
sql = Sql.newInstance("jdbc:sqlite:./transaction_script.db", "org.sqlite.JDBC")

def creerTables() {
	sql.execute("DROP TABLE IF EXISTS products")
	sql.execute("DROP TABLE IF EXISTS contracts")
	sql.execute("DROP TABLE IF EXISTS revenueRecognitions")

	sql.execute("CREATE TABLE products (ID int primary key, name varchar, type varchar)")
	sql.execute("CREATE TABLE contracts (ID int primary key, product int, revenue decimal, dateSigned date)")
	sql.execute("CREATE TABLE revenueRecognitions (contract int, amount decimal, recognizedOn date, PRIMARY KEY (contract, recognizedOn))")

	sql.execute("insert into products values (1, 'a', 'b')")
	assert sql.firstRow("select * from products") == ['ID':1, 'name':'a', 'type':'b']
	sql.execute("delete from products")

	sql.execute("insert into contracts values (1, 1, 1.5, '2011-08-02')")
	assert sql.firstRow("select * from contracts") == ['ID':1, 'product':1, 'revenue':1.5, 'dateSigned':'2011-08-02']
	sql.execute("delete from contracts")

	sql.execute("insert into revenueRecognitions values (1, 3.2, '2011-08-09')")
	assert sql.firstRow("select * from revenueRecognitions") == ['contract':1, 'amount':3.2, 'recognizedOn':'2011-08-09']
	sql.execute("delete from revenueRecognitions")
}

def alimenterTables() {
	sql.execute("delete from products")
	sql.execute("delete from contracts")
	sql.execute("delete from revenueRecognitions")

	sql.execute("insert into products values (10, 'name_1', 'W')")
	sql.execute("insert into products values (15, 'name_2', 'S')")
	sql.execute("insert into products values (20, 'name_3', 'D')")
	sql.execute("insert into contracts values (200, 10, 500.3, '2011-08-02')")
	sql.execute("insert into contracts values (300, 15, 600.1, '2011-08-02')")
	sql.execute("insert into contracts values (400, 20, 400.2, '2011-08-02')")
	sql.execute("insert into revenueRecognitions values (1000, 100.2, '2011-08-09')")
	sql.execute("insert into revenueRecognitions values (1000, 200.1, '2011-08-11')")
}

class Gateway { 
	
	def private db

	def public Gateway() {
		db = Sql.newInstance("jdbc:sqlite:./transaction_script.db", "org.sqlite.JDBC") 
	}
   
	def public findRecognitionsFor(contractID, asof) {
		return db.rows("SELECT amount FROM revenueRecognitions WHERE contract = ? AND recognizedOn <= ?", [contractID, asof])
	}
	
	def public findContract (contractID) {
		return db.rows("SELECT * FROM contracts c, products p WHERE c.ID = ? AND c.product = p.ID", [contractID])
   }  
	
	def public insertRecognition (contractID, amount, asof) {
		db.execute("INSERT INTO revenueRecognitions VALUES (?, ?, ?)", [contractID, amount, asof])
	}
	
	def addDays(date, numberOfDays) {
		def add_string = '+' + numberOfDays +'days'
		return db.rows("select date(?, ?)", [date, add_string])['date(?, ?)'][0]
	}
}

class RecognitionService {
	def gateway = new Gateway()
	def public recognizedRevenue(contractNumber, asOf) {
		def result = 0
		gateway.findRecognitionsFor(contractNumber, asOf).each() { row ->
			result += row["amount"]
		}
		return result
	}
	
	private addDays(date, numberOfDays) {
		return gateway.addDays(date, numberOfDays)
	}
	
	def public calculateRevenueRecognitions(contractNumber) {

		gateway.findContract(contractNumber).each() { contract ->

			def (totalRevenue,  recognitionDate, type) = [contract["revenue"], contract["dateSigned"], contract["type"]]

			if (type == "S"){
				def allocation = totalRevenue / 3 // Simplifié volontiarement pour l'exemple, il faudrait utiliser une "allocation" de la classe Money pour éviter les problèmes d'arrondi
				gateway.insertRecognition (contractNumber, allocation, recognitionDate)
				gateway.insertRecognition (contractNumber, allocation, addDays(recognitionDate, 60)) // .addDays(60)
				gateway.insertRecognition (contractNumber, allocation, addDays(recognitionDate,90)) // .addDays(90)
			}else if (type.equals("W")){
				gateway.insertRecognition(contractNumber, totalRevenue, recognitionDate)
			}else if (type.equals("D")) {
				def allocation = totalRevenue / 3
				gateway.insertRecognition (contractNumber, allocation, recognitionDate)
				gateway.insertRecognition (contractNumber, allocation, addDays(recognitionDate, 30))
				gateway.insertRecognition (contractNumber, allocation, addDays(recognitionDate, 60))
			}
		}
	}
}

creerTables()
alimenterTables()
recognitionService = new RecognitionService()

recognitionService.calculateRevenueRecognitions(contractNumber=200)
println recognitionService.recognizedRevenue(contractNumber=200, '2011-09-01')

recognitionService.calculateRevenueRecognitions(contractNumber=300)
println recognitionService.recognizedRevenue(contractNumber=300, '2011-09-01')

recognitionService.calculateRevenueRecognitions(contractNumber=400)
println recognitionService.recognizedRevenue(contractNumber=400, '2011-09-31')


