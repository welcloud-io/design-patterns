/*import groovy.sql.Sql
sql = Sql.newInstance("jdbc:postgresql://localhost:5432/postgres", "postgres", "delta", "org.postgresql.Driver")

sql.execute("DROP TABLE products" );
sql.execute("CREATE TABLE products (ID int primary key, name varchar, type varchar)" );

sql.execute("DROP TABLE contracts" );
sql.execute("CREATE TABLE contracts (ID int primary key, product int, revenue decimal, dateSigned date)" );

sql.execute("DROP TABLE revenueRecognitions" );
sql.execute("CREATE TABLE revenueRecognitions (contract int, amount decimal, recognizedOn date, PRIMARY KEY (contract, recognizedOn))" );*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Date;

class CreateTables {
	public void creerTables() throws ClassNotFoundException, SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Class.forName("org.postgresql.Driver");
		con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "delta");
		stmt = con.createStatement();
		//stmt.executeUpdate("DROP TABLE products");
		stmt.executeUpdate("CREATE TABLE products (ID int primary key, name varchar, type varchar)");
		//stmt.executeUpdate("DROP TABLE contracts");
		stmt.executeUpdate("CREATE TABLE contracts (ID int primary key, product int, revenue decimal, dateSigned date)");
		//stmt.executeUpdate("DROP TABLE revenueRecognitions");
		stmt.executeUpdate("CREATE TABLE revenueRecognitions (contract int, amount decimal, recognizedOn date, PRIMARY KEY (contract, recognizedOn))");		
	}
}

class Gateway { 
	
	private static final String findRecognitionsStatement =
	"SELECT amount " +
	"FROM revenueRecognitions " +
	"WHERE contract = ? AND recognizedOn <= ?";
      
	private Connection db;

	public Gateway () throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "delta");  
	}
   
	public ResultSet findRecognitionsFor(long contractID, java.sql.Date asof) throws SQLException{
		PreparedStatement stmt = db.prepareStatement(findRecognitionsStatement);
		stmt = db.prepareStatement(findRecognitionsStatement);
		stmt.setLong(1, contractID);
		stmt.setDate(2, asof);
		ResultSet result = stmt.executeQuery();
		return result;
	}
	
}

class RecognitionService { 
	public Money recognizedRevenue(long contractNumber, java.sql.Date asOf) {
		Money result = Money.dollars(0);
		try {
			ResultSet rs = db.findRecognitionsFor(contractNumber, asOf);
			while (rs.next()) {
				result = result.add(Money.dollars(rs.getBigDecimal("amount")));
			}
			return result;
		}catch (SQLException e) 
			{throw new ApplicationException (e);
		}
	}
}

class TransactionScript {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		CreateTables c = new CreateTables();
		c.creerTables();
	}
}