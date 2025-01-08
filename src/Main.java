import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static Squad[] squads = new Squad[32];
    public static Match[] groupStage = new Match[(6*8)];                                // 6 matches per group and 8 groups
    public static ArrayList<Squad> squadsArrayList = new ArrayList<Squad>();            // used for tourneyment initial picks 
    public static ArrayList<Squad> squadsShuffledArrayList = new ArrayList<Squad>();    // used for tourneyment initial picks 
    public static ArrayList<GroupStats> groupStats = new ArrayList<GroupStats>(4*8);    // 8 groups
    public static ArrayList<Match> knockoutStage = new ArrayList<Match>();
    public static Comparator<GroupStats> groupComparator = Comparator                   // comparator for ordering the group stage table results
        .comparing(GroupStats::getGroupName)  
        .thenComparing(GroupStats::getPoints,Comparator.reverseOrder())        
        .thenComparing(GroupStats::getGoalsScored,Comparator.reverseOrder())
        .thenComparing(GroupStats::getGoalDifference,Comparator.reverseOrder());  

    public static String cReset = "\u001B[0m";                                          // reset ansi colour
    public static String cRed = "\u001B[31m";
    public static String cGreen = "\u001B[32m";
    public static String cYellow = "\u001B[33m";
    public static String cBlue = "\u001B[34m";
    public static String cMagenta = "\u001B[35m";
   
    public static void main(String[] args){
        String dataFileManager = "Managers.csv";
        String dataFilePlayer = "Players.csv";
        int dataRowsLoaded = 0;
        String[] dataRowColumns = {};
        Manager manager;
        Player player;
        String teamSearch = "";
      
        File managerFile = new File(dataFileManager);                                   // load manager data
        try {          
            Scanner scanner = new Scanner(managerFile);
            System.out.println("**Loading data from: " + dataFileManager + "**");
            while (scanner.hasNextLine()) {
                String dataRow = scanner.nextLine();
                if (dataRowsLoaded == 0) {                                              // first row of the data file contains headings so skip these
                    dataRow = scanner.nextLine();
                    dataRowsLoaded++;
                }
                dataRow = dataRow.replace(", ",",");                // remove the spaces we don't need
                dataRowColumns = dataRow.split("\\,");
                manager = new Manager(dataRowColumns[0], dataRowColumns[1], dataRowColumns[2], dataRowColumns[3], Double.parseDouble(dataRowColumns[4]), Double.parseDouble(dataRowColumns[5]), Double.parseDouble(dataRowColumns[6]), Double.parseDouble(dataRowColumns[7]));
                squads[dataRowsLoaded-1] = new Squad(manager.getTeamName(),manager);
                dataRowsLoaded++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("  an error occured reading the file");
            e.printStackTrace();
        }
        
        dataRowsLoaded = 0;                                                            // load player data
        File playerFile = new File(dataFilePlayer);
        try {          
            Scanner scanner = new Scanner(playerFile);
            System.out.println("**Loading data from: " + dataFilePlayer + "**");
            while (scanner.hasNextLine()) {
                String dataRow = scanner.nextLine();
                if (dataRowsLoaded == 0) {                                             // first row of the data file contains headings so skip these
                    dataRow = scanner.nextLine();
                    dataRowsLoaded++;
                }
                dataRow = dataRow.replace(", ",",");                // remove the spaces we don't need
                dataRowColumns = dataRow.split("\\,");
                player = new Player(dataRowColumns[0], dataRowColumns[1], dataRowColumns[2], dataRowColumns[3], Double.parseDouble(dataRowColumns[4]), Double.parseDouble(dataRowColumns[5]), Double.parseDouble(dataRowColumns[6]), Double.parseDouble(dataRowColumns[7]),Double.parseDouble(dataRowColumns[8]), Double.parseDouble(dataRowColumns[9]), Double.parseDouble(dataRowColumns[10]), Double.parseDouble(dataRowColumns[11]), Double.parseDouble(dataRowColumns[12]), Double.parseDouble(dataRowColumns[13]));               
                teamSearch = player.getTeamName();
                int i;
                for (i=0; i< (squads.length-1);i++ ) {
                    if (squads[i].getTeamName().equals(teamSearch)) {
                        break;
                    }
                }
                squads[i].addPlayer(player);
                dataRowsLoaded++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("  an error occured reading the file");
            e.printStackTrace();
        }

        System.out.println("Initialisation: Colour Test: " + cRed+ "Red " + cGreen + "Green " + cYellow + "Yellow " + cBlue + "Blue " + cMagenta + "Magenta" + cReset);
        int inputCharCount = -1;
        char inputOption = '\n';
        do {         
            System.out.println("Can you see words in different colours above? (Y/N)");                   // test if user can see colours because codes might not work on all platforms
            try {
                inputCharCount = System.in.read();
                if(inputCharCount != -1){
                    inputOption = (char)inputCharCount;
                    inputOption = Character.toUpperCase(inputOption);
                    do{
                        System.in.skip(1);
                        inputCharCount--;
                    }while (inputCharCount > 0);
                }
            }catch(Exception e) {
                System.out.println("Exception error System.in.read");
            }
        } while (!(inputOption == 'Y' || inputOption == 'N'));

        if (inputOption == 'N') {                                                   // if colours not visible to user - remove them from the output
            cReset = "";                                          
            cRed = "";
            cGreen = "";
            cYellow = "";
            cBlue = "";
            cMagenta = "";                        
        }

        runTournament();
    }

    public static Team getTeam(Squad s){
        Team t = new Team(s.getTeamName(), s.getManager());                             // get the formation for this manager split it to get defenders [0], midfielders [1] and forwards [2]
        String[]formation = s.getManager().getFavouredFormation().split("-");     // convert it to an integer array so we can work with it
        int[]formationInt = new int[formation.length+1];
        for (int i=0;i<formation.length;i++) {
            formationInt[i] = Integer.parseInt(formation[i]);
        }                                                                                       
        formationInt[3] = 1;                                                            // add an extra entry for the goal keeper
        ArrayList<Player> squadPlayers = s.getPlayers();
        Collections.sort(squadPlayers, new Comparator<Player>() {                       // Sort players by player rating in descending order
            @Override
            public int compare(Player p1, Player p2) {
                return Double.compare(p2.getRating(), p1.getRating());
            }
        });
        for (Player p : squadPlayers) {
            switch (p.getPosition()) {
                case "Defender" :
                    if(formationInt[0]>0){
                        t.addPlayer(p);
                        formationInt[0]--;
                    }
                    break;
                case "Midfielder" :
                    if(formationInt[1]>0){
                        t.addPlayer(p);
                        formationInt[1]--;
                    }
                    break;
                case "Forward" :
                    if(formationInt[2]>0){
                        t.addPlayer(p);
                        formationInt[2]--;
                    }
                    break;
                case "Goal Keeper" :
                    if(formationInt[3]>0){
                        t.addPlayer(p);
                        formationInt[3]--;
                    }
                break;
            }
            if ((formationInt[0]+formationInt[1]+formationInt[2]+formationInt[3]) == 0) break;              // if we have selected all the players then exit the for
        }
        return t;
    }

    public static int getSquadIndexByTeamName(String teamName){
        int i = 0;
        for (i=0;i<squads.length;i++) {
            if (squads[i].getTeamName().equals(teamName)){
                return i;        
            }
        }
        return -1;
    }

    public static void runTournament(){

        System.out.println(cGreen + "The tournament has started..." + cReset);        
        String groupName = ""; 
        char groupLetter;
        int matchCount = 0;
        // https://en.wikipedia.org/wiki/2022_FIFA_World_Cup#Draw
        /* For the draw, 32 teams were allocated into four pots based on the FIFA Men's World Rankings of 
        *  31 March 2022.[113] Pot 1 contained host Qatar (who were automatically assigned to position A1) 
        *  and the best seven teams. Pot 2 contained the next best eight teams, with the next best eight teams 
        *  into pot 3. Pot 4 contained the five lowest-ranked teams, along with the placeholders for the two 
        *  inter-confederation play-off winners and the UEFA Path A play-off winner
        *  for my implementation I will calculate the strength of each of the squads and order the squads by
        *  average player and manaager strength.  I will use this order to allocate the squads to groups.
        */

        for (int i=0;i<squads.length;i++) {                                      // move the squads into an arraylist so we can order them easily
            squadsArrayList.add(squads[i]);
        }
        Collections.sort(squadsArrayList, Comparator.comparingDouble(Squad::getRating).reversed());     // order the squads by their strength strongest to weakest team
        String hostnation = "Qatar";                                            // find the host nation, because this nees special treatment
        System.out.println(cGreen + " Draw\t" + cReset + hostnation + " is the host nation and is automatically placed in Group A in the strongest team position.");
        int index = -1;
        for (int i = 0; i < squadsArrayList.size(); i++) {
            if (squadsArrayList.get(i).getTeamName().equals(hostnation)) {
                index = i;
                break;
            }
        }     
        if (index != -1) {                                                       // move host nation to first postion in the array
            Squad squadToMove = squadsArrayList.remove(index);                   // Remove the host nation from the array list and keep a reference to it
            squadsArrayList.add(0, squadToMove);                           // Insert the host nation at the beginning of the array list
        }
        System.out.println(cGreen + " Draw\t" + cReset + "Squads split into pots of 8 teams ordered from strongest to weakest");
        System.out.println(cGreen + " Draw\t" + cReset + "Groups selected by randomly drawing one team from each of the pots.");
       
        ArrayList<Squad> subShuffle = new ArrayList<>(); 
        for (int i =0;i<4;i++) {                                                // shuffle squads with array pos 1-7, 8-15, 16-23, 24-31 to simulate the draw of teams
            subShuffle.clear();                                                 // empty the temp array
            for (int j = 0; j< 8;j++) {                                         // deal with in groups of 8
                if (i+j != 0) {
                    subShuffle.add(squadsArrayList.get((i*8)+j));               // host team doesn't get shuffled but others do
                 }
            }
            Collections.shuffle(subShuffle);                                    // shuffle these teams
            if (i==0) subShuffle.add(0,squadsArrayList.get(0));     // add the home nation at the top        
            for (int j = 0; j< 8;j++) {                                         // deal with in groups of 8
                squadsShuffledArrayList.add(subShuffle.get(j));
            }  
        } 

        squadsArrayList.clear();                                                // re-order array so group a is at the top, group b is second etc. group A has one team from each of the 4 - which contain 8 teams
        for (int i= 0; i<8;i++) {
            for (int j = i; j<squadsShuffledArrayList.size();j+=8) {
                squadsArrayList.add(squadsShuffledArrayList.get(j));
            }
        }

        System.out.println(cGreen + "Group draw results:"+ cReset);
        for (int i=0;i<squads.length;i+=4){                                         // allocate groups names and setup matches
            groupLetter = (char) ('A' + (matchCount));
            groupName = "Group " + groupLetter;
            Team t1 = getTeam(squadsArrayList.get(i));
            Team t2 = getTeam(squadsArrayList.get(i+1));
            Team t3 = getTeam(squadsArrayList.get(i+2));
            Team t4 = getTeam(squadsArrayList.get(i+3));
            System.out.println( groupName + ": " + t1.getTeamName() + ", " + t2.getTeamName() + ", "+ t3.getTeamName() + ", "+ t4.getTeamName() + ".");
            
            groupStage[(matchCount*6)] = new Match(t1,t2,groupName);                // set up group matches
            groupStage[(matchCount*6)+1] = new Match(t3,t4,groupName);
            groupStage[(matchCount*6)+2] = new Match(t1,t4,groupName);
            groupStage[(matchCount*6)+3] = new Match(t2,t3,groupName);
            groupStage[(matchCount*6)+4] = new Match(t2,t4,groupName);
            groupStage[(matchCount*6)+5] = new Match(t1,t3,groupName);
            matchCount++;
        }
    
        // for (int i=0; i<groupStage.length;i++) {                                   // show the group matches
        //     System.out.println(groupStage[i].getMatchType() + "\t" + groupStage[i].getMatchTitle());
        //}        

        System.out.println("");
        System.out.println(cGreen + "Group Stage matches start..." + cReset);
        String result = "";
        for (int i=0; i<groupStage.length; i++) {                                   // play the Group Stage Matches
            System.out.print(cGreen + " " + groupStage[i].getMatchType() + cReset + " " + groupStage[i].getMatchTitle());
            result = groupStage[i].playMatch();
            System.out.println(cGreen + " " + result + cReset + " ");
        }       

        String searchTeam = "";
        for (int i=0;i<squads.length;i++) {                                         // create an entry for every squad in the group statistics
            groupStats.add(new GroupStats(squads[i].getTeamName(),""));
        }
        for (Match m: groupStage) {                                                 // update the group tables with data from the matches played
            searchTeam = m.getTeam1Name();                                          // deal with team1
            for (GroupStats gs: groupStats ) {
                if (gs.getTeamName().equals(searchTeam)) {
                    gs.addPoints(m.getTeam1Result());
                    gs.addGoalsScored(m.getTeam1GoalsScored());
                    gs.addGoalDifference(m.getTeam1GoalsScored());
                    gs.setGroupName(m.getMatchType());
                    break;
                }
            }
            searchTeam = m.getTeam2Name();                                          // deal with team2
            for (GroupStats gs: groupStats ) {
                if (gs.getTeamName().equals(searchTeam)) {
                    gs.addPoints(m.getTeam2Result());
                    gs.addGoalsScored(m.getTeam2GoalsScored());
                    gs.addGoalDifference(m.getTeam2GoalsScored());
                    gs.setGroupName(m.getMatchType());
                    break;
                }
            }
        }
        
        groupStats.sort(groupComparator);                                           // order the group stats
        System.out.println("");                                                   
        System.out.println(cGreen + "Group Stage Results" + cReset);
        String tableRow = "";
        tableRow = "Group\tTeam          \tPoints\tGoals\tGdiff\t";                 // output the group tables
        tableRow = tableRow + tableRow;
        System.out.println(tableRow);
        int rowCount = 0; 
        for (int i=0;i<groupStats.size();i+=8) {                                    // print out the group tables in two columns
            for (int j=0;j<4;j++) {
                tableRow = "";              
                if  (rowCount%4 == 0 || rowCount%4 == 1) {                          // change the colour of the top two rows as these teams go through to the knockout rounds
                    tableRow = tableRow + cGreen;
                }
                for (int k=0;k<2;k++) {                                             // display group n and group n+1 side by side
                    tableRow = tableRow + groupStats.get(i+j+(k*4)).getGroupName() + "\t" + groupStats.get(i+j+(k*4)).getTeamName();
                    for (int l=0; l<(15-groupStats.get(i+j+(k*4)).getTeamName().length());l++) {
                        tableRow = tableRow + " ";
                    }
                    tableRow = tableRow + "\t" + groupStats.get(i+j+(k*4)).getPoints() + "\t"; 
                    tableRow = tableRow + groupStats.get(i+j+(k*4)).getGoalsScored() + "\t" + groupStats.get(i+j+(k*4)).getGoalDifference() + "\t";
                }
                if  (rowCount%4 == 0 || rowCount%4 == 1) {
                    tableRow = tableRow + cReset;
                }
                System.out.println(tableRow);
                rowCount++;
            }
        }
        System.out.println("");                                                   
        System.out.println(cGreen + "Knockout Matches Start..." + cReset);
        // first round knockout is different from others winner from group a plays runner up from group b, winner from group b plays runner up from group a
        // take teams in batches of 8 from the group stage data
        matchCount = 0;
        for (int i=0;i<groupStats.size();i=i+8){                                // create knockout round 1 schedule
            Team t1 = getTeam(squads[getSquadIndexByTeamName(groupStats.get((matchCount*8)+0).getTeamName())]);  // arraypos 0 plays arraypos 5
            Team t2 = getTeam(squads[getSquadIndexByTeamName(groupStats.get((matchCount*8)+5).getTeamName())]);  // arraypos 0 plays arraypos 5
            knockoutStage.add(new Match(t1,t2,"Knockout Round 1"));
            Team t3 = getTeam(squads[getSquadIndexByTeamName(groupStats.get((matchCount*8)+1).getTeamName())]);  // arraypos 1 plays arraypos 4
            Team t4 = getTeam(squads[getSquadIndexByTeamName(groupStats.get((matchCount*8)+4).getTeamName())]);  // arraypos 1 plays arraypos 4
            knockoutStage.add(new Match(t3,t4,"Knockout Round 1"));
            matchCount++;
        }

        // play the matches for the knockout rounds using knockoutStage like a queue,add the winners of the games, in pairs to the next round
        String matchType = "";
        matchCount = 0;
        result = "";
        for (int i=0; i<knockoutStage.size(); i++) {                        // keep going until no more matches to play
            matchCount++;                                                   // track which match number this is
            if (knockoutStage.size() >= 8) {                                // use matchcount to name each round
                matchType="Quarter-Final";
            }  
            if (knockoutStage.size() >= 12) {
                matchType = "Semi-Final";
            }
            if (knockoutStage.size() >= 14) {
                matchType = "Final";
            } 
            if (knockoutStage.get(i).getIsComplete() == false) {                // ignore Matches already played
                System.out.print(cGreen +" "+ knockoutStage.get(i).getMatchType() + cReset + " " + knockoutStage.get(i).getMatchTitle());
                result = knockoutStage.get(i).playMatch();                      // play the match
                System.out.print(cGreen + " " + result + cReset + " ");
                System.out.println(""); 
                if (i>0 && i%2 == 1) {                                          // if we have completed two matches then we can schedule the next match by additing it to the ArrayList
                    if (matchType == "Semi-Final") {                            // remove red and yellow cards before the semi-finals
                        squads[getSquadIndexByTeamName(knockoutStage.get(i-1).getWinningTeam())].setCardsZero();
                        squads[getSquadIndexByTeamName(knockoutStage.get(i).getWinningTeam())].setCardsZero();
                    }
                    if (matchType == "Final" && knockoutStage.size() == 14) {   // if we have just finished the semi-final then we must also arrange the 3rd place play off
                        squads[getSquadIndexByTeamName(knockoutStage.get(i-1).getLosingTeam())].setCardsZero();
                        squads[getSquadIndexByTeamName(knockoutStage.get(i).getLosingTeam())].setCardsZero();
                        Team t3 = getTeam(squads[getSquadIndexByTeamName(knockoutStage.get(i-1).getLosingTeam())]);
                        Team t4 = getTeam(squads[getSquadIndexByTeamName(knockoutStage.get(i).getLosingTeam())]);
                        knockoutStage.add(new Match(t3,t4,"3rd Place Playoff")); 
                    }
                    if (knockoutStage.get(i).getMatchType() != "3rd Place Playoff" && knockoutStage.size() < 16) {  // last two winners play each other except if this is the final or this is the 3rd place playoff
                        Team t1 = getTeam(squads[getSquadIndexByTeamName(knockoutStage.get(i-1).getWinningTeam())]);
                        Team t2 = getTeam(squads[getSquadIndexByTeamName(knockoutStage.get(i).getWinningTeam())]);                
                        knockoutStage.add(new Match(t1,t2,matchType)); 
                    }
                }
            }
        }
    }
}