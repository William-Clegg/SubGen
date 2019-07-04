package com.company

import Windows.SelectMembersWindow.chosenMembers
import Windows.ProjectInfoWindow.*
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import javax.imageio.ImageIO
import java.io.*

class GeneralProcessing {

    private var coverPageDoc = PDDocument()
    private var memberPageDoc = PDDocument()

    fun createSubmittal() {


        coverPageDoc.addPage(PDPage())
        val page = coverPageDoc.getPage(0)
        val pw = page.mediaBox.upperRightX
        val ph = page.mediaBox.upperRightY
        val font = PDType1Font.HELVETICA
        val boldFont = PDType1Font.HELVETICA_BOLD
        val pdImage = PDImageXObject.createFromFile(imgPath, coverPageDoc)
        val bimg = ImageIO.read(File(imgPath))
        val height = bimg.height.toDouble()
        val width = bimg.width
        val adjustment = 210 / height
        val textWriter = PDFTextWriter()
        println("height " + height + " width " + width + "  " + imgPath)

        val contentStream = PDPageContentStream(coverPageDoc, page, PDPageContentStream.AppendMode.APPEND, false)
        contentStream.drawImage(pdImage, pw/11, (ph/1.5).toFloat(), (width*adjustment).toFloat(),210.toFloat())
        contentStream.close()

        lateinit var docType : String

        if(operationAndMain) {
            docType = "Operation and Maintenance Manual"
        } else {
            docType ="Plumbing Submittal"
        }

        var textLength =boldFont.getStringWidth(docType) / 1000 * 32
        textWriter.writeText(coverPageDoc, page,boldFont, 32, (pw/2)-(textLength/2), (ph/1.8).toFloat(), docType)

        textLength =boldFont.getStringWidth("For") / 1000 * 26
        textWriter.writeText(coverPageDoc, page,boldFont, 26, (pw/2)-(textLength/2), (ph/2.1).toFloat(), "For")

        textLength =boldFont.getStringWidth(job) / 1000 * 48
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
            val length1 =boldFont.getStringWidth(job1) / 1000 * 48
            val length2 =boldFont.getStringWidth(job2) / 1000 * 48
            textWriter.writeText(coverPageDoc, page,boldFont, 48, (pw / 2) - (length1/2),  (ph / 2.6).toFloat(), job1)


            textWriter.writeText(coverPageDoc, page,boldFont, 48, (pw / 2) - (length2/2),  (ph / 3.2).toFloat(), job2)

        } else {
            textWriter.writeText(coverPageDoc, page,boldFont, 48, (pw / 2) - (textLength / 2),  (ph / 2.6).toFloat(), job)

        }

        if(!volume.equals("")) {
            textLength =boldFont.getStringWidth(volume) / 1000 * 28

            if(!longText) {
                textWriter.writeText(coverPageDoc, page,boldFont, 28, (pw / 2) - (textLength / 2),  (ph / 3.2).toFloat(), volume)
            } else {
                textWriter.writeText(coverPageDoc, page,boldFont, 28, (pw / 2) - (textLength / 2),  (ph / 4), volume)
            }
        }

        if(!date.equals("")) {
            textLength =boldFont.getStringWidth(date) / 1000 * 22

            if(!longText) {
                textWriter. writeText(coverPageDoc, page,boldFont, 22, (pw / 2) - (textLength / 2),  (ph / 3.8).toFloat(), date)
            } else {
                textWriter.writeText(coverPageDoc, page,boldFont, 22, (pw / 2) - (textLength / 2),  (ph / 4.7).toFloat(), date)
            }
        }

        textLength = boldFont.getStringWidth("Prepared By Stasco Mechanical") / 1000 * 24
        textWriter.writeText(coverPageDoc, page,boldFont, 24, (pw/2)-(textLength/2),  (ph/13), "Prepared By Stasco Mechanical")


        coverPageDoc.save("temp\\PictureDocument.pdf")
        coverPageDoc.close()

