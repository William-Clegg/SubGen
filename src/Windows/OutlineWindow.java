package Windows;

import com.company.GeneralProcessing;
import com.company.PDFLineItem;
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

public class OutlineWindow {

    public static GridPane outlineGrid() {

        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        /*---------------------------------------------------------------------------
         *  Setting up GridPane, Scene, the ListView, and setting window title.
         */

        GridPane grid = new GridPane();
        SubGenApp.scene1 = new Scene(grid, 1200, 900);
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

        Button createSubmittalButton = new Button();
        createSubmittalButton.setText("Create Submittal");
        HBox nextBtn = new HBox(10);
        nextBtn.getChildren().add(createSubmittalButton);
        grid.add(nextBtn, 4, 9);

        createSubmittalButton.setOnAction(e -> {
            listAddSave();
            GeneralProcessing generalProcessor = new GeneralProcessing();
            generalProcessor.createSubmittal();
        });

        ProgressBar pb = new ProgressBar();

        return grid;
    }

    private static void addMainCategory(TextField mainCatField) {
        if (!mainCatField.getText().isEmpty() /*&& !contentList.contains(mainCatField.getText())*/) {

            TreeItem<PDFLineItem> selectedItem = treeView.getSelectionModel().getSelectedItem();
            TreeItem<PDFLineItem> newItem = new TreeItem<>(new PDFLineItem(1, mainCatField.getText(), null));
            newItem.setExpanded(true);
            if(selectedItem != null && selectedItem.getParent() != null) {
                if (selectedItem.getParent().getValue().getTitle().equals("Submittal")) {
                    selectedItem.getParent().getChildren().add(selectedItem.getParent().getChildren().indexOf(selectedItem), newItem);
                } else {
                    root.getChildren().add(newItem);
                }
            } else {
                root.getChildren().add(newItem);
            }
            listAddSave();
        }
    }

    private static void addSubCategory(TextField subCatField) {

        if (!subCatField.getText().isEmpty() && root.getChildren().size() != 0) {

            TreeItem<PDFLineItem> selectedItem = treeView.getSelectionModel().getSelectedItem();

            String content = subCatField.getText();
            TreeItem<PDFLineItem> newItem = new TreeItem<>(new PDFLineItem(2, content,null));
            newItem.setExpanded(true);

            if(selectedItem != null && selectedItem.getParent() != null) {

                if (selectedItem.getParent().getValue().getTitle().equals("Submittal")) {

                    selectedItem.getChildren().add(newItem);
                } else if (selectedItem.getParent().getParent() != null) {

                    selectedItem.getParent().getChildren().add(selectedItem.getParent().getChildren().indexOf(selectedItem)+1,newItem);

                } else {

                    root.getChildren().get(root.getChildren().size() - 1).getChildren().add(newItem);
                }

            } else if(root.getChildren().size() > 0) {
                root.getChildren().get(root.getChildren().size() - 1).getChildren().add(newItem);
            }
            listAddSave();
        }
    }

    private static void deleteItem() {

        treeView.getSelectionModel().getSelectedItem().getParent().getChildren().remove(treeView.getSelectionModel().getSelectedItem());
        listDeleteSave();
    }

    public static TreeItem getRoot() {
        return root;
    }

    public static void setRoot(TreeItem<PDFLineItem> loadRoot) {
        root = loadRoot;
    }

    public static void createListFromTree(TreeItem<PDFLineItem> node, int level) {

        contentList.add(node.getValue());

        if(level < 3) {
            for (TreeItem<PDFLineItem> it : node.getChildren()) {
                createListFromTree(it, level + 1);
            }
        }

        if(contentList.get(contentList.size()-1).getTier() == 0) {
            contentList.remove(0);
        }
    }

    public static TreeView<PDFLineItem> getTreeView() {
        return treeView;
    }
}
