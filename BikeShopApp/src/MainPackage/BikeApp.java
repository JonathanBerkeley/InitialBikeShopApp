package MainPackage;

/**
 *
 * @author N00181859
 */
import java.util.List;
import java.util.Scanner;

public class BikeApp {

    public static void main(String[] args) {
        Scanner readIn = new Scanner(System.in); //Used for user input throughout program
        Model model = Model.getInstance(); //Singleton instantiated
        BikeObj b;
        try {
            for (int i = 0; i != 10;) { //Begins input output loop
                System.out.print("\n1. Add a bike model\n"
                        + "2. View stored bike models\n"
                        + "3. Update stored bike model\n"
                        + "4. Delete stored bike model\n"
                        + "5. Fill database with random entries\n"
                        + "6. Dynamic database search\n"
                        + "7. Custom SQL query or update\n"
                        + "8. View bike locations\n"
                        + "9. View stored bike models by ID\n"
                        + "10. Exit\n"
                        + "Enter selection: ");

                try {
                    i = readIn.nextInt();
                } catch (Exception e1) {
                    readIn.nextLine(); //Prevents infinite menu looping on exception
                }
                if (i > 0 && i < 11) { //Checks option is within allowed range
                    System.out.printf("\nOption %s selected\n", i);
                }
                switch (i) { //Checks value of i and does the following accordingly:
                    case 1:
                        b = createModelInfo(readIn, -1); //Starts creation form for a new bike.
                        if (b != null) {
                            model.addBikeModel(b);
                        }
                        break;
                    case 2:
                        printBikeModels(model.getBikeModels()); //Gets and formats results from database
                        break;
                    case 3:
                        printBikeModels(model.getBikeModels());
                        b = updateSavedModel(readIn); //Starts update function for bike
                        if (b != null) {
                            model.updateBikeModel(b);
                        }
                        break;
                    case 4:
                        if(deleteByID(readIn)) //Executes and checks for success of deleteByID function
                            System.out.println("Successfully deleted bike model.");
                        else
                            System.out.println("ATTENTION: Entered ID was not found, check entries and try again.");
                        break;
                    case 5:
                        fillDB(readIn); //Starts database filler form
                        break;
                    case 6:
                        dynamicSearch(readIn); //Starts dynamic search form
                        break;
                    case 7:
                        sqlReader(readIn); //Starts embedded SQL editor
                        break;
                    case 8:
                        
                        break;
                    case 9:
                        viewByID(readIn); //Starts search by id function
                        break;
                    case 10:
                        break;
                    default:
                        System.out.println("\n\tInvalid selection. Please only enter number of a listed option\n");
                        break;
                }
            }
        } catch (Exception e2) {
            System.out.printf("Something went wrong, here's what we know: %s\n", e2);
            System.exit(1);
        }
        System.out.println("---Exiting---");
        System.exit(0);
    }

    private static BikeObj createModelInfo(Scanner readIn, int caller) {
        int bikeType, modelNo, gearCount;
        double weight, price;
        String frame, brand, brakeType, colour;
        System.out.println("Enter bike type number: ");
        try {
            bikeType = readIn.nextInt();
            readIn.nextLine();
            System.out.println("Enter frame description: ");
            frame = readIn.nextLine();
            System.out.println("Enter brand name: ");
            brand = readIn.nextLine();
            System.out.println("Enter model number: ");
            modelNo = readIn.nextInt();
            readIn.nextLine();
            System.out.println("Enter brake type: ");
            brakeType = readIn.nextLine();
            System.out.println("Enter gear count: ");
            gearCount = readIn.nextInt();
            readIn.nextLine();
            System.out.println("Enter colour: ");
            colour = readIn.nextLine();
            System.out.println("Enter weight: ");
            weight = readIn.nextDouble();
            readIn.nextLine();
            System.out.println("Enter price: ");
            price = readIn.nextDouble();
            readIn.nextLine();
            if (caller == -1) {
                BikeObj b = new BikeObj(bikeType, modelNo, gearCount, weight, frame, brand, brakeType, colour, price);
                return b;
            } else {
                BikeObj b = new BikeObj(caller, bikeType, modelNo, gearCount, weight, frame, brand, brakeType, colour, price);
                return b;
            }
        } catch (Exception badRead) {
            System.out.println("ATTENTION: " + badRead + " \n------ Reloading ------\n ");
            return null;
        }
    }

    private static void printBikeModels(List<BikeObj> bm) {
        for (BikeObj bo : bm) {
            System.out.println(bo);
        }
    }

    private static BikeObj updateSavedModel(Scanner readIn) {
        int nID;
        try {
            System.out.println("Enter ID of model to be updated");
            nID = readIn.nextInt();
            BikeObj b = createModelInfo(readIn, nID);
            return b;
        } catch (Exception badID) {
            System.out.println("ATTENTION: " + badID + " \n------ Reloading ------\n ");
            return null;
        }
    }
    private static boolean deleteByID(Scanner readIn){
        int dID;
        boolean dResult = false;
        System.out.println("Enter ID of model to be deleted");
        try {
            dID = readIn.nextInt();
            dResult = Model.getInstance().deleteBikeModel(dID); //EAFP style ID existence check, no search function required
        } catch (Exception badID) {
            System.out.println("ATTENTION: " + badID + " \n------ Reloading ------\n ");
        }
        return dResult;
    }
    private static void viewByID(Scanner readIn){
        int vID;
        List<BikeObj> vResults;
        System.out.println("Enter ID of model to view");
        try {
            vID = readIn.nextInt();
            vResults = Model.getInstance().getBikeModelByID(vID);
            if(vResults == null)
                System.out.println("Requested ID does not exist in the database.");
            else
                printBikeModels(vResults);
        } catch (Exception badID){
            System.out.println("ATTENTION: " + badID + " \n------ Reloading ------\n ");
        }
    }
    private static void dynamicSearch(Scanner readIn){
        String ds;
        List<BikeObj> dsResults;
        System.out.println("----Searches tables by term----\n Search tables for: ");
        readIn.nextLine();
        try {
            ds = readIn.nextLine();
            dsResults = Model.getInstance().dynamicSearchModel(ds);
            if(dsResults == null)
                System.out.println("Search returned null.");
            else
                printBikeModels(dsResults);
        } catch (Exception badTerm){
            System.out.println("ATTENTION: " + badTerm + " \n------ Reloading ------\n ");
        }
    }
    private static void sqlReader(Scanner readIn){
        String sqlReaderQuery;
        List<BikeObj> sqlResults;
        readIn.nextLine();
        System.out.println("Enter SQL statement: ");
        try {
            sqlReaderQuery = readIn.nextLine();
            sqlResults = Model.getInstance().customSQLModel(sqlReaderQuery);
            if(sqlResults == null)
                System.out.println("SQL returned null.");
            else
                printBikeModels(sqlResults);
        } catch (Exception badQuery){
            System.out.println("ATTENTION: " + badQuery + " \n------ Reloading ------\n ");
        }
    }
    private static void fillDB(Scanner readIn) {
        System.out.print("Enter number of entries to generate for the database: ");
        try {
            int req = readIn.nextInt();
            DatabasePopulator.dataFill(req);
        } catch (Exception badReq){
            System.out.println("ATTENTION: " + badReq + " \n------ Reloading ------\n ");
        }  
    }
}
