package com.company

import javafx.scene.control.TreeCell
import javafx.scene.control.TreeItem
import javafx.scene.input.ClipboardContent
import javafx.scene.input.TransferMode

class CustomTreeCell : TreeCell<String>() {

    var thisCell = this

    init {

        setOnDragDetected { event ->
            if(item == null) {
                return@setOnDragDetected
            }
            var dragboard = startDragAndDrop(TransferMode.MOVE)
            var content = ClipboardContent()
            content.putString(item)
            dragboard.setContent(content)
            event.consume()
        }

        setOnDragOver { event ->

            var db = event.dragboard
            if(event.gestureSource != thisCell && db.hasString()) {
                event.acceptTransferModes(TransferMode.MOVE)
            } else if(db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY)
            } else {
                event.consume()
            }
        }

        setOnDragEntered { event ->
            if (event.gestureSource !== thisCell && event.dragboard.hasString()) {
                opacity = 0.3
            }
        }

        setOnDragExited { event ->
            if (event.gestureSource !== thisCell && event.dragboard.hasString()) {
                opacity = 1.0
            }
        }

        setOnDragDropped { event ->

            val db = event.dragboard
            val selectedItem = treeView.selectionModel.selectedItem

            if(item == null) {
                if(db.hasFiles()) {

                    db.files.forEach {
                        val childrenOfLastMain = treeView.root.children.get(treeView.root.children.size-1).children
                        if(childrenOfLastMain.size > 0) {
                            childrenOfLastMain.get(childrenOfLastMain.size - 1).children.add(TreeItem<String>(it.absolutePath))
                        }
                    }
                    event.consume()
                } else {

                    if(selectedItem.parent.value.equals("Submittal")) {
                        treeView.root.children.remove(selectedItem)
                        treeView.root.children.add(selectedItem)
                        event.consume()
                    } else if(selectedItem.parent.parent.value.equals("Submittal")) {
                        val list = selectedItem.children
                        treeView.root.children.get(treeView.root.children.size-1).children.add(TreeItem(selectedItem.value))
                        treeView.root.children.get(treeView.root.children.size-1).children.get(treeView.root.children.size-1).children.addAll(list)
                        selectedItem.parent.children.remove(selectedItem)
                        event.consume()
                    } else if(selectedItem.parent.parent.parent != null) {
                        val childrenOfLastMain = treeView.root.children.get(treeView.root.children.size-1).children
                        if(childrenOfLastMain.size > 0) {
                            childrenOfLastMain.get(childrenOfLastMain.size - 1).children.add(TreeItem<String>(selectedItem.value))
                            selectedItem.parent.children.remove(selectedItem)
                        }
                        event.consume()
                    }
                }
            } else if(treeItem != null) {

                var topItem = treeItem

                if(db.hasFiles()) {

                    db.files.forEach {
                        if (treeItem.parent.parent.value.equals("Submittal")) {
                            treeItem.children.add(0, TreeItem(it.absolutePath))
                        } else if (treeItem.parent.parent.parent.value.equals("Submittal")) {
                            treeItem.parent.children.add(treeItem.parent.children.indexOf(treeItem)+1, TreeItem(it.absolutePath))
                        }
                    }
                    event.consume()

                } else if(selectedItem.parent.value.equals("Submittal")) {

                    treeView.root.children.remove(selectedItem)
                    if(treeItem.parent != null) {
                        while (!topItem.parent.value.equals("Submittal")) {
                            topItem = treeItem.parent
                        }
                        val thisIndex = treeView.root.children.indexOf(topItem)
                        treeView.root.children.add(thisIndex+1, selectedItem)
                    } else {
                        treeView.root.children.add(0, selectedItem)
                    }
                    event.consume()

                } else if(selectedItem.parent.parent.value.equals("Submittal")) {

                    if(treeItem.parent != null) {
                        if(treeItem.parent.value.equals("Submittal")) {
                            treeItem.children.add(0, selectedItem)
                        } else {
                            while(!topItem.parent.parent.value.equals("Submittal")) {
                                topItem = treeItem.parent
                            }
                            val thisIndex = topItem.parent.children.indexOf(topItem)
                            topItem.parent.children.add(thisIndex+1, selectedItem)
                        }
                        selectedItem.parent.children.remove(selectedItem)
                    }
                    event.consume()

                } else if(selectedItem.parent.parent.parent != null) {

                    if(treeItem.parent != null && treeItem.parent.parent != null) {
                        selectedItem.parent.children.remove(selectedItem)
                        if(treeItem.parent.parent.value.equals("Submittal")) {
                            treeItem.children.add(0, selectedItem)
                        } else {
                            treeItem.parent.children.add(treeItem.parent.children.indexOf(treeItem), selectedItem)
                        }
                    }
                    event.consume()
                }
            }
        }
    }

    override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)

        if (empty) {
            text = null
        } else {
            if(getItem() == null) {
                text = ""
            } else if(item?.contains("\\") ?: false) {
                text = item?.substring(item.lastIndexOf('\\')+1, item.lastIndexOf('.'))
            } else {
                text = item?.trim()
            }
        }
        graphic = null
    }
}