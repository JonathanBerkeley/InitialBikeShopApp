package MainPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainTableGateway {

    private static final String TABLE_NAME = "bikemodels";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_BIKE_TYPE = "bikeType";
    private static final String COLUMN_MODEL_NO = "modelNo";
    private static final String COLUMN_GEAR_COUNT = "gearCount";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_FRAME = "frame";
    private static final String COLUMN_BRAND = "brand";
    private static final String COLUMN_BRAKE_TYPE = "braketype";
    private static final String COLUMN_COLOUR = "colour";
    private static final String COLUMN_PRICE = "price";
    private final Connection mConnection;

    public MainTableGateway(Connection connection) {
        mConnection = connection;
        if (mConnection == null) {
            if (Meta.debug) {
                System.out.println("-Debug- Failure to establish connection in MainTableGateway.");
            }
            System.exit(1);
        }
    }

    public BikeObj updateBikeModel(BikeObj b) throws SQLException {
        String query;
        PreparedStatement stmt;
        int rowsAff;
        query = "UPDATE " + TABLE_NAME + " SET "
                + COLUMN_BIKE_TYPE + " = ?,"
                + COLUMN_MODEL_NO + " = ?,"
                + COLUMN_GEAR_COUNT + " = ?,"
                + COLUMN_WEIGHT + " = ?,"
                + COLUMN_FRAME + " = ?,"
                + COLUMN_BRAND + " = ?,"
                + COLUMN_BRAKE_TYPE + " = ?,"
                + COLUMN_COLOUR + " = ?,"
                + COLUMN_PRICE + " = ?"
                + " WHERE " + COLUMN_ID + " = ?";
        try {
            stmt = mConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, b.getBikeType());
            stmt.setInt(2, b.getModelNo());
            stmt.setInt(3, b.getGearCount());
            stmt.setDouble(4, b.getWeight());
            stmt.setString(5, b.getFrame());
            stmt.setString(6, b.getBrand());
            stmt.setString(7, b.getBrakeType());
            stmt.setString(8, b.getColour());
            stmt.setDouble(9, b.getPrice());
            stmt.setInt(10, b.getBikeID());
            rowsAff = stmt.executeUpdate();
        } catch (SQLException ex) {
            rowsAff = 0;
            if (Meta.debug) {
                System.out.println("-Debug- Exception caught in MainTableGateway: " + ex);
            }
            System.out.println("There was an error trying to update the database. Check entries are correctly formatted and try again.");
        }
        if (rowsAff == 1) {
            System.out.println("Insertion successful");
        } else {
            System.out.println("Insertion unsuccessful - make sure ID exists");
        }
        return b;
    }

    public BikeObj insertBikeModel(BikeObj b) throws SQLException {
        String query;
        PreparedStatement stmt;
        int numRowsAffected;
        int id;
        query = "INSERT INTO " + TABLE_NAME + " ("
                + COLUMN_BIKE_TYPE + ", "
                + COLUMN_MODEL_NO + ", "
                + COLUMN_GEAR_COUNT + ", "
                + COLUMN_WEIGHT + ", "
                + COLUMN_FRAME + ", "
                + COLUMN_BRAND + ", "
                + COLUMN_BRAKE_TYPE + ", "
                + COLUMN_COLOUR + ", "
                + COLUMN_PRICE
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        stmt = mConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, b.getBikeType());
        stmt.setInt(2, b.getModelNo());
        stmt.setInt(3, b.getGearCount());
        stmt.setDouble(4, b.getWeight());
        stmt.setString(5, b.getFrame());
        stmt.setString(6, b.getBrand());
        stmt.setString(7, b.getBrakeType());
        stmt.setString(8, b.getColour());
        stmt.setDouble(9, b.getPrice());

        numRowsAffected = stmt.executeUpdate();

        if (numRowsAffected == 1) {
            ResultSet keys = stmt.getGeneratedKeys();
            keys.next();
            id = keys.getInt(1);
            b.setID(id);
        }
        return b; //Doesn't serve any current purpose
    }

    public boolean deleteBikeModel(int dNID) throws SQLException {
        String query;
        int change;
        PreparedStatement stmt;
        query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ID + " = ?";
        try {
            stmt = mConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, dNID);
            change = stmt.executeUpdate();
            return change == 1;
        } catch (SQLException ex) {
            if (Meta.debug) {
                System.out.println("-Debug- Exception caught in MainTableGateway.deleteBikeModel: " + ex);
            }
            System.out.println("Bad usage of delete.");
        }
        return false;
    }

    public List<BikeObj> customSQLEngine(String userSQL) throws SQLException {
        try {
            boolean getAll = false;
            int columnCount = 0;
            Statement stmt = this.mConnection.createStatement();
            userSQL = userSQL.toLowerCase(); //Set to lowercase for later comparison
            String[] columnCompare = new String[]{COLUMN_ID, COLUMN_BIKE_TYPE, COLUMN_MODEL_NO, COLUMN_GEAR_COUNT, COLUMN_WEIGHT,
                COLUMN_FRAME, COLUMN_BRAND, COLUMN_BRAKE_TYPE, COLUMN_COLOUR, COLUMN_PRICE};
            for (int x = 0; x < columnCompare.length; ++x) {
                if (userSQL.contains(columnCompare[x].toLowerCase())) {
                    ++columnCount;
                } else if (userSQL.contains("select * from")){
                    getAll = true;
                }
            }
            String chkQueryOrUpdate[] = userSQL.split(" ", 2); //Splits userSQL at the first space
            if (chkQueryOrUpdate[0].equals("select")) { //Checks if it's a query or an update
                ResultSet rs = stmt.executeQuery(userSQL);
                if(getAll){
                    List<BikeObj> cBikeObjsL = formatResultSet(rs); //Gets and formats results of query
                    return cBikeObjsL;
                } else {
                    List<BikeObj> cBikeObjsL2 = formatCustomSet(rs, columnCompare);
                    return cBikeObjsL2;
                }
            } else {
                int success = stmt.executeUpdate(userSQL); //Executes user update
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + TABLE_NAME);
                List<BikeObj> cBikeObjsL = formatResultSet(rs);
                if(success > 0)
                    System.out.println("Successful update");
                else
                    System.out.println("Failed to update, check syntax and try again");
                return cBikeObjsL; //Returns all table info after update performed
            }
        } catch (SQLException ex) {
            if (Meta.debug) {
                System.out.println("-Debug- Exception caught in MainTableGateway.customSQLEngine: " + ex);
            }
            System.out.println("Couldn't execute your SQL because:\n\t" + ex);
        }
        return null;
    }

    public List<BikeObj> dynamicEngine(String dArgs) throws SQLException {
        String query;
        Statement stmt;
        query = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + COLUMN_BIKE_TYPE + " LIKE '%" + dArgs + "%' OR "
                + COLUMN_MODEL_NO + " LIKE '%" + dArgs + "%' OR "
                + COLUMN_GEAR_COUNT + " LIKE '%" + dArgs + "%' OR "
                + COLUMN_WEIGHT + " LIKE '%" + dArgs + "%' OR "
                + COLUMN_FRAME + " LIKE '%" + dArgs + "%' OR "
                + COLUMN_BRAND + " LIKE '%" + dArgs + "%' OR "
                + COLUMN_BRAKE_TYPE + " LIKE '%" + dArgs + "%' OR "
                + COLUMN_COLOUR + " LIKE '%" + dArgs + "%' OR "
                + COLUMN_PRICE + " LIKE '%" + dArgs + "%'";
        try {
            stmt = this.mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            List<BikeObj> dBikeObjsL = formatResultSet(rs);
            return dBikeObjsL;
        } catch (SQLException ex) {
            if (Meta.debug) {
                System.out.println("-Debug- Exception caught in MainTableGateway.dynamicEngine: " + ex);
            }
            System.out.println("Bad usage of dynamic search.");
        }
        return null;
    }

    public List<BikeObj> viewBikesWithID(int vID) throws SQLException {
        boolean idExists;
        String query = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + COLUMN_ID + " = " + vID;
        PreparedStatement stmt;
        try {
            stmt = mConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.executeQuery();
            idExists = rs.next(); //Returns true if the resultset contains a row
            rs.previous(); //Goes back to the beginning of the resultset
            List<BikeObj> bl = formatResultSet(rs);
            if (idExists) {
                return bl;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            if (Meta.debug) {
                System.out.println("-Debug- Exception caught in MainTableGateway.viewBikesWithID: " + ex);
            }
            System.out.println("Something went wrong requesting entered ID from the database.");
        }
        return null;
    }

    public List<BikeObj> gConGetBikeModelsL() throws SQLException {
        String sqlQuery = "SELECT * FROM " + TABLE_NAME;
        try {
            Statement stmt = this.mConnection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            List<BikeObj> gBikeObjsL = formatResultSet(rs);
            return gBikeObjsL;
        } catch (SQLException ex) {
            if (Meta.debug) {
                System.out.println("-Debug- Exception caught in MainTableGateway.gConGetBikeModelsL: " + ex);
            }
            System.out.println("Something went wrong getting listings from database");
        }
        return null;
    }

    private List<BikeObj> formatResultSet(ResultSet rs) throws SQLException {
        List<BikeObj> mBikeObjsL = new ArrayList<>();
        int id, bikeType, modelNo, gearCount;
        double weight, price;
        String frame, brand, brakeType, colour;
        BikeObj b;
        try {
            while (rs.next()) {
                id = rs.getInt(COLUMN_ID);
                bikeType = rs.getInt(COLUMN_BIKE_TYPE);
                modelNo = rs.getInt(COLUMN_MODEL_NO);
                gearCount = rs.getInt(COLUMN_GEAR_COUNT);
                weight = rs.getDouble(COLUMN_WEIGHT);
                frame = rs.getString(COLUMN_FRAME);
                brand = rs.getString(COLUMN_BRAND);
                brakeType = rs.getString(COLUMN_BRAKE_TYPE);
                colour = rs.getString(COLUMN_COLOUR);
                price = rs.getDouble(COLUMN_PRICE);
                b = new BikeObj(id, bikeType, modelNo, gearCount, weight, frame, brand, brakeType, colour, price);
                mBikeObjsL.add(b);
            }
            return mBikeObjsL;
        } catch (SQLException ex) {
            if (Meta.debug) {
                System.out.println("-Debug- Exception caught in MainTableGateway.formatResultSet: " + ex);
            }
            System.out.println("Something went wrong formatting entries from database");
        }
        return null;
    }
    private List<BikeObj> formatCustomSet(ResultSet rs, String[] column) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        List<BikeObj> iBikeObjsL = new ArrayList<>();
        String fcsID = "", fcsBikeType = "", fcsModelNo = "", fcsColumnGC = "", fcsWeight = "", fcsFrame = "", fcsBrand = "", fcsBrake = "", fcsColour = "", fcsPrice = "";
        BikeObj b;
        try {
            while (rs.next()) {
                try {
                    fcsID = rs.getString(rs.findColumn(column[0]));
                } catch (Exception ix){}
                try {
                    fcsBikeType = rs.getString(rs.findColumn(column[1]));
                } catch (Exception ix){}
                try {
                    fcsModelNo = rs.getString(rs.findColumn(column[2]));
                } catch (Exception ix){}
                try {
                    fcsColumnGC = rs.getString(rs.findColumn(column[3]));
                } catch (Exception ix){}
                try {
                    fcsWeight = rs.getString(rs.findColumn(column[4]));
                } catch (Exception ix){}
                try {
                    fcsFrame = rs.getString(rs.findColumn(column[5]));
                } catch (Exception ix){}
                try {
                    fcsBrand = rs.getString(rs.findColumn(column[6]));
                } catch (Exception ix){}
                try {
                    fcsBrake = rs.getString(rs.findColumn(column[7]));
                } catch (Exception ix){}
                try {
                    fcsColour = rs.getString(rs.findColumn(column[8]));
                } catch (Exception ix){}
                try {
                    fcsPrice = rs.getString(rs.findColumn(column[9]));
                } catch (Exception ix){}
                b = new BikeObj(fcsID, fcsBikeType, fcsModelNo, fcsColumnGC, fcsWeight, fcsFrame, fcsBrand, fcsBrake, fcsColour, fcsPrice);
                iBikeObjsL.add(b);
            }
            return iBikeObjsL;
        } catch (SQLException ex) {
            if (Meta.debug) {
                System.out.println("-Debug- Exception caught in MainTableGateway.formatCustomSet: " + ex);
            }
            System.out.println("Something went wrong getting formatting entries from database");
        }
        return null;
    }
}
