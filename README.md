# Running the program

This program must be run with Java 11.

The program's source and bytecode output .class files are within the jar archive itself.

The program is run by executing the following command at the command line.

    $ java -jar cards.jar

# Running the test suite

Tests source files are inside the "test" directory. The tests' bytecode output .class files are within the zip archive's base directory.

The .jar files required to run the JUnit-4.13.2 tests are provided in the base directory of cardsTest.zip

To run the test suite,

- Extract the cardsTest.zip archive to a folder
- Open a terminal and navigate to the folder
- Run the following command


    $ java -cp junit-4.13.2.jar:hamcrest-core-1.3.jar:. org.junit.runner.JUnitCore TestPrimer