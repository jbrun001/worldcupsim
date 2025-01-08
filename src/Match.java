public class Match {

    Team t1;
    Team t2;
    String matchType;
    Boolean isComplete;
    int team1Result;
    int team2Result;
    int team1GoalsScored;
    int team2GoalsScored;
    int team1GoalDifference;
    int team2GoalDifference;
    String winningTeam;

    Match (Team t1, Team t2, String matchType) {
        this.t1 = t1;
        this.t2 = t2;
        this.matchType = matchType;
        isComplete = false; 
        team1Result = 0;
        team2Result = 0;
        team1GoalsScored = 0;
        team2GoalsScored = 0;
        team1GoalDifference = 0;
        team2GoalDifference = 0;
        winningTeam = "";
    }

    /**
     * gets the rating for the team - can be used multiple times in the match
     * @param t0
     * @param playerType - "" for all of the team, "Defender" for just the defenders, "Forward" for just the forwards etc.
     * @return
     */
    private Double getTeamRating(Team t0, String playerType) {
        Double rating;
        rating = 0.0;
        for (Player p: t0.getPlayers()){
            if (playerType.equals("")) {
                rating += p.getRating();
            }
            else if (playerType.equals("Defender")) {
                rating += p.getRating();                
            }
            else if (playerType.equals("Forward")) {
                rating += p.getRating();                
            }
            else if (playerType.equals("Midfielder")) {
                rating += p.getRating();                
            }
            else if (playerType.equals("Goal Keeper")) {
                rating += p.getRating();                
            }
        }
        // if getting the whole team then get the average otherwise get the total scores
        if (playerType.equals("")) {
            rating = rating / t0.getPlayers().size();
        }
        // adjust team score for manager respect and belief
        rating = rating + t0.getManager().getBelief();
        rating = rating + t0.getManager().getRespect();
        return rating;
    }

    private Double getTeamAgression(Team t0) {
        Double rating;
        rating = 0.0;
        for (Player p: t0.getPlayers()){
            rating += p.getAgressionRating();
        }
        // get the average 
        rating = rating / t0.getPlayers().size();
        return rating;
    }

    public String getMatchType() {
        return this.matchType;
    }

    public int getTeam1Result() {
        return this.team1Result;
    }

    public int getTeam2Result() {
        return this.team2Result;
    }

    public int getTeam1GoalsScored() {
        return this.team1GoalsScored;
    }

    public int getTeam2GoalsScored() {
        return this.team2GoalsScored;
    }

    public String getMatchTitle () {
        return t1.getTeamName() + " vs " + t2.getTeamName();
    }

    public String getTeam1Name() {
        return t1.getTeamName();
    }

    public String getTeam2Name() {
        return t2.getTeamName();
    }

    public boolean getIsComplete() {
        return this.isComplete;
    }

    public String getWinningTeam() {
        return this.winningTeam;
    }

    public String getLosingTeam() {
        if (getWinningTeam() == t1.getTeamName()) return t2.getTeamName();
        else return t1.getTeamName();
    }

    public void updateTeam1GoalsScored (int goalsScored){
        this.team1GoalsScored = this.team1GoalsScored+goalsScored;
    }

    public void updateTeam2GoalsScored (int goalsScored){
        this.team2GoalsScored = this.team2GoalsScored+goalsScored;
    }

    /**
     * playSession - this runs a playing session, in which one attempt on goal is made. Some will result in a goal, some will not
     *              a random number between 0 and goalUpper is calculated, if the current team strength is greater than
     *              this number then the attack was successful and a goal was scored.
     *              after each playSession all the players in the team have their energy reduced
     *              different parameters are used for normal match time, extra time and the two possible penalty play sessions.
     * @param duration
     * @param t1Rating
     * @param t2Rating
     * @param goalUpper
     * @param cardUpper
     * @param energyChange
     */
    private void playSession(int duration, double t1Rating, double t2Rating, double goalUpper, double cardUpper, double energyChange){
        double goalChance = 0.0;
        double cardChance = 0.0;
        String attackingPlayer = "";
        String defendingPlayer = "";
        String scorerText = "";
        String defenderText = "";
        int currentCards = 0;
        for (int i=0;i<duration;i++){
            attackingPlayer = t1.getAttacker();                         // which player is making the attack
            defendingPlayer = t2.getDefender();                         // which player is defending
            goalChance = Math.random()*goalUpper;
            scorerText = "";
            defenderText = "";                                          // calcualte goal chance for team 1
            if (goalChance < t1Rating) {
                updateTeam1GoalsScored(1);                  // update the match goalcount
                t1.getPlayer(attackingPlayer).setGoalsScored(t1.getPlayer(attackingPlayer).getGoalsScored()+1); // update the match goalcount
                scorerText = attackingPlayer + "(1)";
            } else {                                                        // if there wasn't a goal was there a yellow card?
                cardChance = Math.random()*cardUpper;
                if (cardChance <= getTeamAgression(t1)) {
                    currentCards = t2.getPlayer(defendingPlayer).getYellowCards();
                    if (currentCards == 0) {
                        t2.getPlayer(defendingPlayer).setYellowCards(1);
                        defenderText = defendingPlayer + "(" + Main.cYellow + "█" + Main.cReset +")";
                    }      
                    else {                                                  // already has one yellow card - so make it a red card 
                        t2.getPlayer(defendingPlayer).setYellowCards(0);
                        t2.getPlayer(defendingPlayer).setRedCards(1);
                        defenderText = defendingPlayer + "(" + Main.cRed + "█" + Main.cReset +")";
                    }
                }
            }
            //System.out.println("\t>Attack by: " + t1.getTeamName() + "(" + ((double)((int)(t1Rating*100))/100) + "/"+ ((double)((int)(goalChance*100))/100) + ") \t" + team1GoalsScored + scorerText + defenderText);
            if (scorerText.length() > 0 || defenderText.length() > 0) System.out.print(" " + scorerText + defenderText);
            attackingPlayer = t2.getAttacker();                         // which player is making the attack
            defendingPlayer = t1.getDefender();                         // which player is defending
            goalChance = Math.random()*goalUpper;                       // calcualte goal chance for team 2
            scorerText = "";
            defenderText = "";
            if (goalChance < t2Rating) {
                updateTeam2GoalsScored(1);
                t2.getPlayer(attackingPlayer).setGoalsScored(t2.getPlayer(attackingPlayer).getGoalsScored()+1); // update the match goalcount
                scorerText = attackingPlayer + "(1)";
            } else {                                                        // if there wasn't a goal was there a yellow card?
                cardChance = Math.random()*cardUpper;
                if (cardChance <= getTeamAgression(t1)) {
                    currentCards = t1.getPlayer(defendingPlayer).getYellowCards();
                    if (currentCards == 0) {
                        t1.getPlayer(defendingPlayer).setYellowCards(1);
                        defenderText = defendingPlayer + "(" + Main.cYellow + "█" + Main.cReset +")";
                    }      
                    else {                                                  // already has one yellow card - so make it a red card 
                        t1.getPlayer(defendingPlayer).setYellowCards(0);
                        t1.getPlayer(defendingPlayer).setRedCards(1);
                        defenderText = defendingPlayer + "(" + Main.cRed + "█" + Main.cReset +")";
                    }
                }
            }
            //System.out.println("\t>Attack by: " + t2.getTeamName() + "(" + ((double)((int)(t2Rating*100))/100) + "/"+ ((double)((int)(goalChance*100))/100) + ") \t" + team2GoalsScored + scorerText + defenderText);
            if (scorerText.length() > 0 || defenderText.length() > 0) System.out.print(" " + scorerText + defenderText);
        }

        // update energy levels for players for each playing session - if players have full time, extra time and penalties then
        // they get 3 reductions.
        this.t1.modifyEnergyForAllTeamMembers(energyChange);
        this.t2.modifyEnergyForAllTeamMembers(energyChange);
    }
    
    public String playMatch () {
        String result = "";
        // normal play
        System.out.println("");
        playSession(4,getTeamRating(t1,""),getTeamRating(t2,""),6.0,10.0,-0.1);
        // knockout rounds have extra time if a draw and penalties if still a draw after that
        if (!getMatchType().substring(0,5).equals("Group")) {      
            if (this.team1GoalsScored == this.team2GoalsScored) {
                System.out.print(" " + Main.cYellow + "FT: " +  team1GoalsScored + "-" + team2GoalsScored + Main.cBlue + " ");
                // Extra time
                playSession(2,getTeamRating(t1,""),getTeamRating(t2,""),5.0,10.0,-0.2);
                System.out.print(Main.cBlue + " ET:" + team1GoalsScored + "-" + team2GoalsScored + Main.cMagenta + " ");
            }
            // penalties if still a draw
            if (this.team1GoalsScored == this.team2GoalsScored) {
                // first round pentaltes
                playSession(5,getTeamRating(t1,""),getTeamRating(t2,""),4.0,100.0,-0.1);
                System.out.print(Main.cMagenta + " Penalties: " + team1GoalsScored + "-" + team2GoalsScored);
            }
            // if still a draw after 5 penalties take one each until one team has a lead
            while ((this.team1GoalsScored == this.team2GoalsScored)) {
                // second round penalties
                playSession(1,getTeamRating(t1,""),getTeamRating(t2,""),4.0,100.0,-0.1);
                System.out.print(Main.cMagenta + " " + team1GoalsScored + "-" + team2GoalsScored);
            }
        }
        if (this.team1GoalsScored > this.team2GoalsScored) {
            if (!getMatchType().substring(0,5).equals("Group")) {
                result = t1.getTeamName() + " wins";
            } else {
                result = t1.getTeamName() + " wins 3 points.";
            }    
            team1Result = 3;
            team2Result = 0;
            winningTeam = t1.getTeamName(); 
            // modify the attibutes for the manager based on the result
            t1.getManager().modifyBelief(0.1);
            t1.getManager().modifyRespect(0.15);
            t2.getManager().modifyBelief(-0.1);
            t2.getManager().modifyRespect(-0.15);
//System.out.println("\t" + t1.getTeamName() + " New Rating: " + getTeamRating(t1,"") + " Belief raised: " + t1.getManager().getBelief() + " Respect raised: " + ((double)((int)(t1.getManager().getRespect()*100))/100));
//System.out.println("\t" + t2.getTeamName() + " New Rating: " + getTeamRating(t2,"") + " Belief lowered: " + t2.getManager().getBelief() + " Respect lowered: " + t2.getManager().getRespect());

        } else if (this.team2GoalsScored > this.team1GoalsScored) {
            if (!getMatchType().substring(0,5).equals("Group")) {
                result = t2.getTeamName() + " wins";
            } else {
                result = t2.getTeamName() + " wins 3 points.";
            }    
            team1Result = 0;
            team2Result = 3; 
            winningTeam = t2.getTeamName(); 
            // modify the attibutes for the manager based on the result
            t2.getManager().modifyBelief(0.1);
            t2.getManager().modifyRespect(0.15);
            t1.getManager().modifyBelief(-0.1);
            t1.getManager().modifyRespect(-0.15);
//System.out.println("\t" + t2.getTeamName() + " New Rating: " + getTeamRating(t2,"") + " Belief raised: " + t2.getManager().getBelief() + " Respect raised: " + t2.getManager().getRespect());
//System.out.println("\t" + t1.getTeamName() + " New Rating: " + getTeamRating(t1,"") + " Belief lowered: " + t1.getManager().getBelief() + " Respect lowered: " + t1.getManager().getRespect());
        } else {
            result = t1.getTeamName() + " and " + t2.getTeamName() + " draw 1 point each";
            team1Result = 1;
            team2Result = 1;
            winningTeam = "draw"; 
        }
 System.out.print(" " + Main.cReset + Main.cRed + t1.getTeamName() + " " + team1GoalsScored + "-" + team2GoalsScored + " " + t2.getTeamName() +"\u001B[0m" );
        this.team1GoalDifference = this.team1GoalsScored - this.team2GoalsScored;
        this.team2GoalDifference = this.team2GoalsScored - this.team1GoalsScored;
        this.isComplete = true;
        return result;
    }
}
