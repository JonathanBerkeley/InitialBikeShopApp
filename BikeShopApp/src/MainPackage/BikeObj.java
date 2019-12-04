package MainPackage;

public class BikeObj {

    private int id, bikeType, modelNo, gearCount;
    private double weight, price;
    private String frame, brand, brakeType, colour, singleResultSQL = "";

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

    public BikeObj(String sr) {
        singleResultSQL = sr;
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
        if (singleResultSQL.equals("")) {
            return "ID = " + id + ", bike type = " + bikeType + ", model number = "
                    + modelNo + ", gear count = " + gearCount + ", weight = " + weight + "kg" + ", frame = "
                    + frame + ", brand = " + brand + ", brake type = " + brakeType + ", colour = "
                    + colour + ", price = €" + price;
        } else {
            return "ID = " + id + " Query result: " + singleResultSQL;
        }
    }
}
