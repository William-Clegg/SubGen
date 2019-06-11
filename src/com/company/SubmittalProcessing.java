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
    private static ArrayList<ArrayList<String>> submittalSectionsFile = new ArrayList<>();

    private static PDFont boldFont = PDType1Font.HELVETICA_BOLD;
    private static PDFont font = PDType1Font.HELVETICA;
    private static int num = 0;
    private static float pageLoc;
    private static int mainIndexPages = 0;
    private static ArrayList<Integer> subIndexPages = new ArrayList<>();
    private static ArrayList<IndexPageLayoutInfo> indexInfo = new ArrayList<>();
    private static IndexPageLayoutInfo mainIndexInfo;

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

                mainIndexInfo = new IndexPageLayoutInfo(null, mainIndexFirstPage, new ArrayList<>(), new ArrayList<>());
                mainIndexInfo.getLocations().add(new ArrayList<>());

                float pw = firstPage.getCropBox().getUpperRightX();
                float ph = firstPage.getCropBox().getUpperRightY();

                float titleWidth = boldFont.getStringWidth("Contents") / 1000 * 36;

                PDPageContentStream stream = new PDPageContentStream(mainPageDoc, mainPageDoc.getPage(0), true, true, true);
                stream.beginText();
                stream.setFont(boldFont, 36);
                stream.newLineAtOffset((pw - titleWidth) / 2, ph - ph/12);
                stream.showText("Contents");
                stream.endText();
                stream.close();

                float mainPageLoc = ph/12;

                for(int i = 0; i < root.getChildren().size(); i++) {

                    mainPageLoc += 50;
                    mainIndexInfo.getLocations().get(mainIndexInfo.getLocations().size()-1).add(mainPageLoc);

                    PDPageContentStream mainStream = new PDPageContentStream(mainPageDoc, mainPageDoc.getPage(mainPageDoc.getNumberOfPages()-1), true, true, true);
                    mainStream.beginText();
                    mainStream.setFont(boldFont, 24);
                    mainStream.newLineAtOffset(pw/10, ph - mainPageLoc);
                    mainStream.showText((i+1) + ") " + root.getChildren().get(i).getValue().getTitle());
                    mainStream.endText();
                    mainStream.close();

                    for(int j = 0; j < root.getChildren().get(i).getChildren().size(); j++) {

                        if(j==0) {
                            mainPageLoc += 40;
                        } else {
                            mainPageLoc += 20;
                        }


                        if(mainPageLoc <= ph - 60) {
                            mainIndexInfo.getLocations().get(mainIndexInfo.getLocations().size()-1).add(mainPageLoc);
                            PDPageContentStream subStream = new PDPageContentStream(mainPageDoc, mainPageDoc.getPage(mainPageDoc.getNumberOfPages() - 1), true, true, true);
                            subStream.beginText();
                            subStream.setFont(font, 14);
                            subStream.newLineAtOffset(pw / 6, ph - mainPageLoc);
                            subStream.showText(root.getChildren().get(i).getChildren().get(j).getValue().getTitle());
                            subStream.endText();
                            subStream.close();
                        } else {
                            mainPageLoc = 60;
                            mainIndexInfo.getLocations().add(new ArrayList<>());
                            mainIndexInfo.getLocations().get(mainIndexInfo.getLocations().size()-1).add(mainPageLoc);

                            mainPageDoc.addPage(new PDPage());
                            PDPageContentStream mainContinueStream = new PDPageContentStream(mainPageDoc, mainPageDoc.getPage(mainPageDoc.getNumberOfPages() - 1), true, true, true);
                            mainContinueStream.beginText();
                            mainContinueStream.setFont(boldFont, 24);
                            mainContinueStream.newLineAtOffset(pw / 10, ph - mainPageLoc);
                            mainContinueStream.showText((i+1) + ") " + root.getChildren().get(i).getValue().getTitle() + " (cont…)");
                            mainContinueStream.endText();
                            mainContinueStream.close();

                            mainPageLoc += 40;
                            mainIndexInfo.getLocations().get(mainIndexInfo.getLocations().size()-1).add(mainPageLoc);

                            PDPageContentStream subStream = new PDPageContentStream(mainPageDoc, mainPageDoc.getPage(mainPageDoc.getNumberOfPages() - 1), true, true, true);
                            subStream.beginText();
                            subStream.setFont(font, 14);
                            subStream.newLineAtOffset(pw / 6, ph - mainPageLoc);
                            subStream.showText(root.getChildren().get(i).getChildren().get(j).getValue().getTitle());
                            subStream.endText();
                            subStream.close();
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
            submittalSectionsFile.add(sectionListFile);
        }

        for(PDFLineItem pdfLineItem : contentList) {

            if(pdfLineItem.getTier() == 1) {

                IndexPageLayoutInfo indexPageLayoutInfo = new IndexPageLayoutInfo(null, null, new ArrayList<>(), new ArrayList());
                indexInfo.add(indexPageLayoutInfo);
            }
        }

        try {
            traverseOutline(root, 0, null, -1);
        } catch(IOException mainIndexIO) {
            System.err.println("IO Exception for mainIndexDoc");
        }

        mergePages(submittalSections);

        try {
            PDDocument completeDoc = PDDocument.load(new File("temp\\mergedSubmittal.pdf"));
            setBoookmarks(completeDoc);
            completeDoc.close();
        }catch(IOException e) {
            System.err.println("IO Error in bookmarking");
        }

        try {
            PDDocument completeDoc = PDDocument.load(new File("temp\\mergedSubmittal.pdf"));
            completeDoc.getDocumentCatalog().setPageMode(PageMode.USE_OUTLINES);

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
                    if (page.getRotation() == 90) {
                        pageNumberBackground.addRect(pw - 20, ph - 25, 20, 30);
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

                /*
                ArrayList<String> pageNumList = new ArrayList<String>();

                PDOutlineItem node = completeDoc.getDocumentCatalog().getDocumentOutline().getFirstChild();

                for(PDOutlineItem mainChild : node.children()) {

                    for(PDOutlineItem subChild : mainChild.children()) {

                        for(PDOutlineItem itemChild : subChild.children()) {

                            PDPageDestination pd = (PDPageDestination) itemChild.getDestination();
                            pageNumList.add("" + (pd.retrievePageNumber() + 1));
                        }
                    }
                }

                for (String num : pageNumList) {
                    searchReplace("```", num, false, completeDoc);
                }

                 */

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

                int continuousLocCount = 0;

                for(int k = 0; k < mainIndexInfo.getPages(); k++) {

                    PDPage currentPage = completeDoc.getPage((mainIndexInfo.getFirstPageNum() + k) - 1);

                    float pw = currentPage.getCropBox().getUpperRightX();
                    float ph = currentPage.getCropBox().getUpperRightY();

                    System.out.println("size " + mainIndexInfo.getLocations().get(k).size());

                    for(int l = 0; l < mainIndexInfo.getLocations().get(k).size(); l++) {

                        if(continuousLocCount == 29) {
                            continuousLocCount = 28;
                        }

                        PDPageContentStream stream = new PDPageContentStream(completeDoc, currentPage, true, true, true);
                        stream.beginText();
                        stream.setFont(font, 12);
                        stream.newLineAtOffset( (pw - 60), ph - mainIndexInfo.getLocations().get(k).get(l));
                        stream.showText("Pg " + mainIndexInfo.getDocPages().get(continuousLocCount));
                        stream.endText();
                        stream.close();

                        PDPageFitWidthDestination pageDestination = new PDPageFitWidthDestination();
                        pageDestination.setPage(completeDoc.getPage(mainIndexInfo.getDocPages().get(continuousLocCount) - 1));
                        PDActionGoTo action = new PDActionGoTo();
                        action.setDestination(pageDestination);

                        PDBorderStyleDictionary dictionary = new PDBorderStyleDictionary();
                        dictionary.setStyle(PDBorderStyleDictionary.STYLE_UNDERLINE);
                        dictionary.setWidth(0);

                        PDAnnotationLink annotationLink = new PDAnnotationLink();
                        annotationLink.setAction(action);
                        annotationLink.setBorderStyle(dictionary);

                        PDRectangle position = new PDRectangle();
                        position.setLowerLeftX(pw/6);
                        position.setLowerLeftY(ph - (mainIndexInfo.getLocations().get(k).get(l) + 4));
                        position.setUpperRightX(pw - pw/25);
                        position.setUpperRightY(ph - (mainIndexInfo.getLocations().get(k).get(l) - 13));

                        annotationLink.setRectangle(position);

                        currentPage.getAnnotations().add(annotationLink);

                        continuousLocCount++;
                    }

                }

                for(int j = 0; j < indexInfo.size(); j++) {

                    continuousLocCount = 0;

                    for(int k = 0; k < indexInfo.get(j).getPages(); k++) {

                        PDPage currentPage = completeDoc.getPage((indexInfo.get(j).getFirstPageNum() + k) - 1);
                        System.out.println((indexInfo.get(j).getFirstPageNum() + k) - 1);

                        float pw = currentPage.getCropBox().getUpperRightX();
                        float ph = currentPage.getCropBox().getUpperRightY();

                        for(int l = 0; l < indexInfo.get(j).getLocations().get(k).size(); l++) {

                            PDPageContentStream stream = new PDPageContentStream(completeDoc, currentPage, true, true, true);
                            stream.beginText();
                            stream.setFont(font, 12);
                            stream.newLineAtOffset( (pw - 60), ph - indexInfo.get(j).getLocations().get(k).get(l));
                            stream.showText("Pg " + indexInfo.get(j).getDocPages().get(continuousLocCount));
                            stream.endText();
                            stream.close();

                            PDPageFitWidthDestination pageDestination = new PDPageFitWidthDestination();
                            pageDestination.setPage(completeDoc.getPage(indexInfo.get(j).getDocPages().get(continuousLocCount) - 1));
                            PDActionGoTo action = new PDActionGoTo();
                            action.setDestination(pageDestination);

                            PDBorderStyleDictionary dictionary = new PDBorderStyleDictionary();
                            dictionary.setStyle(PDBorderStyleDictionary.STYLE_UNDERLINE);
                            dictionary.setWidth(0);

                            PDAnnotationLink annotationLink = new PDAnnotationLink();
                            annotationLink.setAction(action);
                            annotationLink.setBorderStyle(dictionary);

                            PDRectangle position = new PDRectangle();
                            position.setLowerLeftX(pw/6);
                            position.setLowerLeftY(ph - (indexInfo.get(j).getLocations().get(k).get(l) + 4));
                            position.setUpperRightX(pw - pw/25);
                            position.setUpperRightY(ph - (indexInfo.get(j).getLocations().get(k).get(l) - 13));

                            annotationLink.setRectangle(position);

                            currentPage.getAnnotations().add(annotationLink);

                            continuousLocCount++;
                        }

                    }
                }



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

            PDPageContentStream stream = new PDPageContentStream(sectionContents, page, true, true, true);
            stream.beginText();
            stream.setFont(boldFont, 20);
            pageLoc += 80;
            stream.newLineAtOffset(pw/10, ph - pageLoc);
            stream.showText((node.getParent().getChildren().indexOf(node)+1) + ") " + node.getValue().getTitle());
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
                    stream.showText(node.getValue().getTitle()/* + " (cont…)"*/);
                } else {
                    stream.showText(node.getValue().getTitle());
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
                stream.showText(node.getValue().getTitle());
                stream.endText();
                stream.close();
            }

        } else if(level == 3) {

            System.out.println("Number of main is now " + numMainContentsCompleted);

            PDPage page = sectionContents.getPage(sectionContents.getNumberOfPages()-1);
            indexInfo.get(numMainContentsCompleted).getLocations().add(new ArrayList());
            //indexInfo.get(numMainContentsCompleted).getDocPages().add(new ArrayList());
            float pw = page.getCropBox().getUpperRightX();
            float ph = page.getCropBox().getUpperRightY();
            String subFileName = node.getValue().getTitle();
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
                indexInfo.get(numMainContentsCompleted).getLocations().get(sectionContents.getNumberOfPages()-1).add(pageLoc);
                stream.showText(subFileName);
                stream.endText();
                stream.close();
            } else {
                pageLoc = 60;
                sectionContents.addPage(new PDPage());
                indexInfo.get(numMainContentsCompleted).getLocations().add(new ArrayList());
                //indexInfo.get(numMainContentsCompleted).getDocPages().add(new ArrayList());
                subIndexPages.set(subIndexPages.size()-1, subIndexPages.get(subIndexPages.size()-1)+1);
                page = sectionContents.getPage(sectionContents.getNumberOfPages()-1);
                PDPageContentStream stream = new PDPageContentStream(sectionContents, page, true, true, true);
                stream.beginText();
                stream.setFont(boldFont, 16);
                stream.newLineAtOffset((float)(pw / 8), ph - pageLoc);
                stream.showText(node.getParent().getValue().getTitle() + " (cont…)");
                stream.endText();

                pageLoc += 40;

                stream.beginText();
                stream.setFont(font, 14);
                stream.newLineAtOffset((float)(pw / 5.5), ph - pageLoc);
                indexInfo.get(numMainContentsCompleted).getLocations().get(sectionContents.getNumberOfPages()-1).add(pageLoc);
                stream.showText(subFileName);
                stream.endText();
                stream.close();
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
                                    header = node.getParent().getParent().getValue().getTitle() + "  /  " + subFileName;
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
            mainIndexInfo.getDocPages().add(currentPage+1);
            currentPage += subIndexPages.get(i);

            for(int j = 0; j < root.getChildren().get(i).getChildren().size(); j++) {

                PDPage subIndex = completeDoc.getPages().get(currentPage);

                PDPageDestination subDest = new PDPageFitWidthDestination();
                subDest.setPage(subIndex);
                PDOutlineItem subItem = new PDOutlineItem();
                subItem.setTitle(root.getChildren().get(i).getChildren().get(j).getValue().getTitle());
                subItem.setDestination(subDest);
                sectionIndexItem.addLast(subItem);
                mainIndexInfo.getDocPages().add(currentPage+1);

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

    private static void searchReplace (String search, String replace, boolean replaceAll, PDDocument doc) throws IOException {

        PDPageTree pages = doc.getDocumentCatalog().getPages();

        for (PDPage page : pages) {
            if(page.getMetadata() != null) {
                PDFStreamParser parser = new PDFStreamParser(page);
                parser.parse();
                List tokens = parser.getTokens();
                for (int j = 0; j < tokens.size(); j++) {
                    Object next = tokens.get(j);
                    if (next instanceof Operator) {
                        Operator op = (Operator) next;
                        // Tj and TJ are the two operators that display strings in a PDF
                        // Tj takes one operator and that is the string to display so lets update that operator
                        if (op.getName().equals("Tj")) {
                            COSString previous = (COSString) tokens.get(j - 1);
                            System.out.println(previous);
                            String string = previous.getString();
                            if (replaceAll) {
                                string = string.replaceAll(search, replace);
                            } else {
                                string = string.replaceFirst(search, replace);
                            }
                            previous.setValue(string.getBytes());
                        } else if (op.getName().equals("TJ")) {
                            COSArray previous = (COSArray) tokens.get(j - 1);
                            for (int k = 0; k < previous.size(); k++) {
                                Object arrElement = previous.getObject(k);
                                if (arrElement instanceof COSString) {
                                    COSString cosString = (COSString) arrElement;
                                    String string = cosString.getString();

                                    if (replaceAll) {
                                        string = string.replaceAll(search, replace);
                                    } else {
                                        string = string.replaceFirst(search, replace);

                                    }

                                    cosString.setValue(string.getBytes());
                                }
                            }
                        }
                    }
                }
                // now that the tokens are updated we will replace the page content stream.
                PDStream updatedStream = new PDStream(doc);
                OutputStream out = updatedStream.createOutputStream();
                ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
                tokenWriter.writeTokens(tokens);
                out.close();
                page.setContents(updatedStream);
            }

        }
    }


}
