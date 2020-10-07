import org.junit.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class testCard {

    @Before
    /**
     * Creates a mock Pack file with contents
     */
    public void createTestPacks() {
        CardGame.pathlocation = new File(".");
        File testPack = new File(CardGame.pathlocation + "/Pack/testPack.txt");
        try {
            PrintWriter writer = new PrintWriter(testPack);
            writer.print(1+"\n"+2+"\n"+3+"\n"+4+"\n"+5+"\n"+6+"\n"+7+"\n"+8);
            writer.println();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * Converts the Pack file into a List of Integers for a valid file which was previously created
     * and a non-existent (invalid) file
     *
     * Checks List created by the valid file is correct
     *
     * Checks if the Validity is set to false when the file is non-existent
     */
    public void testPackToList() {
        File testPack = new File(CardGame.pathlocation + "/Pack/testPack.txt");
        File nonexistent = new File(CardGame.pathlocation + "/Pack/nonexistent.txt");
        Card testCard = new Card(testPack);
        boolean output = false;
        List<Integer> expectedOrgPack = Arrays.asList(1,2,3,4,5,6,7,8);
        if ((testCard.getOrgPack()).equals(expectedOrgPack)) {
            output = true;
        }
        Card fail = new Card(nonexistent);
        if (CardGame.tempvalidity == true){
            output = false;
        }
        assertTrue(output);
    }

    @Test
    /**
     * Distributes the mock pack file after converting it and tests the resulting decks and hands
     */
    public void testDistribute() {
        List<Integer> expected1 = Arrays.asList(1,3,5,7);
        List<Integer> expected2 = Arrays.asList(2,4,6,8);
        boolean output = false;
        Card a = new Card(new File(CardGame.pathlocation +"/Pack/testPack.txt"));
        if ((Card.distribute(a.getOrgPack()).get(0).equals(expected1))
                && Card.distribute(a.getOrgPack()).get(1).equals(expected2)) {
            output = true;
        }
        assertTrue(output);
    }

    @Test
    /**
     * Tests all the get functions at once by testing the returned list of Hands, and Decks
     * with the original pack in a List
     */

    public void testGet(){
        Card test = new Card(new File(CardGame.pathlocation +"/Pack/testPack.txt"));
        boolean output = false;
        if ((test.getHands().toString().equals("[[1, 3, 5, 7]]")) &&
                (test.getDecks().toString().equals("[[2, 4, 6, 8]]")) &&
                (test.getOrgPack().toString().equals("[1, 2, 3, 4, 5, 6, 7, 8]"))) {
            output = true;
        }
        assertTrue(output);
    }

    @After
    /**
     * Cleans up by deleting all  files created by test
     */
    public void cleanUp () {
        File dir = new File(CardGame.pathlocation +"/Pack");
        File testPack = new File(CardGame.pathlocation +"/Pack/testPack.txt");
        for (File i :dir.listFiles()) {
            if (i.equals(testPack)) {
                i.delete();
            }
        }
    }
}

