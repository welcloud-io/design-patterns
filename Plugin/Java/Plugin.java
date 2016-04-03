interface IdGenerator {

	public static final IdGenerator INSTANCE = (IdGenerator) PluginFactory.getPlugin(IdGenerator.class);

	public Long nextId();

}

class Environment {
	public static String getProperty(String propertyKey) {
		if (propertyKey.equals("id.sequence")) 	return "a";
		if (propertyKey.equals("id.source")) 	return "b";
		return "";
	}
}

class PostgresIdGenerator implements IdGenerator {
	
	String sequence;
	String datasource;

	public PostgresIdGenerator() {
		this.sequence = Environment.getProperty("id.sequence");
		this.datasource = Environment.getProperty("id.source");
	}
   
	public Long nextId() {
		return (long) 0;
	}
}

class TestIDGenerator implements IdGenerator {

	private long count = 0;
	public synchronized Long nextId() {
		return new Long(count++);
	}

}

class Properties {
	
}

class PluginFactory {
	
	private static Properties props = new Properties();

	static {
		try {
			props.load(PluginFactory.class.getResourceAsStream("/plugins.properties"));
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Object getPlugin(Class iface) {

		String implName = props.getProperty(iface.getName());
		if (implName == null) {
			throw new RuntimeException("implementation not specified for " + iface.getName() + " in PluginFactory propeties.");
		}
		try {
			return Class.forName(implName).newInstance();
		} catch (Exception ex) {
			throw new RuntimeException("factory unable to construct instance of " + iface.getName());
		}
		
	}
}

class DomainObject {
	
}

class Customer extends DomainObject {

	private Customer(String name, Long id) {
		super(id);
		this.name = name;
	}
}

class Plugin {
	
	public Customer create(String name) {
		Long newObjId = IdGenerator.INSTANCE.nextId();
		Customer obj = new Customer(name, newObjId);
		return obj;
	}
	
	public static void main(String[] args) {
		System.out.println ("Test plugin");
		customer = create("C1");
		System.out.println(customer.ID);
		customer = create("C2");
		System.out.println(customer.ID);
		customer = create("C3");
		System.out.println(customer.ID);	
	}
}
