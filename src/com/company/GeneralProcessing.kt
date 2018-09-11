package com.company

import Windows.ProjectInfoWindow.*
import org.apache.poi.util.Units
import org.apache.poi.xwpf.usermodel.*
import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import javax.imageio.ImageIO

class GeneralProcessing {

    private var generalInfoDoc = XWPFDocument()

    fun createSubmittal() {

        val sectPr = generalInfoDoc.document.body.addNewSectPr()
        val pageMar = sectPr.addNewPgMar()
        pageMar.left = BigInteger.valueOf(600L)
        pageMar.top = BigInteger.valueOf(600L)
        pageMar.right = BigInteger.valueOf(600L)
        pageMar.bottom = BigInteger.valueOf(600L)

        val p = generalInfoDoc.createParagraph()
        p.alignment = ParagraphAlignment.LEFT
        val r = p.createRun()
        try {
            val bimg = ImageIO.read(File(imgPath))
            val width = bimg.width
            val height = bimg.height.toDouble()
            val adjustment = 220 / height
            val format = XWPFDocument.PICTURE_TYPE_JPEG
            r.addBreak()
            r.ctr.insertNewBr(1)
            r.ctr.insertNewBr(1)
            r.ctr.insertNewBr(1)
            r.addPicture(FileInputStream(imgPath), format, imgPath, Units.toEMU(Math.round(width * adjustment).toDouble()), Units.toEMU(220.0))

            p.alignment = ParagraphAlignment.CENTER

        } catch (e: Exception) {
            System.err.println(e.toString() + "Image file not found")
        }


        val p1 = generalInfoDoc.createParagraph()
        p1.alignment = ParagraphAlignment.CENTER
        p1.verticalAlignment = TextAlignment.CENTER

        val r0 = p1.createRun()
        r0.ctr.insertNewBr(1)
        r0.fontSize = 36
        r0.isBold =true
        r0.setText("Plumbing Submittal")
        r0.fontFamily = "Calibri (Body)"

        val r1 = p1.createRun()
        r1.ctr.insertNewBr(1)
        r1.ctr.insertNewBr(1)
        r1.fontSize = 46
        r1.isBold = true
        r1.underline = UnderlinePatterns.SINGLE
        r1.setText(job)
        r1.fontFamily = "Calibri (Body)"

        if (volumeCheck.isSelected) {
            val r21 = p1.createRun()
            r21.ctr.insertNewBr(1)
            r21.fontSize = 28
            r21.fontFamily = "Calibri (Body)"
            r21.isBold = true
            r21.underline = UnderlinePatterns.SINGLE
            r21.setText(volume)
        }

        if (dateCheck.isSelected) {
            val r22 = p1.createRun()
            r22.ctr.insertNewBr(1)
            r22.fontSize = 18
            r22.fontFamily = "Calibri (Body)"
            r22.setText(date)
        }

        val p2 = generalInfoDoc.createParagraph()
        p2.alignment = ParagraphAlignment.CENTER
        p2.verticalAlignment = TextAlignment.BOTTOM

        val r2 = p2.createRun()
        if (job.length < 26) {
            r2.ctr.insertNewBr(1)
        }
        r2.ctr.insertNewBr(1)
        r2.fontSize = 20
        r2.isBold = true
        r2.setText("Prepared by Stasco Mechanical")
        r2.ctr.insertNewBr(1)
        r2.isBold = true
        r2.fontFamily = "Calibri (Body)"
        r2.addBreak(BreakType.PAGE)

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
        stascoPhone.addBreak(BreakType.PAGE)

        val p8 = generalInfoDoc.createParagraph()
        p8.alignment = ParagraphAlignment.CENTER
        p8.verticalAlignment = TextAlignment.TOP

        val r11 = p8.createRun()
        r11.fontSize = 22
        r11.fontFamily = "Calibri (Body)"

        r11.ctr.insertNewBr(1)

        r11.underline = UnderlinePatterns.SINGLE
        r11.isBold = true
        r11.setText("INDEX")

        val submittalSheetProcessor = SubmittalProcessing()
        submittalSheetProcessor.processSubmittalContent(generalInfoDoc)
    }
}