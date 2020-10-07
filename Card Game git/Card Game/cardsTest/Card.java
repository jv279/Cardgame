import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.util.*;

public class Card {
    private List<List<Integer>> Hands;
    private List<List<Integer>> Decks;
    private List<Integer> OrgPack;

    //Constructs the Cards
    public Card(File location) {
        this.OrgPack = packToList(location);
        this.Hands = distribute(this.OrgPack).subList(0, ((distribute(this.OrgPack).size() / 2)));
        this.Decks = distribute(this.OrgPack).subList(((distribute(this.OrgPack).size() / 2)), (distribute(this.OrgPack).size()));
    }

    /**
     * This turns the pack file into a list of Integers
     * @param location this is the location of the pack given by the user
     * @return it returns a List<Integer> consisting of the pack values
     */
    public static List<Integer> packToList(File location) {
        String line;
        List<Integer> values = new ArrayList<Integer>();
        try {
            FileReader fileReader = new FileReader(location);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                values.add(Integer.parseInt(line));
            }
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            CardGame.tempvalidity = false;
        } catch (IOException ex) {
            CardGame.tempvalidity = false;
        }
        return values;
    }


    /**
     * This distributes the cards from the pack to form hands and decks
     * @param Pack - the pack in the form of a list of Integers
     * @return it returns a list of lists of the card values
     */
    public static List distribute(List<Integer> Pack) {
        List fours = new ArrayList<ArrayList<Integer>>();
        List templist = new ArrayList<Integer>();

        for (int i = 0; i < (Pack.size()/4); i++) {
            //this is equivalent of handing out cards 1 by 1.
            int indexFirst = i;
            int indexSecond = i + Pack.size()/4;
            int indexThird = i + Pack.size()/2;
            int indexFourth = i + Pack.size()*3/4;
            templist.add(Pack.get(indexFirst));
            templist.add(Pack.get(indexSecond));
            templist.add(Pack.get(indexThird));
            templist.add(Pack.get(indexFourth));
            fours.add(new ArrayList<Integer>(templist));
            templist.clear();
        }
        return fours;
    }

    /**
     * getHands(), and getDecks() return all hands, and decks in a List of List of Integers
     * getOrgPack() returns the pack without distribution or splitting in a List of Integers
     */
    public List<List<Integer>> getHands(){
        return Hands;
    }

    public List<List<Integer>> getDecks(){
        return Decks;
    }

    public List<Integer> getOrgPack(){
        return OrgPack;
    }

}