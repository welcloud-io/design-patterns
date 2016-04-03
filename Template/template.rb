# encoding: cp1252
class Report
	def initialize
		@title = 'Monthly Report'
		@text = [ 'Things are going', 'really, really well.' ]
	end
	def output_report
		puts('<html>')
		puts(' <head>')
		puts(" <title>#{@title}</title>")
		59
		puts(' </head>')
		puts(' <body>')
		@text.each do |line|
			puts(" <p>#{line}</p>" )
		end
		puts(' </body>')
		puts('</html>')
	end
end

puts "---1 simple rapport html"
report = Report.new
report.output_report

class Report
	def initialize
		@title = 'Monthly Report'
		@text = ['Things are going', 'really, really well.']
	end
	
	def output_report(format)
		if format == :plain
			puts("*** #{@title} ***")
		elsif format == :html
			puts('<html>')
			puts(' <head>')
			puts(" <title>#{@title}</title>")
			puts(' </head>')
			puts(' <body>')
		else
			raise "Unknown format: #{format}"
		end
		@text.each do |line|
			if format == :plain
				puts(line)
			else
				puts(" <p>#{line}</p>" )
			end
		end
		if format == :html
			puts(' </body>')
			puts('</html>')
		end
	end
end

puts "façon simple d'ajouter un nouveau type de rapport"
report = Report.new
report.output_report(:plain)
report.output_report(:html)

class Report
	def initialize
		@title = 'Monthly Report'
		@text = ['Things are going', 'really, really well.']
	end
	def output_report
		output_start
		output_head
		output_body_start
		output_body
		output_body_end
		output_end
	end
	def output_body
		@text.each do |line|
			output_line(line)
		end
	end
	def output_start
		raise 'Called abstract method: output_start'
	end
	def output_head
		raise 'Called abstract method: output_head'
	end
	def output_body_start
		raise 'Called abstract method: output_body_start'
	end
	def output_line(line)
		raise 'Called abstract method: output_line'
	end
	def output_body_end
		raise 'Called abstract method: output_body_end'
	end
	def output_end
		raise 'Called abstract method: output_end'
	end
end

class HTMLReport < Report
	def output_start
		puts('<html>')
	end
	def output_head
		puts(' <head>')
		puts(" <title>#{@title}</title>")
		puts(' </head>')
	end
	def output_body_start
		puts('<body>')
	end
	def output_line(line)
		puts(" <p>#{line}</p>")
	end
	def output_body_end
		puts('</body>')
	end
	def output_end
		puts('</html>')
	end
end

class PlainTextReport < Report
	def output_start
	end
	def output_head
		puts("**** #{@title} ****")
		puts
	end
	def output_body_start
	end
	def output_line(line)
		puts(line)
	end
	def output_body_end
	end
	def output_end
	end
end

puts "Utilisation d'un classe abstraite pour séparer les deux rapports : text et html => cette classe abstraite est le template"
report = HTMLReport.new
report.output_report
report = PlainTextReport.new
report.output_report