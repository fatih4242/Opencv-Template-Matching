import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class Main extends JFrame{
	
	private JLabel cameraScreen;
	private boolean detectFace = false;

	public Main() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		
		JButton btn = new JButton("Detect Face");
		btn.setBounds(0,0, 100, 100);
		add(btn);
		
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!detectFace) {
					btn.setText("Stop Detecting");
					detectFace = true;
				}else {
					btn.setText("Detect Face");
					detectFace = false;
				}
				
			}
		});
		
		cameraScreen = new JLabel();
		cameraScreen.setSize(new Dimension(600,600));;
		cameraScreen.setLocation(width / 4, height / 4);
		add(cameraScreen);
	
	
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setSize(new Dimension(width, height));
		
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			
			EventQueue.invokeLater( new Runnable() {
				@Override
				public void run() {
					Main main = new Main();
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
						
							main.startCamera();
						}
					}).start();
				}
			});
	}


	private void startCamera() {

		VideoCapture capture;
		byte[] imageData;
		capture= new VideoCapture(0);
		
		Mat img = new Mat();
		String width = String.valueOf(capture.get(MAXIMIZED_HORIZ));
		
		String height =  String.valueOf(capture.get(MAXIMIZED_VERT));
		System.out.println(width +"-"+ height);
		
		while (true) {
			capture.read(img);
			if (detectFace) {
				
				String xmlFile = "xml/haarcascade_frontalface_default.xml";
				CascadeClassifier cc = new CascadeClassifier(xmlFile);
				
				MatOfRect faceDetection = new MatOfRect();
				cc.detectMultiScale(img, faceDetection);
				
				
				Rect rectCrop = null;
				for(Rect rect : faceDetection.toArray()) {
					Imgproc.rectangle(img, new Point(rect.x, rect.y),new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0,0,255),3);
					 rectCrop = new Rect(rect.x, rect.y, rect.width, rect.height);
				}
				Mat mat = new Mat(img, rectCrop);
				Mat grayScaleImgMat = new Mat();
				
				Imgproc.cvtColor(mat, grayScaleImgMat, Imgproc.COLOR_RGB2GRAY);	
				Size sz = new Size(100,100);
				
				Mat resizedImg = new Mat();
				Imgproc.resize( grayScaleImgMat, resizedImg, sz);
				Imgcodecs.imwrite("images/Hello.jpg", resizedImg);//saving cropped Image
				

		         
		
		        

		       
		        //faceRecognizer.predict(resizedImg, intPointer, doublePointer);
				
				final MatOfByte buf = new MatOfByte();
				Imgcodecs.imencode(".jpg", img, buf);
				
				imageData = buf.toArray();
				
				ImageIcon icon  = new ImageIcon(imageData);
				cameraScreen.setIcon(icon);
			}else {
				
				final MatOfByte buf = new MatOfByte();
				Imgcodecs.imencode(".jpg", img, buf);
				
				imageData = buf.toArray();
				
				ImageIcon icon  = new ImageIcon(imageData);
				cameraScreen.setIcon(icon);
			}
				
				
				
				
				
				
			
		}
		
	
		
		
		
		
		
	}


}