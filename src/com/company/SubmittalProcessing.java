package com.company;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.spi.IIORegistry;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import static OCR.OcrProcessing.readSouthernDoc;
import static Windows.ProjectInfoWindow.*;
import static Windows.ProjectInfoWindow.genConPhone;
import static com.company.SubGenApp.*;
import static com.company.SubGenApp.southernList;
import static com.company.SubGenApp.subSheets;

/*--------------------------------------------------------------------------------
 *  Class which creates the submittal document from the project info and outline.
 */

public class SubmittalProcessing {

    public static void createSubmittal() {
        XWPFDocument doc = new XWPFDocument();

        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.registerServiceProvider(new com.github.jaiimageio.jpeg2000.impl.J2KImageReaderSpi());

        subSheets.clear();
        for (String it: contentList) {
            subSheets.add(it);
        }

        /*--------------------------------------------------------------------------------
         *  Margins are set to zero so that the submittal sheets fill each page properly.
         */

        CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(0L));
        pageMar.setTop(BigInteger.valueOf(0L));
        pageMar.setRight(BigInteger.valueOf(0L));
        pageMar.setBottom(BigInteger.valueOf(0L));

        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun r = p.createRun();
        try {
            BufferedImage bimg = ImageIO.read(new File(imgPath));
            int width = bimg.getWidth();
            double height = bimg.getHeight();
            double adjustment = 220/height;
            System.out.println(width + " this is the path " + imgPath + " this be the height " + height + " this be adjustment " + adjustment);
            int format;
            format = XWPFDocument.PICTURE_TYPE_JPEG;
            r.addBreak();
            r.getCTR().insertNewBr(1);
            r.getCTR().insertNewBr(1);
            r.getCTR().insertNewBr(1);
            r.addPicture(new FileInputStream(imgPath), format, imgPath, Units.toEMU(Math.round(width*adjustment)), Units.toEMU(220));

            p.setAlignment(ParagraphAlignment.CENTER);

        } catch(Exception e) {
            System.err.println(e + "Image file not found");
        }

        XWPFParagraph p1 = doc.createParagraph();
        p1.setAlignment(ParagraphAlignment.CENTER);
        p1.setVerticalAlignment(TextAlignment.CENTER);

        XWPFRun r0 = p1.createRun();
        r0.getCTR().insertNewBr(1);
        r0.setFontSize(36);
        r0.setBold(true);
        r0.setText("Plumbing Submittal");
        r0.setBold(true);
        r0.setFontFamily("Calibri (Body)");

        XWPFRun r1 = p1.createRun();
        r1.getCTR().insertNewBr(1);
        r1.getCTR().insertNewBr(1);
        r1.setFontSize(46);
        r1.setBold(true);
        r1.setUnderline(UnderlinePatterns.SINGLE);
        r1.setText(job);
        r1.setFontFamily("Calibri (Body)");

        if(volumeCheck.isSelected()) {
            XWPFRun r21 = p1.createRun();
            r21.getCTR().insertNewBr(1);
            r21.setFontSize(28);
            r21.setFontFamily("Calibri (Body)");
            r21.setBold(true);
            r21.setUnderline(UnderlinePatterns.SINGLE);
            r21.setText(volume);
        }

        if(dateCheck.isSelected()) {
            XWPFRun r22 = p1.createRun();
            r22.getCTR().insertNewBr(1);
            r22.setFontSize(18);
            r22.setFontFamily("Calibri (Body)");
            r22.setText(date);
        }

        XWPFParagraph p2 = doc.createParagraph();
        p2.setAlignment(ParagraphAlignment.CENTER);
        p2.setVerticalAlignment(TextAlignment.BOTTOM);

        XWPFRun r2 = p2.createRun();
        if(job.length() < 26) {
            r2.getCTR().insertNewBr(1);
        }
        r2.getCTR().insertNewBr(1);
        r2.setFontSize(20);
        r2.setBold(true);
        r2.setText("Prepared by Stasco Mechanical");
        r2.getCTR().insertNewBr(1);
        r2.setBold(true);
        r2.setFontFamily("Calibri (Body)");
        r2.addBreak(BreakType.PAGE);

