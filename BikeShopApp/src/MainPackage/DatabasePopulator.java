package MainPackage;

/**
 *
 * @author N00181859
 */
import java.util.Random;

public class DatabasePopulator {

    public static void dataFill(int req) { //auto fill database for testing purposes
        Random r = new Random();
        Model model = Model.getInstance();
        int bt, mn, gc;
        double wt, pr;
        String fm, bd, br, cr;
        String[] fmOptions = {"Sports", "Lightweight", "Heavy", "Mountain bike", "Heavyweight", "Offroad", "Reinforced", "Carbon fibre",};
        String[] bdOptions = {"Zoltec", "Pinarello", "BMC", "Trek", "Specialized", "Raleigh", "GT", "Focus", "Cannondale", "Cervelo", "Diamondback", "Kona",};
        String[] brOptions = {"Sports", "Heavy", "Mountain bike", "Offroad",};
        String[] crOptions = {"Blue", "Red", "Yellow", "Black", "White", "Pink", "Brown", "Purple", "Green", "Multicoloured",};
        for (int i = 0; req > i; i++) { //Loops 
            //Ints & double
            bt = genInt(1, 20, r);
            mn = genInt(10000, 100000, r);
            gc = genInt(4, 16, r);
            wt = genInt(7, 25, r);
            pr = genInt(200, 2000, r);
            pr = Math.round(pr/10)*10;
            
            //Strings
            fm = fmOptions[genInt(0, fmOptions.length-1, r)]; //Works by picking a psuedorandom number for the index of respective string arrays
            bd = bdOptions[genInt(0, bdOptions.length-1, r)];
            br = brOptions[genInt(0, brOptions.length-1, r)];
            cr = crOptions[genInt(0, crOptions.length-1, r)];

            //Add obj to DB
            try {
                BikeObj b = new BikeObj(bt, mn, gc, wt, fm, bd, br, cr, pr);
                model.addBikeModel(b);
            } catch(Exception ex){
                System.out.println("Populating failed.");
                if (Meta.debug)
                    System.out.println("-Debug- Exception caught in DatabasePopulator: " + ex);
            }
            System.out.printf("---- Added entry, %s remaining ----\n", req-i); //Let's the user know how many entries remain
        }
        System.out.println("Added entries");
    }
    
    private static int genInt(int min, int max, Random r) { //Generates random numbers between the range of min and max
        return r.nextInt((max - min) + 1) + min;
    }
}
