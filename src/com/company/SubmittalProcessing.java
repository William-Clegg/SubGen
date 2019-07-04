package com.company;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import javafx.scene.control.TreeItem;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.PDLineDashPattern;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitDestination;
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
import java.util.ArrayList;
import java.util.List;

import static Windows.ProjectInfoWindow.*;
import static com.company.SubGenApp.*;

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
    private static ArrayList<IndexPageLayoutInfo> indexInfo = new ArrayList<>();
    private static IndexPageLayoutInfo mainIndexInfo;
    private static PDFTextWriter textWriter = new PDFTextWriter();
    private PageNumAppender appender = new PageNumAppender();

    public void processSubmittalContent() {

        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.registerServiceProvider(new com.github.jaiimageio.jpeg2000.impl.J2KImageReaderSpi());

        if(!removeMainContents) {

            try {

                PDDocument mainPageDoc = new PDDocument();
                PDPage firstPage = new PDPage();
                mainPageDoc.addPage(firstPage);

                int mainIndexFirstPage;
                if(removeMembers) {
                    mainIndexFirstPage = 2;
                } else {
                    mainIndexFirstPage = 3;
                }

                mainIndexInfo = new IndexPageLayoutInfo(1, mainIndexFirstPage, new ArrayList<>(), new ArrayList<>());
                mainIndexInfo.getLocations().add(new ArrayList<>());

                float pw = firstPage.getCropBox().getUpperRightX();
                float ph = firstPage.getCropBox().getUpperRightY();

                float titleWidth = boldFont.getStringWidth("Contents") / 1000 * 36;

                textWriter.writeText(mainPageDoc, mainPageDoc.getPage(0), boldFont, 36,
                        (pw - titleWidth) / 2, ph - ph/12, "Contents");

                float mainPageLoc = ph/12;

                for(int i = 0; i < root.getChildren().size(); i++) {

                    mainPageLoc += 50;
                    mainIndexInfo.getLocations().get(mainIndexInfo.getLocations().size()-1).add(mainPageLoc);

                    TreeItem<PDFLineItem> currMain = root.getChildren().get(i);

                    textWriter.writeText(mainPageDoc, mainPageDoc.getPage(mainPageDoc.getNumberOfPages()-1), boldFont, 24,
                            pw/10, ph - mainPageLoc, (i+1) + ") " + currMain.getValue().getTitle());

                    for(int j = 0; j < root.getChildren().get(i).getChildren().size(); j++) {

                        if(j==0) {
                            mainPageLoc += 40;
                        } else {
                            mainPageLoc += 20;
                        }


                        if(mainPageLoc <= ph - 60) {
                            mainIndexInfo.getLocations().get(mainIndexInfo.getLocations().size()-1).add(mainPageLoc);
                            textWriter.writeText(mainPageDoc, mainPageDoc.getPage(mainPageDoc.getNumberOfPages()-1), font, 14,
                                    pw/6, ph - mainPageLoc, currMain.getChildren().get(j).getValue().getTitle());

                        } else {
                            mainPageLoc = 60;
                            mainIndexInfo.getLocations().add(new ArrayList<>());
                            //mainIndexInfo.getLocations().get(mainIndexInfo.getLocations().size()-1).add(mainPageLoc);

                            mainPageDoc.addPage(new PDPage());
                            textWriter.writeText(mainPageDoc, mainPageDoc.getPage(mainPageDoc.getNumberOfPages()-1), boldFont, 24,
                                    pw/10, ph - mainPageLoc, (i+1) + ") " + currMain.getValue().getTitle() + " (cont…)");

                            mainPageLoc += 40;
                            mainIndexInfo.getLocations().get(mainIndexInfo.getLocations().size()-1).add(mainPageLoc);

                            textWriter.writeText(mainPageDoc,
                                    mainPageDoc.getPage(mainPageDoc.getNumberOfPages()-1), font, 14,
                                    pw/6, ph - mainPageLoc, currMain.getChildren().get(j).getValue().getTitle());
                        }
                    }
                }

                String name = "mainIndex";

                File outFile = new File("temp\\" + name + ".pdf");
                FileOutputStream fos = new FileOutputStream(outFile);
                mainPageDoc.save(fos);
                fos.close();

                mainIndexInfo.setPages(mainPageDoc.getNumberOfPages());

                if(!name.equals("coverPage") && !name.equals("genInfo")) {
                    PDDocument doc = PDDocument.load(outFile);
                    if(name.equals("mainIndex")) {
                        mainIndexPages = doc.getNumberOfPages();
                        System.out.println("Main index pages = " + mainIndexPages);
                    }
                    doc.close();
                }

                System.out.println("Hello?");

                mainPageDoc.close();
            } catch(IOException e) {
                System.err.println(e.getMessage());
            }
        }

        for(int i = 0; i < root.getChildren().size(); i++) {
            PDPage page = new PDPage();
            ArrayList<PDDocument> sectionList = new ArrayList<>();
            ArrayList<String> sectionListFile = new ArrayList<>();
            for(int j = 0; j < root.getChildren().get(i).getChildren().size(); j++) {
                for(int k = 0; k < root.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                    File file = new File(root.getChildren().get(i).getChildren().get(j).getChildren().get(k).getValue().getLinePath());
                    try {
                        PDDocument doc = PDDocument.load(file);
                        sectionList.add(doc);
                        sectionListFile.add(file.toString());
                        doc.close();
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
            submittalSections.add(sectionList);
        }

        for(PDFLineItem pdfLineItem : contentList) {

            if(pdfLineItem.getTier() == 1) {

                IndexPageLayoutInfo indexPageLayoutInfo = new IndexPageLayoutInfo(1, null, new ArrayList<>(), new ArrayList());
                indexInfo.add(indexPageLayoutInfo);
            }
        }

        try {
            traverseOutline(root, 0, null, -1);
        } catch(IOException mainIndexIO) {
            System.err.println(mainIndexIO.getMessage());
        }

        mergePages(submittalSections);

        try {
            PDDocument completeDoc = PDDocument.load(new File("temp\\mergedSubmittal.pdf"));
            setBoookmarks(completeDoc);
            completeDoc.close();
        }catch(IOException e) {
            System.err.println(e.getMessage());
        }

        try {
            PDDocument completeDoc = PDDocument.load(new File("temp\\mergedSubmittal.pdf"));
            completeDoc.getDocumentCatalog().setPageMode(PageMode.USE_OUTLINES);


            PDOutlineItem rootBookmark = completeDoc.getDocumentCatalog().getDocumentOutline().getFirstChild();

            int i = 0;

            for(PDOutlineItem outItem : rootBookmark.children()) {

                if(!outItem.getTitle().equals("Main Contents")) {

                    PDPageDestination pd = (PDPageDestination) outItem.getDestination();

                    indexInfo.get(i).setPages(subIndexPages.get(i));

                    indexInfo.get(i).setFirstPageNum(pd.retrievePageNumber() + 1);

                    i++;
                }

            }

            if(!removeMainContents) {
                appender.appendIndexPageNums(completeDoc, mainIndexInfo, font, 12);
            }


            for(IndexPageLayoutInfo layoutInfo : indexInfo) {

                appender.appendIndexPageNums(completeDoc, layoutInfo, font, 12);
            }



            if(operationAndMain) {
                File path = new File("Saves\\" + job + "\\Operation & Maintenance");
                path.mkdirs();
                completeDoc.save(path + "\\" + job + " Plubming Submittal Stasco.pdf");
            } else {
                if (volume.equals("")) {
                    File path = new File("Saves\\" + job);
                    path.mkdirs();
                    completeDoc.save(path + "\\" + job + " Plubming Submittal Stasco.pdf");
                } else {
                    File path = new File("Saves\\" + job + "\\" + volume);
                    path.mkdirs();
                    completeDoc.save(path + "\\" + job + " " + volume + " Plubming Submittal Stasco.pdf");
                }
            }


            if(pageNumbers) {

                appender.appendPageNums(completeDoc, font, 12);

                if(operationAndMain) {
                        File path = new File("Saves\\" + job + "\\Operation & Maintenance");
                        path.mkdirs();
                        completeDoc.save(path + "\\" + job + " FOR PRINTING Plubming Submittal Stasco.pdf");
                } else {
                    if (volume.equals("")) {
                        File path = new File("Saves\\" + job);
                        path.mkdirs();
                        completeDoc.save(path + "\\" + job + " FOR PRINTING Plubming Submittal Stasco.pdf");
                    } else {
                        File path = new File("Saves\\" + job + "\\" + volume);
                        path.mkdirs();
                        completeDoc.save(path + "\\" + job + " " + volume + " FOR PRINTING Plubming Submittal Stasco.pdf");
                    }
                }
            }

            completeDoc.close();

        } catch(IOException exc) {
            System.err.println(exc.getMessage());
        }

        SubGenApp.window.close();

        File tempFolder = new File("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\temp");

    }

    private static void traverseOutline(TreeItem<PDFLineItem> node, int level, PDDocument sectionContents, int numMainContentsCompleted) throws IOException {

        if(level == 1) {

            pageLoc = 0;

            sectionContents = new PDDocument();
            sectionContents.addPage(new PDPage());
            subIndexPages.add(1);
            PDPage page = sectionContents.getPage(0);
            float pw = page.getCropBox().getUpperRightX();
            float ph = page.getCropBox().getUpperRightY();
            pageLoc += 80;

            textWriter.writeText(sectionContents, page, boldFont, 20,
                    pw/10, ph - pageLoc, (node.getParent().getChildren().indexOf(node)+1) + ") " + node.getValue().getTitle());


        } else if(level == 2) {

            PDPage page = sectionContents.getPage(sectionContents.getNumberOfPages()-1);
            float pw = page.getCropBox().getUpperRightX();
            float ph = page.getCropBox().getUpperRightY();

            pageLoc += 50;
            if(pageLoc <= ph - 80) {

                textWriter.writeText(sectionContents, page, boldFont, 16,
                        pw/8, ph - pageLoc, node.getValue().getTitle());


            } else {
                pageLoc = 60;
                sectionContents.addPage(new PDPage());
                subIndexPages.set(subIndexPages.size()-1, subIndexPages.get(subIndexPages.size()-1)+1);
                page = sectionContents.getPage(sectionContents.getNumberOfPages()-1);

                textWriter.writeText(sectionContents, page, boldFont, 16,
                        pw/8, ph - pageLoc, node.getValue().getTitle());

            }

        } else if(level == 3) {

            PDPage page = sectionContents.getPage(sectionContents.getNumberOfPages()-1);
            indexInfo.get(numMainContentsCompleted).getLocations().add(new ArrayList());

            float pw = page.getCropBox().getUpperRightX();
            float ph = page.getCropBox().getUpperRightY();
            String subFileName = node.getValue().getTitle();
            pageLoc += 20;
            if(pageLoc <= ph - 60) {

                if (node.getParent().getChildren().indexOf(node) == 0) {
                    pageLoc += 20;
                }

                textWriter.writeText(sectionContents, page, font, 14,
                        (float)(pw / 5.5), ph - pageLoc, subFileName);


                indexInfo.get(numMainContentsCompleted).getLocations().get(sectionContents.getNumberOfPages()-1).add(pageLoc);
            } else {
                pageLoc = 60;
                sectionContents.addPage(new PDPage());
                indexInfo.get(numMainContentsCompleted).getLocations().add(new ArrayList());

                subIndexPages.set(subIndexPages.size()-1, subIndexPages.get(subIndexPages.size()-1)+1);
                page = sectionContents.getPage(sectionContents.getNumberOfPages()-1);

                textWriter.writeText(sectionContents, page, boldFont, 16,
                        pw / 8, ph - pageLoc, node.getParent().getValue().getTitle() + " (cont…)");


                pageLoc += 40;

                textWriter.writeText(sectionContents, page, font, 14,
                        (float)(pw / 5.5), ph - pageLoc, subFileName);


                indexInfo.get(numMainContentsCompleted).getLocations().get(sectionContents.getNumberOfPages()-1).add(pageLoc);
            }



            File sourceFile = new File(node.getValue().getLinePath());
            if(sourceFile.exists()) {

                try {

                    PDDocument document = PDDocument.load(sourceFile);
                    document.getDocumentCatalog().setDocumentOutline(null);
                    int totalPages = document.getNumberOfPages();
                    for (int k = 0; k < totalPages; k++) {
                        PDPage specPage = document.getPage(k);
                        float specPw = specPage.getCropBox().getUpperRightX();
                        float specPh = specPage.getCropBox().getUpperRightY();

                        try {

                            String header;
                            if(operationAndMain) {
                                header = node.getParent().getParent().getValue().getTitle() + " / " + node.getParent().getValue().getTitle() + " / " +
                                        node.getValue().getTitle();

                            } else {
                                if (node.getParent().getParent().getValue().getTitle().contains("Fixtures")) {
                                    header = node.getParent().getParent().getValue().getTitle() + "  /  "  + node.getParent().getValue().getTitle() + "  /  " + subFileName;
                                } else {
                                    header = node.getParent().getParent().getValue().getTitle() + "  /  " + node.getParent().getValue().getTitle();
                                }
                            }
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

                    int num = 0;
                    for(int i = 0; i < node.getParent().getParent().getChildren().indexOf(node.getParent()); i++) {
                        num += node.getParent().getParent().getChildren().get(i).getChildren().size();
                    }
                    num += node.getParent().getChildren().indexOf(node);
                    File file = new File("temp\\" + node.getParent().getParent().getParent().getChildren().indexOf(node.getParent().getParent()) + "." +
                                            num + ".pdf");
                    document.save(file);
                    document.close();

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
            for (TreeItem<PDFLineItem> it : node.getChildren()) {

                if (level == 0) {
                    traverseOutline(it, level + 1, null, node.getChildren().indexOf(it));
                } else if (level == 1) {
                    traverseOutline(it, level + 1, sectionContents, numMainContentsCompleted);
                } else if (level == 2) {
                    traverseOutline(it, level + 1, sectionContents, numMainContentsCompleted);
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
                pdfMerger.addSource("temp\\" + i + ".pdf");

                for (int j = 0; j < submittalSectionList.get(i).size(); j++) {
                    File file = new File("temp\\" + i + "." + j + ".pdf");
                    pdfMerger.addSource(file);
                }
            }
            pdfMerger.mergeDocuments(null);
        } catch (IOException e) {
            System.err.println(e.getMessage());
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
            sectionIndexItem.setTitle(root.getChildren().get(i).getValue().getTitle());
            sectionIndexItem.setDestination(sectionDest);
            PDroot.addLast(sectionIndexItem);
            sectionIndexItem.openNode();
            if(!removeMainContents) {
                mainIndexInfo.getDocPages().add(currentPage + 1);
            }
            currentPage += subIndexPages.get(i);

            for(int j = 0; j < root.getChildren().get(i).getChildren().size(); j++) {

                PDPage subIndex = completeDoc.getPages().get(currentPage);

                PDPageDestination subDest = new PDPageFitWidthDestination();
                subDest.setPage(subIndex);
                PDOutlineItem subItem = new PDOutlineItem();
                subItem.setTitle(root.getChildren().get(i).getChildren().get(j).getValue().getTitle());
                subItem.setDestination(subDest);
                sectionIndexItem.addLast(subItem);
                if(!removeMainContents) {
                    mainIndexInfo.getDocPages().add(currentPage + 1);
                }

                for(int k = 0; k < root.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {

                    PDPage subSheet = completeDoc.getPages().get(currentPage);

                    PDPageDestination sheetDest = new PDPageFitWidthDestination();
                    sheetDest.setPage(subSheet);
                    PDOutlineItem subSheetItem = new PDOutlineItem();
                    String fullPath = root.getChildren().get(i).getChildren().get(j).getChildren().get(k).getValue().getLinePath();
                    subSheetItem.setTitle(fullPath.substring(fullPath.lastIndexOf('\\')+1, fullPath.lastIndexOf('.')));
                    subSheetItem.setDestination(sheetDest);
                    subItem.addLast(subSheetItem);
                    indexInfo.get(i).getDocPages().add(currentPage+1);
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
            System.err.println(e.getMessage());
        }

    }
}
