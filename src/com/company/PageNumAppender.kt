package com.company

import Windows.ProjectInfoWindow
import Windows.ProjectInfoWindow.*
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitWidthDestination
import org.apache.pdfbox.util.Matrix
import java.awt.Color

class PageNumAppender {

    val textWriter = PDFTextWriter()

    fun appendIndexPageNums(doc : PDDocument, layoutInfo : IndexPageLayoutInfo, font : PDFont, fontSize : Int) {

        var continuousLocCount = 0

        for (k in 0 until layoutInfo.pages) {

            val currentPage = doc.getPage(layoutInfo.firstPageNum!! + k - 1)

            val pw = currentPage.getCropBox().getUpperRightX()
            val ph = currentPage.getCropBox().getUpperRightY()


            for (l in 0 until layoutInfo.locations.get(k).size) {

                textWriter.writeText(doc, currentPage, font, fontSize,
                        pw - 60, ph - layoutInfo.locations.get(k).get(l), "Pg " + layoutInfo.docPages.get(continuousLocCount))

                val pageDestination = PDPageFitWidthDestination()
                val num : Int = layoutInfo.docPages.get(continuousLocCount).toInt() - 1
                pageDestination.page = doc.getPage(num)
                val action = PDActionGoTo()
                action.destination = pageDestination

                val dictionary = PDBorderStyleDictionary()
                dictionary.style = PDBorderStyleDictionary.STYLE_UNDERLINE
                dictionary.width = 0f

                val annotationLink = PDAnnotationLink()
                annotationLink.action = action
                annotationLink.borderStyle = dictionary

                val position = PDRectangle()
                position.lowerLeftX = pw / 6
                position.lowerLeftY = ph - (layoutInfo.locations.get(k).get(l) + 4)
                position.upperRightX = pw - pw / 25
                position.upperRightY = ph - (layoutInfo.locations.get(k).get(l) - 13)

                annotationLink.rectangle = position

                currentPage.getAnnotations().add(annotationLink)

                continuousLocCount++
            }
        }
    }





    fun appendPageNums(doc: PDDocument, font: PDFont, fontSize: Int) {
        for (i in 0 until doc.getPages().getCount()) {

            val page = doc.getPages().get(i)

            val pw = page.getCropBox().getUpperRightX()
            val ph = page.getCropBox().getUpperRightY()

            val pageNumberBackground = PDPageContentStream(doc, page, true, false, true)
            if (red != null) {
                pageNumberBackground.setNonStrokingColor(red, green, blue)
            } else {
                pageNumberBackground.setNonStrokingColor(Color.YELLOW)
            }
            if (page.getRotation() == 90) {
                pageNumberBackground.addRect(pw - 20, ph - 25, 20f, 30f)
            } else {
                pageNumberBackground.addRect(pw - 25, 0f, 50f, 20f)
            }
            pageNumberBackground.fill()
            pageNumberBackground.close()


            val pageNum = PDPageContentStream(doc, page, true, false, true)
            pageNum.beginText()
            pageNum.setFont(font, fontSize.toFloat())
            if (page.getRotation() == 90) {
                System.out.println("PAGE ROTATION WAS JUST EQUAL TO 90")
                if (i < 9) {
                    pageNum.setTextMatrix(Matrix.getRotateInstance(1.5708, pw - 6, ph - 12))
                } else if (i < 99) {
                    pageNum.setTextMatrix(Matrix.getRotateInstance(1.5708, pw - 6, ph - 18))
                } else {
                    pageNum.setTextMatrix(Matrix.getRotateInstance(1.5708, pw - 6, ph - 22))
                }
            } else {
                if (i < 9) {
                    pageNum.newLineAtOffset(pw - 16, 6f)
                } else if (i < 99) {
                    pageNum.newLineAtOffset(pw - 19, 6f)
                } else {
                    pageNum.newLineAtOffset(pw - 23, 6f)
                }
            }
            pageNum.showText("" + (i + 1))
            pageNum.endText()
            pageNum.close()
        }
    }
}