        XWPFParagraph p3 = doc.createParagraph();
        p3.setAlignment(ParagraphAlignment.CENTER);
        p3.setVerticalAlignment(TextAlignment.TOP);

        XWPFRun r3 = p3.createRun();
        r3.setFontSize(22);
        r3.getCTR().insertNewBr(1);
        r3.getCTR().insertNewBr(1);
        r3.setUnderline(UnderlinePatterns.SINGLE);
        r3.setBold(true);
        r3.setText("Plumbing Submittal");
        r3.setBold(true);
        r3.setFontFamily("Calibri (Body)");


        XWPFParagraph p4 = doc.createParagraph();
        p4.setAlignment(ParagraphAlignment.LEFT);
        p4.setIndentationLeft(4500);
        p4.setVerticalAlignment(TextAlignment.TOP);

        XWPFRun r4 = p4.createRun();
        r4.setFontSize(16);
        r4.setFontFamily("Calibri (Body)");
        r4.getCTR().insertNewBr(1);
        r4.getCTR().insertNewBr(1);
        r4.getCTR().insertNewBr(1);
        r4.getCTR().insertNewBr(1);
        r4.setBold(true);
        r4.setText("Project");
        r4.setBold(true);

        XWPFRun proName = p4.createRun();
        proName.setFontSize(14);
        proName.setFontFamily("Calibri (Body)");
        proName.getCTR().insertNewBr(1);
        proName.setText(job);

        XWPFRun proAdd1 = p4.createRun();
        proAdd1.setFontSize(12);
        proAdd1.setFontFamily("Calibri (Body)");
        proAdd1.getCTR().insertNewBr(1);
        proAdd1.setText(jobAdd1);

        XWPFRun proAdd2 = p4.createRun();
        proAdd2.setFontSize(12);
        proAdd2.setFontFamily("Calibri (Body)");
        proAdd2.getCTR().insertNewBr(1);
        proAdd2.setText(jobAdd2);
        p4.setSpacingAfter(1);


        XWPFParagraph p5 = doc.createParagraph();
        p5.setAlignment(ParagraphAlignment.LEFT);
        p5.setIndentationLeft(4500);
        p5.setVerticalAlignment(TextAlignment.TOP);

        XWPFRun r6 = p5.createRun();
        r6.getCTR().insertNewBr(1);
        r6.getCTR().insertNewBr(1);
        r6.setFontSize(16);
        r6.setFontFamily("Calibri (Body)");
        r6.setBold(true);
        r6.setText("Architect");
        r6.setBold(true);

        XWPFRun archTitle = p5.createRun();
        archTitle.getCTR().insertNewBr(1);
        archTitle.setFontSize(14);
        archTitle.setFontFamily("Calibri (Body)");
        archTitle.setText(architectName);

        XWPFRun archAddress1 = p5.createRun();
        archAddress1.setFontSize(12);
        archAddress1.setFontFamily("Calibri (Body)");
        archAddress1.getCTR().insertNewBr(1);
        archAddress1.setText(architectAdd1);

        XWPFRun archAddress2 = p5.createRun();
        archAddress2.setFontSize(12);
        archAddress2.setFontFamily("Calibri (Body)");
        archAddress2.getCTR().insertNewBr(1);
        archAddress2.setText(architectAdd2);

        XWPFRun archPhoneNum = p5.createRun();
        archPhoneNum.setFontSize(12);
        archPhoneNum.setFontFamily("Calibri (Body)");
        archPhoneNum.getCTR().insertNewBr(1);
        archPhoneNum.setText(architectPhone);
        p5.setSpacingAfter(1);


        XWPFParagraph p6 = doc.createParagraph();
        p6.setAlignment(ParagraphAlignment.LEFT);
        p6.setIndentationLeft(4500);
        p6.setVerticalAlignment(TextAlignment.TOP);

        XWPFRun r8 = p6.createRun();
        r8.getCTR().insertNewBr(1);
        r8.getCTR().insertNewBr(1);
        r8.setFontSize(16);
        r8.setFontFamily("Calibri (Body)");
        r8.setBold(true);
        r8.setText("General Contractor");
        r8.setBold(true);

