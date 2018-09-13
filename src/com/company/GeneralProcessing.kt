package com.company

import Windows.ProjectInfoWindow.*
import org.apache.poi.util.Units
import org.apache.poi.xwpf.usermodel.*
import org.apache.xmlbeans.XmlObject
import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import javax.imageio.ImageIO
import  org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import java.io.FileOutputStream

class GeneralProcessing {

    private var coverPageDoc = XWPFDocument(FileInputStream("CoverPage.docx"))
    private var generalInfoDoc = XWPFDocument()

    fun createSubmittal() {

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

        for (paragraph:XWPFParagraph in coverPageDoc.paragraphs) {

            val cursor = paragraph.ctp.newCursor()
            cursor.selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//*/w:txbxContent/w:p/w:r")

            val ctrsInTextBox = ArrayList<XmlObject>()

            while(cursor.hasNextSelection()) {
                cursor.toNextSelection()
                val obj = cursor.`object`
                ctrsInTextBox.add(obj)
            }

            for(obj:XmlObject in ctrsInTextBox) {
                val ctr = CTR.Factory.parse(obj.toString())
                val bufferRun = XWPFRun(ctr, paragraph as IRunBody)
                var text = bufferRun.getText(0)
                println(text)
                if(text != null) {
                    if(text.contains(titleString)) {
                        text = text.replace(titleString, "Submittal Thing")
                        bufferRun.setText(text, 0)
                    } else if(text.contains(projectString)) {
                        text = text.replace(projectString, "Coca-Cola Warehouse")
                        bufferRun.setText(text, 0)
                        bufferRun.isBold = true

                    } else if(text.contains(volumeString)) {

                        if (volumeCheck.isSelected) {
                            text = text.replace(volumeString, volume)
                            bufferRun.setText(text, 0)
                            bufferRun.isBold = true

                            bufferRun.fontSize = 28
                            bufferRun.fontFamily = "Calibri (Body)"
                            bufferRun.underline = UnderlinePatterns.SINGLE
                        } else {
                            text = text.replace(volumeString, "")
                            bufferRun.setText(text, 0)
                        }
                    } else if(text.contains(dateString)) {

                        if (dateCheck.isSelected) {
                            text = text.replace(dateString, date)
                            bufferRun.setText(text, 0)
                            bufferRun.isBold = true

                            bufferRun.fontSize = 18
                            bufferRun.fontFamily = "Calibri (Body)"

                        } else {

                            text = text.replace(dateString, "")
                            bufferRun.setText(text, 0)
                        }
                    } else if(text.contains(authorString)) {
                        text = text.replace(authorString, "Prepared by Stasco Mechanical Contractors")
                        bufferRun.setText(text, 0)
                    }
                }
                obj.set(bufferRun.ctr)
            }
        }

        val sp = SubmittalProcessing()
        sp.convertToPdf(coverPageDoc, "coverPage")

        /*
        val sectPr = generalInfoDoc.document.body.addNewSectPr()
        val pageMar = sectPr.addNewPgMar()
        pageMar.left = BigInteger.valueOf(600L)
        pageMar.top = BigInteger.valueOf(600L)
        pageMar.right = BigInteger.valueOf(600L)
        pageMar.bottom = BigInteger.valueOf(600L)
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