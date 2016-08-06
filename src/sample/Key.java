package sample;

import javafx.beans.property.*;

/**
 * Created by Eez on 12/07/2016.
 */
public class Key {

    public IntegerProperty keyID;
    public StringProperty keyRoomName;
    public StringProperty keyRoomDescription;
    public StringProperty available;
    public StringProperty currentHolderName;
    public StringProperty currentHolderNumber;
    public StringProperty dateTaken;
    public IntegerProperty quantity;

    public Key(int keyID, String keyRoomName){
        super();
        this.keyID = new SimpleIntegerProperty(keyID);
        this.keyRoomName = new SimpleStringProperty(keyRoomName);
        this.keyRoomDescription = new SimpleStringProperty();
        this.available = new SimpleStringProperty("Yes");
        this.currentHolderName =  new SimpleStringProperty();
        this.currentHolderNumber = new SimpleStringProperty();
        this.dateTaken =  new SimpleStringProperty();
        this.quantity = new SimpleIntegerProperty(0);
    }

    public int getKeyID() {
        return keyID.get();
    }

    public String getKeyRoomName() {
        return keyRoomName.get();
    }

    public String getKeyRoomDescription() {
        return keyRoomDescription.get();
    }

    public String isAvailable() {
        return available.get();
    }

    public String getCurrentHolderName() {
        return currentHolderName.get();
    }

    public String getCurrentHolderNumber() {
        return currentHolderNumber.get();
    }

    public String getDateTaken() {
        return dateTaken.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void toggleAvailability() {
        if(this.available.get().equals("No")){
            this.available.set("Yes");
        }
        else this.available.set("No");
    }

    public void setUnavailable(){
        this.available.set("No");
    }

    public void setKeyRoomDescription(String keyRoomDescrition) {
        this.keyRoomDescription.set(keyRoomDescrition);
    }

    public void setQuantity(int quantity){
        this.quantity.set(quantity);
    }

    public void borrowKey(String name, String number, String date){
        if(name!=null){
            this.currentHolderName.set(name);
            this.currentHolderNumber.set(number);
            this.dateTaken.set(date);
        }
        setUnavailable();
    }

    @Override
    public String toString(){
        return keyID.get() + " - " + keyRoomName.get();
    }

}

