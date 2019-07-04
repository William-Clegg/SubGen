package Windows;

import com.company.AutoSave;
import com.company.Member;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.company.SubGenApp.*;


public class SettingsWindow {

    public static GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        Text scenetitle = new Text("Settings");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);


        Button chooseImage = new Button("Choose an Image");
        HBox chooseImageBox = new HBox(10);
        chooseImageBox.setAlignment(Pos.BOTTOM_LEFT);
        chooseImageBox.getChildren().add(chooseImage);
        grid.add(chooseImageBox, 0, 3);

        TextField imagePath = new TextField();
        grid.add(imagePath, 1, 3, 1, 1);

        Label companyTypeLabel = new Label("Company Type");
        grid.add(companyTypeLabel, 0, 4);

        final TextField companyTypeField = new TextField();
        grid.add(companyTypeField, 1, 4);

        Label companyNameLabel = new Label("Company Name");
        grid.add(companyNameLabel, 0, 5);

        final TextField companyNameField = new TextField();
        grid.add(companyNameField, 1, 5);

        Label companyAdd1Label = new Label("Address One");
        grid.add(companyAdd1Label, 0, 6);

        final TextField companyAdd1Field = new TextField();
        grid.add(companyAdd1Field, 1, 6);

        Label companyAdd2Label = new Label("Address Two");
        grid.add(companyAdd2Label, 0, 7);

        final TextField companyAdd2Field = new TextField();
        grid.add(companyAdd2Field, 1, 7);

        if(!profileList.isEmpty()) {
            imagePath.setText(profileList.get(0));
            companyTypeField.setText(profileList.get(1));
            companyNameField.setText(profileList.get(2));
            companyAdd1Field.setText(profileList.get(3));
            companyAdd2Field.setText(profileList.get(4));
        }





        Button saveSettings = new Button("Save Settings");
        HBox saveProfileBox = new HBox(10);
        saveProfileBox.setAlignment(Pos.BOTTOM_LEFT);
        saveProfileBox.getChildren().add(saveSettings);
        grid.add(saveProfileBox, 0, 12);

        saveSettings.setOnAction( e -> {

            List<String> profileInfoList = new ArrayList<String>();

            profileInfoList.add(imagePath.getText());
            profileInfoList.add(companyTypeField.getText());
            profileInfoList.add(companyNameField.getText());
            profileInfoList.add(companyAdd1Field.getText());
            profileInfoList.add(companyAdd2Field.getText());

            /*
            if(exists) {
                profileList.set(existingIndex, profileInfoList);
            } else {
                profileList.add(profileInfoList);
            }

             */
            AutoSave.saveProfile(profileInfoList);

            /*
            observableProfileNameList.clear();
            observableProfileNameList.add("[Add New Profile...]");
            for(List<String> list : profileList) {
                observableProfileNameList.add(list.get(2));
            }
            profileBox.setItems(FXCollections.observableArrayList(observableProfileNameList));

             */


            boolean memberExists = false;
            int existingMemberIndex = -1;
            for(Member member : memberList) {
                if(member.getName().equals(companyNameField.getText())) {
                    memberExists = true;
                    existingMemberIndex = memberList.indexOf(member);
                }
            }

            Member newMember = new Member(companyTypeField.getText(), companyNameField.getText(), companyAdd1Field.getText(), companyAdd2Field.getText());

            if(memberExists) {
                memberList.set(existingMemberIndex, newMember);
            } else {
                memberList.add(newMember);
            }
            AutoSave.saveMember(memberList);

        });


        chooseImage.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a Title Page Image");
            File file = fileChooser.showOpenDialog(window);

            if (file != null) {
                imagePath.setText(file.getAbsolutePath());
            }

        });



        return grid;
    }
}
