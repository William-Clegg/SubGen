package com.company;

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.company.AutoSave.bottomDragSave;
import static com.company.AutoSave.listAddSave;
import static com.company.AutoSave.listSwapSave;
import static com.company.SubGenApp.subSheets;

/*------------------------------------------------------------------------------
 *  Class which allows swapping of the items in the list, and dragging/dropping
 *  external submittal sheets.
 */

public class FileCell extends ListCell<String> {

    public FileCell() {
        ListCell<String> thisCell = this;

        /*------------------------------------------------------------------------------
         *  Event handler for beginning a drag inside of the ListView.
         */

        setOnDragDetected(event -> {
            if (getItem() == null) {
                return;
            }
            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(getItem());
            dragboard.setContent(content);
            event.consume();
        });

        /*------------------------------------------------------------------------------
         *  Event handler for an active drag hovering over ListView. Differentiates
         *  between files for external drags and strings for internal drags.
         */

        setOnDragOver(event -> {

            Dragboard db = event.getDragboard();
            if (event.getGestureSource() != thisCell && db.hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            } else if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });

        /*------------------------------------------------------------------------------
         *  Handlers to change opacity during drags.
         */

        setOnDragEntered(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                setOpacity(0.3);
            }
        });

        setOnDragExited(event -> {
            if (event.getGestureSource() != thisCell &&
                    event.getDragboard().hasString()) {
                setOpacity(1);
            }
        });

        /*------------------------------------------------------------------------------
         *  Handler for a drag dropped over the ListView. Has five conditions:
         *  external drop below list,
         *  external drop within list,
         *  internal drop below list,
         *  internal upwards drag within list,
         *  and internal downwards drag within list.
         */

        setOnDragDropped(event -> {

            Dragboard db = event.getDragboard();
            boolean success = false;

            if (getItem() == null) {
                if(db.hasFiles()) {
                    for (File file : db.getFiles()) {

                        String filePath = "    " + file.getAbsolutePath();
                        if(subSheets.contains(filePath)) {
                            do {
                                filePath = filePath + " ";
                            } while(subSheets.contains(filePath));
                        }
                        subSheets.add(filePath);
                        System.out.println(subSheets);
                        listAddSave();
                    }
                    success = true;
                    bottomDragSave();
                    event.setDropCompleted(success);
                    event.consume();

                } else {
                    int draggedIdx = subSheets.indexOf(db.getString());
                    int thisIdx = subSheets.size() - 1;

                    System.out.println("SUBSHEETS BEFORE DRAG " + subSheets.toString());

                    for (int i = draggedIdx + 1; i < subSheets.size(); i++) {

                        subSheets.set(draggedIdx + (i - (draggedIdx + 1)), subSheets.get(i));
                    }

                    subSheets.set(thisIdx, db.getString());
                    List<String> itemscopy = new ArrayList<>(getListView().getItems());
                    getListView().getItems().setAll(itemscopy);
                    success = true;
                    System.out.println("SUBSHEETS AFTER DRAG " + subSheets.toString());

                    bottomDragSave();
                    event.setDropCompleted(success);
                    event.consume();
                }
            }

            if (getItem() != null) {

                if (db.hasFiles()) {
                    ObservableList<String> items = getListView().getItems();
                    int thisIdx = items.indexOf(getItem());

                    for (File file : db.getFiles()) {

                        String filePath = "    " + file.getAbsolutePath();
                        if(subSheets.contains(filePath)) {
                            do {
                                filePath = filePath + " ";
                            } while(subSheets.contains(filePath));
                        }
                        subSheets.add(thisIdx, filePath);
                        System.out.println(subSheets);
                        listAddSave();
                    }

                    List<String> itemscopy = new ArrayList<>(getListView().getItems());
                    getListView().getItems().setAll(itemscopy);
                    success = true;
                    listSwapSave();

                } else {
                    ObservableList<String> items = getListView().getItems();
                    int draggedIdx = items.indexOf(db.getString());
                    int thisIdx = items.indexOf(getItem());

                    String itemTemp2 = items.get(draggedIdx);

                    if (draggedIdx > thisIdx) { //if the item is dragged up

                        for (int i = 0; i <= draggedIdx - thisIdx; i += 1) {


                            if (draggedIdx - (i) == thisIdx) {
                                items.set(thisIdx, itemTemp2);
                            } else {
                                items.set(draggedIdx - i, items.get(draggedIdx - (i + 1)));
                            }

                        }
                    } else { //if item is dragged down

                        for (int i = 0; i < thisIdx - draggedIdx; i += 1) {

                            if (i + draggedIdx == thisIdx - 1) {
                                items.set(thisIdx - 1, itemTemp2);
                            } else {
                                items.set(draggedIdx + i, items.get(draggedIdx + i + 1));
                            }
                        }
                    }
                    List<String> itemscopy = new ArrayList<>(getListView().getItems());
                    getListView().getItems().setAll(itemscopy);
                    success = true;
                    listSwapSave();
                }
            event.setDropCompleted(success);
            event.consume();
            }
        });
        setOnDragDone(DragEvent::consume);
    }

    /*------------------------------------------------------------------------------
     *  Required updateItem() method for ListCell. Allows the ListView to only
     *  display the file name rather than the entire path.
     */

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item,  empty);
        if (empty) {
            setText(null);
        } else {
            if(item.contains("\\")) {
                setText("      " + item.substring(item.lastIndexOf('\\')+1,item.lastIndexOf('.')));
            } else {
                setText(item);
            }
        }
    }
}
