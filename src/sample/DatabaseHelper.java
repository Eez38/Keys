package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * Created by Ese Emore (08/2016)
 */
public class DatabaseHelper {

    private String connectionString = null;
    private Connection connection = null;

    public DatabaseHelper(){
        connectionString = "jdbc:sqlite:database.db";
        try{
            Class.forName("org.sqlite.JDBC");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection(){
        try{
            connection = DriverManager.getConnection(connectionString);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    public void initialiseTables(){
        try{
            Statement statement = getConnection().createStatement();
            String createTables = "CREATE TABLE IF NOT EXISTS KEYS " +
                    "(ID INT PRIMARY KEY NOT NULL, " +
                    "ROOM_NAME TEXT NOT NULL, " +
                    "ROOM_DESCRIPTION TEXT, " +
                    "AVAILABLE TEXT NOT NULL, " +
                    "CURRENT_HOLDER_NAME TEXT, " +
                    "CURRENT_HOLDER_NUMBER TEXT, " +
                    "DATE_TAKEN TEXT, " +
                    "QUANTITY INT)";
            String createHistoryTables = "CREATE TABLE IF NOT EXISTS HISTORY " +
                    "(H_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "ID INT NOT NULL, " +
                    "CURRENT_HOLDER_NAME TEXT NOT NULL, " +
                    "CURRENT_HOLDER_NUMBER TEXT, " +
                    "DATE_TAKEN TEXT, " +
                    "DATE_RETURNED TEXT)";
            statement.execute(createTables);
            statement.execute(createHistoryTables);
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addKeyToDatabase(Key key){
        try{
            Statement statement = getConnection().createStatement();
            String addKey = "INSERT INTO KEYS (ID,ROOM_NAME,ROOM_DESCRIPTION,AVAILABLE,QUANTITY) " +
                    "VALUES (" + key.getKeyID() + ", '" + key.getKeyRoomName() +
                    "', '" + key.getKeyRoomDescription() + "', 'Yes', '" + key.getQuantity() + "' );";
            statement.executeUpdate(addKey);
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateKey(Key key){
        try{
            Statement statement = getConnection().createStatement();
            String updateKey = "UPDATE KEYS " +
                    "SET ROOM_NAME = '" + key.getKeyRoomName() +
                    "', ROOM_DESCRIPTION = '" + key.getKeyRoomDescription() +
                    "', QUANTITY = " + key.getQuantity() +
                    " WHERE ID = " + key.getKeyID() + ";";
            statement.executeUpdate(updateKey);
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void checkOutKey(Key key){
        //Availability now equals No
        try{
            Statement statement = getConnection().createStatement();
            String checkout = "UPDATE KEYS " +
                    "SET AVAILABLE = 'No'" +
                    ", CURRENT_HOLDER_NAME = '" + key.getCurrentHolderName() +
                    "', CURRENT_HOLDER_NUMBER = '" + key.getCurrentHolderNumber() +
                    "', DATE_TAKEN = '" + key.getDateTaken() +
                    "' WHERE ID = " + key.getKeyID() + ";";
            statement.executeUpdate(checkout);
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void checkInKey(Key key){
        //Availability now equals Yes
        try{
            Statement statement = getConnection().createStatement();
            String checkin = "UPDATE KEYS " +
                    "SET AVAILABLE = 'Yes'" +
                    ", CURRENT_HOLDER_NAME = NULL" +
                    ", CURRENT_HOLDER_NUMBER = '0'" +
                    ", DATE_TAKEN = NULL" +
                    " WHERE ID = " + key.getKeyID() + ";";
            String addHistory = "INSERT INTO HISTORY (ID,CURRENT_HOLDER_NAME,CURRENT_HOLDER_NUMBER,DATE_TAKEN,DATE_RETURNED) " +
                    "VALUES (" + key.getKeyID() + ", '" + key.getCurrentHolderName() +
                    "', '" + key.getCurrentHolderNumber() + "', '" + key.getDateTaken() + "', '" + key.getDateReturned() + "' );";
            statement.executeUpdate(checkin);
            statement.executeUpdate(addHistory);
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    //TODO History table changes dates when keys are added to it.

    public Key getKey(int keyID){
        Key key = null;
        try{
            Statement statement = getConnection().createStatement();
            String getKey = "SELECT * FROM KEYS WHERE ID = " + keyID + ";";
            ResultSet  resultSet = statement.executeQuery(getKey);
            while (resultSet.next()){
                key = new Key (resultSet.getInt("id"), resultSet.getString("room_name"));
                if(resultSet.getString("room_description")!=null) {
                    key.setKeyRoomDescription(resultSet.getString("room_description"));
                }
                key.setQuantity(resultSet.getInt("quantity"));
                if(resultSet.getString("available").equals("No")){
                    key.toggleAvailability();
                }
                if(resultSet.getString("current_holder_name")!=null && resultSet.getString("date_taken")!=null) {
                    key.borrowKey(resultSet.getString("current_holder_name"), resultSet.getString("current_holder_number"), resultSet.getString("date_taken"));
                }
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return key;
    }

    public void deleteKey(int keyID){
        try{
            Statement statement = getConnection().createStatement();
            String delete = "DELETE FROM KEYS WHERE ID = " + keyID + ";";
            statement.executeUpdate(delete);
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ObservableList<Key> getListOfKeys(){
        ObservableList<Key> keysList = FXCollections.observableArrayList();
        try{
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM KEYS;");
            while (resultSet.next()){
                Key key = new Key (resultSet.getInt("id"), resultSet.getString("room_name"));
                if(resultSet.getString("room_description")!=null) {
                    key.setKeyRoomDescription(resultSet.getString("room_description"));
                }
                key.setQuantity(resultSet.getInt("quantity"));
                if(resultSet.getString("current_holder_name")!=null && resultSet.getString("date_taken")!=null) {
                    key.borrowKey(resultSet.getString("current_holder_name"), resultSet.getString("current_holder_number"), resultSet.getString("date_taken"));
                }
                keysList.add(key);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return keysList;
    }

    public ObservableList<Key> getHistoryOfKey(Key key){
        ObservableList<Key> historyList = FXCollections.observableArrayList();
        try{
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM HISTORY WHERE ID = " + key.getKeyID() + ";");
            while (resultSet.next()){
                if(resultSet.getString("current_holder_name")!=null && resultSet.getString("date_taken")!=null) {
                    key.borrowKey(resultSet.getString("current_holder_name"), resultSet.getString("current_holder_number"), resultSet.getString("date_taken"));
                    key.setDateReturned(resultSet.getString("date_returned"));
                }
                historyList.add(key);
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return historyList;
    }

}
