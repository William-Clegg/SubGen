package com.company

import java.awt.datatransfer.*

class PDFLineItemClipboardAdapter(var lineItem : PDFLineItem) : Transferable, ClipboardOwner {

    private val pdfClass : Class<PDFLineItem> = PDFLineItem::class.java
    private val dmselFlavor = DataFlavor(pdfClass, "Test Data Flavor")


    override fun getTransferDataFlavors(): Array<DataFlavor> {
        System.out.println("Get transfer data flavors")
        val ret : Array<DataFlavor> = arrayOf(dmselFlavor)
        return ret
    }

    override fun isDataFlavorSupported(flavor: DataFlavor?): Boolean {
        return dmselFlavor.equals(flavor)
    }

    //If anything here is wrong it's this next method probably
    override fun getTransferData(flavor: DataFlavor?): Any {
        if(isDataFlavorSupported(flavor)) {
            return this.lineItem
        } else {
            throw UnsupportedFlavorException(dmselFlavor)
        }
    }

    override fun lostOwnership(clipboard: Clipboard?, contents: Transferable?) {
        System.out.println("Lost Ownership")
    }
}