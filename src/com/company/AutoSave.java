package com.company;

import Windows.OutlineWindow;
import Windows.ProjectInfoWindow;
import javafx.scene.control.TreeItem;

import java.io.*;
import java.util.ArrayList;

import static Windows.OutlineWindow.getRoot;
import static Windows.OutlineWindow.getTreeView;
import static com.company.SubGenApp.*;

public class AutoSave extends ProjectInfoWindow{


    /*---------------------------------------------------------------------------
     *  Method used when Load Last Info is clicked. Loads serialized string array
     *  from the project folder.
     */

    public static String[] loadProjectInfo(File file) {

        String[] savedInfo = new String[18];

        try {
            if(file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                savedInfo = (String[]) ois.readObject();
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
            contentList = (ArrayList<String>) ois.readObject();
            System.out.println("The list of Strings " + contentList.toString());

            treeView.getRoot().getChildren().clear();

            for(int i = 0; i < contentList.size(); i++) {

                if(contentList.get(i).substring(0,4).equals("    ")) {
                    getTreeView().getRoot().getChildren().get(getTreeView().getRoot().getChildren().size()-1).getChildren()
                            .get(getTreeView().getRoot().getChildren().get(getTreeView().getRoot().getChildren().size()-1).getChildren().size()-1)
                            .getChildren().add(new TreeItem<>(contentList.get(i).substring(4)));
                } else if(contentList.get(i).substring(0,2).equals("  ") && !contentList.get(i).substring(0,4).equals("    ")) {
                    getTreeView().getRoot().getChildren().get(getTreeView().getRoot().getChildren().size()-1).getChildren()
                            .add(new TreeItem<>(contentList.get(i).substring(2)));
                } else if(!contentList.get(i).substring(0,2).equals("  ")) {
                    getTreeView().getRoot().getChildren().add(new TreeItem<>(contentList.get(i)));
                }
            }

            ois.close();

        } catch (IOException savedInfoLoadE) {
            savedInfoLoadE.printStackTrace();
        } catch (ClassNotFoundException noClass) {
            noClass.printStackTrace();
        }
    }


    /*---------------------------------------------------------------------------
     *  Method used to load a default image.
     */

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

    public static void saveProjectInfo(String[] savedInfo) {


        try {
            File saveInfo = new File("Saves\\" + job);
            saveInfo.mkdirs();
            FileOutputStream fos;
            if(volume.equals("")) {
                fos = new FileOutputStream(saveInfo + "\\ProjectInfo.ser");
            } else {
                File volSaveInfo = new File("Saves\\" + job + "\\" + volume);
                volSaveInfo.mkdirs();
                fos = new FileOutputStream(volSaveInfo + "\\ProjectInfo.ser");
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
            if(volume.equals("")) {
                fosMain = new FileOutputStream(listAddition + "\\ProjectOutline.ser");
            } else {
                fosMain = new FileOutputStream(listAddition + "\\" + volume + "\\ProjectOutline.ser");
            }
            ObjectOutputStream oosMain = new ObjectOutputStream(fosMain);
            contentList.clear();
            OutlineWindow.createListFromTree(getRoot(), 0);
            oosMain.writeObject(contentList);
            oosMain.close();

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
            if(volume.equals("")) {
                fosDeleteButton = new FileOutputStream(listDeletion + "\\ProjectOutline.ser");
            } else {
                fosDeleteButton = new FileOutputStream(listDeletion + "\\" + volume + "\\ProjectOutline.ser");
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
            if(volume.equals("")) {
                fosDropped = new FileOutputStream(bottomDrag + "\\ProjectOutline.ser");
            } else {
                fosDropped = new FileOutputStream(bottomDrag + "\\" + volume + "\\ProjectOutline.ser");
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
            if(volume.equals("")) {
                fosDropped = new FileOutputStream(listSwap + "\\ProjectOutline.ser");
            } else {
                fosDropped = new FileOutputStream(listSwap + "\\" + volume + "\\ProjectOutline.ser");
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
}
