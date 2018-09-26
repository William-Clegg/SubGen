package com.company;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import javafx.scene.control.TreeItem;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitHeightDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitWidthDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.util.Matrix;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import javax.imageio.spi.IIORegistry;
import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static Windows.ProjectInfoWindow.*;
import static com.company.SubGenApp.*;
import static com.company.SubGenApp.subSheets;

/*--------------------------------------------------------------------------------
 *  Class which creates the submittal document from the project info and outline.
 */

//word page with 600L margins comprised of 69 vertical 8 point font 69*8 = 552

public class SubmittalProcessing {

    private static ArrayList<ArrayList<PDDocument>> submittalSections = new ArrayList<>();

    private static PDFont boldFont = PDType1Font.HELVETICA_BOLD;
    private static PDFont font = PDType1Font.HELVETICA;
    private static int num = 0;
    private static float pageLoc;
    private static int mainIndexPages = 0;
    private static ArrayList<Integer> subIndexPages = new ArrayList<>();
    private static ArrayList<Integer> subIndexPagesForecast = new ArrayList<>();
    private static int currentDocPage = 1;

    public void processSubmittalContent() {

        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.registerServiceProvider(new com.github.jaiimageio.jpeg2000.impl.J2KImageReaderSpi());

        subSheets.clear();
        for (String it: contentList) {
            subSheets.add(it);
        }

        if(!removeMainContents) {
            XWPFDocument mainIndexDoc = new XWPFDocument();

            XWPFParagraph p8 = mainIndexDoc.createParagraph();
            p8.setAlignment(ParagraphAlignment.CENTER);
            p8.setVerticalAlignment(TextAlignment.TOP);

            XWPFRun r11 = p8.createRun();
            r11.setFontSize(22);
            r11.setFontFamily("Calibri (Body)");

            r11.setUnderline(UnderlinePatterns.SINGLE);
            r11.setBold(true);
            r11.setText("INDEX");

            XWPFParagraph p9 = mainIndexDoc.createParagraph();
            p9.setAlignment(ParagraphAlignment.LEFT);
            p9.setVerticalAlignment(TextAlignment.TOP);
            p9.setIndentationLeft(540);

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

                    if (i > 0) {
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

                    if (newCat) {
                        r12.setFontSize(12);
                        r12.setFontFamily("Calibri (Body)");

                        if (curSubNum == 1) {
                            r12.getCTR().insertNewBr(1);
                        }
                        r12.getCTR().insertNewBr(1);

                        r12.setText("    " + subSheets.get(i));
                        curSubNum += 1;
                    }
                }
            }
            try {
                convertToPdf(mainIndexDoc, "mainIndex");
            } catch (IOException exc) {
                System.err.println("IOexception converting main index to pdf");
            }
        }

