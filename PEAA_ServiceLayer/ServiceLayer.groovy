public class ApplicationService { 
	protected EmailGateway getEmailGateway() {
		//return an instance of EmailGateway
	}
	protected IntegrationGateway getIntegrationGateway() {
		//return an instance of IntegrationGateway
	}
}

public interface EmailGateway {
	void sendEmailMessage(String toAddress, String subject, String body)
	}
	public interface IntegrationGateway {
		void publishRevenueRecognitionCalculation(Contract contract)
	}

public class RecognitionService extends ApplicationService {
	public void calculateRevenueRecognitions(long contractNumber) {
		Contract contract = Contract.readForUpdate(contractNumber)
		contract.calculateRecognitions()
		getEmailGateway().sendEmailMessage(
			contract.getAdministratorEmailAddress(),
			"RE: Contract #" + contractNumber, contract + " has had revenue recognitions calculated."
		)
		getIntegrationGateway().publishRevenueRecognitionCalculation(contract)
	}
	public Money recognizedRevenue(long contractNumber, Date asOf) {
		return Contract.read(contractNumber).recognizedRevenue(asOf)
	}
}

