package OCR;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

import static com.company.SubGenApp.southernList;
import static com.company.SubGenApp.southernListIndex;

public class OcrProcessing {

    public static void readSouthernDoc(int i) {

        /*
        System.out.println("GETTING IN ONE");
        try {
            System.out.println("GETTING IN TWO");

            String southernSourceDir = subSheets.get(i + 1).substring(4);
            File sourceFile = new File(southernSourceDir);
            PDDocument document = PDDocument.load(sourceFile);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            @SuppressWarnings("unchecked")
            String fileName = sourceFile.getName().replace(".pdf", "");

            //OCRScanner ocrScanner = OcrTrain.trainFont(true);

            for (int j = 0; j < document.getPages().getCount(); j++) {
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setStartPage(j);
                stripper.setEndPage(j);
                String text = stripper.getText(document);

                System.out.println("GETTING IN THREE");




                if(j >360) {
                    System.out.println(text + "!!!!!!!!!!!!");
                }
                if(text.length() < 40 && text.length() > 3) {
                    southernListIndex.add(j-1);
                    southernList.add(text);
                    System.out.println("[" + text + "]!!!");
                }

            }

            /*for (PDPage page : document.getPages()) {
                System.out.println("going through pdf pages");

                if (pageNumber > 1) {
                    BufferedImage image = pdfRenderer.renderImageWithDPI(pageNumber, 190, ImageType.RGB);
                    String imageFilename = "C:\\Users\\Rudy\\IdeaProjects\\SubGen\\ocrTempImages\\imgFile" + i + pageNumber + ".png";

                    ImageIOUtil.writeImage(image, imageFilename, 190);

                    try {
                        image = ImageIO.read(new File(imageFilename));
                    } catch (IOException ocrE) {
                        //ocrE.printStackTrace();
                    }

                    PixelImage pixelImage = new PixelImage(image);

                    pixelImage.toGrayScale(true);

                    pixelImage.filter();

                    String text = ocrScanner.scan(image, 0, 0, 0, 0, null);
                    if (!text.contains("\n") && text.length() > 2 && text.length() < 30) {
                        System.out.println(imageFilename + ":");
                        System.out.println("[" + text + "]!!!");

                        reader.setStartPage(pageNumber);
                        reader.setEndPage(pageNumber);
                        southernListIndex.add(pageNumber);
                        readText = reader.getText(document);
                        southernList.add(readText);
                        System.out.println(southernListIndex);

                    }
                }

                pageNumber++;
            }
            document.close();

        } catch (Exception ocr) {
            ocr.printStackTrace();
        }
        */

    }

}
