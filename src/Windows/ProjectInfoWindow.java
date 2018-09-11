package Windows;

import com.company.SubGenApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import static com.company.AutoSave.*;
import static com.company.SubGenApp.*;

public class ProjectInfoWindow {

    public static String job, jobAdd1, jobAdd2, architectName, architectAdd1, architectAdd2, architectPhone,
            genConName, genConAdd1, genConAdd2, genConPhone, imgPath, date, volume;
    public static TextField pnField, pAdd1, pAdd2, archNameField, aAdd1, aAdd2, archPhoneField, gcNameField, gAdd1, gAdd2, gcPhoneField;
    public static CheckBox dateCheck;
    public static CheckBox volumeCheck;

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
                "Wakefield Beasley & Associates")
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
                }
            }
        });


        /*-------------------------------------------------------
         *  Creating the ChoiceBox for General Contractors and its
         *  info.
         */

        Label gcBox = new Label("General Contractor List:");
        final ChoiceBox<String> genContractor = new ChoiceBox<>(FXCollections.observableArrayList(
                "Balfour Beatty", "Batson-Cook", "Brasfield & Gorrie", "DPR Construction", "Van Winkle Construction", "Hoar Construction", "Gay Construction")
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
                        gAdd1.setText("1990 Vaughn Road");
                        gAdd2.setText("Kennesaw, GA 30144");
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
                }
            }
        });

        Label volumeLabel = new Label();
        volumeLabel.setText("Include a volume label");
        grid.add(volumeLabel,5,11);

        volumeCheck = new CheckBox();
        grid.add(volumeCheck,6,11);

        TextField volumeText = new TextField();
        volumeText.setVisible(false);
        grid.add(volumeText, 7,11);

        volumeCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                volumeText.setVisible(newValue);
            }
        });

        Label dateCheckLabel = new Label();
        dateCheckLabel.setText("Include a date");
        grid.add(dateCheckLabel,5,10);

        dateCheck = new CheckBox();
        grid.add(dateCheck,6,10);

        DatePicker datePick = new DatePicker();
        grid.add(datePick, 7,10);
        datePick.setValue(getLocalDate());
        datePick.setVisible(false);

        dateCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                datePick.setVisible(newValue);
            }
        });
        Button nextButton = new Button("Next");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(nextButton);
        grid.add(hbBtn, 1, 12);

        Button chooseImage = new Button("Choose an Image");
        HBox chooseImageBox = new HBox(10);
        chooseImageBox.setAlignment(Pos.BOTTOM_LEFT);
        chooseImageBox.getChildren().add(chooseImage);
        grid.add(chooseImageBox, 5, 1);

        TextField imagePath = new TextField();
        imagePath.setText(loadDefaultimage());
        grid.add(imagePath, 5, 2);

        chooseImage.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a Title Page Image");
            File file = fileChooser.showOpenDialog(window);

            if(file != null) {
                imagePath.setText(file.getAbsolutePath());
                saveDefaultimage(file.getPath());
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
        grid.add(loadLastInfoHB, 0, 12);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 13);

        loadLastInfo.setOnAction(e -> {

            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select A Save Folder");
            File defaultDir = new File("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\Saves");
            directoryChooser.setInitialDirectory(defaultDir);
            File file = directoryChooser.showDialog(window);

            if(file != null) {
                String[] savedInfo = new String[14];
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
            imgPath = imagePath.getText();
            if(dateCheck.isSelected()){date = datePick.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));} else {date = "";}
            if(volumeCheck.isSelected()){volume = volumeText.getText();} else {volume = "";}

            String[] savedInfo = new String[14];
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
