package MainPackage;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Model {

    private static Model instance = null;

    public static synchronized Model getInstance() { //Manages the singleton instance, only allows one instance to be created
        if (instance != null && Meta.debug) {
            System.out.println("-Debug- Singleton model already instantiated.");
        }
        if (instance == null) {
            if (Meta.debug) {
                System.out.println("-Debug- Singleton model instantiated.");
            }
            instance = new Model();
        }
        return instance;
    }

    private List<BikeObj> bikeObjsL;
    private MainTableGateway gateway;

    private Model() {
        try {
            this.gateway = new MainTableGateway(DBConnection.getInstance());
            bikeObjsL = gateway.gConGetBikeModelsL();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addBikeModel(BikeObj b) {
        try {
            BikeObj bWithID = this.gateway.insertBikeModel(b);
            //bikeObjsL.add(bWithID); //Only local
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateBikeModel(BikeObj b) {
        try {
            this.gateway.updateBikeModel(b);
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean deleteBikeModel(int dID) {
        boolean result = false;
        try {
            result = this.gateway.deleteBikeModel(dID);
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public List<BikeObj> dynamicSearchModel(String searchTerm) {
        try {
            bikeObjsL = gateway.dynamicEngine(searchTerm);
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bikeObjsL;
    }

    public List<BikeObj> getBikeModelByID(int vID) {
        try {
            List<BikeObj> b = gateway.viewBikesWithID(vID);
            return b;
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<BikeObj> customSQLModel(String q) {
        try {
            bikeObjsL = gateway.customSQLEngine(q);
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bikeObjsL;
    }

    public List<BikeObj> getBikeModels() {
        try {
            bikeObjsL = gateway.gConGetBikeModelsL();
        } catch (SQLException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bikeObjsL;
        //return bikeObjsL; //Only local version
    }
}
