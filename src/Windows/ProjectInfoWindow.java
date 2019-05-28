package Windows;

import com.company.SubGenApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import static com.company.AutoSave.*;
import static com.company.SubGenApp.*;

public class ProjectInfoWindow {

    public static String job, jobAdd1, jobAdd2, architectName, architectAdd1, architectAdd2, architectPhone,
            genConName, genConAdd1, genConAdd2, genConPhone, engineerName, engineerAdd1, engineerAdd2, engineerPhone,
            imgPath, date, volume, pageNumbersText, rgbText, removeMembersText, removeMainContentsText, operationAndMainText;
    public static boolean pageNumbers, removeMembers, removeMainContents, operationAndMain;
    public static TextField pnField, pAdd1, pAdd2, archNameField, aAdd1, aAdd2, archPhoneField, gcNameField, gAdd1, gAdd2, gcPhoneField;
    private static CheckBox dateCheck, volumeCheck, pageNumbersCheck, memberCheck, mainContentsCheck, operationAndMainCheck;
    public static Integer red, green, blue;

    public static GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

            Text scenetitle = new Text("Project Information");
            scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            grid.add(scenetitle, 0, 0, 2, 1);

            Label projName = new Label("Project Name:");
            grid.add(projName, 0, 1);

            final TextField pnField = new TextField();
            grid.add(pnField, 1, 1);

            Label projAdd1 = new Label("Project Address Part One:");
            grid.add(projAdd1, 0, 2);

            final TextField pAdd1 = new TextField();
            grid.add(pAdd1, 1, 2);

            Label projAdd2 = new Label("Project Address Part Two:");
            grid.add(projAdd2, 0, 3);

            final TextField pAdd2 = new TextField();
            grid.add(pAdd2, 1, 3);

            Label archName = new Label("Architect Name:");
            grid.add(archName, 0, 4);

            final TextField archNameField = new TextField();
            grid.add(archNameField, 1, 4);

            Label archAdd1 = new Label("Architect Address Part One:");
            grid.add(archAdd1, 0, 5);

            final TextField aAdd1 = new TextField();
            grid.add(aAdd1, 1, 5);

            Label archAdd2 = new Label("Architect Address Part Two:");
            grid.add(archAdd2, 0, 6);

            final TextField aAdd2 = new TextField();
            grid.add(aAdd2, 1, 6);

            Label archPhone = new Label("Architect Phone Number:");
            grid.add(archPhone, 0, 7);

            final TextField archPhoneField = new TextField();
            grid.add(archPhoneField, 1, 7);

            Label gcName = new Label("General Contractor Name:");
            grid.add(gcName, 0, 8);

            final TextField gcNameField = new TextField();
            grid.add(gcNameField, 1, 8);

            Label gcAdd1 = new Label("General Contractor Address Part One:");
            grid.add(gcAdd1, 0, 9);

            final TextField gAdd1 = new TextField();
            grid.add(gAdd1, 1, 9);

            Label gcAdd2 = new Label("General Contractor Address Part Two:");
            grid.add(gcAdd2, 0, 10);

            final TextField gAdd2 = new TextField();
            grid.add(gAdd2, 1, 10);

            Label gcPhone = new Label("General Contractor Phone Number:");
            grid.add(gcPhone, 0, 11);

            final TextField gcPhoneField = new TextField();
            grid.add(gcPhoneField, 1, 11);

            Label engName = new Label("Engineer Name:");
            grid.add(engName, 0, 12);

            final TextField engNameField = new TextField();
            grid.add(engNameField, 1, 12);

            Label engAdd1 = new Label("Engineer Address Part One:");
            grid.add(engAdd1, 0, 13);

            final TextField eAdd1 = new TextField();
            grid.add(eAdd1, 1, 13);

            Label engAdd2 = new Label("Engineer Address Part Two:");
            grid.add(engAdd2, 0, 14);

