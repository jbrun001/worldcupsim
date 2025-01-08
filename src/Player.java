public class Player extends Person {
    private String position;
    private double fitness;
    private double passingAccuracy;
    private double shotAccuracy;
    private double shotFrequency; 
    private double defensiveness;
    private double aggression; 
    private double positioning; 
    private double dribbling;
    private double chanceCreation;
    private double offsideAdherence;
    // added additional attributes only used in the tournement
    private double energy;
    private int yellowCards;
    private int redCards;
    private int goalsScored;
    private int matchesPlayed;

    /**
     * Constructor for Player 
     * @param firstName     // part of Person super class
     * @param surname       // part of Person super class
     * @param team          // part of Person super class
     * @param position
     * @param fitness
     * @param passingAccuracy
     * @param shotAccuracy
     * @param shotFrequency
     * @param defensiveness
     * @param aggression
     * @param positioning
     * @param dribbling
     * @param chanceCreation
     * @param offsideAdherence
     */
    Player(String firstName, String surname, String team, String position, double fitness, double passingAccuracy, double shotAccuracy, double shotFrequency, double defensiveness, double aggression, double positioning, double dribbling, double chanceCreation, double offsideAdherence){
        super(firstName, surname, team);  
        this.position = position;
        this.fitness = fitness; 
        this.passingAccuracy = passingAccuracy;
        this.shotAccuracy = shotAccuracy;
        this.shotFrequency = shotFrequency;
        this.defensiveness = defensiveness;
        this.aggression = aggression;
        this.positioning = positioning;
        this.dribbling = dribbling;
        this.chanceCreation = chanceCreation;
        this.offsideAdherence = offsideAdherence;
        // add additonal attributes that are only used in the tournament
        this.energy = 1;
        this.yellowCards = 0;
        this.redCards = 0;
        this.goalsScored = 0;
        this.matchesPlayed = 0;
    }
 
    // getters
    /**
     * gets the player rating: this is the average of all their attributes
     * @return
     */
    public double getRating() {
        double rating = 0.0;
        // if a player has a red card then Rating is 0 so they can't be selected
        if (redCards > 0) {
            rating = 0.0;
        }else {
            // returns the average of all the ratings multiplied by the energy value which starts at 1
            rating = (((fitness+passingAccuracy+shotAccuracy+shotFrequency+defensiveness+aggression+positioning+dribbling+chanceCreation+offsideAdherence)/11)*energy);
        }
        return rating;
    }
    
    public double getAgressionRating() {
        double rating = 0.0;
        if (this.redCards > 0) {
            rating = 0.0;
        }else {
            // returns the average of all the ratings multiplied by the energy value which starts at 1
            rating = this.aggression;
        }
        return rating;

    }

    // changes the players energy rating by the amount specified
    public void modifyEnergy(double energyChange) {
        if (this.energy + energyChange > 0 && this.energy + energyChange < 1) {
            this.energy = this.energy + energyChange;
        }
    }

    public String getPosition() {
        return this.position;
    }

    public int getYellowCards() {
        return this.yellowCards;
    }

    public void setYellowCards(int cardCount) {
        this.yellowCards = cardCount;
    }

    public int getRedCards() {
        return this.redCards;
    }

    public void setRedCards(int cardCount) {
        this.redCards = cardCount;
    }

    public int getGoalsScored() {
        return this.goalsScored;
    }

    public void setGoalsScored(int goalCount) {
        this.goalsScored = goalCount;
    }

    public int getMatchesPlayed() {
        return this.matchesPlayed;
    }

    public void setMatchesPlayed(int matchCount) {
        this.matchesPlayed = matchCount;
    }

}
