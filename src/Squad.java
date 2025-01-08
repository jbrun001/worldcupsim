import java.util.ArrayList;

public class Squad {
    private String teamName;
    private ArrayList<Player> players;
    private Manager manager;

    Squad(String teamName, Manager manager){
        this.teamName = teamName;
        this.manager = manager;
        players = new ArrayList<>();
    }

    @Override
    public String toString() {
        String squadAsString;
        squadAsString = "\u001B[31m" + getTeamName() + "\u001B[0m\t" + "Manager: " + manager.getSurname() + "\t" + "Formation: " + manager.getFavouredFormation() + "\n";
        squadAsString = squadAsString + "Players\n";
        for (Player p : players) {
            squadAsString = squadAsString + p.getSurname() + "\t Position: " + p.getPosition() + "\t Rating: " + p.getRating() + "\n";
        }
        return squadAsString;
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    //get a player object by surname
    public Player getPlayer(String surname){
        for(Player p: players){
            if(p.getSurname().equals(surname)){
                return p;
            }
        }
        return null;
    }

    //get a player object by index
    public Player getPlayer(int n){
        return players.get(n);
    }

    public String getTeamName() {
        return teamName;
    }

    public Manager getManager() {
        return manager;
    }

    // getPlayers gets all the players in the squad
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public double getRating() {
        double rating = 0.0;
        ArrayList<Player> squadPlayers = getPlayers();
        for (Player p : squadPlayers) {
            rating = rating + p.getRating();
        }
        rating=rating+getManager().getBelief()+getManager().getRespect();
        return rating;        
    }

    // this removes any yellow or red cards from players in the squad - used after the quater finals
    public void setCardsZero() {       
        for (Player player : getPlayers()) {
            player.setYellowCards(0);
            player.setRedCards(0);
        }
    }
}