        for(int i = 0; i < root.getChildren().size(); i++) {
            PDPage page = new PDPage();
            float pw = page.getCropBox().getUpperRightX();
            float ph = page.getCropBox().getUpperRightY();
            subIndexPagesForecast.add(pageNumberForecast(root.getChildren().get(i), pw, ph));
            ArrayList<PDDocument> sectionList = new ArrayList<>();
            for(int j = 0; j < root.getChildren().get(i).getChildren().size(); j++) {
                for(int k = 0; k < root.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {

                    File file = new File(root.getChildren().get(i).getChildren().get(j).getChildren().get(k).getValue());
                    try {
                        PDDocument doc = PDDocument.load(file);
                        sectionList.add(doc);
                    } catch (Exception e) {
                        System.err.println("Exception in submittalsectioncontent creation");
                    }
                }
                submittalSections.add(sectionList);
            }
        }

        try {
            traverseOutline(root, 0, null);
        } catch(IOException mainIndexIO) {
            System.out.println("IO Exception for mainIndexDoc");
        }

        mergePages(submittalSections);

        try {
            PDDocument completeDoc = PDDocument.load(new File("temp\\mergedSubmittal.pdf"));
            setBoookmarks(completeDoc);
        }catch(IOException e) {
            System.err.println("IO Error in bookmarking");
        }

        try {
            PDDocument completeDoc = PDDocument.load(new File("temp\\mergedSubmittal.pdf"));
            completeDoc.getDocumentCatalog().setPageMode(PageMode.USE_OUTLINES);

            if(volume.equals("")) {
                File path = new File("Saves\\" + job);
                path.mkdirs();
                completeDoc.save(path + "\\" + job + " Plubming Submittal Stasco.pdf");
            } else {
                File path = new File("Saves\\" + job + "\\" + volume);
                path.mkdirs();
                completeDoc.save(path + "\\" + job + " " + volume + " Plubming Submittal Stasco.pdf");
            }

            if(pageNumbers) {
                for (int i = 0; i < completeDoc.getPages().getCount(); i++) {

                    PDPage page = completeDoc.getPages().get(i);
                    float pw = page.getCropBox().getUpperRightX();
                    float ph = page.getCropBox().getUpperRightY();

                    PDPageContentStream pageNumberBackground = new PDPageContentStream(completeDoc, page, true, false, true);
                    if (red != null) {
                        pageNumberBackground.setNonStrokingColor(red, green, blue);
                    } else {
                        pageNumberBackground.setNonStrokingColor(Color.YELLOW);
                    }
                    if(page.getRotation() == 90) {
                        pageNumberBackground.addRect(pw - 20, ph-25, 20, 30);
                    } else {
                        pageNumberBackground.addRect(pw - 25, 0, 50, 20);
                    }
                    pageNumberBackground.fill();
                    pageNumberBackground.close();

                    PDPageContentStream pageNum = new PDPageContentStream(completeDoc, page, true, false, true);
                    pageNum.beginText();
                    pageNum.setFont(boldFont, 12);
                    if(page.getRotation() == 90) {
                        if (i < 9) {
                            pageNum.setTextMatrix(Matrix.getRotateInstance(1.5708, pw - 6, ph - 12));
                        } else if (i < 99) {
                            pageNum.setTextMatrix(Matrix.getRotateInstance(1.5708, pw - 6, ph - 18));
                        } else {
                            pageNum.setTextMatrix(Matrix.getRotateInstance(1.5708, pw - 6, ph - 22));
                        }
                    } else {
                        if (i < 9) {
                            pageNum.newLineAtOffset(pw - 16, 6);
                        } else if (i < 99) {
                            pageNum.newLineAtOffset(pw - 19, 6);
                        } else {
                            pageNum.newLineAtOffset(pw - 23, 6);
                        }
                    }
                    pageNum.showText("" + (i + 1));
                    pageNum.endText();
                    pageNum.close();
                }
                if(volume.equals("")) {
                    File path = new File("Saves\\" + job);
                    path.mkdirs();
                    completeDoc.save(path + "\\" + job + " FOR PRINTING Plubming Submittal Stasco.pdf");
                } else {
                    File path = new File("Saves\\" + job + "\\" + volume);
                    path.mkdirs();
                    completeDoc.save(path + "\\" + job + " " + volume + " FOR PRINTING Plubming Submittal Stasco.pdf");
                }
            }
            completeDoc.close();

        } catch(IOException exc) {
            for(int i = 0; i < exc.getStackTrace().length; i++) {
                System.err.println(exc.getStackTrace()[i]);
            }
        }

        SubGenApp.window.close();

        File tempFolder = new File("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\temp");


        for(File file: tempFolder.listFiles()) {
            file.delete();
        }

    }

