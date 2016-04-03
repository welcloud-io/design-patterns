// Inspiré de PEAA : Domain model

class RevenueRecognition {

	private amount
	private date
	
	public RevenueRecognition(amount,  date) {
		this.amount = amount
		this.date = date
	}
	
	public getAmount() {
		return amount
	}
	
	boolean isRecognizableBy(asOf) {
		return (asOf > date) || (asOf == date) // asOf.after(date) || asOf.equals(date)
	}
	
}


class Contract {

	private product
	private revenue
	private whenSigned
	private id
	Contract(product, revenue, whenSigned) {
		this.product = product
		this.revenue = revenue
		this.whenSigned = whenSigned
	}

	private revenueRecognitions = []
	
	def recognizedRevenue(asOf) {
		def result = 0
		revenueRecognitions.each() { revenueRecognition ->
			if (revenueRecognition.isRecognizableBy(asOf))	 { result += revenueRecognition.getAmount() }
		}
		return result
	}
	
	def calculateRecognitions() {
		product.calculateRevenueRecognitions(this);
	}
	
	def getRevenue() {
		return revenue
	}
	
	def getWhenSigned() {
		return whenSigned
	}
	
	def addRevenueRecognition(revenueRecognition) {
		revenueRecognitions << revenueRecognition
	}
	
}

class Product {

	private name
	private recognitionStrategy
	public Product(name, recognitionStrategy) {
		this.name = name
		this.recognitionStrategy = recognitionStrategy
	}
	public static newWordProcessor(name) {
		return new Product(name, new CompleteRecognitionStrategy())
	}
	public static newSpreadsheet(name) {
		return new Product(name, new ThreeWayRecognitionStrategy(60, 90))
	}
	public static newDatabase(name) {
		return new Product(name, new ThreeWayRecognitionStrategy(30, 60))
	}
	
	def calculateRevenueRecognitions(Contract contract) {
		recognitionStrategy.calculateRevenueRecognitions(contract);
	}
   
}

abstract class RecognitionStrategy {
	abstract void calculateRevenueRecognitions(contract)
}

class CompleteRecognitionStrategy {
	def calculateRevenueRecognitions(contract) {
		contract.addRevenueRecognition(new RevenueRecognition(contract.getRevenue(), contract.getWhenSigned()))
	}
}

class ThreeWayRecognitionStrategy {

   private int firstRecognitionOffset
   private int secondRecognitionOffset
   public ThreeWayRecognitionStrategy(firstRecognitionOffset, secondRecognitionOffset) {
		this.firstRecognitionOffset 	= firstRecognitionOffset
		this.secondRecognitionOffset = secondRecognitionOffset
   }
   
   void calculateRevenueRecognitions(Contract contract) {
		def allocation = contract.getRevenue() / 3 // contract.getRevenue().allocate(3)
		contract.addRevenueRecognition(new RevenueRecognition (allocation, contract.getWhenSigned()))
		contract.addRevenueRecognition(new RevenueRecognition (allocation, contract.getWhenSigned())) //.addDays(firstRecognitionOffset)))
		contract.addRevenueRecognition(new RevenueRecognition (allocation, contract.getWhenSigned())) //.addDays(secondRecognitionOffset)))
	}
   
}

word 	= Product.newWordProcessor("Thinking Word")
calc 	= Product.newSpreadsheet("Thinking Calc")
db 	= Product.newDatabase("Thinking DB")

revenueRecognition = new RevenueRecognition(100, '2011-08-02')

contract = new Contract(product=Product.newWordProcessor("Thinking Word"), revenue=100, whenSigned='2008-08-02')
contract.calculateRecognitions()
// println contract.getRevenueRecognitions()
println contract.recognizedRevenue('2011-09-01')

contract = new Contract(product=Product.newSpreadsheet("Thinking Calc"), revenue=100, whenSigned='2008-08-02')
contract.calculateRecognitions()
println contract.recognizedRevenue('2011-09-01')
