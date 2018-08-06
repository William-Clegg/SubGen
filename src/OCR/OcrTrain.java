package OCR;

import net.sourceforge.javaocr.ocrPlugins.mseOCR.CharacterRange;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.OCRScanner;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.TrainingImage;
import net.sourceforge.javaocr.ocrPlugins.mseOCR.TrainingImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class OcrTrain {

    public static OCRScanner trainFont(Boolean debug) {

        //Trains JavaOCR for Old Goudy Style Bold font

        String trainingImageDir = "C:\\Users\\Rudy\\Documents\\trainingImages\\";

        if (!trainingImageDir.endsWith(File.separator)) {
            trainingImageDir += File.separator;
        }

        OCRScanner ocrScanner = new OCRScanner();
        ocrScanner.clearTrainingImages();


        TrainingImageLoader loader = new TrainingImageLoader();
        HashMap<Character, ArrayList<TrainingImage>> trainingImageMap = new HashMap<Character, ArrayList<TrainingImage>>();

        try {
            loader.load(
                    trainingImageDir + "numberTrain0.png",
                    new CharacterRange('0', '0'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "numberTrain1.png",
                    new CharacterRange('1', '1'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "numberTrain2.png",
                    new CharacterRange('2', '2'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "numberTrain3.png",
                    new CharacterRange('3', '3'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "numberTrain4.png",
                    new CharacterRange('4', '4'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "numberTrain5.png",
                    new CharacterRange('5', '5'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "numberTrain6.png",
                    new CharacterRange('6', '6'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "numberTrain7.png",
                    new CharacterRange('7', '7'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "numberTrain8.png",
                    new CharacterRange('8', '8'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "numberTrain9.png",
                    new CharacterRange('9', '9'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainA.png",
                    new CharacterRange('A', 'A'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainB.png",
                    new CharacterRange('B', 'B'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainC.png",
                    new CharacterRange('C', 'C'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainD.png",
                    new CharacterRange('D', 'D'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainE.png",
                    new CharacterRange('E', 'E'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainF.png",
                    new CharacterRange('F', 'F'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainG.png",
                    new CharacterRange('G', 'G'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainH.png",
                    new CharacterRange('H', 'H'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainI.png",
                    new CharacterRange('I', 'I'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainJ.png",
                    new CharacterRange('J', 'J'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainK.png",
                    new CharacterRange('K', 'K'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainL.png",
                    new CharacterRange('L', 'L'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainM.png",
                    new CharacterRange('M', 'M'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainN.png",
                    new CharacterRange('N', 'N'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainO.png",
                    new CharacterRange('O', 'O'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainP.png",
                    new CharacterRange('P', 'P'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainQ.png",
                    new CharacterRange('Q', 'Q'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainR.png",
                    new CharacterRange('R', 'R'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainS.png",
                    new CharacterRange('S', 'S'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainT.png",
                    new CharacterRange('T', 'T'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainU.png",
                    new CharacterRange('U', 'U'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainV.png",
                    new CharacterRange('V', 'V'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainW.png",
                    new CharacterRange('W', 'W'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainX.png",
                    new CharacterRange('X', 'X'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainY.png",
                    new CharacterRange('Y', 'Y'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "capitalTrainZ.png",
                    new CharacterRange('Z', 'Z'),
                    trainingImageMap);
            loader.load(
                    trainingImageDir + "dashTrain.png",
                    new CharacterRange('-', '-'),
                    trainingImageMap);
            if (debug) {
                System.err.println("adding images");
            }
            ocrScanner.addTrainingImages(trainingImageMap);
            if (debug) {
                System.err.println("loadTrainingImages() done");
            }
        } catch (IOException trainingException) {
            System.err.println("IO Exception in OcrTrain");
        }

        return ocrScanner;
    }

}