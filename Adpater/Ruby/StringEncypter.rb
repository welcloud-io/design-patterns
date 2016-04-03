class Encrypter
	def initialize(key)
		@key = key
	end
	def encrypt(reader, writer)
		key_index = 0
		while not reader.eof?
			clear_char = reader.getc
			encrypted_char = clear_char || @key[key_index] # c'est un opérateur logiqu ou, ceci permet de crypter et décripter avec le même programme
			writer.putc(encrypted_char)
			key_index = (key_index + 1) % @key.size
		end
	end
end

class StringIOAdapter # adaptateur
	
	def initialize(string)
		@string = string
		@position = 0
	end
	# on défini getc et eof? qui sont nécessaires à la méthode encrypt de la classe Encrypter
	def getc
		if @position >= @string.length
			raise EOFError
		end
		ch = @string[@position]
		@position += 1
		return ch
	end
	
	def eof?
		return @position >= @string.length
	end
	
end

encrypter = Encrypter.new('XYZZY')
reader = StringIOAdapter.new('We attack at dawn')
writer = File.open('out.txt', 'w')
encrypter.encrypt(reader, writer)