    private static void traverseOutline(TreeItem<String> node, int level, PDDocument sectionContents) throws IOException {

        if(level == 1) {
            pageLoc = 0;

            sectionContents = new PDDocument();
            sectionContents.addPage(new PDPage());
            subIndexPages.add(1);
            PDPage page = sectionContents.getPage(0);
            float pw = page.getCropBox().getUpperRightX();
            float ph = page.getCropBox().getUpperRightY();

            PDPageContentStream stream = new PDPageContentStream(sectionContents, page, true, true, true);
            stream.beginText();
            stream.setFont(boldFont, 20);
            pageLoc += 80;
            stream.newLineAtOffset(pw/10, ph - pageLoc);
            stream.showText((node.getParent().getChildren().indexOf(node)+1) + ") " + node.getValue());
            stream.endText();
            stream.close();

        } else if(level == 2) {

            PDPage page = sectionContents.getPage(sectionContents.getNumberOfPages()-1);
            float pw = page.getCropBox().getUpperRightX();
            float ph = page.getCropBox().getUpperRightY();

            pageLoc += 50;
            if(pageLoc <= ph - 80) {
                PDPageContentStream stream = new PDPageContentStream(sectionContents, page, true, true, true);
                stream.beginText();
                stream.setFont(boldFont, 16);
                stream.newLineAtOffset(pw / 8, ph - pageLoc);
                if((pageLoc+40) + ((node.getChildren().size()-1)*20) > ph-40) {
                    stream.showText(node.getValue() + " (cont. next page…)");
                } else {
                    stream.showText(node.getValue());
                }
                stream.endText();
                stream.close();
            } else {
                pageLoc = 60;
                sectionContents.addPage(new PDPage());
                subIndexPages.set(subIndexPages.size()-1, subIndexPages.get(subIndexPages.size()-1)+1);
                page = sectionContents.getPage(sectionContents.getNumberOfPages()-1);
                PDPageContentStream stream = new PDPageContentStream(sectionContents, page, true, true, true);
                stream.beginText();
                stream.setFont(boldFont, 16);
                stream.newLineAtOffset(pw / 8, ph - pageLoc);
                stream.showText(node.getValue());
                stream.endText();
                stream.close();
            }

        } else if(level == 3) {

            PDPage page = sectionContents.getPage(sectionContents.getNumberOfPages()-1);
            float pw = page.getCropBox().getUpperRightX();
            float ph = page.getCropBox().getUpperRightY();
            int slashIndex = node.getValue().lastIndexOf('\\') + 1;
            int dotIndex = node.getValue().lastIndexOf('.');
            String subFileName = node.getValue().substring(slashIndex, dotIndex);
            pageLoc += 20;
            if(pageLoc <= ph - 60) {
                PDPageContentStream stream = new PDPageContentStream(sectionContents, page, true, true, true);
                stream.beginText();
                stream.setFont(font, 14);
                if (node.getParent().getChildren().indexOf(node) == 0) {
                    pageLoc += 20;
                    stream.newLineAtOffset((float)(pw / 5.5), ph - pageLoc);
                } else {
                    stream.newLineAtOffset((float)(pw / 5.5), ph - pageLoc);
                }
                stream.showText(subFileName);
                stream.endText();
                stream.close();
            } else {
                pageLoc = 60;
                sectionContents.addPage(new PDPage());
                subIndexPages.set(subIndexPages.size()-1, subIndexPages.get(subIndexPages.size()-1)+1);
                page = sectionContents.getPage(sectionContents.getNumberOfPages()-1);
                PDPageContentStream stream = new PDPageContentStream(sectionContents, page, true, true, true);
                stream.beginText();
                stream.setFont(boldFont, 16);
                stream.newLineAtOffset((float)(pw / 8), ph - pageLoc);
                stream.showText(node.getParent().getValue() + " (cont.)");
                stream.endText();

                pageLoc += 40;

                stream.beginText();
                stream.setFont(font, 14);
                stream.newLineAtOffset((float)(pw / 5.5), ph - pageLoc);
                stream.showText(subFileName);
                stream.endText();
                stream.close();
            }

            File sourceFile = new File(node.getValue());
            if(sourceFile.exists()) {

                try {

                    PDDocument document = PDDocument.load(sourceFile);
                    document.getDocumentCatalog().setDocumentOutline(null);
                    int totalPages = document.getNumberOfPages();
                    for (int k = 0; k < totalPages; k++) {
                        PDPage specPage = document.getPage(k);
                        float specPw = specPage.getCropBox().getUpperRightX();
                        float specPh = specPage.getCropBox().getUpperRightY();
                        System.out.println(subFileName);

                        try {

                            String header = node.getParent().getParent().getValue() + "  /  " + node.getParent().getValue();
                            float textWidth = boldFont.getStringWidth(header) / 1000 * 12;

                            PDPageContentStream rectangle = new PDPageContentStream(document, specPage, true, false, true);
                            if (red != null) {
                                rectangle.setNonStrokingColor(red, green, blue);
                            } else {
                                rectangle.setNonStrokingColor(Color.YELLOW);
                            }
                            if(specPage.getRotation() == 90) {
                                rectangle.addRect(0, specPh - (textWidth + 7), 20, textWidth + 10);
                            } else {
                                rectangle.addRect(specPw - (textWidth + 7), specPh - 16, textWidth + 10, 30);
                            }
                            rectangle.fill();
                            rectangle.close();

                            PDPageContentStream contents = new PDPageContentStream(document, specPage, true, false, true);
                            contents.beginText();
                            contents.setFont(boldFont, 12);
                            if(specPage.getRotation() == 90) {
                                contents.setTextMatrix(Matrix.getRotateInstance(1.5708, 14, specPh - (textWidth + 3)));
                            } else {
                                contents.newLineAtOffset(specPw - (textWidth + 3), specPh - 12);
                            }
                            contents.showText(header);
                            contents.endText();
                            contents.close();


                        } catch (Exception f) {}
                    }

                } catch(Exception docException) {}
            }

            if(node.getParent().getChildren().indexOf(node) == node.getParent().getChildren().size()-1
                    && node.getParent().getParent().getChildren().indexOf(node.getParent()) == node.getParent().getParent().getChildren().size()-1) {

                try {

                    sectionContents.save("temp\\" + node.getParent().getParent().getParent().getChildren().indexOf(node.getParent().getParent()) + ".pdf");
                    sectionContents.close();

                } catch(IOException e) {
                    for(int i = 0; i < e.getStackTrace().length; i++) {
                        System.err.println(e.getStackTrace()[i]);
                    }
                }
            }
        }

        if(level < 3) {
            for (TreeItem<String> it : node.getChildren()) {

                if (level == 0) {
                    traverseOutline(it, level + 1, null);
                } else if (level == 1) {
                    traverseOutline(it, level + 1, sectionContents);
                } else if (level == 2) {
                    traverseOutline(it, level + 1, sectionContents);
                }
            }
        }
    }


