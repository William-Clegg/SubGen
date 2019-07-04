package com.company

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import java.io.IOException

class PDFTextWriter {

    @Throws(IOException::class)
    fun writeText(doc: PDDocument, page: PDPage, chosenFont: PDFont, fontSize: Int, xOff: Float, yOff: Float, text: String?) {
        val stream = PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, false)
        stream.beginText()
        stream.setFont(chosenFont, fontSize.toFloat())
        stream.newLineAtOffset(xOff, yOff)
        stream.showText(text!!)
        stream.endText()
        stream.close()
    }
}