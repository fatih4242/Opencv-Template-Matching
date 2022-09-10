import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_face;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_img_hash;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


import org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer;;

public class LBHP {

	public static void main(String[] args) {
		   //Eğitim için kullanacağım veri setinin dizini
        String trainingDir = "C:/Users/Toker Software/git/repository/FaceRecognizer/images/";
        //Eşleştirme için kullanacağım diğer yüz
        Mat testImage = opencv_imgcodecs.imread("C:/Users/Toker Software/git/repository/FaceRecognizer/images/Hello.jpg");
        
 
        File root = new File(trainingDir);
 
        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
            }
        };
 
        File[] imageFiles = root.listFiles(imgFilter);
 
        MatVector images = new MatVector(imageFiles.length);
 
        Mat labels = new Mat(imageFiles.length, 1, opencv_core.CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();
 
        int counter = 0;
 
        for (File image : imageFiles) {
            Mat img = opencv_imgcodecs.imread("C:/Users/Toker Software/git/repository/FaceRecognizer/images/");
 
            int label = Integer.parseInt(image.getName().split("\\-")[0]);
            System.out.println("Eğitilen Yüz: "+label);
            images.put(counter, img);
 
            labelsBuf.put(counter, label);
 
            counter++;
        }
         FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();
         
   
 
        faceRecognizer.train(images, labels);
        IntPointer intPointer = new IntPointer(1);
        DoublePointer doublePointer = new DoublePointer(1);
        
        faceRecognizer.predict(testImage, intPointer, doublePointer);
        
        System.out.println("Bulunan Yüz ID: " + doublePointer.get());
        
    }

	

}