        XWPFRun gcTitle = p6.createRun();
        gcTitle.getCTR().insertNewBr(1);
        gcTitle.setFontSize(14);
        gcTitle.setFontFamily("Calibri (Body)");
        gcTitle.setText(genConName);

        XWPFRun gcAddress1 = p6.createRun();
        gcAddress1.setFontSize(12);
        gcAddress1.setFontFamily("Calibri (Body)");
        gcAddress1.getCTR().insertNewBr(1);
        gcAddress1.setText(genConAdd1);

        XWPFRun gcAddress2 = p6.createRun();
        gcAddress2.setFontSize(12);
        gcAddress2.setFontFamily("Calibri (Body)");
        gcAddress2.getCTR().insertNewBr(1);
        gcAddress2.setText(genConAdd2);

        XWPFRun gcPhoneNum = p6.createRun();
        gcPhoneNum.setFontSize(12);
        gcPhoneNum.setFontFamily("Calibri (Body)");
        gcPhoneNum.getCTR().insertNewBr(1);
        gcPhoneNum.setText(genConPhone);
        p6.setSpacingAfter(1);


        XWPFParagraph p7 = doc.createParagraph();
        p7.setAlignment(ParagraphAlignment.LEFT);
        p7.setIndentationLeft(4500);
        p7.setVerticalAlignment(TextAlignment.TOP);

        XWPFRun r10 = p7.createRun();
        r10.getCTR().insertNewBr(1);
        r10.getCTR().insertNewBr(1);
        r10.setFontSize(16);
        r10.setFontFamily("Calibri (Body)");
        r10.setBold(true);
        r10.setText("SubContractor");
        r10.setBold(true);

        XWPFRun stascoName = p7.createRun();
        stascoName.getCTR().insertNewBr(1);
        stascoName.setFontSize(14);
        stascoName.setFontFamily("Calibri (Body)");
        stascoName.setText("Stasco Mechanical Contractors");

        XWPFRun stascoAdd1 = p7.createRun();
        stascoAdd1.setFontSize(12);
        stascoAdd1.setFontFamily("Calibri (Body)");
        stascoAdd1.getCTR().insertNewBr(1);
        stascoAdd1.setText("1391 Cobb Parkway North");

        XWPFRun stascoAdd2 = p7.createRun();
        stascoAdd2.setFontSize(12);
        stascoAdd2.setFontFamily("Calibri (Body)");
        stascoAdd2.getCTR().insertNewBr(1);
        stascoAdd2.setText("Marietta, Georgia 30062");

        XWPFRun stascoPhone = p7.createRun();
        stascoPhone.setFontSize(12);
        stascoPhone.setFontFamily("Calibri (Body)");
        stascoPhone.getCTR().insertNewBr(1);
        stascoPhone.setText("770-422-7118");
        stascoPhone.addBreak(BreakType.PAGE);


        XWPFParagraph p8 = doc.createParagraph();
        p8.setAlignment(ParagraphAlignment.CENTER);
        p8.setVerticalAlignment(TextAlignment.TOP);

        XWPFRun r11 = p8.createRun();
        r11.setFontSize(22);
        r11.setFontFamily("Calibri (Body)");

        r11.getCTR().insertNewBr(1);
        //r11.getCTR().insertNewBr(1);

        r11.setUnderline(UnderlinePatterns.SINGLE);
        r11.setBold(true);
        r11.setText("INDEX");
        r11.setBold(true);

        XWPFParagraph p9 = doc.createParagraph();
        p9.setAlignment(ParagraphAlignment.LEFT);
        p9.setVerticalAlignment(TextAlignment.TOP);
        p9.setIndentationLeft(1080);

        String curNumString;
        int curNum = 0;
        int curSubNum = 1;
        boolean newCat = false;

        System.out.println("subsheets right before processing" + subSheets.toString());

