package com.company;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

import javax.imageio.IIOImage;
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
import static com.company.SubGenApp.*;
import static com.company.SubGenApp.southernList;
import static com.company.SubGenApp.subSheets;

/*--------------------------------------------------------------------------------
 *  Class which creates the submittal document from the project info and outline.
 */

//word page with 600L margins comprised of 69 vertical 8 point font 69*8 = 552

public class SubmittalProcessing {

    public void processSubmittalContent(XWPFDocument partialDoc) {
        XWPFDocument submittalDoc = partialDoc;

        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.registerServiceProvider(new com.github.jaiimageio.jpeg2000.impl.J2KImageReaderSpi());

        subSheets.clear();
        for (String it: contentList) {
            subSheets.add(it);
        }

        /*--------------------------------------------------------------------------------
         *  Margins are set to zero so that the submittal sheets fill each page properly.
         */

        CTSectPr sectPr = submittalDoc.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(600L));
        pageMar.setTop(BigInteger.valueOf(600L));
        pageMar.setRight(BigInteger.valueOf(600L));
        pageMar.setBottom(BigInteger.valueOf(600L));

        XWPFParagraph p9 = submittalDoc.createParagraph();
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

                    XWPFParagraph p11 = submittalDoc.createParagraph();
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
                                                XWPFParagraph p11 = submittalDoc.createParagraph();
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
                    XWPFParagraph p11 = submittalDoc.createParagraph();
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
                        XWPFParagraph p11 = submittalDoc.createParagraph();
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
                    XWPFParagraph p11 = submittalDoc.createParagraph();
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
                XWPFParagraph p11 = submittalDoc.createParagraph();
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
            submittalDoc.write(out);
            submittalDoc.close();
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
