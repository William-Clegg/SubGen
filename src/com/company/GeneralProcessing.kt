package com.company

import Windows.ProjectInfoWindow.*
import org.apache.pdfbox.contentstream.PDContentStream
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.poi.util.Units
import org.apache.poi.xwpf.usermodel.*
import org.apache.xmlbeans.XmlObject
import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import javax.imageio.ImageIO
import  org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import java.io.FileOutputStream
import org.apache.poi.xwpf.usermodel.XWPFRun
import org.apache.poi.xwpf.usermodel.XWPFParagraph



class GeneralProcessing {

    //private var coverPageDoc = XWPFDocument(FileInputStream("CoverPage.docx"))
    private var coverPageDoc = PDDocument()

    fun createSubmittal() {


        coverPageDoc.addPage(PDPage())
        val page = coverPageDoc.getPage(0)
        val pw = page.mediaBox.upperRightX
        val ph = page.mediaBox.upperRightY
        val font = PDType1Font.HELVETICA_BOLD
        val pdImage = PDImageXObject.createFromFile(imgPath, coverPageDoc)
        val contentStream = PDPageContentStream(coverPageDoc, page, PDPageContentStream.AppendMode.APPEND, false)
        val bimg = ImageIO.read(File(imgPath))
        val height = bimg.height.toDouble()
        val width = bimg.width
        val adjustment = 210 / height
        println("height " + height + " width " + width + "  " + imgPath)
        contentStream.drawImage(pdImage, pw/11, (ph/1.5).toFloat(), (width*adjustment).toFloat(),210.toFloat())

        var textLength = font.getStringWidth("Plumbing Submittal") / 1000 * 32
        contentStream.beginText()
        contentStream.setFont(font, 32.toFloat())
        contentStream.newLineAtOffset((pw/2)-(textLength/2), (ph/1.8).toFloat())
        contentStream.showText("Plumbing Submittal")
        contentStream.endText()

        textLength = font.getStringWidth("For") / 1000 * 26
        contentStream.beginText()
        contentStream.setFont(font, 26.toFloat())
        contentStream.newLineAtOffset((pw/2)-(textLength/2), (ph/2.1).toFloat())
        contentStream.showText("For")
        contentStream.endText()

        textLength = font.getStringWidth(job) / 1000 * 48
        val longText = textLength > (pw - 150)
        if(longText) {
            val jobChars = job.toCharArray()
            val spaceIndicies = ArrayList<Int>()
            for(i in 0..jobChars.size - 1) {
                if(jobChars[i].equals(' ')) {
                    spaceIndicies.add(i)
                }
            }

            var middleSpace = 0
            var difference = 255
            val halfStringLength = job.length/2
            for(item : Int in spaceIndicies) {
                if(Math.abs(halfStringLength - item) < difference) {
                    difference = (Math.abs(halfStringLength - item))
                    middleSpace = item
                }
            }
            val job1 = job.substring(0, middleSpace)
            val job2 = job.substring(middleSpace+1)
            val length1 = font.getStringWidth(job1) / 1000 * 48
            val length2 = font.getStringWidth(job2) / 1000 * 48
            contentStream.beginText()
            contentStream.setFont(font, 48.toFloat())
            contentStream.newLineAtOffset((pw / 2) - (length1/2), (ph / 2.6).toFloat())
            contentStream.showText(job1)
            contentStream.endText()

            contentStream.beginText()
            contentStream.setFont(font, 48.toFloat())
            contentStream.newLineAtOffset((pw / 2) - (length2/2), (ph / 3.2).toFloat())
            contentStream.showText(job2)
            contentStream.endText()
        } else {
            contentStream.beginText()
            contentStream.setFont(font, 48.toFloat())
            contentStream.newLineAtOffset((pw / 2) - (textLength / 2), (ph / 2.6).toFloat())
            contentStream.showText(job)
            contentStream.endText()
        }

        if(!volume.equals("")) {
            textLength = font.getStringWidth(volume) / 1000 * 28
            contentStream.beginText()
            contentStream.setFont(font, 28.toFloat())
            if(!longText) {
                contentStream.newLineAtOffset((pw / 2) - (textLength / 2), (ph / 3.2).toFloat())
            } else {
                contentStream.newLineAtOffset((pw / 2) - (textLength / 2), (ph / 4).toFloat())
            }
            contentStream.showText(volume)
            contentStream.endText()
        }

        if(!date.equals("")) {
            textLength = font.getStringWidth(date) / 1000 * 22
            contentStream.beginText()
            contentStream.setFont(font, 22.toFloat())
            if(!longText) {
                contentStream.newLineAtOffset((pw / 2) - (textLength / 2), (ph / 3.8).toFloat())
            } else {
                contentStream.newLineAtOffset((pw / 2) - (textLength / 2), (ph / 4.7).toFloat())
            }
            contentStream.showText(date)
            contentStream.endText()
        }

        textLength = font.getStringWidth("Prepared By Stasco Mechanical") / 1000 * 24
        contentStream.beginText()
        contentStream.setFont(font, 24.toFloat())
        contentStream.newLineAtOffset((pw/2)-(textLength/2), (ph/13))
        contentStream.showText("Prepared By Stasco Mechanical")
        contentStream.endText()
        contentStream.close()

        coverPageDoc.save("temp\\PictureDocument.pdf")
        coverPageDoc.close()

        if(!removeMembers) {
            val generalInfoDoc = XWPFDocument()
            val p3 = generalInfoDoc.createParagraph()
            p3.alignment = ParagraphAlignment.CENTER
            p3.verticalAlignment = TextAlignment.TOP

            val r3 = p3.createRun()
            r3.fontSize = 22
            r3.ctr.insertNewBr(1)
            r3.underline = UnderlinePatterns.SINGLE
            r3.isBold = true
            r3.setText("Plumbing Submittal")
            r3.fontFamily = "Calibri (Body)"

            val p4 = generalInfoDoc.createParagraph()
            p4.alignment = ParagraphAlignment.LEFT
            p4.indentationLeft = 4000
            p4.verticalAlignment = TextAlignment.TOP

            val r4 = p4.createRun()
            r4.fontSize = 16
            r4.fontFamily = "Calibri (Body)"
            r4.ctr.insertNewBr(1)
            r4.ctr.insertNewBr(1)
            r4.ctr.insertNewBr(1)
            r4.ctr.insertNewBr(1)
            r4.isBold = true
            r4.setText("Project")

            val proName = p4.createRun()
            proName.fontSize = 14
            proName.fontFamily = "Calibri (Body)"
            proName.ctr.insertNewBr(1)
            proName.setText(job)

            val proAdd1 = p4.createRun()
            proAdd1.fontSize = 12
            proAdd1.fontFamily = "Calibri (Body)"
            proAdd1.ctr.insertNewBr(1)
            proAdd1.setText(jobAdd1)

            val proAdd2 = p4.createRun()
            proAdd2.fontSize = 12
            proAdd2.fontFamily = "Calibri (Body)"
            proAdd2.ctr.insertNewBr(1)
            proAdd2.setText(jobAdd2)
            p4.spacingAfter = 1

            val p5 = generalInfoDoc.createParagraph()
            p5.alignment = ParagraphAlignment.LEFT
            p5.indentationLeft = 4000
            p5.verticalAlignment = TextAlignment.TOP

            val r6 = p5.createRun()
            r6.ctr.insertNewBr(1)
            r6.ctr.insertNewBr(1)
            r6.fontSize = 16
            r6.fontFamily = "Calibri (Body)"
            r6.isBold = true
            r6.setText("Architect")

            val archTitle = p5.createRun()
            archTitle.ctr.insertNewBr(1)
            archTitle.fontSize = 14
            archTitle.fontFamily = "Calibri (Body)"
            archTitle.setText(architectName)

            val archAddress1 = p5.createRun()
            archAddress1.fontSize = 12
            archAddress1.fontFamily = "Calibri (Body)"
            archAddress1.ctr.insertNewBr(1)
            archAddress1.setText(architectAdd1)

            val archAddress2 = p5.createRun()
            archAddress2.fontSize = 12
            archAddress2.fontFamily = "Calibri (Body)"
            archAddress2.ctr.insertNewBr(1)
            archAddress2.setText(architectAdd2)

            val archPhoneNum = p5.createRun()
            archPhoneNum.fontSize = 12
            archPhoneNum.fontFamily = "Calibri (Body)"
            archPhoneNum.ctr.insertNewBr(1)
            archPhoneNum.setText(architectPhone)
            p5.spacingAfter = 1

            val p6 = generalInfoDoc.createParagraph()
            p6.alignment = ParagraphAlignment.LEFT
            p6.indentationLeft = 4000
            p6.verticalAlignment = TextAlignment.TOP

            val r8 = p6.createRun()
            r8.ctr.insertNewBr(1)
            r8.ctr.insertNewBr(1)
            r8.fontSize = 16
            r8.fontFamily = "Calibri (Body)"
            r8.isBold = true
            r8.setText("General Contractor")

            val gcTitle = p6.createRun()
            gcTitle.ctr.insertNewBr(1)
            gcTitle.fontSize = 14
            gcTitle.fontFamily = "Calibri (Body)"
            gcTitle.setText(genConName)

            val gcAddress1 = p6.createRun()
            gcAddress1.fontSize = 12
            gcAddress1.fontFamily = "Calibri (Body)"
            gcAddress1.ctr.insertNewBr(1)
            gcAddress1.setText(genConAdd1)

            val gcAddress2 = p6.createRun()
            gcAddress2.fontSize = 12
            gcAddress2.fontFamily = "Calibri (Body)"
            gcAddress2.ctr.insertNewBr(1)
            gcAddress2.setText(genConAdd2)

            val gcPhoneNum = p6.createRun()
            gcPhoneNum.fontSize = 12
            gcPhoneNum.fontFamily = "Calibri (Body)"
            gcPhoneNum.ctr.insertNewBr(1)
            gcPhoneNum.setText(genConPhone)
            p6.spacingAfter = 1


            val p7 = generalInfoDoc.createParagraph()
            p7.alignment = ParagraphAlignment.LEFT
            p7.indentationLeft = 4000
            p7.verticalAlignment = TextAlignment.TOP

            val r10 = p7.createRun()
            r10.ctr.insertNewBr(1)
            r10.ctr.insertNewBr(1)
            r10.fontSize = 16
            r10.fontFamily = "Calibri (Body)"
            r10.isBold = true
            r10.setText("SubContractor")

            val stascoName = p7.createRun()
            stascoName.ctr.insertNewBr(1)
            stascoName.fontSize = 14
            stascoName.fontFamily = "Calibri (Body)"
            stascoName.setText("Stasco Mechanical Contractors")

            val stascoAdd1 = p7.createRun()
            stascoAdd1.fontSize = 12
            stascoAdd1.fontFamily = "Calibri (Body)"
            stascoAdd1.ctr.insertNewBr(1)
            stascoAdd1.setText("1391 Cobb Parkway North")

            val stascoAdd2 = p7.createRun()
            stascoAdd2.fontSize = 12
            stascoAdd2.fontFamily = "Calibri (Body)"
            stascoAdd2.ctr.insertNewBr(1)
            stascoAdd2.setText("Marietta, Georgia 30062")

            val stascoPhone = p7.createRun()
            stascoPhone.fontSize = 12
            stascoPhone.fontFamily = "Calibri (Body)"
            stascoPhone.ctr.insertNewBr(1)
            stascoPhone.setText("770-422-7118")

            val sp1 = SubmittalProcessing()
            generalInfoDoc.createNumbering()
            generalInfoDoc.write((FileOutputStream("GeneralInfoPage.docx")))
            sp1.convertToPdf(generalInfoDoc, "genInfo")
        }

        val submittalSheetProcessor = SubmittalProcessing()
        submittalSheetProcessor.processSubmittalContent()
    }
}