        for (int i = 0; i < subSheets.size(); i++) {
            XWPFRun r12 = p9.createRun();
            r12.setFontSize(14);
            r12.setFontFamily("Calibri (Body)");

            if (!subSheets.get(i).substring(0, 2).equals("  ")) {
                r12.getCTR().insertNewBr(1);

                if(i > 0) {
                    r12.getCTR().insertNewBr(1);
                }

                newCat = true;
                curSubNum = 1;
                curNum += 1;

                curNumString = curNum + ") ";
                r12.setBold(true);
                r12.setText(curNumString + subSheets.get(i));
                r12.setBold(true);

            }

            if (subSheets.get(i).substring(0, 2).equals("  ") && (!subSheets.get(i).substring(0, 4).equals("    ") || subSheets.get(i).length() < 4)) {

                if (subSheets.get(i + 1).contains("SP&S")) {
                    readSouthernDoc(i);

                }
                if (newCat) {
                    r12.setFontSize(12);
                    r12.setFontFamily("Calibri (Body)");

                    if (curSubNum == 1) {
                        r12.getCTR().insertNewBr(1);
                    }
                    r12.getCTR().insertNewBr(1);

                    curNumString = "     " + String.valueOf((char) (curSubNum + 64)) + ". ";
                    r12.setText(/*curNumString +*/ "    " + subSheets.get(i));
                    curSubNum += 1;
                }
            }
        }
        String file;
        int slashIndex;
        int dotIndex;
        CharBuffer slash = CharBuffer.allocate(1);
        slash.append('\\');
        curNum = 1;
        curSubNum = 1;
        newCat = false;
        List<Integer> list = new ArrayList<>();
        int numDocs = 0;
        String mainHeader = "";
        String subHeader = "";
        int subNumber = 0;
        int lastMain = 0;

