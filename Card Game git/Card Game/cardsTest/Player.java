// importing
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;


public class Player implements Runnable {
    private int ID;
    private List<Integer> hand;
    private List<Integer> rightDeck;
    private List<Integer> leftDeck;
    private int rdeckindex;
    /**
     * Constructs the Player object
     *
     * @param ID            player number
     * @param hand          player hand
     * @param rightDeck     discard deck
     * @param leftDeck      pick up deck
     * @param rdeckindex    deck index
     */
    public Player(int ID, List<Integer> hand, List<Integer> rightDeck, List<Integer> leftDeck, int rdeckindex) {
        this.ID = ID;
        this.hand = hand;
        this.rightDeck = rightDeck;
        this.leftDeck = leftDeck;
        this.rdeckindex = rdeckindex;
    }
    /**
     * It removes the existing output files (formed from previous compilations) in the directory first
     * Initialises text files for each player with their initial hand
     * Is only run once per game
     */
    public void createfile() {
        // <2 so that it only runs once when the game is simulated in both testing and running CardGame
        if(ID < 2) {
            File dir = new File(CardGame.pathlocation+"/textfiles");
            if(dir.exists()){
            for (File file : dir.listFiles()){
                if (!file.isDirectory()&& file.getName().contains("_output.txt"))
                    file.delete();
            }
            }
            else {
                dir.mkdir();
            }
        }

        try {
            //this is just naming the file playerN.txt where N is the ID
            File file = new File(CardGame.pathlocation+ "/textfiles/player" + ID + "_output.txt");
            PrintWriter writer = new PrintWriter(file);
            writer.append("player"+ID+" initial hand " +hand);
            writer.println();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Allows the player to pick up a card and assess which card to discard
     * This is done based upon whether the player has a preference for a card
     * They go through their hand until they notice a card that isn't the same as their ID
     * It then removes that card
     *
     * Provided all players have had the same number of turns, Deck and Hand sizes remain consistently 4
     *
     * This method is not called if the player has a winning hand
     */
    public synchronized void turn() {
        int drawCard = leftDeck.get(0);
        hand.add(leftDeck.get(0));
        leftDeck.remove(0);
        //This is the Draw
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i) != ID) {
                int discardCard = hand.get(i);
                rightDeck.add(hand.get(i));
                hand.remove(i);
                //This is the Discard
                try {
                    PrintWriter pw = new PrintWriter(new FileOutputStream(new File(CardGame.pathlocation.toString()+"/textfiles/player" + ID + "_output.txt"), true));
                    pw.append("player" + ID + " draws a " + drawCard + " from deck " + ID);
                    pw.println();
                    pw.append("player" + ID + " discards a " + discardCard + " to deck " + rdeckindex);
                    pw.println();
                    pw.append("player" + ID + " current Hand " + hand);
                    pw.println();
                    pw.append("");
                    pw.println();
                    pw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        rightDeck.add(hand.get(0));
        hand.remove(4);
        return;
    }
    /**
     * This creates the text files for the final decks
     */
    public void createDeckFile() {
        String locationstring = "/textfiles/Deck" + ID + "_output.txt";
        try {
            File file = new File(CardGame.pathlocation.toString()+locationstring);
            PrintWriter writer = new PrintWriter(file);
            writer.print("Deck"+ID+" contents " +leftDeck);
            writer.println();
            writer.close();
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Allows the hand of the Player to escape as a List of Integers
     *
     * @return List<Integer> hand
     */
    public synchronized List<Integer> getHand() {
        return hand;
    }
    /**
     * Allows the deck to the left of the Player to escape as a List of Integers
     *
     * @return List<Integer> leftDeck
     */
    public synchronized List<Integer> getLdeck() {
        return leftDeck;
    }
    /**
     * Allows the deck to the right of the Player to escape as a List of Integers
     *
     * @return List<Integer> rightDeck
     */
    public synchronized List<Integer> getRdeck() {
        return rightDeck;
    }
    public synchronized Integer getID() {
        return ID;
    }

    /**
     * Delays the threads before starting the turn to make sure all hands and decks are complete
     * Checks if the player has won
     *
     * This only runs if the game hasn't already ended
     */
    @Override
    public synchronized void run() {
        while (!CardGame.GameEnd) {
            if (hand.get(0) == hand.get(1) && hand.get(1) == hand.get(2) && hand.get(3) == hand.get(2)) {
                CardGame.GameEnd = true;
            }
            else {
                turn();
                if (hand.get(0) == hand.get(1) && hand.get(1) == hand.get(2) && hand.get(3) == hand.get(2)) {
                    CardGame.GameEnd = true;
                }

                try {
                    wait(3000000);
                } catch (InterruptedException e) {
                   continue;
                }
            }
            }
        }
}



