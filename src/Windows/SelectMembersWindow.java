package Windows;

import com.company.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static Windows.ProjectInfoWindow.*;
import static com.company.SubGenApp.*;

public class SelectMembersWindow {

    public static List<Member> chosenMembers = new ArrayList<>();

    public static GridPane memberGrid() {

        /*
        for(int i = 0; i < 10; i++) {
            chosenMembers.add(null);
        }

         */

        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        GridPane grid = new GridPane();
        SubGenApp.memberScene = new Scene(grid, 1200, 900);
        grid.setAlignment(Pos.CENTER);

        ColumnConstraints listCol = new ColumnConstraints();
        //listCol.setHgrow(Priority.ALWAYS);
        //grid.getColumnConstraints().add(listCol);

        RowConstraints listRow = new RowConstraints();
        //listRow.setVgrow(Priority.ALWAYS);
        // grid.getRowConstraints().add(listRow);

        grid.setHgap(25);
        grid.setVgap(10);
        grid.setGridLinesVisible(false);
        grid.setPadding(new Insets(25, 25, 50, 50));

        Text scenetitle = new Text("Select Project Members");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Member projectAsMember = new Member("Project", job, jobAdd1, jobAdd2);
        memberList.add(projectAsMember);

        List<Member> observableMemberList = FXCollections.observableArrayList(memberList);

        ComboBox<Member> chooseMemberBoxOne = new ComboBox<>(FXCollections.observableArrayList(observableMemberList));
        new AutoCompleteMemberBoxListener<Member>(chooseMemberBoxOne);
        chooseMemberBoxOne.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseMemberBoxOne, 1, 1, 1, 1);

        Member profileMember = null;
        System.out.println(memberList.size());

        for (Member member : memberList) {
            if (member.getName().equals(companyName)) {
                profileMember = member;
            }
        }

        chooseMemberBoxOne.setValue(profileMember);

        Text previewTypeLabelOne = new Text(profileMember.getType());
        grid.add(previewTypeLabelOne, 1, 2);

        Text previewNameLabelOne = new Text(profileMember.getName());
        grid.add(previewNameLabelOne, 1, 3);

        Text previewAdd1LabelOne = new Text(profileMember.getAddOne());
        grid.add(previewAdd1LabelOne, 1, 4);

        Text previewAdd2LabelOne = new Text(profileMember.getAddTwo());
        grid.add(previewAdd2LabelOne, 1, 5);

        List<String> observableContactListOne = new ArrayList<>();

        for (Member member : memberList) {
            if (member.getName().equals(chooseMemberBoxOne.getValue())) {
                for (Contact contact : member.getContacts()) {
                    observableContactListOne.add(contact.getName());
                }

            }

            ComboBox<String> chooseContactBoxOne = new ComboBox<>(FXCollections.observableArrayList(observableContactListOne));
            new AutoCompleteComboBoxListener<String>(chooseContactBoxOne);
            chooseContactBoxOne.setMaxWidth(Double.POSITIVE_INFINITY);
            grid.add(chooseContactBoxOne, 1, 6, 1, 1);

            Text previewContactNameOne = new Text();
            grid.add(previewContactNameOne, 1, 7);

            Text previewEmailOne = new Text();
            grid.add(previewEmailOne, 1, 8);

            Text previewPhoneOne = new Text();
            grid.add(previewPhoneOne, 1, 9);


            chooseContactBoxOne.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                    if (newValue.intValue() == -1) {
                        previewContactNameOne.setText("");
                        previewEmailOne.setText("");
                        previewPhoneOne.setText("");
                    } else {
                        /*

                        int indexOfContact = -1;
                        for (Member member : memberList) {

                            if (member.get(1).equals(chooseContactBoxOne.getValue())) {
                                indexOfContact = contactList.indexOf(list);

                            }
                        }
                        chosenContacts.set(0, contactList.get(indexOfContact));
                        previewContactNameOne.setText(contactList.get(indexOfContact).get(1));
                        previewEmailOne.setText(contactList.get(indexOfContact).get(2));
                        previewPhoneOne.setText(contactList.get(indexOfContact).get(3));

                         */
                    }
                }
            });


            chooseMemberBoxOne.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                    if (newValue.intValue() == -1) {
                        chosenMembers.set(0, null);
                        previewTypeLabelOne.setText("");
                        previewNameLabelOne.setText("");
                        previewAdd1LabelOne.setText("");
                        previewAdd2LabelOne.setText("");
                        previewContactNameOne.setText("");
                        previewEmailOne.setText("");
                        previewPhoneOne.setText("");
                    } else {
                        /*
                        chosenMembers.set(0, memberList.get(newValue.intValue()));
                        previewTypeLabelOne.setText(memberList.get(newValue.intValue()).get(0));
                        previewNameLabelOne.setText(memberList.get(newValue.intValue()).get(1));
                        previewAdd1LabelOne.setText(memberList.get(newValue.intValue()).get(2));
                        previewAdd2LabelOne.setText(memberList.get(newValue.intValue()).get(3));



                        observableContactListOne.clear();
                        for (List<String> list : contactList) {
                            System.out.println(list.get(0) + " equals " + chooseMemberBoxOne.getValue());
                            if (list.get(0).equals(chooseMemberBoxOne.getValue())) {
                                observableContactListOne.add(list.get(1));
                                System.out.println(list.get(1));
                            }
                        }
                         */

                        chooseContactBoxOne.setValue("");
                        previewContactNameOne.setText("");
                        previewEmailOne.setText("");
                        previewPhoneOne.setText("");
                        chooseContactBoxOne.setItems(FXCollections.observableArrayList(observableContactListOne));
                    }
                }
            });














