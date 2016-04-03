class Encrypter
	def initialize(key)
		@key = key
	end
	def encrypt(reader, writer)
		key_index = 0
		while not reader.eof?
			clear_char = reader.getc
			encrypted_char = clear_char ^ @key[key_index] # c'est un opérateur logiqu ou, ceci permet de crypter et décripter avec le même programme
			writer.putc(encrypted_char)
			key_index = (key_index + 1) % @key.size
		end
	end
end

reader = File.open('message.txt')
writer = File.open('message.encrypted','w')
encrypter = Encrypter.new('my secret key')
encrypter.encrypt(reader, writer)

reader = File.open('message.encrypted')
writer = File.open('message.decrypted','w')
encrypter = Encrypter.new('my secret key')
encrypter.encrypt(reader, writer)