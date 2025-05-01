package main.java.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import main.java.controller.DataManager;
import main.java.model.DataEntry;

public class DataManagerScreen {
    private final DataManager dataManager = new DataManager();
    private final ObservableList<DataEntry> dataList = FXCollections.observableArrayList();

    public VBox getLayout() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(10));

        Text title = new Text("Data Management");
        title.setStyle("-fx-font-size: 24px; -fx-fill: white; -fx-font-weight: bold;");
        StackPane titleContainer = new StackPane(title);
        titleContainer.setStyle("-fx-background-color: linear-gradient(to right, #4CAF50, #2E7D32); " + "-fx-padding: 20px; ");


        // Input Fields
        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField descField = new TextField();
        idField.setPromptText("ID");
        nameField.setPromptText("Name");
        descField.setPromptText("Position");

        // TextFields
        idField.setStyle(" -fx-padding: 6; -fx-border-color: #bbb;");
        nameField.setStyle(" -fx-padding: 6; -fx-border-color: #bbb;");
        descField.setStyle(" -fx-padding: 6; -fx-border-color: #bbb;");


        // Buttons
        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");

        // Buttons
        addButton.setStyle("-fx-background-color: #4285f4; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 5px 15px;");
        updateButton.setStyle("-fx-background-color: #34a853; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding:5px 15px;");
        deleteButton.setStyle("-fx-background-color: #ea4335; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding:5px 15px;");


        // GridPane for Inputs
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(5);

        inputGrid.add(new Label("ID:"), 0, 0);
        inputGrid.add(idField, 1, 0);
        inputGrid.add(new Label("Name:"), 2, 0);
        inputGrid.add(nameField, 3, 0);
        inputGrid.add(new Label("Position:"), 4, 0);
        inputGrid.add(descField, 5, 0);

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);

        // TableView
        TableView<DataEntry> tableView = new TableView<>(dataList);

        TableColumn<DataEntry, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(100);

        TableColumn<DataEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(150);

        TableColumn<DataEntry, String> descColumn = new TableColumn<>("Position");
        descColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        descColumn.setPrefWidth(250);

        tableView.getColumns().addAll(idColumn, nameColumn, descColumn);
        tableView.setPrefHeight(250);

        // TableView styling
        tableView.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5;");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No data available."));

        // Button Events
        addButton.setOnAction(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();

            if (dataManager.getEntryById(id).isPresent()) {
                showAlert("Duplicate ID", "An entry with this ID already exists.");
                return;
            }

            if (id.isEmpty() || name.isEmpty()) {
                showAlert("Invalid Input", "ID and Name are required.");
                return;
            }

            DataEntry entry = new DataEntry(id, name, desc);
            dataManager.addEntry(entry);
            refreshTable();
            clearFields(idField, nameField, descField);
        });

        updateButton.setOnAction(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();

            if (id.isEmpty() || name.isEmpty()) {
                showAlert("Invalid Input", "ID and Name are required.");
                return;
            }

            DataEntry entry = new DataEntry(id, name, desc);
            dataManager.updateEntry(entry);
            refreshTable();
            clearFields(idField, nameField, descField);
        });

        deleteButton.setOnAction(e -> {
            String id = idField.getText().trim();
            if (!id.isEmpty()) {
                dataManager.deleteEntry(id);
                refreshTable();
                clearFields(idField, nameField, descField);
            } else {
                showAlert("Invalid Input", "Please enter an ID to delete.");
            }
        });

        tableView.setOnMouseClicked(e -> {
            DataEntry selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                idField.setText(selected.getId());
                nameField.setText(selected.getName());
                descField.setText(selected.getPosition());
            }
        });

        // Final Layout
        root.getChildren().addAll(
            titleContainer,
            inputGrid,
            buttonBox,
            new Label("Data Entries"),
            tableView
        );

        refreshTable();
        return root;
    }

    private void refreshTable() {
        dataList.setAll(dataManager.getAllEntries());
    }

    private void clearFields(TextField id, TextField name, TextField desc) {
        id.clear();
        name.clear();
        desc.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
