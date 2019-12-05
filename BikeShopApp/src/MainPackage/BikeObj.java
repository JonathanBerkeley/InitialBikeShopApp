package MainPackage;

public class BikeObj {

    private int id, bikeType, modelNo, gearCount;
    private double weight, price;
    private String frame, brand, brakeType, colour;
    private String sid = "",  sbt = "",  smn = "",  scg = "",  swt = "",  sfm = "",  sbr = "",  sbk = "",  sco = "",  spr = "";
    
    public BikeObj(int bt, int mn, int gc, double wt, String fm, String br, String bk, String co, double pr) {
        bikeType = bt;
        modelNo = mn;
        gearCount = gc;
        weight = wt;
        frame = fm;
        brand = br;
        brakeType = bk;
        colour = co;
        price = pr;
    }

    public BikeObj(int id, int bt, int mn, int gc, double wt, String fm, String br, String bk, String co, double pr) {
        this.id = id;
        bikeType = bt;
        modelNo = mn;
        gearCount = gc;
        weight = wt;
        frame = fm;
        brand = br;
        brakeType = bk;
        colour = co;
        price = pr;
    }

    public BikeObj(String sid, String sbt, String smn, String scg, String swt, String sfm, String sbr, String sbk, String sco, String spr) {
        this.sid = sid;
        this.sbt = sbt;
        this.smn = smn;
        this.scg = scg;
        this.swt = swt;
        this.sfm = sfm;
        this.sbr = sbr;
        this.sbk = sbk;
        this.sco = sco;
        this.spr = spr;
    }
    
    //Set
    public void setID(int id) {
        this.id = id;
    }

    //Get
    public int getBikeID() {
        return id;
    }

    public int getBikeType() {
        return bikeType;
    }

    public int getModelNo() {
        return modelNo;
    }

    public int getGearCount() {
        return gearCount;
    }

    public double getWeight() {
        return weight;
    }

    public String getFrame() {
        return frame;
    }

    public String getBrand() {
        return brand;
    }

    public String getBrakeType() {
        return brakeType;
    }

    public String getColour() {
        return colour;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() { //Formats object data into a string
        if (sid.equals("") && sbt.equals("") && smn.equals("") && scg.equals("") && swt.equals("") && sfm.equals("") && sbr.equals("") && sbk.equals("") && sco.equals("") && spr.equals("")) {
            return "ID = " + id + ", bike type = " + bikeType + ", model number = "
                    + modelNo + ", gear count = " + gearCount + ", weight = " + weight + "kg" + ", frame = "
                    + frame + ", brand = " + brand + ", brake type = " + brakeType + ", colour = "
                    + colour + ", price = €" + price;
        } else {
            /*
            This following section allows for the formatted returning of arbitrary SQL query results.
            Checks if each value has been set, if it has then it's added to the string.
            If not then it is ignored. Pseudo explanatory code:
            if (<value> does not equal nothing) append '"Value type: "<value>' to string
            else if (<value> does equal nothing) append '""' (nothing) to string
            return formatted string.
            */
            String formattedReturn = String.format(" "
                    +(!sid.equals("")?" ID: "+sid:"")
                    +(!sbt.equals("")?" Bike type: "+sbt:"")
                    +(!smn.equals("")?" Model number: "+smn:"")
                    +(!scg.equals("")?" Gear count: "+scg:"")
                    +(!swt.equals("")?" Weight: "+swt+"kg":"")
                    +(!sfm.equals("")?" Frame: "+sfm:"")
                    +(!sbr.equals("")?" Brand: "+sbr:"")
                    +(!sbk.equals("")?" Brake type: "+sbk:"")
                    +(!sco.equals("")?" Colour: "+sco:"")
                    +(!spr.equals("")?" Price: €"+spr:""));
            return formattedReturn;
        }
    }
}
