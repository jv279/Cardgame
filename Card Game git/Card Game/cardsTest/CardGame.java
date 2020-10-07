import javax.swing.plaf.TableHeaderUI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Scanner;

public class CardGame {
    /**
     * Initialises all the global variables
     */
    public static boolean GameEnd = false;
    public static int WinnerID = 0;
    public static Card pack;
    public static Boolean tempvalidity;
    public static File pathlocation;


    public static void main(String[] args) throws InterruptedException {
        /**
         * Initialises all the local variables
         */
        Scanner scan = new Scanner(System.in);
        String locationstring;
        File location;
        Boolean validpack = false;
        /**
         * Takes and only accepts valid User inputs for the number of players and the Pack File
         */
        System.out.println("Enter the number of players :- ");
        int playerno = scan.nextInt();
        /**
         * Allows the user to input a different number of players if they give an invalid one
         */
        while (playerno < 2) {
            System.out.println("Input invalid - Please enter the number of players");
            playerno = scan.nextInt();
        }
        /**
         * Allows the user to input a different Pack File if they give an invalid one
         */
        while (validpack == false) {
            tempvalidity = true;
            System.out.println("Enter a path to a valid pack file:- ");
            locationstring = scan.next();
            location = new File(locationstring);
            pathlocation = location.getParentFile();
            pack = new Card(location);
            for (int i : pack.getOrgPack()) {
                if (i < 1) {
                    tempvalidity = false;
                }
            }
            if ((pack.getOrgPack()).size() != (8 * playerno)){
                tempvalidity = false;
            }
            validpack = tempvalidity;
        }
        /**
         * Initialises local variables dependent on the user inputs
         */
        List<Player> players = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        List<List<Integer>> hands = pack.getHands();
        List<List<Integer>> decks = pack.getDecks();
        /**
         * Constructs the players by working out the ID, hand, left and right decks, right deck index
         *
         * The right deck index is an argument as it can change inconsistently relative to the PlayerID
         */
        for (int x = 0; x < playerno; x++) {
            List<Integer> hand = hands.get(x);
            List<Integer> ldeck = decks.get(x);
            List<Integer> rdeck;
            Integer rdeckindex;

            if (x == playerno - 1) {
                rdeck = decks.get(0);
                rdeckindex = 1;
            } else {
                rdeck = decks.get(x + 1);
                rdeckindex = x + 2;
            }
            players.add(new Player(x + 1, hand, rdeck, ldeck, rdeckindex));
            //adding player objects into an array
            threads.add(new Thread(players.get(x)));
            //creating an array of threads
            players.get(x).createfile();
        }
        /**
         * Checks if any of the players have been dealt a winning hand
         * If so, it crowns the first player with 4 identical cards in their hand as the winner
         * and ends the game
         *
         * Else it starts threads for each player and runs until the game has ended
         */
        while (GameEnd == false) {
            boolean firstwinner = true;
            Boolean wakeup = true;
            for (int x = 0; x < playerno; x++){
                //checking if a hand has won
                int a = players.get(x).getHand().get(0);
                int b = players.get(x).getHand().get(1);
                int c = players.get(x).getHand().get(2);
                int d = players.get(x).getHand().get(3);

                if ((a == b) && (b == c) && (c == d) && firstwinner){
                    WinnerID = players.get(x).getID();;
                    System.out.println("player "+ (x+1) +" won");
                    firstwinner=false;
                }
            }

            //GameEnd variable is not changed here but is within players so that all players have the same number of goes

            for(Thread i : threads) {
                //this starts all the threads and joins them
                if (i.getState() == Thread.State.NEW) {
                    // if is because we only want to start the threads once
                    i.join();
                    i.start();
                }
                for (Thread y : threads){
                    if (y.getState() == Thread.State.RUNNABLE){
                        wakeup = false;
                    }
                }
                if (wakeup == true){
                    for (Thread x : threads){ x.interrupt();
                    }
                }
            }
            if(!firstwinner){
                CardGame.GameEnd = true;
            }
        }
        /**
         * For each player it writes the outcome of the game on each players' outcome text files
         * For each deck, creates a text file with the Deck ID and it's contents
         */
        for (Player i : players){
                if (WinnerID != i.getID()) {
                    //only run this if its not the winning ID
                    try {
                        PrintWriter pw = new PrintWriter(new FileOutputStream(new File(pathlocation+"/textfiles/player" + i.getID() + "_output.txt"), true));
                        pw.append("player" + WinnerID + " has informed player" + i.getID() + " that player " + WinnerID + " has won");
                        pw.println();
                        pw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                else {
                    //wining player has different final text
                    try {
                        PrintWriter pw = new PrintWriter(new FileOutputStream(new File(pathlocation+"/textfiles/player" + i.getID() + "_output.txt"), true));
                        pw.append("player" + i.getID() + " wins");
                        pw.println();
                        pw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    PrintWriter pw = new PrintWriter(new FileOutputStream(new File(pathlocation+"/textfiles/player" + i.getID() + "_output.txt"), true));
                    pw.append("player" + i.getID() + " exits");
                    pw.println();
                    pw.append("player" + i.getID() + " final hand " + i.getHand());
                    pw.println();
                    pw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // creates all the deck output files.
                i.createDeckFile();

        }
    }

}
