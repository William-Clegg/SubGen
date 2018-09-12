package com.company;

import javafx.scene.control.TreeItem;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.poi.wp.usermodel.Paragraph;
import org.apache.poi.xwpf.usermodel.*;

import javax.imageio.spi.IIORegistry;
import java.io.*;
import java.nio.CharBuffer;
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

    public void processSubmittalContent() {

        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.registerServiceProvider(new com.github.jaiimageio.jpeg2000.impl.J2KImageReaderSpi());

        subSheets.clear();
        for (String it: contentList) {
            subSheets.add(it);
        }

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

        try {
            mainIndexDoc.write(new FileOutputStream("MainIndexDoc.docx"));
        } catch(IOException mainIndexIO) {
            System.out.println("IO Exception for mainIndexDoc");
        }

        traverseOutline(root, 0, null, null);

        SubGenApp.window.close();

        File tempFolder = new File("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\tempImages");

        File[] files = tempFolder.listFiles();
        for(File it: files) {
            it.delete();
        }
    }

    private static void traverseOutline(TreeItem<String> node, int level, ArrayList<PDDocument> sectionList, XWPFDocument sectionIndex) {

        if(level == 1) {
            sectionList = new ArrayList<>();

            sectionIndex = new XWPFDocument();
            XWPFParagraph paragraph = sectionIndex.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.LEFT);
            paragraph.setVerticalAlignment(TextAlignment.TOP);
            paragraph.setIndentationLeft(540);

            XWPFRun run = paragraph.createRun();
            run.setFontSize(20);
            run.setFontFamily("Calibri (Body)");

            run.setBold(true);
            run.setText((node.getParent().getChildren().indexOf(node)+1) + ") " + node.getValue());

        } else if(level == 2) {

            XWPFParagraph paragraph = sectionIndex.createParagraph();
            paragraph.setIndentationLeft(1080);
            XWPFRun run = paragraph.createRun();
            run.setFontSize(16);
            run.setFontFamily("Calibri (Body)");
            run.getCTR().insertNewBr(1);
            run.setText(node.getValue());
        } else if(level == 3) {

            XWPFParagraph paragraph = sectionIndex.createParagraph();
            paragraph.setIndentationLeft(1260);
            XWPFRun run = paragraph.createRun();
            run.setFontSize(12);
            run.setFontFamily("Calibri (Body)");
            run.getCTR().insertNewBr(1);
            if(node.getParent().getChildren().indexOf(node) == 0) {
                run.getCTR().insertNewBr(1);
            }
            int slashIndex = node.getValue().lastIndexOf('\\') + 1;
            int dotIndex = node.getValue().lastIndexOf('.');
            String subFileName = node.getValue().substring(slashIndex, dotIndex);
            run.setText(subFileName);

            File sourceFile = new File(node.getValue());
            if(sourceFile.exists()) {

                try {

                    PDDocument document = PDDocument.load(sourceFile);
                    int totalPages = document.getNumberOfPages();
                    for (int k = 0; k < totalPages; k++) {
                        PDPage page = document.getPage(k);
                        List<PDAnnotation> annotations = page.getAnnotations();
                        float pw = page.getMediaBox().getUpperRightX();
                        float ph = page.getMediaBox().getUpperRightY();
                        System.out.println(subFileName);

                        PDFont font = PDType1Font.HELVETICA_BOLD;
                        try (PDPageContentStream contents = new PDPageContentStream(document, page, true, false, true))
                        {
                            String header = node.getParent().getParent().getValue() + "  /" + node.getParent().getValue();
                            float textWidth = font.getStringWidth(header) / 1000 * 15;
                            contents.beginText();
                            contents.setFont(font, 15);
                            contents.newLineAtOffset(pw - (textWidth + 6), ph - 14);
                            contents.showText(header);
                            contents.endText();

                            PDAnnotationTextMarkup highlight = new PDAnnotationTextMarkup(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT);
                            PDRectangle position = new PDRectangle();
                            position.setLowerLeftX(pw - (textWidth + 12));
                            position.setLowerLeftY(ph - 18);
                            position.setUpperRightX(pw);
                            position.setUpperRightY(ph);
                            highlight.setRectangle(position);
                            highlight.setConstantOpacity((float) 1.0);

                            float[] quadPoints = new float[]{pw - (textWidth + 12), ph, pw, ph, pw - (textWidth + 12), ph - 18, pw, ph - 18};

                            highlight.setQuadPoints(quadPoints);
                            PDColor yellow = new PDColor(new float[]{1, 1, 80 / 255F}, PDDeviceRGB.INSTANCE);
                            highlight.setColor(yellow);
                            annotations.add(highlight);
                        }
                    }
                    sectionList.add(document);

                } catch(Exception docException) {}
            }

            if(node.getParent().getChildren().indexOf(node) == node.getParent().getChildren().size()-1
                    && node.getParent().getParent().getChildren().indexOf(node.getParent()) == node.getParent().getParent().getChildren().size()-1) {

                System.out.println("hello??");

                try {
                    sectionIndex.write(new FileOutputStream(node.getParent().getParent().getValue() + ".docx"));
                    sectionIndex.close();
                    submittalSections.add(sectionList);

                    System.out.print("!!!!!!!!!!!!" + node.getParent().getParent().getValue());
                } catch(IOException e) {}
            }
        }

        if(level < 3) {
            for (TreeItem<String> it : node.getChildren()) {

                if (level == 0) {
                    System.out.println("hello0");
                    traverseOutline(it, level + 1, null, null);
                } else if (level == 1) {
                    System.out.println("hello1");
                    traverseOutline(it, level + 1, sectionList, sectionIndex);
                } else if (level == 2) {
                    System.out.println("hello2");
                    traverseOutline(it, level + 1, sectionList, sectionIndex);
                }
            }
        }
    }

}
