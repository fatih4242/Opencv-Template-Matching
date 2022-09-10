import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat source = null;
		Mat template = null;
		source = Imgcodecs.imread("images/template.jpg");//tam resim
		template = Imgcodecs.imread("images/source.jpg");//kesit resim
		
		Mat output = new Mat();
		int machMethod = Imgproc.TM_CCOEFF;
		
		Imgproc.matchTemplate(source, template, output, machMethod);
		
		MinMaxLocResult mmr = Core.minMaxLoc(output);
		Point matchLocmmr = mmr.maxLoc;
	
		Imgproc.rectangle(source, matchLocmmr, new Point(matchLocmmr.x + template.cols(),
				matchLocmmr.y + template.rows()), new Scalar(255,0,0));
		
		Imgcodecs.imwrite("images/finish.jpg", source);
		System.out.println("Bir at hırsızı tespit edildi");
		
		
	}
}
