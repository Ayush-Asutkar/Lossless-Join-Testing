import java.util.Scanner;

public class Main {
    private static final AttributesAndFD attributesFD = new AttributesAndFD();

    public static void main(String[] args) {
        System.out.println(Colors.ANSI_RED + "\n\n******For this code, please keep the attributes only of single letter characters.******\n" + Colors.ANSI_RESET);

        Scanner sc = new Scanner(System.in);

        System.out.println(Colors.ANSI_RESET + "Enter all the attributes (space separated): ");
        String input = sc.nextLine();
        attributesFD.addAttributes(input);

        System.out.println(Colors.ANSI_PURPLE + attributesFD);

        System.out.print(Colors.ANSI_RESET + "\nEnter the number of functional dependencies: ");
        int sizeOfFD = sc.nextInt();
        String backslash = sc.nextLine();
        attributesFD.initializeFD(sizeOfFD);


        System.out.println(Colors.ANSI_RESET + "\nEnter the functional dependency in following format");
        System.out.println("Format:- AB->D");

        for(int i=0; i<attributesFD.getSizeOfFD(); i++) {
            input = sc.nextLine();
            boolean addedCorrectly = attributesFD.addFD(input);
            if(!addedCorrectly) {
                System.out.println(Colors.ANSI_RED + "Entered functional dependency:- " + input + " is not correct, " +
                        "it is having attributes that were not declared.\n" +
                        "Please re-enter the functional dependency");
                i--;
            }
        }

        System.out.println(Colors.ANSI_PURPLE + attributesFD);


        //test for Lossless Join decomposition
        //take input the decomposition
        System.out.print(Colors.ANSI_RESET + "Enter the number of decomposition: ");
        int numOfDecomposition = sc.nextInt();
        backslash = sc.nextLine();
        attributesFD.initializeDecomposition(numOfDecomposition);

        System.out.println(Colors.ANSI_RESET + "\nEnter the decomposition: (each on new line)");
        for(int i=0; i<attributesFD.getNumOfDecomposition(); i++) {
            input = sc.nextLine();
            boolean addedCorrectly = attributesFD.addDecomposition(input);
            if(!addedCorrectly) {
                System.out.println(Colors.ANSI_RED + "Entered decomposition:- " + input + " is not correct, " +
                        "it is having attributes that were not declared.\n" +
                        "Please re-enter the decomposition");
                i--;
            }
        }

        attributesFD.printDecomposition();


        //Testing LossLess Join Decomposition
        if (attributesFD.testLossLessDecomposition()) {
            System.out.println(Colors.ANSI_BLUE + "The given decomposition is LossLess Join Decomposition");
        } else {
            System.out.println(Colors.ANSI_BLUE + "This given decomposition is not LossLess Join Decomposition");
        }
    }
}
