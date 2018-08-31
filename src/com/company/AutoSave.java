package com.company;

import Windows.OutlineWindow;
import Windows.ProjectInfoWindow;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static Windows.OutlineWindow.getRoot;
import static Windows.OutlineWindow.setRoot;
import static com.company.SubGenApp.*;

public class AutoSave extends ProjectInfoWindow{


    /*---------------------------------------------------------------------------
     *  Method used when Load Last Info is clicked. Loads serialized string array
     *  from the project folder.
     */

    public static String[] loadProjectInfo(File file) {

        String[] savedInfo = new String[14];

        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            savedInfo = (String[]) ois.readObject();
            System.out.println(savedInfo.length);
            ois.close();

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
            TreeItem<String> loadSubList = (TreeItem<String>) ois.readObject();
            setRoot(loadSubList);
            System.out.println("The list of Strings " + subSheets.toString());
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
                fos = new FileOutputStream(saveInfo + "\\" + volume + "\\ProjectInfo.ser");
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
            oosMain.writeObject(getRoot());
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
            oosDeleteButton.writeObject(getRoot());
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
            oosDropped.writeObject(getRoot());
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
            oosDropped.writeObject(getRoot());
            oosDropped.close();

        } catch (FileNotFoundException mainSave) {
            mainSave.printStackTrace();
        } catch (IOException mainSave) {
            mainSave.printStackTrace();
        }
    }
}
