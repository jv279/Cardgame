import org.junit.*;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;
import java.util.Scanner;

public class testPlayer {

    @Before
    //Emulates the userinput for the Path of the Pack file
    public void fixedfile(){
        CardGame.pathlocation = new File(".");
    }

    @Test
    /**
     * Creates a mock file to check if Player.createFile clears the textfiles directory of all
     * output files from previous compilations
     *
     * Creates a mock player and checks if their output file is correctly initialised
     *
     * if any of the tests fail, the output variable is set as false, causing an Assertion error
     */
    public void testCreateFile() {
        File deletionTest = new File("./textfiles/deletionTest_output.txt");
        try {
            deletionTest.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Integer> hand = Arrays.asList(1, 3, 5, 7);
        boolean output = true;

        Player test = new Player(0, hand, null, null, 0);
        File dir = new File("./textfiles");
        test.createfile();

        String line;
        for (File i : dir.listFiles()) {
            if (i.getName().equals("deletionTest_output.txt")) {
                output = false;
                continue;
            }
            try {
                Scanner scanner = new Scanner(i);
                line = scanner.nextLine();
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                output = false;
                continue;
            }
            if (line.equals("player0 initial hand [1, 3, 5, 7]")) {
                continue;
            } else {
                output = false;
            }
        }
        assertTrue(output);
    }

    @Test
    /**
     * Plays a turn in two different scenarios
     *
     * 1) Where there isn't a preference for the first card that is considered to be discarded
     *      ldeck = left deck
     *      rdeck = right deck
     * 2) Where there is
     *      al = Alternative scenario hands, and decks
     * This is done by initialising two Players in those scenarios, creating their files and
     * running their turns
     *
     * Then this tests whether the output files are correctly updated:
     *      As the first scenario is deleted by the seconds createFile method, we only test
     *      the second scenario. As the change in choice doesn't affect the test
     * It then checks the output file after one turn line by line and sets output to false if
     * there are any differences
     *
     * if the output remains true throughout the test, it has passed
     *
     */
    public void testTurn() {
        boolean output = true;
        List<Integer> ldeck = new LinkedList<>(Arrays.asList(1,2,3,4));
        List<Integer> hand = new LinkedList<>(Arrays.asList(5,6,7,8));
        List<Integer> rdeck = new LinkedList<>(Arrays.asList(1,2,3,4));

        List<Integer> alHand = new LinkedList<>(Arrays.asList(0,6,7,8));
        List<Integer> alLdeck = new LinkedList<>(Arrays.asList(1,2,3,4));
        List<Integer> alRdeck = new LinkedList<>(Arrays.asList(1,2,3,4));

        List<Integer> exLdeck = new LinkedList<>(Arrays.asList(2,3,4));
        List<Integer> exHand = new LinkedList<>(Arrays.asList(6,7,8,1));
        List<Integer> exRdeck = new LinkedList<>(Arrays.asList(1,2,3,4,5));

        List<Integer> exAlLdeck = new LinkedList<>(Arrays.asList(2,3,4));
        List<Integer> exAlHand = new LinkedList<>(Arrays.asList(0,7,8,1));
        List<Integer> exAlRdeck = new LinkedList<>(Arrays.asList(1,2,3,4,6));

        Player test = new Player(0, hand, rdeck, ldeck, 0);
        test.createfile();
        test.turn();
        if (!(test.getHand().equals(exHand)) || !(test.getLdeck().equals(exLdeck)) || !(test.getRdeck().equals(exRdeck))) {
            output = false;
        }

        Player test2 = new Player(0, alHand, alRdeck, alLdeck, 0);
        test2.createfile();
        test2.turn();

        if (!(test2.getHand().equals(exAlHand)) || !(test2.getLdeck().equals(exAlLdeck)) || !(test2.getRdeck().equals(exAlRdeck))) {
            output = false;
        }


        List<String> expectedFileContents = new LinkedList<>(Arrays.asList(
                "player0 initial hand [0, 6, 7, 8]",
                "player0 draws a 1 from deck 0",
                "player0 discards a 6 to deck 0",
                "player0 current Hand [0, 7, 8, 1]", "",
                "player0 draws a 2 from deck 0",
                "player0 discards a 7 to deck 0",
                "player0 current Hand [0, 8, 1, 2]"));
        File player0File = new File("./textfiles/player0_output.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(player0File))) {
            String line;
            int i = 0;
            while (((line = br.readLine()) != null)&&(i<8)) {
                if (!(expectedFileContents.get(i).equals(line))) {
                    output = false;
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(output);
    }

    @Test
    /**
     * Checks if the createDeckFile is correct by creating a mock player, and deck.
     * Creates the deck file and checks it by comparing it's contents to the correct string
     *
     * if it is the same, the output variable remains true and the test passes
     */
    public void testCreateDeckFile (){
        List<Integer> ldeck = new LinkedList<>(Arrays.asList(1,2,3,4));
        Player test = new Player (0, null, null, ldeck, 0);
        test.createDeckFile();
        File testDeckFile = new File(CardGame.pathlocation + "/textfiles/Deck0_output.txt");
        String line;
        Boolean output = true;
        try {
            Scanner scanner = new Scanner(testDeckFile);
            line = scanner.nextLine();
            scanner.close();
            if (!(line.equals("Deck0 contents [1, 2, 3, 4]"))) {
                output = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            output = false;
        }
        assertTrue(output);
    }



    @Test
    /**
     * Tests all the get functions at once by creating a player and checking the returned hands, decks, and ID
     */
    public void testGet(){
        List<Integer> hand = Arrays.asList(1,3,5,7);
        List<Integer> ldeck = Arrays.asList(2,4,6,8);
        List<Integer> rdeck = Arrays.asList(9,10,11,12);
        Player test = new Player(0, hand, rdeck, ldeck, 0);
        boolean output = false;
        if ((test.getHand() == hand) && (test.getRdeck() == rdeck) && (test.getLdeck() == ldeck) && (test.getID() == 0)) {
            output = true;
        }
        assertTrue(output);

    }


    @After
    //Cleans up by removing the file that was needed for testing
    public void cleanUp () {
        File dir = new File("./textfiles");
        try{
        for (File i :dir.listFiles()) {
            if (i.getName().contains("player0_output.txt")) {
                i.delete();
            }
        }
        }catch(NullPointerException e){}
    }
}


