class Person {

    private String ID;
    private String lastName;
    private String firstName;
    private int numberOfDependents;
    
    public static Person find(long id) { 
        /* 
            Cr�ation de l'instance de 'Person' en allant chercher dans la base de donn�es et si cette personne 
            n'a pas d�ja �t� cr��e (utilisation pour cela d'un objet (ou plut�t une classe statique) 'Registry'
        */
    }
    
    public void update() { 
        /* Mise � jour avec les attributs de l'objet */
    }
    
    public Long insert() { 
        /* Insertion avec les attributs de l'objet */
    }
    
    public Money getExemption() {
        /* Logique m�tier sp�cifique au pattern ActiveRecord et qui le diff�rencie de RowDataGateway */
    }

}