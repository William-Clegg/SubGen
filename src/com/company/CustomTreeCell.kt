package com.company

import com.company.AutoSave.bottomDragSave
import com.company.AutoSave.listSwapSave
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.control.TreeCell
import javafx.scene.control.TreeItem
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import java.io.FileInputStream
import com.company.FilepathToTitle
import com.company.PDFLineItem.Companion.format
import javafx.scene.input.*
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.Transferable
import javax.sound.sampled.Clip


class CustomTreeCell : TreeCell<PDFLineItem>() {

    var thisCell = this
    private var textField : TextField? = null

    val fp = FilepathToTitle()

    init {

        setOnDragDetected { event ->
            if(item == null) {
                return@setOnDragDetected
            }
            val dragboard = startDragAndDrop(TransferMode.MOVE)
            val content = ClipboardContent()
            content.put(format, item)
            dragboard.setContent(content)
            event.consume()
        }

        setOnDragOver { event ->

            val db = event.dragboard
            if(event.gestureSource != thisCell && db.hasContent(format)) {
                event.acceptTransferModes(TransferMode.MOVE)
            } else if(db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY)
            } else {
                event.consume()
            }
        }

        setOnDragEntered { event ->
            if (event.gestureSource !== thisCell && event.dragboard.hasContent(format)) {
                opacity = 0.3
            }
        }

        setOnDragExited { event ->
            if (event.gestureSource !== thisCell && event.dragboard.hasContent(format)) {
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
                            childrenOfLastMain.get(childrenOfLastMain.size - 1).children.add(TreeItem<PDFLineItem>(PDFLineItem(3, fp.toTitle(it.absolutePath),it.absolutePath)))
                        }
                    }
                    event.consume()
                } else {

                    if(selectedItem.parent.value.title.equals("Submittal")) {
                        treeView.root.children.remove(selectedItem)
                        treeView.root.children.add(selectedItem)
                        event.consume()
                    } else if(selectedItem.parent.parent.value.title.equals("Submittal")) {
                        val list = selectedItem.children
                        selectedItem.parent.children.remove(selectedItem)
                        treeView.root.children.get(treeView.root.children.size-1).children.add(TreeItem(selectedItem.value))
                        treeView.root.children.get(treeView.root.children.size-1).children.get(treeView.root.children.size-1).children.addAll(list)
                        event.consume()
                    } else if(selectedItem.parent.parent.parent != null) {
                        val childrenOfLastMain = treeView.root.children.get(treeView.root.children.size-1).children
                        if(childrenOfLastMain.size > 0) {
                            selectedItem.parent.children.remove(selectedItem)
                            childrenOfLastMain.get(childrenOfLastMain.size - 1).children.add(TreeItem<PDFLineItem>(selectedItem.value))
                        }
                        event.consume()
                    }
                }
                bottomDragSave()
            } else if(treeItem != null) {

                var topItem = treeItem

                if(db.hasFiles()) {

                    db.files.forEach {
                        if (treeItem.parent.parent.value.title.equals("Submittal")) {
                            treeItem.children.add(0, TreeItem(PDFLineItem(3, fp.toTitle(it.absolutePath), it.absolutePath)))
                        } else if (treeItem.parent.parent.parent.value.title.equals("Submittal")) {
                            treeItem.parent.children.add(treeItem.parent.children.indexOf(treeItem)+1, TreeItem(PDFLineItem(3, fp.toTitle(it.absolutePath), it.absolutePath)))
                        }
                    }
                    event.consume()

                } else if(selectedItem.parent.value.title.equals("Submittal")) {

                    treeView.root.children.remove(selectedItem)
                    if(treeItem.parent != null) {
                        while (!topItem.parent.value.title.equals("Submittal")) {
                            topItem = treeItem.parent
                        }
                        val thisIndex = treeView.root.children.indexOf(topItem)
                        treeView.root.children.add(thisIndex+1, selectedItem)
                    } else {
                        treeView.root.children.add(0, selectedItem)
                    }
                    event.consume()

                } else if(selectedItem.parent.parent.value.title.equals("Submittal")) {

                    if(treeItem.parent != null) {
                        selectedItem.parent.children.remove(selectedItem)
                        if(treeItem.parent.value.title.equals("Submittal")) {
                            treeItem.children.add(0, selectedItem)
                        } else {
                            if(!treeItem.parent.parent.value.title.equals("Submittal")) {
                                topItem = treeItem.parent
                            }
                            val thisIndex = topItem.parent.children.indexOf(topItem)
                            if(topItem.parent.children.size-1 > thisIndex) {
                                topItem.parent.children.add(thisIndex + 1, selectedItem)
                            } else {
                                topItem.parent.children.add(selectedItem)
                            }
                        }
                    }
                    event.consume()

                } else if(selectedItem.parent.parent.parent != null) {

                    if(treeItem.parent.parent != null) {

                        selectedItem.parent.children.remove(selectedItem)
                        if(treeItem.parent.parent.value.title.equals("Submittal")) {
                            treeItem.children.add(0, selectedItem)
                        } else {
                            treeItem.parent.children.add(treeItem.parent.children.indexOf(treeItem), selectedItem)
                        }

                    }
                    event.consume()
                }
                listSwapSave()
            }
        }
    }


    override fun startEdit() {
        super.startEdit()

        if(textField == null) {
            createTextField()
        }
        text = null
        graphic = textField
        textField?.selectAll()
    }

    private fun createTextField() {
        textField = TextField(getString())
        textField?.setOnKeyPressed { event: KeyEvent? ->

            if(event?.code == KeyCode.ENTER) {
                commitEdit(PDFLineItem(item.tier, textField?.text, item.linePath))
            } else if (event?.code == KeyCode.ESCAPE){
                cancelEdit()
            }

        }
    }

    override fun cancelEdit() {
        super.cancelEdit()
        text = item.title
        textField?.text = item.title
        graphic = treeItem.graphic
    }



    private fun getString() : String? {
        if(item == null) {
            return ""
        } else {
            return item.title
        }
    }

    override fun updateItem(item: PDFLineItem?, empty: Boolean) {
        super.updateItem(item, empty)

        //graphic credit
        /*<div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>*/

        if (empty) {
            text = null
            graphic = null
        } else {

            //val image = ImageView(Image(FileInputStream("pencil-edit-button-light.png")))
            //image.fitHeight = 12.0
            //image.fitWidth = 12.0
            //val cellBox = HBox(10.0)
            //cellBox.children.add(image)
            //image.isPickOnBounds = true

            if(isEditing) {
                if(textField != null) {
                    textField?.text = getString()
                }
                text = null
                graphic = textField
            } else {

                //graphic = cellBox
                if (getItem() == null) {
                    text = ""
                } else {
                    text = item?.title
                }
            }

            /*
            image.setOnMouseClicked { event ->
                val field = TextField()
            }
            */
        }
    }
}