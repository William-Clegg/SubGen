package com.company;

import Windows.OutlineWindow;
import Windows.ProjectInfoWindow;
import javafx.scene.control.TreeItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static Windows.OutlineWindow.getRoot;
import static Windows.OutlineWindow.getTreeView;
import static com.company.SubGenApp.*;

public class AutoSave extends ProjectInfoWindow{


    /*---------------------------------------------------------------------------
     *  Method used when Load Last Info is clicked. Loads serialized string array
     *  from the project folder.
     */

    public static List<List<String>> loadProjectInfo(File file) {

        List<List<String>> savedInfo = new ArrayList<>();

        try {
            if(file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                savedInfo = (List<List<String>>) ois.readObject();
                ois.close();
            }

        } catch (IOException savedInfoLoadE) {
            savedInfoLoadE.printStackTrace();
        } catch (ClassNotFoundException noClass) {
            noClass.printStackTrace();
        }

        return savedInfo;
    }




    /*---------------------------------------------------------------------------
     *  Method used when Load Last List is clicked. Loads serialized array lists
     *  in project folder and sets the actual outline String content into subSheets.
     *  Converts snapshots of those strings from byte arrays to readable images
     *  for the list view, which are placed into subSheetsImages.
     */

    public static void loadLastList(File file) {

        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            contentList = (ArrayList<PDFLineItem>) ois.readObject();

            treeView.getRoot().getChildren().clear();

            for(int i = 0; i < contentList.size(); i++) {

                if(contentList.get(i).getTier() == 3) {
                    getTreeView().getRoot().getChildren().get(getTreeView().getRoot().getChildren().size()-1).getChildren()
                            .get(getTreeView().getRoot().getChildren().get(getTreeView().getRoot().getChildren().size()-1).getChildren().size()-1)
                            .getChildren().add(new TreeItem<PDFLineItem>(contentList.get(i)));
                } else if(contentList.get(i).getTier() == 2) {
                    getTreeView().getRoot().getChildren().get(getTreeView().getRoot().getChildren().size()-1).getChildren()
                            .add(new TreeItem<>(contentList.get(i)));
                } else if(contentList.get(i).getTier() == 1) {
                    getTreeView().getRoot().getChildren().add(new TreeItem<>(contentList.get(i)));
                }
            }

            ois.close();

        } catch (IOException savedInfoLoadE) {
            System.err.println("IOException in AutoSave trying to load last list");
            savedInfoLoadE.printStackTrace();
        } catch (ClassNotFoundException noClass) {
            System.err.println("ClassNotFoundException in AutoSave trying to load last list");
            noClass.printStackTrace();
        }
    }


    /*---------------------------------------------------------------------------
     *  Method used to load a default image.
     */

    /*
    public static String loadDefaultimage() {

        try {
            FileInputStream fis = new FileInputStream("DefaultImage");  //throws exception first time, change
            ObjectInputStream ois = new ObjectInputStream(fis);
            String imagePath = (String) ois.readObject();
            ois.close();
            if(imagePath != null) {
                return imagePath;
            } else {
                return "";
            }

        } catch (FileNotFoundException savedInfoE) {
            savedInfoE.printStackTrace();
        } catch (IOException savedInfoE) {
            savedInfoE.printStackTrace();
        } catch (ClassNotFoundException savedInfoE) {
            savedInfoE.printStackTrace();
        }
        return "";
    }

     */


    /*---------------------------------------------------------------------------
     *  Method used to save a default image.
     */

    public static void saveDefaultimage(String filePath) {

        try {
            File saveInfo = new File("DefaultImage");
            FileOutputStream fos = new FileOutputStream(saveInfo);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(filePath);
            oos.close();

        } catch (FileNotFoundException savedInfoE) {
            savedInfoE.printStackTrace();
        } catch (IOException savedInfoE) {
            savedInfoE.printStackTrace();
        }
    }


    /*---------------------------------------------------------------------------
     *  Method used when Create Submittal is clicked. Saves serialized string array
     *  in project folder of whatever text was in the fields at the time.
     */

    public static void saveProjectInfo(List<List<String>> savedInfo) {


        try {
            File saveInfo = new File("Saves\\" + job);
            saveInfo.mkdirs();
            FileOutputStream fos;
            if(operationAndMain) {
                    fos = new FileOutputStream(saveInfo + "\\Operation & Maintenance\\ProjectInfo.ser");
            } else {
                if (volume.equals("")) {
                    fos = new FileOutputStream(saveInfo + "\\ProjectInfo.ser");
                } else {
                    File volSaveInfo = new File("Saves\\" + job + "\\" + volume);
                    volSaveInfo.mkdirs();
                    fos = new FileOutputStream(volSaveInfo + "\\ProjectInfo.ser");
                }
            }
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(savedInfo);
            oos.close();

        } catch (FileNotFoundException savedInfoE) {
            savedInfoE.printStackTrace();
        } catch (IOException savedInfoE) {
            savedInfoE.printStackTrace();
        }
    }



    /*---------------------------------------------------------------------------
     *  Method used when any entry is added to the outline. Serializes and saves
     *  updates lists.
     */

    public static void listAddSave() {

        try {
            File listAddition = new File("Saves\\" + job);
            listAddition.mkdirs();
            FileOutputStream fosMain;
            if(operationAndMain) {
                fosMain = new FileOutputStream(listAddition + "\\Operation & Maintenance\\ProjectOutline.ser");
            } else {
                if (volume.equals("")) {
                    fosMain = new FileOutputStream(listAddition + "\\ProjectOutline.ser");
                } else {
                    fosMain = new FileOutputStream(listAddition + "\\" + volume + "\\ProjectOutline.ser");
                }
            }
            ObjectOutputStream oosMain = new ObjectOutputStream(fosMain);
            System.out.println("Size of contentList before clear is " + contentList.size());
            contentList.clear();
            OutlineWindow.createListFromTree(getRoot(), 0);
            System.out.println("Size of contentList is " + contentList.size());
            oosMain.writeObject(contentList);
            oosMain.close();


            for(PDFLineItem item : contentList) {
                System.out.print(item.getTitle() + " : ");
            }

        } catch (FileNotFoundException mainSave) {
            mainSave.printStackTrace();
        } catch (IOException mainSave) {
            mainSave.printStackTrace();
        }
    }




    /*---------------------------------------------------------------------------
     *  Method used when any entry is removed from the outline. Serializes and saves
     *  updates lists.
     */

    public static void listDeleteSave() {

        try {
            File listDeletion = new File("Saves\\" + job);
            listDeletion.mkdirs();
            FileOutputStream fosDeleteButton;
            if(operationAndMain) {
                    fosDeleteButton = new FileOutputStream(listDeletion + "\\Operation & Maintenance\\ProjectOutline.ser");
            } else {
                if (volume.equals("")) {
                    fosDeleteButton = new FileOutputStream(listDeletion + "\\ProjectOutline.ser");
                } else {
                    fosDeleteButton = new FileOutputStream(listDeletion + "\\" + volume + "\\ProjectOutline.ser");
                }
            }
            ObjectOutputStream oosDeleteButton = new ObjectOutputStream(fosDeleteButton);
            contentList.clear();
            OutlineWindow.createListFromTree(getRoot(), 0);
            oosDeleteButton.writeObject(contentList);
            oosDeleteButton.close();

        } catch (FileNotFoundException mainSave) {
            mainSave.printStackTrace();
        } catch (IOException mainSave) {
            mainSave.printStackTrace();
        }
    }



    /*---------------------------------------------------------------------------
     *  Method used to save the outline state when any entry is dragged to an
     *  empty line underneath the bottom entry of the outline.
     */

    static void bottomDragSave() {

        try {

            File bottomDrag = new File("Saves\\" + job);
            bottomDrag.mkdirs();
            FileOutputStream fosDropped;
            if(operationAndMain) {
                    fosDropped = new FileOutputStream(bottomDrag + "\\Operation & Maintenance\\ProjectOutline.ser");
            } else {
                if (volume.equals("")) {
                    fosDropped = new FileOutputStream(bottomDrag + "\\ProjectOutline.ser");
                } else {
                    fosDropped = new FileOutputStream(bottomDrag + "\\" + volume + "\\ProjectOutline.ser");
                }
            }
            ObjectOutputStream oosDropped = new ObjectOutputStream(fosDropped);
            contentList.clear();
            OutlineWindow.createListFromTree(getRoot(), 0);
            oosDropped.writeObject(contentList);
            oosDropped.close();

        } catch (FileNotFoundException mainSave) {
            mainSave.printStackTrace();
        } catch (IOException mainSave) {
            mainSave.printStackTrace();
        }
    }


    /*---------------------------------------------------------------------------
     *  Method used to save the outline state when an entry is dragged to a new
     *  location on the outline.
     */

    static void listSwapSave() {

        try {
            File listSwap = new File("Saves\\" + job);
            listSwap.mkdirs();
            FileOutputStream fosDropped;
            if(operationAndMain) {
                    fosDropped = new FileOutputStream(listSwap + "\\Operation & Maintenance\\ProjectOutline.ser");
            } else {
                if (volume.equals("")) {
                    fosDropped = new FileOutputStream(listSwap + "\\ProjectOutline.ser");
                } else {
                    fosDropped = new FileOutputStream(listSwap + "\\" + volume + "\\ProjectOutline.ser");
                }
            }
            ObjectOutputStream oosDropped = new ObjectOutputStream(fosDropped);
            contentList.clear();
            OutlineWindow.createListFromTree(getRoot(), 0);
            oosDropped.writeObject(contentList);
            oosDropped.close();

        } catch (FileNotFoundException mainSave) {
            mainSave.printStackTrace();
        } catch (IOException mainSave) {
            mainSave.printStackTrace();
        }
    }


    public static void saveProfile(List<String> profileInfo) {


        try {
            FileOutputStream fos;
            fos = new FileOutputStream("Profiles.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(profileInfo);
            oos.close();

        } catch (FileNotFoundException savedInfoE) {
            savedInfoE.printStackTrace();
        } catch (IOException savedInfoE) {
            savedInfoE.printStackTrace();
        }
    }


    public static void saveMember(List<Member> memberInfo) {


        try {
            FileOutputStream fos;
            fos = new FileOutputStream("Members.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(memberInfo);
            oos.close();

        } catch (FileNotFoundException savedInfoE) {
            savedInfoE.printStackTrace();
        } catch (IOException savedInfoE) {
            savedInfoE.printStackTrace();
        }
    }

    public static void saveContact(List<List<String>> contactInfo) {


        try {
            FileOutputStream fos;
            fos = new FileOutputStream("Contacts.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(contactInfo);
            oos.close();

        } catch (FileNotFoundException savedInfoE) {
            savedInfoE.printStackTrace();
        } catch (IOException savedInfoE) {
            savedInfoE.printStackTrace();
        }
    }


    public static List<String> loadProfile() {


        try {
            File profileInfo = new File("Profiles.ser");
            FileInputStream fis;
            fis = new FileInputStream(profileInfo);
            ObjectInputStream ois = new ObjectInputStream(fis);

            List<String> profileInfoList = new ArrayList<>();
            profileInfoList = (List<String>) ois.readObject();
            ois.close();

            return profileInfoList;

        } catch (FileNotFoundException savedInfoE) {
            savedInfoE.getMessage();
        } catch (ClassNotFoundException savedInfoE) {
            savedInfoE.getMessage();
        } catch (IOException ioException) {
            ioException.getMessage();
        }

        return null;
    }


    public static List<Member> loadMemberList() {


        try {
            File memberInfo = new File("Members.ser");
            FileInputStream fis;
            fis = new FileInputStream(memberInfo);
            ObjectInputStream ois = new ObjectInputStream(fis);

            List<Member> memberInfoList;
            memberInfoList = (List<Member>) ois.readObject();
            ois.close();

            return memberInfoList;

        } catch (FileNotFoundException savedInfoE) {
            savedInfoE.getMessage();
        } catch (ClassNotFoundException savedInfoE) {
            savedInfoE.getMessage();
        } catch (IOException ioException) {
            ioException.getMessage();
        }

        return null;
    }


    public static List<List<String>> loadContactList() {


        try {
            File contactInfo = new File("Contacts.ser");
            FileInputStream fis;
            fis = new FileInputStream(contactInfo);
            ObjectInputStream ois = new ObjectInputStream(fis);

            List<List<String>> contactInfoList = new ArrayList<>();
            contactInfoList = (List<List<String>>) ois.readObject();
            ois.close();

            return contactInfoList;

        } catch (FileNotFoundException savedInfoE) {
            savedInfoE.getMessage();
        } catch (ClassNotFoundException savedInfoE) {
            savedInfoE.getMessage();
        } catch (IOException ioException) {
            ioException.getMessage();
        }

        return null;
    }
}
