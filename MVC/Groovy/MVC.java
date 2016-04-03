// 1 Separate human-computer interactionfrom corefunctionality
class Model{
	List<long> votes;
	List<String> parties;
	
	public : 
	
	Model(List<String> partyNames);
	
	// access interface for modification by controller
	void clearVotes0; // set voting values to 0
	void changeVote(String party, long vote);
	
	// factory functions for view access to data
	Iterator<long> makeVoteIterator() {
		return Iterator<long>(votes);
	}
	
	Iterator<String> makePartyIterator0 {
		return Iterator<String>(parties);
	}
	// ... to be continued
	//2 Implement the change-propagation mechanism.
	// ... continued
	public:
		void attach(Observer *s) { registry.add (s) ; }
		void detach (Observer *s) { registry. remove (s) ; }
	protected:
		virtual void notify();
	private :
		Set<Observer*> registry;
}

//2 Implement the change-propagation mechanism.
class Observer{ // common ancestor for view and controller
	public :
	virtual void update() {}
	// default is no-op
} ;

void Model : :notify ( ) (
	// call update for all observers
	Iterator<Observer*> iter(registry) ;
	while (iter.next()) {
		iter.curr() ->update () ;
	}
}

// 3 Design and implement the views
class View : public Observer (
	public:
	View (Model *m) : myModel (m) , mycontroller (0)
		{ myModel ->attach( this) ; }
	virtual ~View ( ) {myModel ->detach (this) ; }
	virtual void update() { this->draw () ; }
	// abstract interface to be redefined:
	virtual void initialize() ;// see below
	virtual void draw() ; // (re-)di splay view

	// . . . to be continued below
	// ... continued
	public:
	//c++ deficit: use initialize to call right factory method
	virtual void initialize()
		{ mycontroller = makeController() ; }
	virtual Controller *makeController()
		{ return new Controller(this); }
	};
	// continue end
	
	Model *getModel() { return myModel; }
	Controller *getcontroller() { return mycontroller;}
	protected:
		Model *myModel ;
		Controller *myController; // set by initialize
};
	
class Barchartview : public View {
	public:
		BarChartView(Model *m) : View(m) } ;
		virtual void draw() ;
	};

void BarChartView::draw() {
	Iterator<String> ip = myModel->makePartyIterator();
	Iterator<long> iv = myModel->makeVoteIterator();
	List<long> dl; //for scaling values to fill screen
	long max = I;// maximum for adjustment
	// calculate maximum vote count
	while (iv.next0) I
		if (iv.curr() > max ) max = iv.curr0;
	}
	iv.reset() ;
	// now calculate screen coordinates for bars
	while (iv.next()) {
		dl.append((MAXBARS1ZE * iv.curr())/max);
	}
	// reuse iterator object for new collection:
	iv = dl; // assignment rebinds iterator to new list
	iv.reset() ;
	while (ip.next() && iv.next()) {
		// draw text: cout << ip.curr 0 << " : " ;
		// draw bar: ... drawbox(BARWIDTH, iv.curr0) ;...
	}
}

// 4 Design and implement the controllers

class Controller : public Observer {
	public :
		virtual void handleEvent(Event *) {}
			// default = no op
			
		Controller( View *v) : myView(v) {
			myModel = myView->getModel();
			myModel ->attach (this) ;
		}
		
		virtual ~Controller () { myModel ->detach(this) ; }
		virtual void update() {} // default = no op
	protected:
		Model *myModel ;
		View *myview;
} ;

// 5 Design and implement the view-controller relationship
class TableController : public Controller
	public:
	TableController( TableView *tv) : Controller (tv) {
		virtual void handleEvent(Event *e) {
			// ... interpret event e,
			// for instance, update votes of a party
			if (vote && party) { // entry complete:
				myModel->changeVote(party,vote);
			}
		}
	};
	
class TableView : public View {
	public:
	TableView(Model *m) : View(m) {}
	virtual void draw () ;
	virtual Controller *makecontroller()
		{ return new TableController (this) ; }
};

// 6 Implement the set-up of MVC.
main() {
	// initialize model
	List<String> parties; 
	parties.append("black");
	parties.append ("blue") ; 
	parties.append ("red");
	parties.append ("green"); 
	parties.append ("oth.");
	
	Model m(parties) ;
	
	// initialize views
	TableView *v1 = new TableView(&m) ;
	v1->initialize();
	BarChartView *v2 = new BarChartView(&m);
	v2->initialize();
	// now start event processing . . .
}