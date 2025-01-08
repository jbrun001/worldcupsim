public class Manager extends Person {

    private String favouredFormation; 
    private double respect;
    private double ability;
    private double knowledge; 
    private double belief;

    /**
     * Constructor for Manager Class
     * @param firstName             // part of Person super class
     * @param surname               // part of Person super class
     * @param team                  // part of Person super class
     * @param favouredFormation
     * @param respect
     * @param ability
     * @param knowledge
     * @param belief
     */
    Manager(String firstName, String surname, String team, String favouredFormation, double respect, double ability, double knowledge, double belief){
        super(firstName, surname, team);
        this.favouredFormation =  favouredFormation;
        this.respect = respect;
        this.ability = ability;
        this.knowledge = knowledge;
        this.belief = belief;
    };

    // getters
    public String getFavouredFormation() {
        return this.favouredFormation;
    }

    public Double getBelief() {
        return this.belief;
    }

    public Double getRespect() {
        return this.respect;
    }

    public Double getKnowledge() {
        return this.knowledge;
    }

    public Double getAbility() {
        return this.ability;
    }

    // changes the managers respect rating by the amount specified
    public void modifyRespect(double respectChange) {
        if (this.respect + respectChange > 0) {
            this.respect = this.respect + respectChange;
        }
    }

    // changes the managers respect rating by the amount specified
    public void modifyBelief(double beliefChange) {
        if (this.respect + beliefChange > 0 && this.respect + beliefChange < 1) {
            this.respect = this.respect + beliefChange;
        }
    }

}
