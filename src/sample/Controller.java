package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Ese Emore (08/2016)
 */
public class Controller implements Initializable{

    private DatabaseHelper db;
    private ObservableList<Key> keyList;
    private ObservableList<Key> unavailableKeyList;
    private ObservableList<Key> availableKeyList;
    private Stage popUpStage;
    private Scene scene = null;
    private boolean admin = false;

//    private String punctuation;


    @FXML public StackPane stackPane;
    @FXML private Button checkInSubmit;
    @FXML private Button checkOutSubmit;
    @FXML private Button submitKey;
    @FXML private Button updateKey;
    @FXML private Button deleteKey;
    @FXML private TableView<Key> viewAllList;
    @FXML private TableView<Key> viewHistoryTable;
    @FXML private Button logInButton;
    @FXML private Button guestButton;
    @FXML private Button logoutButon;

//    TextField usernameField;
//    PasswordField passwordField;

    //TODO login button should take user back to login page
    //TODO guest button should take user to main view with guest restrictions (admin = false)
    //TODO logout button should take user to main view with no restrictions (admin = true)
    //TODO Application should start on login page.


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        punctuation = ".(_+-.,!@#$%^&*();\\/|<>\"')";

        this.db = new DatabaseHelper();
        db.initialiseTables();
        this.keyList = db.getListOfKeys();
        this.unavailableKeyList = FXCollections.observableArrayList();
        this.availableKeyList = FXCollections.observableArrayList();
        updateKeyLists();

//        logInButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//
//            }
//        });
//
//        guestButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//
//            }
//        });
    }

    private void updateKeyLists(){
        keyList = db.getListOfKeys();
        unavailableKeyList.clear();
        availableKeyList.clear();
        for(Key key: keyList){
            if(key.isAvailable().equals("No")){
                unavailableKeyList.add(key);
            }
            else{
                availableKeyList.add(key);
            }
        }
    }

    @FXML public void handleMenuCreate(){
        popUpStage = new Stage();
        popUpStage.initModality(Modality.APPLICATION_MODAL);
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("menu_views/editKey.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Make choice box invisible and text box visible
        TextField keyIDText = (TextField) scene.lookup("#keyIDText");
        keyIDText.setVisible(true);
        updateKeyLists();
        keyIDText.setText(String.valueOf(keyList.size()+1));
        ChoiceBox<Key> keyIDChoice = (ChoiceBox) scene.lookup("#keyIDChoice");
        keyIDChoice.setVisible(false);
        TextField quantity = (TextField) scene.lookup("#quantityText");
        quantity.setText(String.valueOf(1));
        TextField roomName = (TextField) scene.lookup("#RoomNameText");
        roomName.requestFocus();
        popUpStage.setTitle("Create Key");
        popUpStage.setScene(scene);
        popUpStage.show();
    }

    @FXML public void handleMenuEdit(){
        //Populate Text with key information:
        popUpStage = new Stage();
        popUpStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("menu_views/editKey.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateKeyLists();
        //Make choice box visible and text box invisible
        TextField keyIDText = (TextField) scene.lookup("#keyIDText");
        keyIDText.setVisible(false);
        ChoiceBox<Key> keyIDChoice = (ChoiceBox) scene.lookup("#keyIDChoice");
        keyIDChoice.setItems(keyList);
        keyIDChoice.setVisible(true);

        //Fill in Key information once it is selected from the combo box list
        TextField name = (TextField) scene.lookup("#RoomNameText");
        TextArea description = (TextArea) scene.lookup("#RoomDescriptionText");
        TextField quantity = (TextField) scene.lookup("#quantityText");
        keyIDChoice.setOnAction((event1) -> {
            Key selected = keyIDChoice.getSelectionModel().getSelectedItem();
            name.setText(selected.getKeyRoomName());
            if(!selected.getKeyRoomDescription().equals("null")) {
                description.setText(selected.getKeyRoomDescription());
            }
            quantity.setText(String.valueOf(selected.getQuantity()));
        });

        popUpStage.setTitle("Edit Key");
        popUpStage.setScene(scene);
        popUpStage.show();
    }

    @FXML public void handleMenuAbout(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("menu_views/about.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("About");
        stage.setScene(scene);
        stage.show();
    }

    //For creating and editing new keys from the menu bar.
    @FXML public void handleSubmitAction(ActionEvent event){
        TextField id = (TextField) submitKey.getScene().lookup("#keyIDText");
        TextField name = (TextField) submitKey.getScene().lookup("#RoomNameText");
        TextArea description = (TextArea) submitKey.getScene().lookup("#RoomDescriptionText");
        TextField quantity = (TextField) submitKey.getScene().lookup("#quantityText");

        ChoiceBox<Key> keyIDChoice = (ChoiceBox) submitKey.getScene().lookup("#keyIDChoice");
        if(!keyIDChoice.getSelectionModel().isEmpty()){
            id.setText(String.valueOf(keyIDChoice.getSelectionModel().getSelectedItem().getKeyID()));
        }

        //TODO - Submit should not accept if there is any punctuation

//        String s1 = name.getText();
//        boolean m1 = name.getText().matches("[.'\"()]*") ;
//        boolean m2 = description.getText().matches(punctuation);
//        boolean m3 = quantity.getText().matches("[A-Z,a-z]");
        //Add error checking here!!!
        if(!id.getText().isEmpty() && id.getText()!=null && !name.getText().isEmpty() && name.getText()!=null){
//            if(name.getText().matches(punctuation) && description.getText().matches(punctuation) && quantity.getText().matches("[A-Z,a-z]") ){
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Warning Dialog");
//                alert.setHeaderText("Invalid information entered");
//                alert.setContentText("Please use characters only. Punctuation is not accepted. Please try again.");
//                alert.showAndWait();
//            }
//            else {
                int keyID = Integer.parseInt(id.getText());
                Key key = new Key(keyID, name.getText());
                if (!description.getText().isEmpty() && description.getText() != null) {
                    key.setKeyRoomDescription(description.getText());
                }
                if (!quantity.getText().isEmpty() && quantity.getText() != null) {
                    key.setQuantity(Integer.parseInt(quantity.getText()));
                } else {
                    key.setQuantity(1);
                }
                Key dbKey = db.getKey(keyID);
                if (dbKey != null) { //If null then key doesn't exist in database
                    db.updateKey(key);
                } else {
                    db.addKeyToDatabase(key);

                }

                updateKeyLists();
//            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Invalid information entered");
            alert.setContentText("Please check the information entered and try again.");
            alert.showAndWait();
        }
        submitKey.defaultButtonProperty().bind(submitKey.focusedProperty());
        ((Node)(event.getSource())).getScene().getWindow().hide();
        //compare new key with keys in database and replace one with the same ID

        //TODO - Submit should refresh viewall page (and check in and out)

    }

    @FXML public void handleCheckInSubmitButtonAction(){
        ComboBox<Key> returnKeyComboBox = (ComboBox) checkInSubmit.getScene().lookup("#returnKeyComboBox");
        Label thanksLabel = (Label) checkInSubmit.getScene().lookup("#thanksLabel");
        Label nameLabel = (Label) checkInSubmit.getScene().lookup("#nameLabel");
        Label returnQuestionLabel = (Label) checkInSubmit.getScene().lookup("#returnQuestionLabel");
        for(Key key: keyList){
            if(key.getKeyID() == returnKeyComboBox.getSelectionModel().getSelectedItem().getKeyID()){
                nameLabel.setText(key.getCurrentHolderName());
                key.toggleAvailability();
                key.setDateReturned(LocalDate.now().toString());
                db.checkInKey(key);
                thanksLabel.setVisible(true);
                checkInSubmit.setVisible(false);
                returnQuestionLabel.setVisible(false);
                nameLabel.setAlignment(Pos.CENTER);
                nameLabel.setVisible(true);
            }
        }
        //Need to update lists after checkIn
    }

    @FXML public void handleCheckOutSubmitButtonAction(){
        ComboBox<Key> takeKeyComboBox = (ComboBox) checkOutSubmit.getScene().lookup("#takeKeyComboBox");
        TextField firstName = (TextField) checkOutSubmit.getScene().lookup("#firstNameText");
        TextField lastName = (TextField) checkOutSubmit.getScene().lookup("#lastNameText");
        TextField contactNumber = (TextField) checkOutSubmit.getScene().lookup("#contactText");
        DatePicker date = (DatePicker) checkOutSubmit.getScene().lookup("#datePicker");
        Label thanksLabel = (Label) checkOutSubmit.getScene().lookup("#thanksLabel");
        if(!takeKeyComboBox.getSelectionModel().isEmpty() && !firstName.getText().isEmpty() && firstName.getText()!=null && !lastName.getText().isEmpty() && lastName.getText()!=null && !contactNumber.getText().isEmpty() && contactNumber.getText()!=null){


            //TODO - Submit should not accept if there is any punctuation

//            if(firstName.getText().matches(punctuation) && lastName.getText().matches(punctuation)){
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Warning Dialog");
//                alert.setHeaderText("Invalid information entered");
//                alert.setContentText("Please use characters only. Punctuation is not accepted. Please try again.");
//                alert.showAndWait();
//            }
//            else
            if( !contactNumber.getText().matches("[A-Z,a-z]") && !contactNumber.getText().matches("[0-9]{11}")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Invalid information entered");
                alert.setContentText("Contact number should be made of numbers only. It should consist of 11 digits. Please try again");
                alert.showAndWait();
            }
            else {
                String name = firstName.getText() + " " + lastName.getText();
                String number = contactNumber.getText();
                for (Key key : keyList) {
                    if (takeKeyComboBox.getSelectionModel().getSelectedItem().getKeyID() == key.getKeyID()) {
                        key.borrowKey(name, number, date.getValue().toString());
                        key.toggleAvailability();
                        db.checkOutKey(key);
//                    key.setUnavailable();
                    }
                }
                checkOutSubmit.setVisible(false);
                thanksLabel.setVisible(true);
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Invalid information entered");
            alert.setContentText("Please check the information entered and try again.");
            alert.showAndWait();
        }
    }

    @FXML public void handleCheckInButtonAction(){
        AnchorPane checkInAnchor = null;
        try {
            checkInAnchor = FXMLLoader.load(getClass().getResource("main_views/checkin.fxml"));

        }
        catch (IOException e){
            e.printStackTrace();
        }

        stackPane.getChildren().clear();
        stackPane.getChildren().add(checkInAnchor);
        ComboBox<Key> returnKeyComboBox = (ComboBox) checkInAnchor.lookup("#returnKeyComboBox");
        updateKeyLists();
        returnKeyComboBox.setItems(unavailableKeyList);
    }

    @FXML public void handleCheckOutButtonAction(){
        AnchorPane checkoutAnchor = null;
        try {
            checkoutAnchor = FXMLLoader.load(getClass().getResource("main_views/checkout.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }

        stackPane.getChildren().clear();
        stackPane.getChildren().add(checkoutAnchor);
        ComboBox<Key> takeKeyComboBox = (ComboBox) checkoutAnchor.lookup("#takeKeyComboBox");
        updateKeyLists();
        takeKeyComboBox.setItems(availableKeyList);
        DatePicker date = (DatePicker) checkoutAnchor.lookup("#datePicker");
        date.setValue(LocalDate.now());
    }

    @FXML public void handleViewAllButtonAction(){
        AnchorPane viewAllAnchor = null;
        try {
            viewAllAnchor = FXMLLoader.load(getClass().getResource("main_views/viewAll.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }

        stackPane.getChildren().clear();
        stackPane.getChildren().add(viewAllAnchor);
        viewAllList = (TableView) viewAllAnchor.lookup("#viewAllTable");
        updateKeyLists();
        populateTable();
        viewAllList.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                handleViewMoreInformation(viewAllList.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void populateTable(){
        TableColumn<Key, Integer> idColumn;
        TableColumn<Key, String> roomNameColumn;
        TableColumn<Key, String> availableColumn;
        TableColumn<Key, String> currentUserColumn;
        TableColumn<Key, String> viewMoreColumn;

        idColumn = (TableColumn<Key, Integer>) viewAllList.getColumns().get(0);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("keyID"));
        idColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        roomNameColumn = (TableColumn<Key, String>) viewAllList.getColumns().get(1);
        roomNameColumn.setCellValueFactory(new PropertyValueFactory("keyRoomName"));
        roomNameColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        availableColumn = (TableColumn<Key, String>) viewAllList.getColumns().get(2);
        availableColumn.setCellValueFactory(new PropertyValueFactory("available"));
        availableColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        currentUserColumn = (TableColumn<Key, String>) viewAllList.getColumns().get(3);
        currentUserColumn.setCellValueFactory(new PropertyValueFactory("currentHolderName"));
        currentUserColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        viewMoreColumn = (TableColumn<Key, String>) viewAllList.getColumns().get(4);
        viewMoreColumn.setCellFactory(param -> new TableCell<Key, String>(){
        @Override public void updateItem(String item, boolean empty){
            setText("View More");
            setStyle("-fx-underline: true");
        }});
        viewAllList.setItems(keyList);
    }

    @FXML private void handleViewMoreInformation(Key key){
        AnchorPane viewMoreAnchor = null;
        try {
            viewMoreAnchor = FXMLLoader.load(getClass().getResource("main_views/viewKeyInfo.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }

        stackPane.getChildren().clear();
        stackPane.getChildren().add(viewMoreAnchor);

        Label idLabel = (Label) viewMoreAnchor.lookup("#idLabel");
        idLabel.setText(String.valueOf(key.getKeyID()));
        Label roomNameLabel = (Label) viewMoreAnchor.lookup("#roomNameLabel");
        roomNameLabel.setText(key.getKeyRoomName());
        Label roomDescLabel = (Label) viewMoreAnchor.lookup("#roomDescLabel");
        if (!key.getKeyRoomDescription().equals("null")) {
            roomDescLabel.setText(key.getKeyRoomDescription());
        }
        Label quantityLabel = (Label) viewMoreAnchor.lookup("#quantityLabel");
        quantityLabel.setText(String.valueOf(key.getQuantity()));
        Label availableLabel = (Label) viewMoreAnchor.lookup("#availableLabel");
        availableLabel.setText(key.isAvailable());

        //If the key is available, these fields should be invisible
        TextField userNameLabel = (TextField) viewMoreAnchor.lookup("#userNameLabel");
        TextField contactNoLabel = (TextField) viewMoreAnchor.lookup("#contactNoLabel");
        DatePicker dateLabel = (DatePicker) viewMoreAnchor.lookup("#dateLabel");
        if (key.isAvailable().equals("No")) {
            userNameLabel.setText(key.getCurrentHolderName());
            contactNoLabel.setText(key.getCurrentHolderNumber());
            dateLabel.setValue(LocalDate.of(Integer.parseInt(key.getDateTaken().split("-")[0]), Integer.parseInt(key.getDateTaken().split("-")[1]), Integer.parseInt(key.getDateTaken().split("-")[2])));
        } else {
            userNameLabel.setVisible(false);
            contactNoLabel.setVisible(false);
            dateLabel.setVisible(false);
            Label userNameLabel2 = (Label) viewMoreAnchor.lookup("#userNameLabel2");
            userNameLabel2.setVisible(false);
            Label contactNoLabel2 = (Label) viewMoreAnchor.lookup("#contactNoLabel2");
            contactNoLabel2.setVisible(false);
            Label dateLabel2 = (Label) viewMoreAnchor.lookup("#dateLabel2");
            dateLabel2.setVisible(false);
            updateKey = (Button) viewMoreAnchor.lookup("#updateKey");
            updateKey.setVisible(false);

        }

        Button viewHistory = (Button) viewMoreAnchor.lookup("#viewHistory");
        viewHistory.setOnMouseClicked(event -> handleViewHistoryAction(key));
    }

    @FXML public void handleUpdateInformationButton(ActionEvent event){
        Label idLabel = (Label) updateKey.getScene().lookup("#idLabel");
        TextField userNameLabel = (TextField) updateKey.getScene().lookup("#userNameLabel");
        TextField contactNoLabel = (TextField) updateKey.getScene().lookup("#contactNoLabel");
        DatePicker dateLabel = (DatePicker) updateKey.getScene().lookup("#dateLabel");

        if(userNameLabel.getText().isEmpty() && contactNoLabel.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Invalid information entered");
            alert.setContentText("Please check the information entered and try again.");
            alert.showAndWait();
        }
        else if(!contactNoLabel.getText().matches("[A-Z,a-z]") && !contactNoLabel.getText().matches("[0-9]{11}")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Invalid information entered");
            alert.setContentText("Contact number should be made of numbers only. It should consist of 11 digits. Please try again");
            alert.showAndWait();
        }
        else {
            Key key = db.getKey(Integer.parseInt(idLabel.getText()));
            key.borrowKey(userNameLabel.getText(), contactNoLabel.getText(), dateLabel.getValue().toString());
            db.checkOutKey(key);
            handleReturnToViewAll(event);
        }

    }

    @FXML public void handleDeleteInformationButton(ActionEvent event){
        Label idLabel = (Label) deleteKey.getScene().lookup("#idLabel");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Delete Key");
        alert.setHeaderText("Are you sure you wish to delete this key?");
//        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // user chose OK, so delete key
            db.deleteKey(Integer.parseInt(idLabel.getText()));
            handleReturnToViewAll(event);
        }
    }

    @FXML public void handleReturnToViewAll(ActionEvent event){
        Scene s = ((Node)(event.getSource())).getScene();
        stackPane = (StackPane) s.lookup("#stackPane");
        handleViewAllButtonAction();
    }

    @FXML public void handleViewHistoryAction(Key key){
        Key current = new Key(key.getKeyID(), key.getKeyRoomName());
        current.setKeyRoomDescription(key.getKeyRoomDescription());
        current.setQuantity(key.getQuantity());
        if(key.isAvailable().equals("No")){
            current.borrowKey(key.getCurrentHolderName(), key.getCurrentHolderNumber(), key.getDateTaken());
            current.setUnavailable();
        }
        AnchorPane viewHistoryAnchor = null;
        try {
            viewHistoryAnchor = FXMLLoader.load(getClass().getResource("main_views/viewKeyHistory.fxml"));
        }
        catch (IOException e){
            e.printStackTrace();
        }

        stackPane.getChildren().clear();
        stackPane.getChildren().add(viewHistoryAnchor);
        viewHistoryTable = (TableView) viewHistoryAnchor.lookup("#viewHistoryTable");
        updateKeyLists();
        populateHistoryTable(key);

        Button goBackHistoryButton = (Button) viewHistoryAnchor.lookup("#goBackHistoryButton");
        goBackHistoryButton.setOnMouseClicked(event -> handleViewMoreInformation(current));

    }

    private void populateHistoryTable(Key key){
        TableColumn<Key, Integer> idColumn;
        TableColumn<Key, String> contactNameColumn;
        TableColumn<Key, String> contactNumberColumn;
        TableColumn<Key, String> dateTakenColumn;
        TableColumn<Key, String> dateReturnedColumn;

        idColumn = (TableColumn<Key, Integer>) viewHistoryTable.getColumns().get(0);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("keyID"));
        idColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        contactNameColumn = (TableColumn<Key, String>) viewHistoryTable.getColumns().get(1);
        contactNameColumn.setCellValueFactory(new PropertyValueFactory("currentHolderName"));
        contactNameColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        contactNumberColumn = (TableColumn<Key, String>) viewHistoryTable.getColumns().get(2);
        contactNumberColumn.setCellValueFactory(new PropertyValueFactory("currentHolderNumber"));
        contactNumberColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        dateTakenColumn = (TableColumn<Key, String>) viewHistoryTable.getColumns().get(3);
        dateTakenColumn.setCellValueFactory(new PropertyValueFactory("dateTaken"));
        dateTakenColumn.setStyle("-fx-alignment: CENTER-RIGHT;");
        dateReturnedColumn = (TableColumn<Key, String>) viewHistoryTable.getColumns().get(4);
        dateReturnedColumn.setCellValueFactory(new PropertyValueFactory("dateReturned"));
        dateReturnedColumn.setStyle("-fx-alignment: CENTER-RIGHT;");

        Key current = new Key(key.getKeyID(), key.getKeyRoomName());
        current.borrowKey(key.getCurrentHolderName(), key.getCurrentHolderNumber(), key.getDateTaken());
        current.setDateReturned(key.getDateReturned());
        ObservableList<Key> historyList = db.getHistoryOfKey(key);
        if(current.getCurrentHolderName() != null){
            historyList.add(current);
        }
        viewHistoryTable.setItems(historyList);
    }

}
