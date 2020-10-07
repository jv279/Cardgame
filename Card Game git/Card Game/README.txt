================================TESTING INSTRUCTIONS======================================

1) 
Unzip the cardsTest file and make note of the destination.
2)
Navigate to the destination in the terminal using cd "[your unzipped file destination]"
NOTE:: This destination if not renamed should end in "/Test"
3)
Enter the following line into the terminal if you are on a WINDOWS
java -cp .;junit-4.12.jar;hamcrest-core-1.3.jar org.junit.runner.JUnitCore testSuite
Or enter the following line into the terminal if you are on a MAC OR LINUX
java -cp .:junit-4.12.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore testSuite
4)
This should then run the test. 
If it doesn't, you may want to use the following command inserting a destination to the jdk "\bin" file
the following example is for the jdk1.5.0_09 version:
set path=%path%;C:\Program Files\Java\jdk1.5.0_09\bin
then repeat these instructions again