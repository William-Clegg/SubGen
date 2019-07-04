package Windows;

import com.company.*;
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
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.company.SubGenApp.*;

public class CreateMembersWindow {

    public static GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        Text scenetitle = new Text("Create Members and Contacts");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);













        Label memberTypeLabel = new Label("Member Type:");
        grid.add(memberTypeLabel, 3, 3);

        final TextField memberTypeField = new TextField();
        grid.add(memberTypeField, 4, 3);

        Label memberNameLabel = new Label("Member Name:");
        grid.add(memberNameLabel, 3, 4);

        final TextField memberNameField = new TextField();
        grid.add(memberNameField, 4, 4);

        Label memberAdd1Label = new Label("Member Address Part One:");
        grid.add(memberAdd1Label, 3, 5);

        final TextField memberAdd1Field = new TextField();
        grid.add(memberAdd1Field, 4, 5);

        Label memberAdd2Label = new Label("Member Address Part Two:");
        grid.add(memberAdd2Label, 3, 6);

        final TextField memberAdd2Field = new TextField();
        grid.add(memberAdd2Field, 4, 6);








        Label contactNameLabel = new Label("Contact Name");
        grid.add(contactNameLabel, 6, 5);

        final TextField contactNameField = new TextField();
        grid.add(contactNameField, 7, 5);

        Label contactEmailLabel = new Label("Contact Email");
        grid.add(contactEmailLabel, 6, 6);

        final TextField contactEmailField = new TextField();
        grid.add(contactEmailField, 7, 6);

        Label contactPhoneLabel = new Label("Contact Phone");
        grid.add(contactPhoneLabel, 6, 7);

        final TextField contactPhoneField = new TextField();
        grid.add(contactPhoneField, 7, 7);












        Label memberBoxLabel = new Label("Create or Edit a Member:");
        List<Member> observableMemberNameList = new ArrayList<>();
        //observableMemberNameList.addAll(memberList);
        final ComboBox<Member> memberBox = new ComboBox<>(FXCollections.observableArrayList(memberList));
        new AutoCompleteMemberBoxListener<Member>(memberBox);
        memberBox.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(memberBox, 4, 2, 1,1);
        grid.add(memberBoxLabel, 3, 2);

        memberBox.setConverter(new StringConverter<Member>() {

            @Override
            public String toString(Member object) {
                if (object == null) return null;
                return object.toString();
            }

            @Override
            public Member fromString(String string) {

                for(Member member : memberList) {

                    if(member.getName().equals(string)) {

                        return member;
                    }
                }
                return null;
            }
        });













        List<Contact> observableContactList = new ArrayList<>();
        Label chooseContactBoxLabel = new Label("Choose Member Contact:");


        final ComboBox<Contact> contactBox = new ComboBox<>();
        new AutoCompleteContactBoxListener<Contact>(contactBox);
        contactBox.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(contactBox, 7, 4, 1, 1);
        grid.add(chooseContactBoxLabel, 6, 4);

        contactBox.setConverter(new StringConverter<Contact>() {

            @Override
            public String toString(Contact object) {
                if (object == null) return null;
                return object.toString();
            }

            @Override
            public Contact fromString(String string) {

                if(memberBox.getValue() != null) {
                    for (Contact contact : memberBox.getValue().getContacts()) {

                        if (contact.getName().equals(string)) {

                            return contact;
                        }
                    }
                }
                return null;
            }
        });


        contactBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() != -1) {
                    if(contactBox.getValue() != null) {
                        contactNameField.setText(contactBox.getValue().getName());
                        contactEmailField.setText(contactBox.getValue().getEmail());
                        contactPhoneField.setText(contactBox.getValue().getPhone());
                    }
                } else {
                    contactNameField.clear();
                    contactEmailField.clear();
                    contactPhoneField.clear();
                    contactBox.setValue(null);
                    if(memberBox.getValue() != null) {
                        contactBox.setItems(FXCollections.observableArrayList(memberBox.getValue().getContacts()));
                    } else {
                        contactBox.setItems(null);
                    }
                }

            }
        });






        memberBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                System.out.println(newValue);
                if(newValue.intValue() != -1) {
                    if(memberBox.getValue() != null) {
                        memberTypeField.setText(memberBox.getValue().getType());
                        memberNameField.setText(memberBox.getValue().getName());
                        memberAdd1Field.setText(memberBox.getValue().getAddOne());
                        memberAdd2Field.setText(memberBox.getValue().getAddTwo());


                        contactBox.setValue(null);
                        contactNameField.clear();
                        contactEmailField.clear();
                        contactPhoneField.clear();

                        observableContactList.clear();
                        observableContactList.addAll(memberBox.getValue().getContacts());
                        contactBox.setItems(FXCollections.observableArrayList(memberBox.getValue().getContacts()));
                    }
                } else {
                    contactBox.setItems(null);
                    memberTypeField.clear();
                    memberNameField.clear();
                    memberAdd1Field.clear();
                    memberAdd2Field.clear();
                    System.out.println(memberBox.getEditor().getText());
                }
            }
        });








        Button saveMember = new Button("Save This Member");
        HBox saveMemberBox = new HBox(10);
        saveMemberBox.setAlignment(Pos.BOTTOM_LEFT);
        saveMemberBox.getChildren().add(saveMember);
        grid.add(saveMemberBox, 3, 12);

        saveMember.setOnAction( e -> {

            boolean exists = false;
            int existingIndex = -1;

            Member newMember = new Member(memberTypeField.getText(), memberNameField.getText(), memberAdd1Field.getText(), memberAdd2Field.getText());
            memberBox.setValue(newMember);

            if(memberList.contains(memberBox.getValue())) {
                exists = true;
                existingIndex = memberList.indexOf(memberBox.getValue());
            }

            if(exists) {
                memberList.set(existingIndex, newMember);
            } else {
                memberList.add(newMember);
            }
            AutoSave.saveMember(memberList);

            observableMemberNameList.clear();
            //observableMemberNameList.add("[Add New Member...]");
            for(Member member : memberList) {
                observableMemberNameList.add(member);
            }
            memberBox.setItems(FXCollections.observableArrayList(memberList));
        });

        Button deleteMember = new Button("Delete This Member");
        HBox deleteMemberBox = new HBox(10);
        deleteMemberBox.setAlignment(Pos.BOTTOM_LEFT);
        deleteMemberBox.getChildren().add(deleteMember);
        grid.add(deleteMemberBox, 4, 12);

        deleteMember.setOnAction( e -> {


        });





        Button saveContact = new Button("Save This Contact");
        HBox saveContactBox = new HBox(10);
        saveContactBox.setAlignment(Pos.BOTTOM_LEFT);
        saveContactBox.getChildren().add(saveContact);
        grid.add(saveContactBox, 6, 12);

        saveContact.setOnAction( e -> {

            boolean exists = false;
            int existingIndex = -1;
            Member existingMember = memberBox.getValue();

            for(Contact contact : existingMember.getContacts()) {

                if(contact.getName().equals(contactNameField.getText())) {

                    exists = true;
                    existingIndex = memberBox.getValue().getContacts().indexOf(contact);

                }
            }


            Contact newContact = new Contact(contactNameField.getText(), contactEmailField.getText(), contactPhoneField.getText());

            if(exists) {
                memberList.get(memberList.indexOf(existingMember)).getContacts().set(existingIndex, newContact);
            } else {
                memberList.get(memberList.indexOf(existingMember)).getContacts().add(newContact);
            }
            AutoSave.saveMember(memberList);

            observableContactList.clear();
            for(Contact contact : memberBox.getValue().getContacts()) {

                observableContactList.add(contact);
            }
            contactBox.setItems(FXCollections.observableArrayList(observableContactList));
            contactBox.setValue(newContact);

            contactNameField.setText(newContact.getName());
            contactEmailField.setText(newContact.getEmail());
            contactPhoneField.setText(newContact.getPhone());
        });

        Button deleteContact = new Button("Delete This Contact");
        HBox deleteContactBox = new HBox(10);
        deleteContactBox.setAlignment(Pos.BOTTOM_LEFT);
        deleteContactBox.getChildren().add(deleteContact);
        grid.add(deleteContactBox, 7, 12);

        deleteContact.setOnAction( e -> {


        });





        return grid;
    }
}
