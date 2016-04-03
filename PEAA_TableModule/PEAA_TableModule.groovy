class TableModule {

	protected table
	protected TableModule(dataSet, tableName) {
		table = dataSet.Tables[tableName]
	}

}

class Contract extends TableModule {

	public Contract (dataSet) {
		super (dataSet, "Contracts")
	}
	
	public DataRow this [key] {
	get {
		return table.Select("ID = #{key}")[0]
	}
      

	def calculateRecognitions (contractID) {
		contractRow 			= this[contractID]
		amount 					= (Decimal)contractRow["amount"]
		revenueRecognition 	= new RevenueRecognition (table.DataSet)
		product 					= new Product(table.DataSet)
		prodID 					= GetProductId(contractID)
		if (prod.GetProductType(prodID) == ProductType.WP) {
			revenueRecognition.Insert(contractID, amount, (DateTime) GetWhenSigned(contractID))
		}else if (prod.GetProductType(prodID) == ProductType.SS) {
			Decimal[] allocation = allocate(amount,3)
			revenueRecognition.Insert(contractID, allocation[0], (DateTime) GetWhenSigned(contractID))
			revenueRecognition.Insert(contractID, allocation[1], (DateTime) GetWhenSigned(contractID).AddDays(60))
			revenueRecognition.Insert(contractID, allocation[2], (DateTime) GetWhenSigned(contractID).AddDays(90))
		}else if (prod.GetProductType(prodID) == ProductType.DB) {
			Decimal[] allocation = allocate(amount,3)
			revenueRecognition.Insert(contractID, allocation[0], (DateTime) GetWhenSigned(contractID))
			revenueRecognition.Insert(contractID, allocation[1], (DateTime) GetWhenSigned(contractID).AddDays(30))
			revenueRecognition.Insert(contractID, allocation[2], (DateTime) GetWhenSigned(contractID).AddDays(60))
		}
	
	private Decimal[] allocate(Decimal amount, int by) {
		Decimal lowResult = amount / by
		lowResult = Decimal.Round(lowResult,2)
		Decimal highResult = lowResult + 0.01m
		Decimal[] results = new Decimal[by]
		int remainder = (int) amount % by
		for (int i = 0 i < remainder i++) results[i] = highResult
		for (int i = remainder i < by i++) results[i] = lowResult
		return results
	}
}

public enum ProductType {WP, SS, DB} 

class Product {

	GetProductType (id) {
		String typeCode = (String) this[id]["type"]
		return (ProductType) Enum.Parse(typeof(ProductType), typeCode)
	}

}

class RevenueRecognition { 

	public long Insert (contractID, amount, date) {
		DataRow newRow = table.NewRow()
		long id = GetNextID()
		newRow["ID"] 				= id
		newRow["contractID"] 	= contractID
		newRow["amount"] 		= amount
		newRow["date"]			= date
		table.Rows.Add(newRow)
		return id
	}
	
	
	public Decimal RecognizedRevenue (long contractID, DateTime asOf) {
		String filter 		= String.Format("ContractID = {0}AND date <= #{1:d}#", contractID, asOf)
		DataRow[] rows 	= table.Select(filter)
		result 				= 0
		foreach (DataRow row in rows) {
			result += (Decimal)row["amount"]
		}
		return result
	}

	public Decimal RecognizedRevenue2 (long contractID, DateTime asOf) {
		String filter 					= String.Format("ContractID = {0}AND date <= #{1:d}#", contractID, asOf);
		String computeExpression 	= "sum(amount)";
		Object sum 					= table.Compute(computeExpression, filter);
		return (sum is System.DBNull) ? 0 : (Decimal) sum;
	}

}


contract = new Contract(dataset)
contract = calculateRecognitions(contractID=200)

