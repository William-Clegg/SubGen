package Windows;

import com.company.CustomTreeCell;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import com.company.SubGenApp;

import static com.company.AutoSave.listAddSave;
import static com.company.AutoSave.listDeleteSave;
import static com.company.SubGenApp.*;
import static com.company.SubmittalProcessing.createSubmittal;

public class OutlineWindow {

    private static TreeItem<String> root;
    private static TreeView<String> treeView;

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

        root = new TreeItem<>("Submittal");
        root.setExpanded(true);
        treeView = new TreeView<String>(root);
        treeView.setCellFactory(param -> new CustomTreeCell());
        ListView<String> fileListView = new ListView<>(subSheets);
        //fileListView.setCellFactory(param -> new FileCell());
        grid.add(treeView, 0, 0, 3, 11);

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
                if(keyEvent.getCode() == KeyCode.ENTER){addMainCategory(mainCatField); mainCatField.clear();}
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
                if(keyEvent.getCode() == KeyCode.ENTER) {addSubCategory(subCatField); subCatField.clear();}
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

        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    //deleteItem();
                }
            }
        });
        delete.setOnAction(e -> deleteItem());

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

            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            TreeItem<String> newItem = new TreeItem<>(mainCatField.getText());
            newItem.setExpanded(true);
            if(selectedItem != null && selectedItem.getParent() != null) {
                if (selectedItem.getParent().getValue().equals("Submittal")) {
                    selectedItem.getParent().getChildren().add(selectedItem.getParent().getChildren().indexOf(selectedItem), newItem);
                } else {
                    root.getChildren().add(newItem);
                }
            } else {
                root.getChildren().add(newItem);
            }
        }
    }

    private static void addSubCategory(TextField subCatField) {
        String category;
        if (!subCatField.getText().isEmpty() && root.getChildren().size() != 0) {
            category = "  " + subCatField.getText();
            if(subSheets.contains(category)) {
                do{
                    category = category + " ";
                } while(subSheets.contains(category));
            }
            subSheets.add(category);
            System.out.println(subSheets);
            listAddSave();

            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            TreeItem<String> newItem = new TreeItem<>(subCatField.getText());
            newItem.setExpanded(true);

            if(selectedItem != null && selectedItem.getParent() != null) {
                if (selectedItem.getParent().getValue().equals("Submittal")) {
                    selectedItem.getChildren().add(newItem);
                } else if (selectedItem.getParent().getParent() != null) {
                    selectedItem.getParent().getChildren().add(selectedItem.getParent().getChildren().indexOf(selectedItem)+1,newItem);
                } else {
                    root.getChildren().get(root.getChildren().size() - 1).getChildren().add(newItem);
                }
            } else if(root.getChildren().size() > 0) {
                root.getChildren().get(root.getChildren().size() - 1).getChildren().add(newItem);
            }
        }
    }

    private static void deleteItem() {

        listDeleteSave();

        treeView.getSelectionModel().getSelectedItem().getParent().getChildren().remove(treeView.getSelectionModel().getSelectedItem());
    }

    public static TreeItem getRoot() {
        return root;
    }

    public static void setRoot(TreeItem<String> loadRoot) {
        root = loadRoot;
    }
}