    public void convertToPdf(XWPFDocument document, String name) throws IOException {

        XWPFStyles styles = document.createStyles();

        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        CTPageSz pageSz = sectPr.addNewPgSz();
        pageSz.setW(BigInteger.valueOf(12240)); //612 pt or 8.5"
        pageSz.setH(BigInteger.valueOf(15840)); //792 pt or 11"

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write(out);

        document = new XWPFDocument(new ByteArrayInputStream(out.toByteArray()));
        out.close();
        PdfOptions options = PdfOptions.create();
        PdfConverter converter = (PdfConverter) PdfConverter.getInstance();
        File outFile = new File("temp\\" + name + ".pdf");
        FileOutputStream fos = new FileOutputStream(outFile);
        converter.convert(document, fos, options);
        fos.close();



        if(!name.equals("coverPage") && !name.equals("genInfo")) {
            PDDocument doc = PDDocument.load(outFile);
            if(name.equals("mainIndex")) {
                mainIndexPages = doc.getNumberOfPages();
                System.out.println("Main index pages = " + mainIndexPages);
            }
            doc.close();
        }
        document.close();
    }


    private static void mergePages(ArrayList<ArrayList<PDDocument>> submittalSectionList) {

        System.out.println(submittalSectionList.size());

        try {
            PDFMergerUtility pdfMerger = new PDFMergerUtility();
            pdfMerger.setDestinationFileName("temp\\mergedSubmittal.pdf");
            pdfMerger.addSource("temp\\PictureDocument.pdf");
            if(!removeMembers) {
                pdfMerger.addSource("temp\\genInfo.pdf");
            }
            if(!removeMainContents) {
                pdfMerger.addSource("temp\\mainIndex.pdf");
            }

            for (int i = 0; i < root.getChildren().size(); i++) {
                System.out.println("i = " + i);
                pdfMerger.addSource("temp\\" + i + ".pdf");

                for (int j = 0; j < submittalSectionList.get(i).size(); j++) {
                    File file = new File("temp\\" + i + j + ".pdf");
                    FileOutputStream fos = new FileOutputStream(file);
                    submittalSectionList.get(i).get(j).save(fos);
                    submittalSectionList.get(i).get(j).close();
                    pdfMerger.addSource(file);
                    fos.close();
                }
            }
            pdfMerger.mergeDocuments(null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void setBoookmarks(PDDocument completeDoc) {

        int currentPage;
        pageLoc = 80;

        PDDocumentOutline outline = new PDDocumentOutline();
        completeDoc.getDocumentCatalog().setDocumentOutline(outline);

        PDOutlineItem PDroot = new PDOutlineItem();
        PDroot.setTitle("Submittal");
        outline.addLast(PDroot);

        if(!removeMembers) {
            currentPage = 2;
        } else {
            currentPage = 1;
        }

        if(!removeMainContents) {
            PDPage mainIndex = completeDoc.getPages().get(currentPage);
            currentPage += mainIndexPages;

            PDPageDestination dest = new PDPageFitWidthDestination();
            dest.setPage(mainIndex);
            PDOutlineItem mainIndexItem = new PDOutlineItem();
            mainIndexItem.setTitle("Main Contents");
            mainIndexItem.setDestination(dest);
            PDroot.addLast(mainIndexItem);
        }

        for(int i = 0; i < root.getChildren().size(); i++) {

            PDPage sectionIndex = completeDoc.getPages().get(currentPage);

            PDPageDestination sectionDest = new PDPageFitWidthDestination();
            sectionDest.setPage(sectionIndex);
            PDOutlineItem sectionIndexItem = new PDOutlineItem();
            sectionIndexItem.setTitle(root.getChildren().get(i).getValue());
            sectionIndexItem.setDestination(sectionDest);
            PDroot.addLast(sectionIndexItem);
            sectionIndexItem.openNode();
            currentPage += subIndexPages.get(i);

            for(int j = 0; j < root.getChildren().get(i).getChildren().size(); j++) {

                PDPage subIndex = completeDoc.getPages().get(currentPage);

                PDPageDestination subDest = new PDPageFitWidthDestination();
                subDest.setPage(subIndex);
                PDOutlineItem subItem = new PDOutlineItem();
                subItem.setTitle(root.getChildren().get(i).getChildren().get(j).getValue());
                subItem.setDestination(subDest);
                sectionIndexItem.addLast(subItem);

                for(int k = 0; k < root.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {

                    PDPage subSheet = completeDoc.getPages().get(currentPage);

                    PDPageDestination sheetDest = new PDPageFitWidthDestination();
                    sheetDest.setPage(subSheet);
                    PDOutlineItem subSheetItem = new PDOutlineItem();
                    String fullPath = root.getChildren().get(i).getChildren().get(j).getChildren().get(k).getValue();
                    subSheetItem.setTitle(fullPath.substring(fullPath.lastIndexOf('\\')+1, fullPath.lastIndexOf('.')));
                    subSheetItem.setDestination(sheetDest);
                    subItem.addLast(subSheetItem);
                    currentPage += submittalSections.get(i).get(num).getNumberOfPages();
                    num+=1;
                }
            }
            num = 0;
        }

        outline.openNode();
        PDroot.openNode();

        try {
            completeDoc.save("temp\\mergedSubmittal.pdf");
            completeDoc.close();
        } catch(IOException e) {
            System.err.println("IO Error in bookmarking");
        }

    }

    private static int pageNumberForecast(TreeItem<String> node, float pw, float ph) {
        int numberOfPages = 1;
        int currentLocation = 80;

        for(int i = 0; i < node.getChildren().size(); i++) {
            currentLocation += 50;
            if(currentLocation > ph - 80) {
                numberOfPages++;
                currentLocation = 60;
            }

            for(int j = 0; j < node.getChildren().get(i).getChildren().size(); j++) {

                if(currentLocation > ph - 80) {
                    numberOfPages++;
                    currentLocation = 100;
                } else {
                    if (j == 0) {
                        currentLocation += 40;
                    } else {
                        currentLocation += 20;
                    }
                }
            }
        }

        return numberOfPages;
    }

}