        for (int i = 0; i < subSheets.size(); i++) {

            if (!subSheets.get(i).substring(0, 2).equals("  ") || i == subSheets.size() - 1) {


                if (i == subSheets.size() - 1) {

                    XWPFParagraph p11 = doc.createParagraph();
                    p11.setSpacingAfter(0);
                    p11.setSpacingBefore(0);

                    XWPFRun r15 = p11.createRun();
                    r15.setFontSize(12);
                    r15.setFontFamily("Calibri (Body)");

                    if (newCat) {
                        r15.getCTR().insertNewBr(1);
                    }

                    slashIndex = subSheets.get(i).lastIndexOf('\\') + 1;
                    dotIndex = subSheets.get(i).lastIndexOf('.');
                    if (subSheets.get(i).contains(slash)) {
                        file = subSheets.get(i).substring(slashIndex, dotIndex);
                    } else {
                        file = subSheets.get(i);
                    }
                    r15.setText("                                   " + file);

                    newCat = false;
                }

                if (i != 0) {

                    for (int j = lastMain; j <= i; j++) {

                        if (subSheets.get(j).substring(0, 2).equals("  ") && !subSheets.get(j).substring(0, 4).equals("    ")) {
                            if (!subSheets.get(j + 1).contains("SP&S")) {
                                subNumber++;
                                subHeader = subSheets.get(j);
                            }
                        }

                        if (subSheets.get(j).substring(0, 4).equals("    ")) {

                            try {

                                if (subSheets.get(j).substring(subSheets.get(j).lastIndexOf('.')).equals(".pdf") && !subSheets.get(j).contains("SP&S")) {

                                    try {

                                        String sourceDir;
                                        sourceDir = subSheets.get(j).substring(4);

                                        System.out.println("the filepath about to be added " + sourceDir);
                                        File sourceFile = new File(sourceDir);

                                        if (sourceFile.exists()) {

                                            PDDocument document = PDDocument.load(new File(sourceDir));
                                            System.out.println("sourceDir just assigned to document");
                                            PDFRenderer pdfRenderer = new PDFRenderer(document);
                                            System.out.println("document just rendered");
                                            //@SuppressWarnings("unchecked")
                                            String fileName = sourceFile.getName().replace(".pdf", "");
                                            int pageNumber = 0;
                                            int totalPages = document.getNumberOfPages();
                                            for (int k = 0; k < totalPages; k++) {
                                                System.out.println("Processing page " + (pageNumber+1) + " of " + totalPages + " / " + fileName);
                                                BufferedImage image = pdfRenderer.renderImageWithDPI(pageNumber, 190, ImageType.RGB);
                                                XWPFParagraph p11 = doc.createParagraph();
                                                p11.setAlignment(ParagraphAlignment.RIGHT);

                                                XWPFRun picRun = p11.createRun();

                                                CTShd cTShd = picRun.getCTR().addNewRPr().addNewShd();
                                                cTShd.setVal(STShd.CLEAR);
                                                cTShd.setColor("auto");
                                                cTShd.setFill("FFFF9e");

                                                picRun.addBreak(BreakType.PAGE);
                                                picRun.setFontSize(14);
                                                picRun.setText(mainHeader + "  /" + /*(curNum - 1) + "-" + String.valueOf((char) (subNumber + 64)) +*/ subHeader);


                                                JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
                                                jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                                                jpegParams.setCompressionQuality(1f);


                                                FileImageOutputStream fos = new FileImageOutputStream(
                                                        new File("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\tempImages\\imgFile" + j + pageNumber + ".jpg"));

                                                writer.setOutput(fos);

                                                int w = image.getWidth();
                                                int h = image.getHeight();
                                                BufferedImage newJpg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                                                int[] rgb = image.getRGB(0, 0, w, h, null, 0, w);
                                                newJpg.setRGB(0, 0, w, h, rgb, 0, w);
                                                writer.write(null, new IIOImage(image, null, null), jpegParams);

                                                File picFile = new File("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\tempImages\\imgFile" + j + pageNumber + ".jpg");
                                                FileInputStream inputStream = new FileInputStream(picFile);

                                                picRun.addPicture(inputStream, XWPFDocument.PICTURE_TYPE_JPEG, subSheets.get(j), Units.toEMU(610), Units.toEMU(770));
                                                writer.reset();
                                                inputStream.close();
                                                fos.close();
                                                pageNumber++;
                                            }
                                            document.close();
                                        } else {
                                            System.err.println(sourceFile.getName() + " File doesn't exist");
                                        }
                                    } catch (Exception pdfNot) {
                                        pdfNot.printStackTrace();
                                        System.out.println("pdf recognized but cant do anything");
                                    }

                                } else if (subSheets.get(j).substring(subSheets.get(j).lastIndexOf('.')).equals(".pdf") && subSheets.get(j).contains("SP&S")) {

                                    try {

                                        String sourceDir;
                                        sourceDir = subSheets.get(j).substring(4);
                                        File sourceFile = new File(sourceDir);
                                        System.out.println("the filepath about to be added " + sourceDir);

                                        if (sourceFile.exists()) {

                                            String fileName = sourceFile.getName().replace(".pdf", "");
                                            PDDocument document = PDDocument.load(new File(sourceDir));
                                            PDFRenderer pdfRenderer = new PDFRenderer(document);
                                            @SuppressWarnings("unchecked")

                                            int pageNumber = 0;
                                            int temp = 0;
                                            int totalPages = document.getNumberOfPages();

                                            for (PDPage page : document.getPages()) {

                                                if (!southernListIndex.isEmpty()) {

                                                    if (pageNumber >= southernListIndex.get(0)) {

                                                        System.out.println("WHAT TEMP IS GETTING " + southernListIndex.get(0));
                                                        temp = southernListIndex.get(0);
                                                        southernListIndex.remove(0);
                                                        System.out.println("removed " + southernListIndex);
                                                        subHeader = southernList.get(0);
                                                        southernList.remove(0);
                                                        System.out.println("removed " + southernList);
                                                    }
                                                }

                                                System.out.println(southernListIndex.size());
                                                System.out.println("HERE IS PAGENUMBER " + pageNumber + subHeader);
                                                System.out.println("HERE IS TEMP " + temp);

                                                if (pageNumber > 1 && pageNumber != temp) {

                                                    System.out.println("Processing page " + (pageNumber+1) + " of " + totalPages + " / " + fileName);
                                                    BufferedImage image = pdfRenderer.renderImageWithDPI(pageNumber, 190, ImageType.RGB);
                                                    XWPFParagraph p11 = doc.createParagraph();
                                                    p11.setAlignment(ParagraphAlignment.RIGHT);

                                                    XWPFRun picRun = p11.createRun();

                                                    CTShd cTShd = picRun.getCTR().addNewRPr().addNewShd();
                                                    cTShd.setVal(STShd.CLEAR);
                                                    cTShd.setColor("auto");
                                                    cTShd.setFill("FFFF9e");

                                                    picRun.addBreak(BreakType.PAGE);
                                                    picRun.setFontSize(14);
                                                    picRun.setText(mainHeader + "  /" + /*(curNum - 1) + "-" + String.valueOf((char) (subNumber + 64)) +*/ subHeader);


                                                    JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
                                                    jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                                                    jpegParams.setCompressionQuality(1f);

                                                    writer.setOutput(new FileImageOutputStream(
                                                            new File("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\tempImages\\imgFile" + j + pageNumber + ".jpg")));

                                                    int w = image.getWidth();
                                                    int h = image.getHeight();
                                                    BufferedImage newJpg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                                                    int[] rgb = image.getRGB(0, 0, w, h, null, 0, w);
                                                    newJpg.setRGB(0, 0, w, h, rgb, 0, w);
                                                    writer.write(null, new IIOImage(image, null, null), jpegParams);
                                                    picRun.addPicture(new FileInputStream("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\tempImages\\imgFile" + j + pageNumber + ".jpg"), XWPFDocument.PICTURE_TYPE_JPEG, subSheets.get(j), Units.toEMU(610), Units.toEMU(770));

                                                }
                                                pageNumber++;
                                            }
                                            document.close();
                                        } else {
                                            System.err.println(sourceFile.getName() + " File doesn't exist");
                                        }
                                    } catch (Exception pdfNot) {
                                        pdfNot.printStackTrace();
                                        System.out.println("pdf recognized but cant do anything");
                                    }

                                } else {

                                    XWPFDocument subDoc = new XWPFDocument(new FileInputStream(subSheets.get(j).substring(4)));
                                    System.out.println("the filepath about to be added " + subSheets.get(j).substring(4));
                                    List<XWPFPictureData> pic = subDoc.getAllPictures();

                                    for (int k = 0; k < pic.size(); k++) {
                                        XWPFParagraph p11 = doc.createParagraph();
                                        p11.setAlignment(ParagraphAlignment.RIGHT);

                                        System.out.println("Processing page " + (k+1) + " of " + pic.size() + " / " + subSheets.get(j).substring(subSheets.get(j).lastIndexOf('\\')+1,subSheets.get(j).lastIndexOf('.')));

                                        XWPFRun picRun = p11.createRun();

                                        CTShd cTShd = picRun.getCTR().addNewRPr().addNewShd();
                                        cTShd.setVal(STShd.CLEAR);
                                        cTShd.setColor("auto");
                                        cTShd.setFill("FFFF9e");

                                        picRun.addBreak(BreakType.PAGE);
                                        picRun.setFontSize(14);
                                        picRun.setText(mainHeader + "  /  " /*(curNum - 1) + "-" + String.valueOf((char) (subNumber + 64)) +*/ + subHeader);


                                        JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
                                        jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                                        jpegParams.setCompressionQuality(1f);

                                        writer.setOutput(new FileImageOutputStream(
                                                new File("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\tempImages\\imgFile" + j + k + ".jpg")));


                                        BufferedImage oldJpg;
                                        XWPFPictureData pict = pic.get(k);
                                        byte[] data = pict.getData();
                                        oldJpg = ImageIO.read(new ByteArrayInputStream(data));
                                        int w = oldJpg.getWidth();
                                        int h = oldJpg.getHeight();
                                        BufferedImage newJpg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                                        int[] rgb = oldJpg.getRGB(0, 0, w, h, null, 0, w);
                                        newJpg.setRGB(0, 0, w, h, rgb, 0, w);

                                        writer.write(null, new IIOImage(newJpg, null, null), jpegParams);
                                        picRun.addPicture(new FileInputStream("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\tempImages\\imgFile" + j + k + ".jpg"), XWPFDocument.PICTURE_TYPE_JPEG, subSheets.get(j), Units.toEMU(610), Units.toEMU(770));
                                    }
                                }
                            } catch (Exception notFile) {
                                System.err.println(notFile);
                            }
                        }
                    }
                    subNumber = 0;
                    list.clear();
                }

                if (i != subSheets.size() - 1) {
                    XWPFParagraph p11 = doc.createParagraph();
                    p11.setAlignment(ParagraphAlignment.LEFT);
                    p11.setVerticalAlignment(TextAlignment.TOP);
                    p11.setSpacingAfter(0);
                    p11.setSpacingBefore(0);

                    XWPFRun r13 = p11.createRun();
                    r13.setFontSize(20);
                    r13.setFontFamily("Calibri (Body)");

                    r13.addBreak(BreakType.PAGE);
                    r13.getCTR().insertNewBr(1);
                    r13.getCTR().insertNewBr(1);
                    r13.getCTR().insertNewBr(1);
                    r13.getCTR().insertNewBr(1);
                    r13.getCTR().insertNewBr(1);
                    r13.getCTR().insertNewBr(1);
                    r13.getCTR().insertNewBr(1);
                    r13.getCTR().insertNewBr(1);
                    r13.getCTR().insertNewBr(1);

                    curNumString = "                " + curNum + ")      ";
                    r13.setBold(true);
                    r13.setText(curNumString + subSheets.get(i));
                    r13.setBold(true);
                    curSubNum = 1;
                    curNum += 1;
                }
                lastMain = i;

                mainHeader = subSheets.get(i);

            } else if (subSheets.get(i).substring(0, 2).equals("  ") && !subSheets.get(i).substring(0, 4).equals("    ")) {

                list.add(numDocs);
                numDocs = 0;
                newCat = true;

                if (subSheets.get(i + 1).contains("SP&S")) {
                    int curSouthSub = curSubNum;
                    for (int s = 0; s < southernList.size(); s++) {
                        XWPFParagraph p11 = doc.createParagraph();
                        p11.setSpacingAfter(0);
                        p11.setSpacingBefore(0);

                        XWPFRun r14 = p11.createRun();
                        r14.setFontSize(16);
                        r14.setFontFamily("Calibri (Body)");

                        r14.getCTR().insertNewBr(1);

                        curNumString = "                    " + String.valueOf((char) (curSouthSub + 64)) + ". ";
                        r14.setText(curNumString + southernList.get(s));
                        curSouthSub += 1;
                    }
                } else {
                    XWPFParagraph p11 = doc.createParagraph();
                    p11.setSpacingAfter(0);
                    p11.setSpacingBefore(0);

                    XWPFRun r14 = p11.createRun();
                    r14.setFontSize(16);
                    r14.setFontFamily("Calibri (Body)");

                    r14.getCTR().insertNewBr(1);

                    curNumString = "                    " /*+ String.valueOf((char) (curSubNum + 64)) + ". "*/;
                    r14.setText(curNumString + subSheets.get(i));
                    curSubNum += 1;
                }

            } else {
                numDocs++;
                XWPFParagraph p11 = doc.createParagraph();
                p11.setSpacingAfter(0);
                p11.setSpacingBefore(0);

                XWPFRun r15 = p11.createRun();
                r15.setFontSize(12);
                r15.setFontFamily("Calibri (Body)");

                if (newCat) {
                    r15.getCTR().insertNewBr(1);
                }

                slashIndex = subSheets.get(i).lastIndexOf('\\') + 1;
                dotIndex = subSheets.get(i).lastIndexOf('.');
                if (subSheets.get(i).contains(slash)) {
                    file = subSheets.get(i).substring(slashIndex, dotIndex);
                } else {
                    file = subSheets.get(i);
                }
                r15.setText("                                   " + file);
                newCat = false;
            }
        }

        try {
            FileOutputStream out;
            if(volume.equals("")) {
                out = new FileOutputStream("Saves\\" + job + "\\" + job + " Submittal.docx");
            } else {
                File volumeFolder = new File("Saves\\" + job + "\\" + volume);
                if(!volumeFolder.exists()) {
                    volumeFolder.mkdirs();
                }
                out = new FileOutputStream("Saves\\" + job + "\\" + volume + "\\" + job + " " + volume + " Submittal.docx");
            }
            doc.write(out);
            doc.close();
        } catch (Exception f) {
            System.err.println();
        }

        SubGenApp.window.close();

        File tempFolder = new File("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\tempImages");
        //File ocrTempFolder = new File("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\ocrTempImages");

        File[] files = tempFolder.listFiles();
        for(File it: files) {
            it.delete();
        }
        //FileUtils.cleanDirectory(ocrTempFolder);



    }

}
