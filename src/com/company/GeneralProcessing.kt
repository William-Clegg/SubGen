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
    private var generalInfoDoc = XWPFDocument()

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
        val adjustment = 220 / height
        println("height " + height + " width " + width + "  " + imgPath)
        contentStream.drawImage(pdImage, pw/11, (ph/1.5).toFloat(), (width*adjustment).toFloat(),220.toFloat())

        var textLength = font.getStringWidth("Plumbing Submittal") / 1000 * 32
        contentStream.beginText()
        contentStream.setFont(font, 32.toFloat())
        contentStream.newLineAtOffset((pw/2)-(textLength/2), (ph/1.8).toFloat())
        contentStream.showText("Plumbing Submittal")
        contentStream.endText()

        textLength = font.getStringWidth("For") / 1000 * 24
        contentStream.beginText()
        contentStream.setFont(font, 24.toFloat())
        contentStream.newLineAtOffset((pw/2)-(textLength/2), (ph/2.2).toFloat())
        contentStream.showText("For")
        contentStream.endText()

        textLength = font.getStringWidth(job) / 1000 * 48
        contentStream.beginText()
        contentStream.setFont(font, 48.toFloat())
        contentStream.newLineAtOffset((pw/2)-(textLength/2), (ph/2.8).toFloat())
        contentStream.showText(job)
        contentStream.endText()

        if(!volume.equals("")) {
            textLength = font.getStringWidth(volume) / 1000 * 28
            contentStream.beginText()
            contentStream.setFont(font, 28.toFloat())
            contentStream.newLineAtOffset((pw/2)-(textLength/2), (ph/3.2).toFloat())
            contentStream.showText(volume)
            contentStream.endText()
        }

        if(!date.equals("")) {
            textLength = font.getStringWidth(date) / 1000 * 22
            contentStream.beginText()
            contentStream.setFont(font, 22.toFloat())
            contentStream.newLineAtOffset((pw/2)-(textLength/2), (ph/3.4).toFloat())
            contentStream.showText(date)
            contentStream.endText()
        }

        textLength = font.getStringWidth("Prepared By Stasco Mechanical") / 1000 * 24
        contentStream.beginText()
        contentStream.setFont(font, 24.toFloat())
        contentStream.newLineAtOffset((pw/2)-(textLength/2), (ph/11))
        contentStream.showText("Prepared By Stasco Mechanical")
        contentStream.endText()
        contentStream.close()


        coverPageDoc.save("temp\\PictureDocument.pdf")
        coverPageDoc.close()

        /*
        val p = coverPageDoc.paragraphs[0]
        p.alignment = ParagraphAlignment.LEFT
        val r = p.runs.get(0)
        try {
            val bimg = ImageIO.read(File(imgPath))
            val width = bimg.width
            val height = bimg.height.toDouble()
            val adjustment = 220 / height
            val format = XWPFDocument.PICTURE_TYPE_JPEG
            r.addBreak()
            r.addPicture(FileInputStream(imgPath), format, imgPath, Units.toEMU(Math.round(width * adjustment).toDouble()), Units.toEMU(220.0))

            p.alignment = ParagraphAlignment.CENTER

        } catch (e: Exception) {
            System.err.println(e.toString() + "Image file not found")
        }

        val titleString = "1"
        val projectString = "2"
        val authorString = "3"
        val volumeString = "4"
        val dateString = "5"

        for (thisParagraph:XWPFParagraph in coverPageDoc.getParagraphs()) {
            val runs = thisParagraph.getRuns()
            if (runs != null) {
                for (run:XWPFRun in runs) {
                    var text = run.getText(0)
                    if (text != null) {
                        if(text.contains(titleString)) {
                            text = text.replace(titleString, "Submittal Thing")
                            run.setText(text, 0)
                        } else if(text.contains(projectString)) {
                            text = text.replace(projectString, "Coca-Cola Warehouse")
                            run.setText(text, 0)
                            run.isBold = true

                        } else if(text.contains(volumeString)) {

                            if (volumeCheck.isSelected) {
                                text = text.replace(volumeString, volume)
                                run.setText(text, 0)
                                run.isBold = true

                                run.fontSize = 28
                                run.fontFamily = "Calibri (Body)"
                                run.underline = UnderlinePatterns.SINGLE
                            } else {
                                text = text.replace(volumeString, "")
                                run.setText(text, 0)
                            }
                        } else if(text.contains(dateString)) {

                            if (dateCheck.isSelected) {
                                text = text.replace(dateString, date)
                                run.setText(text, 0)
                                run.isBold = true

                                run.fontSize = 18
                                run.fontFamily = "Calibri (Body)"

                            } else {

                                text = text.replace(dateString, "")
                                run.setText(text, 0)
                            }
                        } else if(text.contains(authorString)) {
                            text = text.replace(authorString, "Prepared by Stasco Mechanical Contractors")
                            run.setText(text, 0)
                        }
                    }
                }
            }
        }


        val sp = SubmittalProcessing()
        sp.convertToPdf(coverPageDoc, "coverPage")

       */

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
        proAdd1.fontSize =12
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

        val submittalSheetProcessor = SubmittalProcessing()
        submittalSheetProcessor.processSubmittalContent()
    }
}