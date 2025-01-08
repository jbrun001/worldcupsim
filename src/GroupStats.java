/* class to store the results of the group matches so that the group results table can be displayed */
public class GroupStats {
    private String teamName;
    private String groupName;
    private int points;
    private int goalsScored;
    private int goalDifference;

    public GroupStats(String teamName, String groupName) {
        this.teamName = teamName;
        this.groupName = groupName;
        this.points = 0;
        this.goalsScored = 0;
        this.goalDifference = 0;
    }

    public void setGroupName(String groupName){
        this.groupName = groupName;
    }

    public void addPoints(int points){
        this.points += points;
    }

    public void addGoalsScored(int goalsScored){
        this.goalsScored += goalsScored;
    }

    public void addGoalDifference(int goalsDifference){
        this.goalDifference += goalsDifference;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public int getPoints() {
        return this.points;
    }
    
    public int getGoalsScored() {
        return this.goalsScored;
    }

    public int getGoalDifference() {
        return this.goalDifference;
    }


}