            final TextField eAdd2 = new TextField();
            grid.add(eAdd2, 1, 14);

            Label engPhone = new Label("Engineer Phone Number:");
            grid.add(engPhone, 0, 15);

            final TextField engPhoneField = new TextField();
            grid.add(engPhoneField, 1, 15);



        /*-------------------------------------------------------
         *  Creating the ChoiceBox for Architects and its
         *  info.
         */

        Label archBox = new Label("Architect List:");
        final ChoiceBox<String> architect = new ChoiceBox<>(FXCollections.observableArrayList(
                "ASD|SKY", "Freespace Architects",
                "Lindsay Pope Brayfield Clifford & Assoc.",
                "Peacock Architects", "The Preston Partnership",
                "Design Group Facility Solutions",
                "Earl Swensson Associates, Inc.",
                "Collins Cooper Carusi Architects",
                "Wakefield Beasley & Associates",
                "Smallwood, Reynolds, Stewart, Stewart & Associates",
                "INOX Design, Inc",
                "Warner Summers")
        );
        grid.add(architect, 5, 4, 1, 1);
        grid.add(archBox, 5, 3);

        architect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                switch (newValue.intValue()) {

                    case 0:
                        archNameField.setText("ASD|SKY");
                        aAdd1.setText("55 Ivan Allen Jr Boulevard NW Suite 100");
                        aAdd2.setText("Atlanta, GA 30308");
                        archPhoneField.setText("404-688-2255");
                        break;

                    case 1:
                        archNameField.setText("Freespace Architecture");
                        aAdd1.setText("887 West Marietta Street NW, Suite T-103");
                        aAdd2.setText("Atlanta, Georgia 30318");
                        archPhoneField.setText("404-591-5670");
                        break;

                    case 2:
                        archNameField.setText("Lindsay Pope Brayfield Clifford & Associates");
                        aAdd1.setText("344 West Pike Street");
                        aAdd2.setText("Lawrenceville, GA 30046");
                        archPhoneField.setText("770-963-8989");
                        break;

                    case 3:
                        archNameField.setText("Peacock Partnership");
                        aAdd1.setText("5525 Interstate North Parkway");
                        aAdd2.setText("Atlanta, GA 30328");
                        archPhoneField.setText("404-214-5200");
                        break;

                    case 4:
                        archNameField.setText("The Preston Partnership");
                        aAdd1.setText("115 Perimeter Center Place, Suite 950");
                        aAdd2.setText("Atlanta, GA 30346");
                        archPhoneField.setText("770-396-7248");
                        break;

                    case 5:
                        archNameField.setText("Design Group Facility Solutions");
                        aAdd1.setText("5 Chenell Drive, Box 3");
                        aAdd2.setText("Concord, New Hampshire 03301");
                        archPhoneField.setText("603-225-0761");
                        break;

                    case 6:
                        archNameField.setText("Earl Swensson Associates, Inc.");
                        aAdd1.setText("1033 Demonbreun Street, Suite 800");
                        aAdd2.setText("Nashville, TN 37203");
                        archPhoneField.setText("615-329-9445");
                        break;

                    case 7:
                        archNameField.setText("Collins Cooper Carusi Architects");
                        aAdd1.setText("3391 Peachtree Road NE, Suite 400");
                        aAdd2.setText("Atlanta, GA 30326");
                        archPhoneField.setText("404-873-0001");
                        break;

                    case 8:
                        archNameField.setText("Wakefield Beasley & Associates");
                        aAdd1.setText("5200 Avalon Boulevard");
                        aAdd2.setText("Alpharetta, GA 30009");
                        archPhoneField.setText("770-209-9393");
                        break;

                    case 9:
                        archNameField.setText("Smallwood, Reynolds, Stewart, Stewart & Associates");
                        aAdd1.setText("3565 Piedmont Road, Ste 303");
                        aAdd2.setText("Atlanta, GA 30305");
                        archPhoneField.setText("404-233-5453");
                        break;

                    case 10:
                        archNameField.setText("INOX Design, Inc");
                        aAdd1.setText("1640 Powers Ferry Rd, Bldg 24, Ste 200");
                        aAdd2.setText("Marietta, GA 30067");
                        archPhoneField.setText("");
                        break;

                    case 11:
                        archNameField.setText("Warner Summers");
                        aAdd1.setText("1550 Southland Circle, Suite 100");
                        aAdd2.setText("Atlanta, GA 30318");
                        archPhoneField.setText("(404)651-6075");
                        break;
                }
            }
        });


        /*-------------------------------------------------------
         *  Creating the ChoiceBox for General Contractors and its
         *  info.
         */

        Label gcBox = new Label("General Contractor List:");
        final ChoiceBox<String> genContractor = new ChoiceBox<>(FXCollections.observableArrayList(
                "Balfour Beatty", "Batson-Cook", "Brasfield & Gorrie", "DPR Construction", "Van Winkle Construction", "Hoar Construction", "Gay Construction", "Anverse, Inc")
        );
        grid.add(genContractor, 5, 8, 1,1);
        grid.add(gcBox, 5, 7);

        genContractor.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                switch (newValue.intValue()) {

                    case 0:
                        gcNameField.setText("Balfour Beatty");
                        gAdd1.setText("600 Galleria Parkway, Suite 1500");
                        gAdd2.setText("Atlanta, GA 30339");
                        gcPhoneField.setText("678-921-6800");
                        break;

                    case 1:
                        gcNameField.setText("Batson-Cook");
                        gAdd1.setText("200 Galleria Parkway, Suite 1300");
                        gAdd2.setText("Atlanta, GA 30339");
                        gcPhoneField.setText("770-955-1951");
                        break;

                    case 2:
                        gcNameField.setText("Brasfield & Gorrie");
                        gAdd1.setText("2999 Circle 75 Parkway");
                        gAdd2.setText("Atlanta, GA 30339");
                        gcPhoneField.setText("678-581-6400");
                        break;

                    case 3:
                        gcNameField.setText("DPR Construction");
                        gAdd1.setText("3301 Windy Ridge Parkway SE Suite 500");
                        gAdd2.setText("Atlanta, GA 30339");
                        gcPhoneField.setText("404-264-0404");
                        break;

                    case 4:
                        gcNameField.setText("Van Winkle Construction");
                        gAdd1.setText("1731 Commerce Drive NW Suite 110");
                        gAdd2.setText("Atlanta, GA 30318");
                        gcPhoneField.setText("404-351-9500");
                        break;

                    case 5:
                        gcNameField.setText("Hoar Construction");
                        gAdd1.setText("215 Centerview Drive, Suite 300");
                        gAdd2.setText("Brentwood, Tennessee 37027");
                        gcPhoneField.setText("615-376-0749");
                        break;

                    case 6:
                        gcNameField.setText("Gay Construction");
                        gAdd1.setText("2907 Log Cabin Drive SE");
                        gAdd2.setText("Atlanta, GA 30339");
                        gcPhoneField.setText("404-873-4941");
                        break;

                    case 7:
                        gcNameField.setText("Anverse, Inc");
                        gAdd1.setText("P.O. Box 3188");
                        gAdd2.setText("Cartersville, GA 30120");
                        gcPhoneField.setText("");
                        break;
                }
            }
        });

        /*-------------------------------------------------------
         *  Creating the ChoiceBox for Engineers and its
         *  info.
         */

        Label engBox = new Label("Engineer List:");
        final ChoiceBox<String> engineer = new ChoiceBox<>(FXCollections.observableArrayList(
                "Stephenson Engineering, Inc",
                        "Jordan & Skala Engineers")
        );
        grid.add(engineer, 5, 12, 1, 1);
        grid.add(engBox, 5, 11);

        engineer.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                switch (newValue.intValue()) {

                    case 0:
                        engNameField.setText("Stephenson Engineering, Inc");
                        eAdd1.setText("P.O. Box 201088");
                        eAdd2.setText("Cartersville, GA 30120");
                        engPhoneField.setText("");
                        break;

                    case 1:
                        engNameField.setText("Jordan & Skala Engineers");
                        eAdd1.setText("4275 Shackleford Road");
                        eAdd2.setText("Norcross, GA 30093");
                        engPhoneField.setText("(770)447-5547");
                        break;
                }
            }
        });

        Label colorLabel = new Label();
        colorLabel.setText("Choose the highlighter color");
        grid.add(colorLabel,5,1);

        ColorPicker colorPicker = new ColorPicker(Color.YELLOW);
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                red = (int)(colorPicker.getValue().getRed()*255);
                green = (int)(colorPicker.getValue().getGreen()*255);
                blue = (int)(colorPicker.getValue().getBlue()*255);
            }
        });
        grid.add(colorPicker, 5, 2);

        Label volumeLabel = new Label();
        volumeLabel.setText("Include a volume label");
        grid.add(volumeLabel,5,14);

        volumeCheck = new CheckBox();
        grid.add(volumeCheck,6,14);

        TextField volumeText = new TextField();
        volumeText.setVisible(false);
        grid.add(volumeText, 7,14);

        volumeCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                volumeText.setVisible(newValue);
            }
        });

        Label pageNumbersLabel = new Label();
        pageNumbersLabel.setText("Include page numbers");
        grid.add(pageNumbersLabel,7,11);

        pageNumbersCheck = new CheckBox();
        grid.add(pageNumbersCheck,8,11);

        pageNumbersCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                pageNumbers = true;
            }
        });

        Label dateCheckLabel = new Label();
        dateCheckLabel.setText("Include a date");
        grid.add(dateCheckLabel,5,15);

        dateCheck = new CheckBox();
        grid.add(dateCheck,6,15);

        DatePicker datePick = new DatePicker();
        grid.add(datePick, 7,15);
        datePick.setValue(getLocalDate());
        datePick.setVisible(false);

        dateCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                datePick.setVisible(newValue);
                datePick.setValue(getLocalDate());
            }
        });

        Label includeMembers = new Label("Do not include members page");
        grid.add(includeMembers, 7, 8);
        memberCheck = new CheckBox();
        grid.add(memberCheck, 8, 8);

        Label includeMainContents = new Label("Do not include main contents");
        grid.add(includeMainContents, 7, 9);
        mainContentsCheck = new CheckBox();
        grid.add(mainContentsCheck, 8, 9);

        Label operationAndMainLabel = new Label("Title as Operation and Maintenance Manual");
        grid.add(operationAndMainLabel, 7, 10);
        operationAndMainCheck = new CheckBox();
        grid.add(operationAndMainCheck, 8, 10);

        Button nextButton = new Button("Next");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(nextButton);
        grid.add(hbBtn, 1, 16);

        Button chooseImage = new Button("Choose an Image");
        HBox chooseImageBox = new HBox(10);
        chooseImageBox.setAlignment(Pos.BOTTOM_LEFT);
        chooseImageBox.getChildren().add(chooseImage);
        grid.add(chooseImageBox, 7, 1);

        TextField imagePath = new TextField();
        imagePath.setText(loadDefaultimage());
        grid.add(imagePath, 7, 2, 2, 1);

        chooseImage.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a Title Page Image");
            File file = fileChooser.showOpenDialog(window);

            if(file != null) {
                imagePath.setText(file.getAbsolutePath());
                saveDefaultimage(file.getAbsolutePath());
            }

        });

        /*-------------------------------------------------------
         *  Add Button to load project info that was entered on
         *  the previous session.
         */

        Button loadLastInfo = new Button("Load Project");
        HBox loadLastInfoHB = new HBox(10);
        loadLastInfoHB.setAlignment(Pos.BOTTOM_LEFT);
        loadLastInfoHB.getChildren().add(loadLastInfo);
        grid.add(loadLastInfoHB, 0, 16);

        //final Text actiontarget = new Text();
        //grid.add(actiontarget, 1, 13);

        loadLastInfo.setOnAction(e -> {

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select A Save Folder");
            File defaultDir = new File("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\Saves");
            directoryChooser.setInitialDirectory(defaultDir);
            File file = directoryChooser.showDialog(window);

            if(file != null) {
                String[] savedInfo = new String[23];
                try {
                    File infoSave = new File(file + "\\ProjectInfo.ser");
                    savedInfo = loadProjectInfo(infoSave);
                    ;
                } catch (NullPointerException saveNotFound) {
                    System.err.println("File not found");
                }

                pnField.setText(savedInfo[0]);
                pAdd1.setText(savedInfo[1]);
                pAdd2.setText(savedInfo[2]);
                archNameField.setText(savedInfo[3]);
                aAdd1.setText(savedInfo[4]);
                aAdd2.setText(savedInfo[5]);
                archPhoneField.setText(savedInfo[6]);
                gcNameField.setText(savedInfo[7]);
                gAdd1.setText(savedInfo[8]);
                gAdd2.setText(savedInfo[9]);
                gcPhoneField.setText(savedInfo[10]);
                engNameField.setText(savedInfo[19]);
                eAdd1.setText(savedInfo[20]);
                eAdd2.setText(savedInfo[21]);
                engPhoneField.setText(savedInfo[22]);
                imagePath.setText(savedInfo[11]);
                if(savedInfo[12] != null) {
                    if (!savedInfo[12].equals("")) {
                        dateCheck.setSelected(true);
                        date = savedInfo[12];
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        LocalDate localDateLoad = LocalDate.parse(date, formatter);
                        datePick.setValue(localDateLoad);
                    }
                }
                if(savedInfo[13] != null) {
                    if (!savedInfo[13].equals("")) {
                        volume = savedInfo[13];
                        volumeCheck.setSelected(true);
                        volumeText.setText(volume);
                    }
                }
                if(savedInfo[14] != null) {
                    if(savedInfo[14].equals("true")) {
                        pageNumbers = true;
                        pageNumbersCheck.setSelected(true);
                    } else {
                        pageNumbers = false;
                        pageNumbersCheck.setSelected(false);
                    }
                }
                if(savedInfo[15] != null) {
                    rgbText = savedInfo[15];
                    red = Integer.parseInt(rgbText.substring(0,rgbText.indexOf(" ")));
                    green = Integer.parseInt(rgbText.substring(rgbText.indexOf(" ")+1,rgbText.lastIndexOf(" ")));
                    blue = Integer.parseInt(rgbText.substring(rgbText.lastIndexOf(" ")+1));
                    colorPicker.setValue(Color.rgb(red, green, blue));
                }
                if(savedInfo[16] != null) {
                    removeMembersText = savedInfo[16];
                    if(removeMembersText.equals("true")) {
                        removeMembers = true;
                        memberCheck.setSelected(true);
                    } else {
                        removeMembers = false;
                        memberCheck.setSelected(false);
                    }
                }
                if(savedInfo[17] != null) {
                    removeMainContentsText = savedInfo[17];
                    if(removeMainContentsText.equals("true")) {
                        removeMainContents = true;
                        mainContentsCheck.setSelected(true);
                    } else {
                        removeMainContents = false;
                        mainContentsCheck.setSelected(false);
                    }
                }
                if(savedInfo[18] != null) {
                    operationAndMainText = savedInfo[18];
                    if(operationAndMainText.equals("true")) {
                        operationAndMain = true;
                        operationAndMainCheck.setSelected(true);
                    } else {
                        operationAndMain = false;
                        operationAndMainCheck.setSelected(false);
                    }
                }


                job = pnField.getText();
                jobAdd1 = pAdd1.getText();
                jobAdd2 = pAdd2.getText();
                architectName = archNameField.getText();
                architectAdd1 = aAdd1.getText();
                architectAdd2 = aAdd2.getText();
                architectPhone = archPhoneField.getText();
                genConName = gcNameField.getText();
                genConAdd1 = gAdd1.getText();
                genConAdd2 = gAdd2.getText();
                genConPhone = gcPhoneField.getText();
                engineerName = engNameField.getText();
                engineerAdd1 = eAdd1.getText();
                engineerAdd2 = eAdd2.getText();
                engineerPhone = engPhoneField.getText();
                imgPath = imagePath.getText();
                date = datePick.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                if(volumeCheck.isSelected()){volume = volumeText.getText();}

                File listSave = new File(file + "\\ProjectOutline.ser");
                if(listSave.exists()) {
                    loadLastList(listSave);
                }

            }
        });

        nextButton.setOnAction(e -> {

            job = pnField.getText();
            jobAdd1 = pAdd1.getText();
            jobAdd2 = pAdd2.getText();
            architectName = archNameField.getText();
            architectAdd1 = aAdd1.getText();
            architectAdd2 = aAdd2.getText();
            architectPhone = archPhoneField.getText();
            genConName = gcNameField.getText();
            genConAdd1 = gAdd1.getText();
            genConAdd2 = gAdd2.getText();
            genConPhone = gcPhoneField.getText();
            engineerName = engNameField.getText();
            engineerAdd1 = eAdd1.getText();
            engineerAdd2 = eAdd2.getText();
            engineerPhone = engPhoneField.getText();
            imgPath = imagePath.getText();
            if(dateCheck.isSelected()){date = datePick.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));} else {date = "";}
            if(volumeCheck.isSelected()){volume = volumeText.getText();} else {volume = "";}
            if(pageNumbersCheck.isSelected()){pageNumbersText = "true";} else {pageNumbersText = "false";}
            if(red != null) {rgbText = red + " " + green + " " + blue;}
            if(memberCheck.isSelected()){removeMembersText = "true"; removeMembers = true;} else {removeMembersText = "false"; removeMembers = false;}
            if(mainContentsCheck.isSelected()){removeMainContentsText = "true"; removeMainContents = true;} else {removeMainContentsText = "false"; removeMainContents = false;}
            if (operationAndMainCheck.isSelected()) { operationAndMainText = "true"; operationAndMain = true;} else {operationAndMainText = "false"; operationAndMain = false;}

            String[] savedInfo = new String[23];
            savedInfo[0] = pnField.getText();
            savedInfo[1] = pAdd1.getText();
            savedInfo[2] = pAdd2.getText();
            savedInfo[3] = archNameField.getText();
            savedInfo[4] = aAdd1.getText();
            savedInfo[5] = aAdd2.getText();
            savedInfo[6] = archPhoneField.getText();
            savedInfo[7] = gcNameField.getText();
            savedInfo[8] = gAdd1.getText();
            savedInfo[9] = gAdd2.getText();
            savedInfo[10] = gcPhoneField.getText();
            savedInfo[11] = imagePath.getText();
            savedInfo[12] = date;
            savedInfo[13] = volume;
            savedInfo[14] = pageNumbersText;
            savedInfo[15] = rgbText;
            savedInfo[16] = removeMembersText;
            savedInfo[17] = removeMainContentsText;
            savedInfo[18] = operationAndMainText;
            savedInfo[19] = engineerName;
            savedInfo[20] = engineerAdd1;
            savedInfo[21] = engineerAdd2;
            savedInfo[22] = engineerPhone;

            saveProjectInfo(savedInfo);

            SubGenApp.createScene1();
        });

        return grid;

    }

    public static final LocalDate getLocalDate(){
        String date = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.parse(date , formatter);
        return localDate;
    }

}
