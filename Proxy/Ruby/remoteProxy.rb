# Il faut avoir démarrer le serveur tomcat qui contient la webapp axis

require 'soap/wsdlDriver'

wsdl_url = 'http://localhost:8080/axis/services/Version?WSDL'
#wsdl_url = 'http://www.webservicex.com/CurrencyConvertor.asmx?WSDL'
#wsdl_url = 'http://www.webservicex.net/whois.asmx?wsdl'
#wsdl_url = 'http://www.xmlme.com/WSAmazonBox.asmx'

proxy = SOAP::WSDLDriverFactory.new( wsdl_url ).create_rpc_driver

puts proxy.getVersion()
#puts proxy.ConversionRate('EUR', 'USD')
#puts proxy.GetWhoIS("google.com")
#puts proxy.ActorDvdBox()
