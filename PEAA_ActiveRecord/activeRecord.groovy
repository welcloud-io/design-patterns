class Person {

    private String ID;
    private String lastName;
    private String firstName;
    private int numberOfDependents;
    
    public static Person find(long id) { 
        /* 
            Création de l'instance de 'Person' en allant chercher dans la base de données et si cette personne 
            n'a pas déja été créée (utilisation pour cela d'un objet (ou plutôt une classe statique) 'Registry'
        */
    }
    
    public void update() { 
        /* Mise à jour avec les attributs de l'objet */
    }
    
    public Long insert() { 
        /* Insertion avec les attributs de l'objet */
    }
    
    public Money getExemption() {
        /* Logique métier spécifique au pattern ActiveRecord et qui le différencie de RowDataGateway */
    }

}