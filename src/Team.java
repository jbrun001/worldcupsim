import java.util.Random;

public class Team extends Squad{

    Team(String teamName, Manager manager) {
        super(teamName, manager);
    }

    public void modifyEnergyForAllTeamMembers(double energy) {
        for (Player player : getPlayers()) {
            // modify the energy in the team so that when the player is selected for another 
            // match it has an impact
            player.modifyEnergy(energy);
        }
    }

    // this gets the surname of an attacking player could be a midfielder or a forward
    public String getAttacker() {   
        Random rand = new Random();    
        int randomPlayer = rand.nextInt(5);
        int currentPlayer = 0; 
        for (Player player : getPlayers()) {
            if (("Forward".equals(player.getPosition())||("Midfielder".equals(player.getPosition())))) {
                if (currentPlayer == randomPlayer) {
                    return player.getSurname();
                }
            currentPlayer++;
            }
        }
        return "no attackers found";        
    }

    // this gets the surname of the defending player - can be a midfielder or a defender
    public String getDefender() {       
        Random rand = new Random();    
        int randomPlayer = rand.nextInt(5);
        int currentPlayer = 0; 
        for (Player player : getPlayers()) {
            if (("Defender".equals(player.getPosition())||("Midfielder".equals(player.getPosition())))) {
                if (currentPlayer == randomPlayer) {
                    return player.getSurname();
                }
            currentPlayer++;
            }
        }
        return "no defenders found";        
    }

}
