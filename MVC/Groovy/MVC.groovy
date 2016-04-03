// 1 Separate human-computer interactionfrom corefunctionality
class Model {
    def votes	= [];
    def parties	= [];
    
    Model(partyNames=[]) { 
	println parties
	println votes
    }
        
    // access interface for modification by controller
    def clearVotes() {} // set voting values to 0
    def changeVote(party, vote) { 
	println "Vote changed :" + vote + " for party :" + party
	votes << vote; parties << party
	notifyToObservers()
    }

//2 Implement the change-propagation mechanism.    
    def registry  =[];
    def attach(observer)    	{ registry.add (observer)     						}
    def detach(observer)  	{ registry.remove (observer)    					}
    def notifyToObservers() 	{ for (observer in this.registry) {observer.update(); }	}
        
};

class Observer {
    def update() {}
};

// 3 Design and implement the views
class View extends Observer {
    def myModel
    def myController
    
    View (model) { 
        myModel         = model;  
        myController    = makeController(); 
        myModel.attach(this); 
    }
    
    def update() 			{ this.draw() }
    def makeController()     	{ return new Controller(this); }
    
    def getModel() 	{ return myModel; 	}
    def getController() 	{ return myController;	}

};
    
class BarChartView extends View {
    BarChartView(model) 	{ super(model); 				}
    def draw() 			{ 
	println "BarchartView Draw"
	println myModel.parties
	println myModel.votes
    }
};

// 4 Design and implement the controllers
class Controller extends Observer {
    def myModel
    def myView
    
    def handleEvent(event) { println event}
    
    Controller(view) {
	println "creation controleur"
        myView = view;
        myModel = myView.getModel();
        myModel.attach(this) ;
    }
    
    def update() {}

};

// 5 Design and implement the view-controller relationship
class TableController extends Controller {
    TableController(tableView) { super(tableView) }
    def handleEvent(event) {
	println "Event on table controller"
        myModel.changeVote(event[0],event[1]);
    }
};
    
class TableView extends View {
    TableView(model) 	{ super (model) 	}
    def draw() { 
	println "TableView Draw" 
	println myModel.parties
	println myModel.votes	
    }
    def makeController() { return new TableController(this);	}
};

// 6 Implement the set-up of MVC.
def main() {
 
    model = new Model(partyNames=["black", "blue", "red", "green", "oth."]) ;

    tableView 	= new TableView(model)
    barChartView 	= new BarChartView(model)
    
    tableView.getController().handleEvent(["black",1])
    
}

main()

 
 


