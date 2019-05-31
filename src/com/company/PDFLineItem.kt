package com.company

import javafx.scene.input.DataFormat
import java.io.Serializable

class PDFLineItem(var tier : Int, var title : String?, var linePath : String?) : Serializable {

    companion object {

        val format = DataFormat("PDFLineItem")
    }

}