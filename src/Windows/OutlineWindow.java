package Windows;


import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import com.company.FileCell;
import com.company.SubGenApp;
import static com.company.AutoSave.listAddSave;
import static com.company.AutoSave.listDeleteSave;
import static com.company.SubGenApp.*;
import static com.company.SubmittalProcessing.createSubmittal;

public class OutlineWindow {

    public static GridPane outlineGrid() {

        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        /*---------------------------------------------------------------------------
         *  Setting up GridPane, Scene, the ListView, and setting window title.
         */

        GridPane grid = new GridPane();
        SubGenApp.scene1 = new Scene(grid, 700, 750);
        grid.setAlignment(Pos.CENTER);

        ColumnConstraints listCol = new ColumnConstraints();
        listCol.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().add(listCol);

        RowConstraints listRow = new RowConstraints();
        listRow.setVgrow(Priority.ALWAYS);
        grid.getRowConstraints().add(listRow);

        grid.setHgap(25);
        grid.setVgap(10);
        grid.setGridLinesVisible(false);
        grid.setPadding(new Insets(25, 25, 50, 50));

        ListView<String> fileListView = new ListView<>(subSheets);
        fileListView.setCellFactory(param -> new FileCell());
        grid.add(fileListView, 0, 0, 3, 11);

        Label title = new Label();
        title.setText("Categories");

        /*---------------------------------------------------------------------------
         *  Adding main category field/button, and setting onClick and onKey handlers
         */

        Label mainCatLabel = new Label("Add a Main Category");
        final TextField mainCatField = new TextField();
        Button addMain = new Button();
        addMain.setText("Add Main Category");

        HBox mainBtn = new HBox(10);
        mainBtn.getChildren().add(addMain);
        grid.add(mainCatLabel, 4, 1);
        grid.add(mainCatField, 3, 1);
        grid.add(mainBtn, 3, 2);

        addMain.setOnAction(e -> {addMainCategory(mainCatField);});
        mainCatField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER){addMainCategory(mainCatField);}
            }
        });

        /*---------------------------------------------------------------------------
         *  Adding sub category field/button, and setting onClick and onKey handlers
         */

        Label subCatLabel = new Label("Add a Sub Category");
        final TextField subCatField = new TextField();
        Button addSub = new Button();
        addSub.setText("Add Sub Category");

        HBox subBtn = new HBox(10);
        subBtn.getChildren().add(addSub);
        grid.add(subCatLabel, 4, 4);
        grid.add(subCatField, 3, 4);
        grid.add(subBtn, 3, 5);

        addSub.setOnAction(e -> { addSubCategory(subCatField); });
        subCatField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER) {addSubCategory(subCatField); }
            }
        });

        /*--------------------------------------------------------------------------------
         *  Adding delete button and setting button click / double click delete handlers
         */

        Button delete = new Button();
        delete.setText("Delete");
        HBox deleteBtn = new HBox(10);
        deleteBtn.getChildren().add(delete);
        grid.add(deleteBtn, 3, 7);

        fileListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2 && !fileListView.getSelectionModel().getSelectedItems().isEmpty()) {
                    deleteItem(fileListView);
                }
            }
        });
        delete.setOnAction(e -> { if (!fileListView.getSelectionModel().getSelectedItems().isEmpty()) { deleteItem(fileListView); }});

        /*-----------------------------------------------------------------------------------
         *  Adding button to initial submittal generation. Found in SubmittalProcessing.java.
         */

        Button button = new Button();
        button.setText("Create Submittal");
        HBox nextBtn = new HBox(10);
        nextBtn.getChildren().add(button);
        grid.add(nextBtn, 4, 9);

        button.setOnAction(e -> {
            createSubmittal();
        });

        ProgressBar pb = new ProgressBar();

        return grid;
    }

    private static void addMainCategory(TextField mainCatField) {
        if (!mainCatField.getText().isEmpty() && !subSheets.contains(mainCatField.getText())) {
            subSheets.add(mainCatField.getText());
            System.out.println(subSheets);
            listAddSave();
        }
    }

    private static void addSubCategory(TextField subCatField) {
        String category;
        if (!subCatField.getText().isEmpty()) {
            category = "  " + subCatField.getText();
            if(subSheets.contains(category)) {
                do{
                    category = category + " ";
                } while(subSheets.contains(category));
            }
            subSheets.add(category);
            System.out.println(subSheets);
            listAddSave();
        }
    }

    private static void deleteItem(ListView fileListView) {
        int index = fileListView.getSelectionModel().getSelectedIndex();
        fileListView.getItems().remove(index);
        listDeleteSave();
    }
}
