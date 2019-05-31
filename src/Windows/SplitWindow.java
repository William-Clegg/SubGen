package Windows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.imageio.spi.IIORegistry;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Windows.ProjectInfoWindow.job;
import static com.company.SubGenApp.window;

public class SplitWindow {


    private static String name = "first";
    private static ArrayList<PDDocument> specSet = new ArrayList<>();
    private static boolean titlePage = false;

    public static GridPane createGrid() {

        IIORegistry registry = IIORegistry.getDefaultInstance();
        registry.registerServiceProvider(new com.github.jaiimageio.jpeg2000.impl.J2KImageReaderSpi());

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Button choosePdf = new Button("Browse…");
        HBox choosePdfBox = new HBox(10);
        choosePdfBox.getChildren().add(choosePdf);
        grid.add(choosePdfBox, 0, 1);

        Label pdfLabel = new Label("Choose PDF to split");
        grid.add(pdfLabel, 0, 0);

        TextField pdfPath = new TextField();
        grid.add(pdfPath, 1, 1, 2, 1);

        choosePdf.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select the PDF you would like to split");
            File file = fileChooser.showOpenDialog(window);

            if (file != null && (file.getName().substring(file.getName().lastIndexOf('.')).equals(".pdf"))) {
                pdfPath.setText(file.getAbsolutePath());
            }

        });

        Button chooseOutput = new Button("Browse…");
        HBox chooseOutputBox = new HBox(10);
        chooseOutputBox.getChildren().add(chooseOutput);
        grid.add(chooseOutputBox, 0, 4);

        Label outputLabel = new Label("Choose output location");
        grid.add(outputLabel, 0, 3);

        TextField outputPath = new TextField();
        grid.add(outputPath, 1, 4, 2, 1);

        chooseOutput.setOnAction(e -> {

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select where to output the splits");
            File defaultDir = new File(System.getProperty("user.home") + "/Desktop");
            directoryChooser.setInitialDirectory(defaultDir);
            File file = directoryChooser.showDialog(window);

            if (file != null) {
                String path = file.getAbsolutePath() + "\\Splits\\";
                outputPath.setText(path);
                File newDir = new File(path);
                newDir.mkdirs();
            }

        });

        Button split = new Button("Split PDF");
        HBox splitBox = new HBox(10);
        splitBox.getChildren().add(split);
        grid.add(splitBox, 0, 6);

        split.setOnAction(e -> {
            try {

                PDFMergerUtility pdfMerger = new PDFMergerUtility();

                PDDocument document = PDDocument.load(new File(pdfPath.getText()));

                Splitter splitter = new Splitter();

                List<PDDocument> Pages = splitter.split(document);

                Iterator<PDDocument> iterator = Pages.listIterator();

                PDFTextStripper pdfStripper = new PDFTextStripper();

                String pattern = "([A-Z]+-[0-9])";

                Pattern jobItem = Pattern.compile(pattern);

                while (iterator.hasNext()) {

                    PDDocument pd = iterator.next();
                    Matcher m = jobItem.matcher(pdfStripper.getText(pd));
                    int textLength = pdfStripper.getText(pd).length();

                    if (isBlank(pd) && textLength > 3 && textLength < 100) {
                        if(!titlePage) {
                            titlePage = true;
                            name = pdfStripper.getText(pd).trim();
                        } else {

                            if(specSet.size() != 0) {
                                PDDocument completeDoc = specSet.get(0);
                                for (int i = 1; i < specSet.size(); i++) {
                                    pdfMerger.appendDocument(completeDoc, specSet.get(i));
                                }
                                if(name.contains("/")) {
                                    name = name.replace("/", "-");
                                }
                                if(name.contains("\n")) {
                                    //name = name.replace("\n", "");
                                    System.out.println(name);
                                    String name1 = name.substring(0, name.indexOf('\n')).trim();
                                    String name2 = name.substring(name.lastIndexOf('\n')+1).trim();
                                    name = name1 + " " + name2;
                                }
                                name = correctCase(name);
                                System.out.println("!" + name);
                                System.out.println(outputPath.getText() + name.trim() + ".pdf");
                                completeDoc.save(outputPath.getText() + name.trim() + ".pdf");
                            }
                            name = pdfStripper.getText(pd).trim();
                        }
                        if(specSet.size() != 0) {
                            specSet.clear();
                        }
                    } else {
                        specSet.add(pd);
                    }
                    //pd.save(outputPath.getText() + name);
                }
                document.close();

            } catch (IOException noFile) {
                System.err.println(noFile.getMessage());
            }
        });

        return grid;
    }


        private static Boolean isBlank (PDDocument doc) throws IOException {
            PDFRenderer renderer = new PDFRenderer(doc);
            BufferedImage bufferedImage = renderer.renderImage(0);
            long count = 0;
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            Double areaFactor = (width * height) * 0.96;

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Color c = new Color(bufferedImage.getRGB(x, y));
                    // verify light gray and white
                    if (c.getRed() == c.getGreen() && c.getRed() == c.getBlue()
                            && c.getRed() >= 248) {
                        count++;
                    }
                }
            }

            if (count >= areaFactor) {
                return true;
            }

            return false;
        }

        private static String correctCase(String string) {

            boolean firstDone = false;
            boolean newWord = false;

            if(string.contains(" ")) {

                char[] stringArr = string.toCharArray();
                string = "";
                StringBuilder stringBuilder = new StringBuilder(string);
                for(int i = 0; i < stringArr.length; i++) {

                    if(!firstDone) {
                        stringBuilder.append((stringArr[i]));
                        if (stringArr[i] == ' ') {
                            stringBuilder.append("- ");
                            firstDone = true;
                            newWord = true;
                        }
                    } else {
                        if(!newWord) {
                            stringBuilder.append(("" + stringArr[i]).toLowerCase());
                        } else {
                            stringBuilder.append((stringArr[i]));
                            newWord = false;
                        }
                        if(stringArr[i] == ' ') {
                            newWord = true;
                        }
                    }
                }
                string = stringBuilder.toString();
                return string;
            } else {
                return string;
            }
        }

}