        if(!removeMembers) {

            var numMembers = 0;

            chosenMembers.forEach {

                if(it != null) {
                    numMembers++
                }
            }

            val memberPage = PDPage()
            memberPageDoc.addPage(memberPage)

            textWriter.writeText(memberPageDoc, memberPage,boldFont, 18, pw/2 - ((boldFont.getStringWidth(docType) / 1000 * 18)/2), ph - ph/12, "Project Members")

            val pageLocations = ArrayList<Float>()

            for(i in 9 downTo 0) {

                if(chosenMembers.get(i) == null) {
                    chosenMembers.removeAt(i)
                }
            }

            when(numMembers) {
                1 -> {
                    pageLocations.add(ph/2)
                }
                2 -> {
                    pageLocations.add(ph - ph / 3)
                    pageLocations.add(ph / 3)
                }
                3 -> {
                    pageLocations.add(ph - ph/4)
                    pageLocations.add(ph/2)
                    pageLocations.add(ph/4)
                }
                4 -> {
                    pageLocations.add(ph - ph/5)
                    pageLocations.add(ph - ph/2.5.toFloat())
                    pageLocations.add(ph/2.5.toFloat())
                    pageLocations.add(ph/5)
                }
                5 -> {
                    pageLocations.add(ph - ph/6)
                    pageLocations.add(ph - ph/4)
                    pageLocations.add(ph/2)
                    pageLocations.add(ph/4)
                    pageLocations.add(ph/6)
                }
                6 -> {
                    pageLocations.add(ph - ph/4)
                    pageLocations.add(ph - ph/4)
                    pageLocations.add(ph/2)
                    pageLocations.add(ph/2)
                    pageLocations.add(ph/4)
                    pageLocations.add(ph/4)
                }
                7 -> {
                    pageLocations.add(ph - ph/5)
                    pageLocations.add(ph - ph/5)
                    pageLocations.add(ph - ph/3)
                    pageLocations.add(ph - ph/3)
                    pageLocations.add(ph/3)
                    pageLocations.add(ph/3)
                    pageLocations.add(ph/5)
                }
                8 -> {
                    pageLocations.add(ph - ph/5)
                    pageLocations.add(ph - ph/5)
                    pageLocations.add(ph - ph/3)
                    pageLocations.add(ph - ph/3)
                    pageLocations.add(ph/3)
                    pageLocations.add(ph/3)
                    pageLocations.add(ph/5)
                    pageLocations.add(ph/5)
                }
                9 -> {
                    pageLocations.add(ph - ph/6)
                    pageLocations.add(ph - ph/6)
                    pageLocations.add(ph - ph/4)
                    pageLocations.add(ph - ph/4)
                    pageLocations.add(ph/2)
                    pageLocations.add(ph/2)
                    pageLocations.add(ph/4)
                    pageLocations.add(ph/4)
                    pageLocations.add(ph/6)
                }
                10 -> {
                    pageLocations.add(ph - ph/6)
                    pageLocations.add(ph - ph/6)
                    pageLocations.add(ph - ph/4)
                    pageLocations.add(ph - ph/4)
                    pageLocations.add(ph/2)
                    pageLocations.add(ph/2)
                    pageLocations.add(ph/4)
                    pageLocations.add(ph/4)
                    pageLocations.add(ph/6)
                    pageLocations.add(ph/6)
                }
            }

            if(pageLocations.size < 6) {

                pageLocations.forEach {

                    var pageLoc = 0.toFloat()

                    /*

                    textWriter.writeText(memberPageDoc, memberPage,boldFont, 14, pw/2.5.toFloat(), it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(0))
                    pageLoc += 15
                    textWriter.writeText(memberPageDoc, memberPage,font, 14, pw/2.5.toFloat(), it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(1))
                    pageLoc += 15
                    textWriter.writeText(memberPageDoc, memberPage,font, 14, pw/2.5.toFloat(), it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(2))
                    pageLoc += 15
                    textWriter.writeText(memberPageDoc, memberPage,font, 14, pw/2.5.toFloat(), it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(3))
                    pageLoc += 15
                    if(chosenMembers.get(pageLocations.indexOf(it)).size > 5 && !chosenMembers.get(pageLocations.indexOf(it)).get(5).equals("")) {
                        textWriter.writeText(memberPageDoc, memberPage, font, 14, pw / 2.5.toFloat(), it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(5))
                        pageLoc += 15
                    }
                    if(chosenMembers.get(pageLocations.indexOf(it)).size > 6 && !chosenMembers.get(pageLocations.indexOf(it)).get(6).equals("")) {
                    textWriter.writeText(memberPageDoc, memberPage, font, 14, pw / 2.5.toFloat(), it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(6))
                        pageLoc += 15
                    }
                    if(chosenMembers.get(pageLocations.indexOf(it)).size > 7 && !chosenMembers.get(pageLocations.indexOf(it)).get(7).equals("")) {
                    textWriter.writeText(memberPageDoc, memberPage, font, 14, pw / 2.5.toFloat(), it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(7))
                    }

                     */
                }
            }

            if(pageLocations.size > 5) {

                val leftLocation = pw/4.toFloat()
                val rightLocation = pw/1.5.toFloat()
                var currentLocation = 0.toFloat()

                pageLocations.forEach {

                    if(pageLocations.indexOf(it).rem(2) == 1) {
                        currentLocation = rightLocation
                    } else {
                        currentLocation = leftLocation
                    }

                    var pageLoc = 0.toFloat()

                    /*
                    textWriter.writeText(memberPageDoc, memberPage,boldFont, 14, currentLocation, it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(0))
                    pageLoc += 15
                    textWriter.writeText(memberPageDoc, memberPage,font, 14, currentLocation, it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(1))
                    pageLoc += 15
                    textWriter.writeText(memberPageDoc, memberPage,font, 14, currentLocation, it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(2))
                    pageLoc += 15
                    textWriter.writeText(memberPageDoc, memberPage,font, 14, currentLocation, it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(3))
                    pageLoc += 15
                    if(chosenMembers.get(pageLocations.indexOf(it)).size > 5 && !chosenMembers.get(pageLocations.indexOf(it)).get(5).equals("")) {
                        textWriter.writeText(memberPageDoc, memberPage, font, 14, currentLocation, it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(5))
                        pageLoc += 15
                    }
                    if(chosenMembers.get(pageLocations.indexOf(it)).size > 6 && !chosenMembers.get(pageLocations.indexOf(it)).get(6).equals("")) {
                        textWriter.writeText(memberPageDoc, memberPage, font, 14, currentLocation, it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(6))
                        pageLoc += 15
                    }
                    if(chosenMembers.get(pageLocations.indexOf(it)).size > 7 && !chosenMembers.get(pageLocations.indexOf(it)).get(7).equals("")) {
                        textWriter.writeText(memberPageDoc, memberPage, font, 14, currentLocation, it - pageLoc, chosenMembers.get(pageLocations.indexOf(it)).get(7))
                    }

                     */
                }
            }
/*
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

            val pEng = generalInfoDoc.createParagraph()
            pEng.alignment = ParagraphAlignment.LEFT
            pEng.indentationLeft = 4000
            pEng.verticalAlignment = TextAlignment.TOP

            val r7 = pEng.createRun()
            r7.ctr.insertNewBr(1)
            r7.ctr.insertNewBr(1)
            r7.fontSize = 16
            r7.fontFamily = "Calibri (Body)"
            r7.isBold = true
            r7.setText("Engineer")

            val engTitle = pEng.createRun()
            engTitle.ctr.insertNewBr(1)
            engTitle.fontSize = 14
            engTitle.fontFamily = "Calibri (Body)"
            engTitle.setText(engineerName)

            val engAddress1 = pEng.createRun()
            engAddress1.fontSize = 12
            engAddress1.fontFamily = "Calibri (Body)"
            engAddress1.ctr.insertNewBr(1)
            engAddress1.setText(engineerAdd1)

            val engAddress2 = pEng.createRun()
            engAddress2.fontSize = 12
            engAddress2.fontFamily = "Calibri (Body)"
            engAddress2.ctr.insertNewBr(1)
            engAddress2.setText(engineerAdd2)

            val engPhoneNumber = pEng.createRun()
            engPhoneNumber.fontSize = 12
            engPhoneNumber.fontFamily = "Calibri (Body)"
            engPhoneNumber.ctr.insertNewBr(1)
            engPhoneNumber.setText(engineerPhone)
            pEng.spacingAfter = 1

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

             */

            memberPageDoc.save("temp\\genInfo.pdf")
            memberPageDoc.close()
        }

        val submittalSheetProcessor = SubmittalProcessing()
        submittalSheetProcessor.processSubmittalContent()
    }
}