/*

        ComboBox<String> chooseMemberBoxTwo = new ComboBox<>(FXCollections.observableArrayList(observableMemberList));
        new AutoCompleteComboBoxListener<String>(chooseMemberBoxTwo);
        chooseMemberBoxTwo.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseMemberBoxTwo, 3, 1, 1, 1);

        Text previewTypeLabelTwo = new Text();
        grid.add(previewTypeLabelTwo, 3,2);

        Text previewNameLabelTwo = new Text();
        grid.add(previewNameLabelTwo, 3,3);

        Text previewAdd1LabelTwo = new Text();
        grid.add(previewAdd1LabelTwo, 3,4);

        Text previewAdd2LabelTwo = new Text();
        grid.add(previewAdd2LabelTwo, 3,5);


        List<String> observableContactListTwo = new ArrayList<>();

        for(List<String> list : contactList) {
            if(list.get(0).equals(chooseMemberBoxTwo.getValue())) {
                observableContactListTwo.add(list.get(1));
            }
        }

        ComboBox<String> chooseContactBoxTwo = new ComboBox<>(FXCollections.observableArrayList(observableContactListTwo));
        new AutoCompleteComboBoxListener<String>(chooseContactBoxTwo);
        chooseContactBoxTwo.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseContactBoxTwo, 3, 6, 1, 1);

        Text previewContactNameTwo = new Text();
        grid.add(previewContactNameTwo, 3,7);

        Text previewEmailTwo = new Text();
        grid.add(previewEmailTwo, 3,8);

        Text previewPhoneTwo = new Text();
        grid.add(previewPhoneTwo, 3,9);



        chooseContactBoxTwo.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenContacts.set(1, null);
                    previewContactNameTwo.setText("");
                    previewEmailTwo.setText("");
                    previewPhoneTwo.setText("");
                } else {

                    int indexOfContact = -1;
                    for(List<String> list : contactList) {

                        if(list.get(1).equals(chooseContactBoxTwo.getValue())) {
                            indexOfContact = contactList.indexOf(list);

                        }
                    }
                    chosenContacts.set(1, contactList.get(indexOfContact));
                    previewContactNameTwo.setText(contactList.get(indexOfContact).get(1));
                    previewEmailTwo.setText(contactList.get(indexOfContact).get(2));
                    previewPhoneTwo.setText(contactList.get(indexOfContact).get(3));
                }
            }
        });



        chooseMemberBoxTwo.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenMembers.set(1, null);
                    previewTypeLabelTwo.setText("");
                    previewNameLabelTwo.setText("");
                    previewAdd1LabelTwo.setText("");
                    previewAdd2LabelTwo.setText("");
                    previewContactNameTwo.setText("");
                    previewEmailTwo.setText("");
                    previewPhoneTwo.setText("");
                } else {
                    chosenMembers.set(1, memberList.get(newValue.intValue()));
                    previewTypeLabelTwo.setText(memberList.get(newValue.intValue()).get(0));
                    previewNameLabelTwo.setText(memberList.get(newValue.intValue()).get(1));
                    previewAdd1LabelTwo.setText(memberList.get(newValue.intValue()).get(2));
                    previewAdd2LabelTwo.setText(memberList.get(newValue.intValue()).get(3));

                    observableContactListTwo.clear();
                    for(List<String> list : contactList) {
                        if(list.get(0).equals(chooseMemberBoxTwo.getValue())) {
                            observableContactListTwo.add(list.get(1));
                        }
                    }
                    chooseContactBoxTwo.setValue("");
                    previewContactNameTwo.setText("");
                    previewEmailTwo.setText("");
                    previewPhoneTwo.setText("");
                    chooseContactBoxTwo.setItems(FXCollections.observableArrayList(observableContactListTwo));
                }
            }
        });










        ComboBox<String> chooseMemberBoxThree = new ComboBox<>(FXCollections.observableArrayList(observableMemberList));
        new AutoCompleteComboBoxListener<String>(chooseMemberBoxThree);
        chooseMemberBoxThree.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseMemberBoxThree, 5, 1, 1, 1);

        Text previewTypeLabelThree = new Text();
        grid.add(previewTypeLabelThree, 5,2);

        Text previewNameLabelThree = new Text();
        grid.add(previewNameLabelThree, 5,3);

        Text previewAdd1LabelThree = new Text();
        grid.add(previewAdd1LabelThree, 5,4);

        Text previewAdd2LabelThree = new Text();
        grid.add(previewAdd2LabelThree, 5,5);


        List<String> observableContactListThree = new ArrayList<>();

        for(List<String> list : contactList) {
            if(list.get(0).equals(chooseMemberBoxThree.getValue())) {
                observableContactListThree.add(list.get(1));
            }
        }

        ComboBox<String> chooseContactBoxThree = new ComboBox<>(FXCollections.observableArrayList(observableContactListThree));
        new AutoCompleteComboBoxListener<String>(chooseContactBoxThree);
        chooseContactBoxThree.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseContactBoxThree, 5, 6, 1, 1);

        Text previewContactNameThree = new Text();
        grid.add(previewContactNameThree, 5,7);

        Text previewEmailThree = new Text();
        grid.add(previewEmailThree, 5,8);

        Text previewPhoneThree = new Text();
        grid.add(previewPhoneThree, 5,9);



        chooseContactBoxThree.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenContacts.set(2, null);
                    previewContactNameThree.setText("");
                    previewEmailThree.setText("");
                    previewPhoneThree.setText("");
                } else {

                    int indexOfContact = -1;
                    for(List<String> list : contactList) {

                        if(list.get(1).equals(chooseContactBoxThree.getValue())) {
                            indexOfContact = contactList.indexOf(list);

                        }
                    }
                    chosenContacts.set(2, contactList.get(indexOfContact));
                    previewContactNameThree.setText(contactList.get(indexOfContact).get(1));
                    previewEmailThree.setText(contactList.get(indexOfContact).get(2));
                    previewPhoneThree.setText(contactList.get(indexOfContact).get(3));
                }
            }
        });



        chooseMemberBoxThree.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenMembers.set(2, null);
                    previewTypeLabelThree.setText("");
                    previewNameLabelThree.setText("");
                    previewAdd1LabelThree.setText("");
                    previewAdd2LabelThree.setText("");
                    previewContactNameThree.setText("");
                    previewEmailThree.setText("");
                    previewPhoneThree.setText("");
                } else {
                    chosenMembers.set(2, memberList.get(newValue.intValue()));
                    previewTypeLabelThree.setText(memberList.get(newValue.intValue()).get(0));
                    previewNameLabelThree.setText(memberList.get(newValue.intValue()).get(1));
                    previewAdd1LabelThree.setText(memberList.get(newValue.intValue()).get(2));
                    previewAdd2LabelThree.setText(memberList.get(newValue.intValue()).get(3));

                    observableContactListThree.clear();
                    for(List<String> list : contactList) {
                        if(list.get(0).equals(chooseMemberBoxThree.getValue())) {
                            observableContactListThree.add(list.get(1));
                        }
                    }
                    chooseContactBoxThree.setValue("");
                    previewContactNameThree.setText("");
                    previewEmailThree.setText("");
                    previewPhoneThree.setText("");
                    chooseContactBoxThree.setItems(FXCollections.observableArrayList(observableContactListThree));
                }
            }
        });







        ComboBox<String> chooseMemberBoxFour = new ComboBox<>(FXCollections.observableArrayList(observableMemberList));
        new AutoCompleteComboBoxListener<String>(chooseMemberBoxFour);
        chooseMemberBoxFour.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseMemberBoxFour, 7, 1, 1, 1);

        Text previewTypeLabelFour = new Text();
        grid.add(previewTypeLabelFour, 7,2);

        Text previewNameLabelFour = new Text();
        grid.add(previewNameLabelFour, 7,3);

        Text previewAdd1LabelFour = new Text();
        grid.add(previewAdd1LabelFour, 7,4);

        Text previewAdd2LabelFour = new Text();
        grid.add(previewAdd2LabelFour, 7,5);


        List<String> observableContactListFour = new ArrayList<>();

        for(List<String> list : contactList) {
            if(list.get(0).equals(chooseMemberBoxFour.getValue())) {
                observableContactListFour.add(list.get(1));
            }
        }

        ComboBox<String> chooseContactBoxFour = new ComboBox<>(FXCollections.observableArrayList(observableContactListFour));
        new AutoCompleteComboBoxListener<String>(chooseContactBoxFour);
        chooseContactBoxFour.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseContactBoxFour, 7, 6, 1, 1);

        Text previewContactNameFour = new Text();
        grid.add(previewContactNameFour, 7,7);

        Text previewEmailFour = new Text();
        grid.add(previewEmailFour, 7,8);

        Text previewPhoneFour = new Text();
        grid.add(previewPhoneFour, 7,9);



        chooseContactBoxFour.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenContacts.set(3, null);
                    previewContactNameFour.setText("");
                    previewEmailFour.setText("");
                    previewPhoneFour.setText("");
                } else {

                    int indexOfContact = -1;
                    for(List<String> list : contactList) {

                        if(list.get(1).equals(chooseContactBoxFour.getValue())) {
                            indexOfContact = contactList.indexOf(list);

                        }
                    }
                    chosenContacts.set(3, contactList.get(indexOfContact));
                    previewContactNameFour.setText(contactList.get(indexOfContact).get(1));
                    previewEmailFour.setText(contactList.get(indexOfContact).get(2));
                    previewPhoneFour.setText(contactList.get(indexOfContact).get(3));
                }
            }
        });



        chooseMemberBoxFour.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenMembers.set(3, null);
                    previewTypeLabelFour.setText("");
                    previewNameLabelFour.setText("");
                    previewAdd1LabelFour.setText("");
                    previewAdd2LabelFour.setText("");
                    previewContactNameFour.setText("");
                    previewEmailFour.setText("");
                    previewPhoneFour.setText("");
                } else {
                    chosenMembers.set(3, memberList.get(newValue.intValue()));
                    previewTypeLabelFour.setText(memberList.get(newValue.intValue()).get(0));
                    previewNameLabelFour.setText(memberList.get(newValue.intValue()).get(1));
                    previewAdd1LabelFour.setText(memberList.get(newValue.intValue()).get(2));
                    previewAdd2LabelFour.setText(memberList.get(newValue.intValue()).get(3));

                    observableContactListFour.clear();
                    for(List<String> list : contactList) {
                        if(list.get(0).equals(chooseMemberBoxFour.getValue())) {
                            observableContactListFour.add(list.get(1));
                        }
                    }
                    chooseContactBoxFour.setValue("");
                    previewContactNameFour.setText("");
                    previewEmailFour.setText("");
                    previewPhoneFour.setText("");
                    chooseContactBoxFour.setItems(FXCollections.observableArrayList(observableContactListFour));
                }
            }
        });









        ComboBox<String> chooseMemberBoxFive = new ComboBox<>(FXCollections.observableArrayList(observableMemberList));
        new AutoCompleteComboBoxListener<String>(chooseMemberBoxFive);
        chooseMemberBoxFive.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseMemberBoxFive, 9, 1, 1, 1);

        Text previewTypeLabelFive = new Text();
        grid.add(previewTypeLabelFive, 9,2);

        Text previewNameLabelFive = new Text();
        grid.add(previewNameLabelFive, 9,3);

        Text previewAdd1LabelFive = new Text();
        grid.add(previewAdd1LabelFive, 9,4);

        Text previewAdd2LabelFive = new Text();
        grid.add(previewAdd2LabelFive, 9,5);


        List<String> observableContactListFive = new ArrayList<>();

        for(List<String> list : contactList) {
            if(list.get(0).equals(chooseMemberBoxFive.getValue())) {
                observableContactListFive.add(list.get(1));
            }
        }

        ComboBox<String> chooseContactBoxFive = new ComboBox<>(FXCollections.observableArrayList(observableContactListFive));
        new AutoCompleteComboBoxListener<String>(chooseContactBoxFive);
        chooseContactBoxFive.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseContactBoxFive, 9, 6, 1, 1);

        Text previewContactNameFive = new Text();
        grid.add(previewContactNameFive, 9,7);

        Text previewEmailFive = new Text();
        grid.add(previewEmailFive, 9,8);

        Text previewPhoneFive = new Text();
        grid.add(previewPhoneFive, 9,9);



        chooseContactBoxFive.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenContacts.set(4, null);
                    previewContactNameFive.setText("");
                    previewEmailFive.setText("");
                    previewPhoneFive.setText("");
                } else {

                    int indexOfContact = -1;
                    for(List<String> list : contactList) {

                        if(list.get(1).equals(chooseContactBoxFive.getValue())) {
                            indexOfContact = contactList.indexOf(list);

                        }
                    }
                    chosenContacts.set(4, contactList.get(indexOfContact));
                    previewContactNameFive.setText(contactList.get(indexOfContact).get(1));
                    previewEmailFive.setText(contactList.get(indexOfContact).get(2));
                    previewPhoneFive.setText(contactList.get(indexOfContact).get(3));
                }
            }
        });



        chooseMemberBoxFive.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenMembers.set(4, null);
                    previewTypeLabelFive.setText("");
                    previewNameLabelFive.setText("");
                    previewAdd1LabelFive.setText("");
                    previewAdd2LabelFive.setText("");
                    previewContactNameFive.setText("");
                    previewEmailFive.setText("");
                    previewPhoneFive.setText("");
                } else {
                    chosenMembers.set(4, memberList.get(newValue.intValue()));
                    previewTypeLabelFive.setText(memberList.get(newValue.intValue()).get(0));
                    previewNameLabelFive.setText(memberList.get(newValue.intValue()).get(1));
                    previewAdd1LabelFive.setText(memberList.get(newValue.intValue()).get(2));
                    previewAdd2LabelFive.setText(memberList.get(newValue.intValue()).get(3));

                    observableContactListFive.clear();
                    for(List<String> list : contactList) {
                        if(list.get(0).equals(chooseMemberBoxFive.getValue())) {
                            observableContactListFive.add(list.get(1));
                        }
                    }
                    chooseContactBoxFive.setValue("");
                    previewContactNameFive.setText("");
                    previewEmailFive.setText("");
                    previewPhoneFive.setText("");
                    chooseContactBoxFive.setItems(FXCollections.observableArrayList(observableContactListFive));
                }
            }
        });









        ComboBox<String> chooseMemberBoxSix = new ComboBox<>(FXCollections.observableArrayList(observableMemberList));
        new AutoCompleteComboBoxListener<String>(chooseMemberBoxSix);
        chooseMemberBoxSix.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseMemberBoxSix, 1, 10, 1, 1);

        Text previewTypeLabelSix = new Text();
        grid.add(previewTypeLabelSix, 1,11);

        Text previewNameLabelSix = new Text();
        grid.add(previewNameLabelSix, 1,12);

        Text previewAdd1LabelSix = new Text();
        grid.add(previewAdd1LabelSix, 1,13);

        Text previewAdd2LabelSix = new Text();
        grid.add(previewAdd2LabelSix, 1,14);


        List<String> observableContactListSix = new ArrayList<>();

        for(List<String> list : contactList) {
            if(list.get(0).equals(chooseMemberBoxSix.getValue())) {
                observableContactListSix.add(list.get(1));
            }
        }

        ComboBox<String> chooseContactBoxSix = new ComboBox<>(FXCollections.observableArrayList(observableContactListSix));
        new AutoCompleteComboBoxListener<String>(chooseContactBoxSix);
        chooseContactBoxSix.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseContactBoxSix, 1, 15, 1, 1);

        Text previewContactNameSix = new Text();
        grid.add(previewContactNameSix, 1,16);

        Text previewEmailSix = new Text();
        grid.add(previewEmailSix, 1,17);

        Text previewPhoneSix = new Text();
        grid.add(previewPhoneSix, 1,18);



        chooseContactBoxSix.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenContacts.set(5, null);
                    previewContactNameSix.setText("");
                    previewEmailSix.setText("");
                    previewPhoneSix.setText("");
                } else {

                    int indexOfContact = -1;
                    for(List<String> list : contactList) {

                        if(list.get(1).equals(chooseContactBoxSix.getValue())) {
                            indexOfContact = contactList.indexOf(list);

                        }
                    }
                    chosenContacts.set(5, contactList.get(indexOfContact));
                    previewContactNameSix.setText(contactList.get(indexOfContact).get(1));
                    previewEmailSix.setText(contactList.get(indexOfContact).get(2));
                    previewPhoneSix.setText(contactList.get(indexOfContact).get(3));
                }
            }
        });



        chooseMemberBoxSix.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenMembers.set(5, null);
                    previewTypeLabelSix.setText("");
                    previewNameLabelSix.setText("");
                    previewAdd1LabelSix.setText("");
                    previewAdd2LabelSix.setText("");
                    previewContactNameSix.setText("");
                    previewEmailSix.setText("");
                    previewPhoneSix.setText("");
                } else {
                    chosenMembers.set(5, memberList.get(newValue.intValue()));
                    previewTypeLabelSix.setText(memberList.get(newValue.intValue()).get(0));
                    previewNameLabelSix.setText(memberList.get(newValue.intValue()).get(1));
                    previewAdd1LabelSix.setText(memberList.get(newValue.intValue()).get(2));
                    previewAdd2LabelSix.setText(memberList.get(newValue.intValue()).get(3));

                    observableContactListSix.clear();
                    for(List<String> list : contactList) {
                        if(list.get(0).equals(chooseMemberBoxSix.getValue())) {
                            observableContactListSix.add(list.get(1));
                        }
                    }
                    chooseContactBoxSix.setValue("");
                    previewContactNameSix.setText("");
                    previewEmailSix.setText("");
                    previewPhoneSix.setText("");
                    chooseContactBoxSix.setItems(FXCollections.observableArrayList(observableContactListSix));
                }
            }
        });









        ComboBox<String> chooseMemberBoxSeven = new ComboBox<>(FXCollections.observableArrayList(observableMemberList));
        new AutoCompleteComboBoxListener<String>(chooseMemberBoxSeven);
        chooseMemberBoxSeven.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseMemberBoxSeven, 3, 10, 1, 1);

        Text previewTypeLabelSeven = new Text();
        grid.add(previewTypeLabelSeven, 3,11);

        Text previewNameLabelSeven = new Text();
        grid.add(previewNameLabelSeven, 3,12);

        Text previewAdd1LabelSeven = new Text();
        grid.add(previewAdd1LabelSeven, 3,13);

        Text previewAdd2LabelSeven = new Text();
        grid.add(previewAdd2LabelSeven, 3,14);


        List<String> observableContactListSeven = new ArrayList<>();

        for(List<String> list : contactList) {
            if(list.get(0).equals(chooseMemberBoxSeven.getValue())) {
                observableContactListSeven.add(list.get(1));
            }
        }

        ComboBox<String> chooseContactBoxSeven = new ComboBox<>(FXCollections.observableArrayList(observableContactListSeven));
        new AutoCompleteComboBoxListener<String>(chooseContactBoxSeven);
        chooseContactBoxSeven.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseContactBoxSeven, 3, 15, 1, 1);

        Text previewContactNameSeven = new Text();
        grid.add(previewContactNameSeven, 3,16);

        Text previewEmailSeven = new Text();
        grid.add(previewEmailSeven, 3,17);

        Text previewPhoneSeven = new Text();
        grid.add(previewPhoneSeven, 3,18);



        chooseContactBoxSeven.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenContacts.set(6, null);
                    previewContactNameSeven.setText("");
                    previewEmailSeven.setText("");
                    previewPhoneSeven.setText("");
                } else {

                    int indexOfContact = -1;
                    for(List<String> list : contactList) {

                        if(list.get(1).equals(chooseContactBoxSeven.getValue())) {
                            indexOfContact = contactList.indexOf(list);

                        }
                    }
                    chosenContacts.set(6, contactList.get(indexOfContact));
                    previewContactNameSeven.setText(contactList.get(indexOfContact).get(1));
                    previewEmailSeven.setText(contactList.get(indexOfContact).get(2));
                    previewPhoneSeven.setText(contactList.get(indexOfContact).get(3));
                }
            }
        });



        chooseMemberBoxSeven.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenMembers.set(6, null);
                    previewTypeLabelSeven.setText("");
                    previewNameLabelSeven.setText("");
                    previewAdd1LabelSeven.setText("");
                    previewAdd2LabelSeven.setText("");
                    previewContactNameSeven.setText("");
                    previewEmailSeven.setText("");
                    previewPhoneSeven.setText("");
                } else {
                    chosenMembers.set(6, memberList.get(newValue.intValue()));
                    previewTypeLabelSeven.setText(memberList.get(newValue.intValue()).get(0));
                    previewNameLabelSeven.setText(memberList.get(newValue.intValue()).get(1));
                    previewAdd1LabelSeven.setText(memberList.get(newValue.intValue()).get(2));
                    previewAdd2LabelSeven.setText(memberList.get(newValue.intValue()).get(3));

                    observableContactListSeven.clear();
                    for(List<String> list : contactList) {
                        if(list.get(0).equals(chooseMemberBoxSeven.getValue())) {
                            observableContactListSeven.add(list.get(1));
                        }
                    }
                    chooseContactBoxSeven.setValue("");
                    previewContactNameSeven.setText("");
                    previewEmailSeven.setText("");
                    previewPhoneSeven.setText("");
                    chooseContactBoxSeven.setItems(FXCollections.observableArrayList(observableContactListSeven));
                }
            }
        });









        ComboBox<String> chooseMemberBoxEight = new ComboBox<>(FXCollections.observableArrayList(observableMemberList));
        new AutoCompleteComboBoxListener<String>(chooseMemberBoxEight);
        chooseMemberBoxEight.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseMemberBoxEight, 5, 10, 1, 1);

        Text previewTypeLabelEight = new Text();
        grid.add(previewTypeLabelEight, 5,11);

        Text previewNameLabelEight = new Text();
        grid.add(previewNameLabelEight, 5,12);

        Text previewAdd1LabelEight = new Text();
        grid.add(previewAdd1LabelEight, 5,13);

        Text previewAdd2LabelEight = new Text();
        grid.add(previewAdd2LabelEight, 5,14);


        List<String> observableContactListEight = new ArrayList<>();

        for(List<String> list : contactList) {
            if(list.get(0).equals(chooseMemberBoxEight.getValue())) {
                observableContactListEight.add(list.get(1));
            }
        }

        ComboBox<String> chooseContactBoxEight = new ComboBox<>(FXCollections.observableArrayList(observableContactListEight));
        new AutoCompleteComboBoxListener<String>(chooseContactBoxEight);
        chooseContactBoxEight.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseContactBoxEight, 5, 15, 1, 1);

        Text previewContactNameEight = new Text();
        grid.add(previewContactNameEight, 5,16);

        Text previewEmailEight = new Text();
        grid.add(previewEmailEight, 5,17);

        Text previewPhoneEight = new Text();
        grid.add(previewPhoneEight, 5,18);



        chooseContactBoxEight.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenContacts.set(7, null);
                    previewContactNameEight.setText("");
                    previewEmailEight.setText("");
                    previewPhoneEight.setText("");
                } else {

                    int indexOfContact = -1;
                    for(List<String> list : contactList) {

                        if(list.get(1).equals(chooseContactBoxEight.getValue())) {
                            indexOfContact = contactList.indexOf(list);

                        }
                    }
                    chosenContacts.set(7, contactList.get(indexOfContact));
                    previewContactNameEight.setText(contactList.get(indexOfContact).get(1));
                    previewEmailEight.setText(contactList.get(indexOfContact).get(2));
                    previewPhoneEight.setText(contactList.get(indexOfContact).get(3));
                }
            }
        });



        chooseMemberBoxEight.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenMembers.set(7, null);
                    previewTypeLabelEight.setText("");
                    previewNameLabelEight.setText("");
                    previewAdd1LabelEight.setText("");
                    previewAdd2LabelEight.setText("");
                    previewContactNameEight.setText("");
                    previewEmailEight.setText("");
                    previewPhoneEight.setText("");
                } else {
                    chosenMembers.set(7, memberList.get(newValue.intValue()));
                    previewTypeLabelEight.setText(memberList.get(newValue.intValue()).get(0));
                    previewNameLabelEight.setText(memberList.get(newValue.intValue()).get(1));
                    previewAdd1LabelEight.setText(memberList.get(newValue.intValue()).get(2));
                    previewAdd2LabelEight.setText(memberList.get(newValue.intValue()).get(3));

                    observableContactListEight.clear();
                    for(List<String> list : contactList) {
                        if(list.get(0).equals(chooseMemberBoxEight.getValue())) {
                            observableContactListEight.add(list.get(1));
                        }
                    }
                    chooseContactBoxEight.setValue("");
                    previewContactNameEight.setText("");
                    previewEmailEight.setText("");
                    previewPhoneEight.setText("");
                    chooseContactBoxEight.setItems(FXCollections.observableArrayList(observableContactListEight));
                }
            }
        });









        ComboBox<String> chooseMemberBoxNine = new ComboBox<>(FXCollections.observableArrayList(observableMemberList));
        new AutoCompleteComboBoxListener<String>(chooseMemberBoxNine);
        chooseMemberBoxNine.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseMemberBoxNine, 7, 10, 1, 1);

        Text previewTypeLabelNine = new Text();
        grid.add(previewTypeLabelNine, 7,11);

        Text previewNameLabelNine = new Text();
        grid.add(previewNameLabelNine, 7,12);

        Text previewAdd1LabelNine = new Text();
        grid.add(previewAdd1LabelNine, 7,13);

        Text previewAdd2LabelNine = new Text();
        grid.add(previewAdd2LabelNine, 7,14);


        List<String> observableContactListNine = new ArrayList<>();

        for(List<String> list : contactList) {
            if(list.get(0).equals(chooseMemberBoxNine.getValue())) {
                observableContactListNine.add(list.get(1));
            }
        }

        ComboBox<String> chooseContactBoxNine = new ComboBox<>(FXCollections.observableArrayList(observableContactListNine));
        new AutoCompleteComboBoxListener<String>(chooseContactBoxNine);
        chooseContactBoxNine.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseContactBoxNine, 7, 15, 1, 1);

        Text previewContactNameNine = new Text();
        grid.add(previewContactNameNine, 7,16);

        Text previewEmailNine = new Text();
        grid.add(previewEmailNine, 7,17);

        Text previewPhoneNine = new Text();
        grid.add(previewPhoneNine, 7,18);



        chooseContactBoxNine.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenContacts.set(8, null);
                    previewContactNameNine.setText("");
                    previewEmailNine.setText("");
                    previewPhoneNine.setText("");
                } else {

                    int indexOfContact = -1;
                    for(List<String> list : contactList) {

                        if(list.get(1).equals(chooseContactBoxNine.getValue())) {
                            indexOfContact = contactList.indexOf(list);

                        }
                    }
                    chosenContacts.set(8, contactList.get(indexOfContact));
                    previewContactNameNine.setText(contactList.get(indexOfContact).get(1));
                    previewEmailNine.setText(contactList.get(indexOfContact).get(2));
                    previewPhoneNine.setText(contactList.get(indexOfContact).get(3));
                }
            }
        });



        chooseMemberBoxNine.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenMembers.set(8, null);
                    previewTypeLabelNine.setText("");
                    previewNameLabelNine.setText("");
                    previewAdd1LabelNine.setText("");
                    previewAdd2LabelNine.setText("");
                    previewContactNameNine.setText("");
                    previewEmailNine.setText("");
                    previewPhoneNine.setText("");
                } else {
                    chosenMembers.set(8, memberList.get(newValue.intValue()));
                    previewTypeLabelNine.setText(memberList.get(newValue.intValue()).get(0));
                    previewNameLabelNine.setText(memberList.get(newValue.intValue()).get(1));
                    previewAdd1LabelNine.setText(memberList.get(newValue.intValue()).get(2));
                    previewAdd2LabelNine.setText(memberList.get(newValue.intValue()).get(3));

                    observableContactListNine.clear();
                    for(List<String> list : contactList) {
                        if(list.get(0).equals(chooseMemberBoxNine.getValue())) {
                            observableContactListNine.add(list.get(1));
                        }
                    }
                    chooseContactBoxNine.setValue("");
                    previewContactNameNine.setText("");
                    previewEmailNine.setText("");
                    previewPhoneNine.setText("");
                    chooseContactBoxNine.setItems(FXCollections.observableArrayList(observableContactListNine));
                }
            }
        });









        ComboBox<String> chooseMemberBoxTen = new ComboBox<>(FXCollections.observableArrayList(observableMemberList));
        new AutoCompleteComboBoxListener<String>(chooseMemberBoxTen);
        chooseMemberBoxTen.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseMemberBoxTen, 9, 10, 1, 1);

        Text previewTypeLabelTen = new Text();
        grid.add(previewTypeLabelTen, 9,11);

        Text previewNameLabelTen = new Text();
        grid.add(previewNameLabelTen, 9,12);

        Text previewAdd1LabelTen = new Text();
        grid.add(previewAdd1LabelTen, 9,13);

        Text previewAdd2LabelTen = new Text();
        grid.add(previewAdd2LabelTen, 9,14);


        List<String> observableContactListTen = new ArrayList<>();

        for(List<String> list : contactList) {
            if(list.get(0).equals(chooseMemberBoxTen.getValue())) {
                observableContactListTen.add(list.get(1));
            }
        }

        ComboBox<String> chooseContactBoxTen = new ComboBox<>(FXCollections.observableArrayList(observableContactListTen));
        new AutoCompleteComboBoxListener<String>(chooseContactBoxTen);
        chooseContactBoxTen.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.add(chooseContactBoxTen, 9, 15, 1, 1);

        Text previewContactNameTen = new Text();
        grid.add(previewContactNameTen, 9,16);

        Text previewEmailTen = new Text();
        grid.add(previewEmailTen, 9,17);

        Text previewPhoneTen = new Text();
        grid.add(previewPhoneTen, 9,18);



        chooseContactBoxTen.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenContacts.set(9, null);
                    previewContactNameTen.setText("");
                    previewEmailTen.setText("");
                    previewPhoneTen.setText("");
                } else {

                    int indexOfContact = -1;
                    for(List<String> list : contactList) {

                        if(list.get(1).equals(chooseContactBoxTen.getValue())) {
                            indexOfContact = contactList.indexOf(list);

                        }
                    }
                    chosenContacts.set(9, contactList.get(indexOfContact));
                    previewContactNameTen.setText(contactList.get(indexOfContact).get(1));
                    previewEmailTen.setText(contactList.get(indexOfContact).get(2));
                    previewPhoneTen.setText(contactList.get(indexOfContact).get(3));
                }
            }
        });



        chooseMemberBoxTen.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                if(newValue.intValue() == -1) {
                    chosenMembers.set(9, null);
                    previewTypeLabelTen.setText("");
                    previewNameLabelTen.setText("");
                    previewAdd1LabelTen.setText("");
                    previewAdd2LabelTen.setText("");
                    previewContactNameTen.setText("");
                    previewEmailTen.setText("");
                    previewPhoneTen.setText("");
                } else {
                    chosenMembers.set(9, memberList.get(newValue.intValue()));
                    previewTypeLabelTen.setText(memberList.get(newValue.intValue()).get(0));
                    previewNameLabelTen.setText(memberList.get(newValue.intValue()).get(1));
                    previewAdd1LabelTen.setText(memberList.get(newValue.intValue()).get(2));
                    previewAdd2LabelTen.setText(memberList.get(newValue.intValue()).get(3));

                    observableContactListTen.clear();
                    for(List<String> list : contactList) {
                        if(list.get(0).equals(chooseMemberBoxTen.getValue())) {
                            observableContactListTen.add(list.get(1));
                        }
                    }
                    chooseContactBoxTen.setValue("");
                    previewContactNameTen.setText("");
                    previewEmailTen.setText("");
                    previewPhoneTen.setText("");
                    chooseContactBoxTen.setItems(FXCollections.observableArrayList(observableContactListTen));
                }
            }
        });


*/


            Label numberLabelOne = new Label("1)");
            grid.add(numberLabelOne, 0, 1);

            Label numberLabelTwo = new Label("2)");
            grid.add(numberLabelTwo, 2, 1);

            Label numberLabelThree = new Label("3)");
            grid.add(numberLabelThree, 4, 1);

            Label numberLabelFour = new Label("4)");
            grid.add(numberLabelFour, 6, 1);

            Label numberLabelFive = new Label("5)");
            grid.add(numberLabelFive, 8, 1);

            Label numberLabelSix = new Label("6)");
            grid.add(numberLabelSix, 0, 10);

            Label numberLabelSeven = new Label("7)");
            grid.add(numberLabelSeven, 2, 10);

            Label numberLabelEight = new Label("8)");
            grid.add(numberLabelEight, 4, 10);

            Label numberLabelNine = new Label("9)");
            grid.add(numberLabelNine, 6, 10);

            Label numberLabelTen = new Label("10)");
            grid.add(numberLabelTen, 8, 10);

            Button nextButton = new Button("Next");
            HBox nextButtonBox = new HBox(10);
            nextButtonBox.setAlignment(Pos.BOTTOM_RIGHT);
            nextButtonBox.getChildren().add(nextButton);
            grid.add(nextButtonBox, 8, 20);

        /*

        nextButton.setOnAction( e -> {

            for(List<String> list : chosenMembers) {

                if(chosenContacts.get(chosenMembers.indexOf(list) )!= null) {

                    for(String str : chosenContacts.get(chosenMembers.indexOf(list))) {

                        list.add(str);
                    }

                }
            }
            SubGenApp.createScene1();
        });

         */


        }
        return grid;
    }
}