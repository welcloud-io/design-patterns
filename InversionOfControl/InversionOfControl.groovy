public interface MovieFinder {
	List findAll();
}

class ColonDelimitedMovieFinder implements MovieFinder {
	ColonDelimitedMovieFinder(file) {}
	List findAll() {
		// lecture dans un fichier d�limit� pour contruire la liste
		return [new Movie('E.T.','spielberg'), new Movie('Jurassic Park','spielberg'), new Movie('Star Wars','lucas')]
	}
}

class XMLMovieFinder implements MovieFinder {
	XMLMovieFinder(file) {}
	List findAll() {
		// lecture dans un fichier XML pour 
		return [new Movie('Les dents de la mer','spielberg'), new Movie('Minority report','spielberg'), new Movie('Willow','lucas')]
	}
}

class Movie {
	def title
	def director
	Movie(title, director) { this.title = title; this.director = director }
	def getDirector() { return director }
}

class MovieLister {
	// utilisation de l'interface
	private MovieFinder finder;
	public MovieLister() {
		// La question de l'injection de d�pendance est comment choisir entre ces deux possibilit�s
		finder = new ColonDelimitedMovieFinder("movies1.txt");
		finder = new XMLMovieFinder("movies1.xml");
	}
	
	// Injection de d�pendance par un setter
	public void setFinder(MovieFinder finder) {
		this.finder = finder;
	}

	// 1�re partie de l'explication
	public Movie[] moviesDirectedBy(String arg) {
		List allMovies = finder.findAll(); // m�thode � d�coupler (rendre ind�pendant) de l'impl�mentation
		for (Iterator it = allMovies.iterator(); it.hasNext();) {
		    Movie movie = (Movie) it.next();
		    if (!movie.getDirector().equals(arg)) it.remove();
		}
		return (Movie[]) allMovies.toArray(new Movie[allMovies.size()]);
	}
}

movieLister = new MovieLister()
movieLister.moviesDirectedBy('spielberg').each { movie -> println movie.title }

// Setter Injection
print '\nSetter Injection\n\n'
movieLister_withInjection = new MovieLister()
/* ===> Lu par le conteneur pour instancier le lister et le finder
<beans>
	<bean id="MovieLister" class="spring.MovieLister">
	    <property name="finder">
		<ref local="MovieFinder"/>
	    </property>
	</bean>
	<bean id="MovieFinder" class="spring.ColonMovieFinder">
	    <property name="filename">
		<value>movies1.txt</value>
	    </property>
	</bean>
</beans>
*/
movieLister_withInjection.setFinder(new ColonDelimitedMovieFinder("movies1.txt")) // Remarque pour �tre complet il faudrait ajouter un setter pour le nom du fichier (ex : ColonMovieFinder.. setFilename) mais ceci n'apporte rien de plus � la compr�hension et alourdi l'exemple
movieLister_withInjection.moviesDirectedBy('spielberg').each { movie -> println movie.title }

