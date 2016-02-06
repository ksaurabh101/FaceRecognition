package com.example.frecognition;

import java.io.File;



import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;

public class MainActivity extends Activity {
	
	String path="";
	String img2path="";
	Recognizer recog;
	EditText text;
	int imgbutton,imgview1,imgview2;
	Bitmap img1,img2;
	ImageView iv1,iv2;
	Button go;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		recog=new Recognizer(path);
		
		text=(EditText) findViewById(R.id.editText1);
		iv1=(ImageView) findViewById(R.id.imageView1);
		iv2=(ImageView) findViewById(R.id.imageView2);
		go=(Button) findViewById(R.id.button4);
		
		text.setVisibility(View.INVISIBLE);
		go.setVisibility(View.INVISIBLE);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public File getImageDirectory() {
		File root = Environment.getExternalStorageDirectory();
		File im_dir = new File(root, "facerec");
		if (!im_dir.exists()) 
		{
		im_dir.mkdir();
		}
		return im_dir;
	}
	public void image1(View v)
	{
		imgbutton=1;
		text.setVisibility(View.VISIBLE);
		go.setVisibility(View.VISIBLE);
		
	}
	public void image2(View v)
	{
		imgbutton=2;
		text.setVisibility(View.VISIBLE);
		go.setVisibility(View.VISIBLE);
	}
	public void go(View v)
	{
		insert();
	}
	public void verify(View v)
	{
		if(imgview1==1 && imgview2==1)
		{
			recog.train();
			IplImage img;
			img=cvLoadImage(img2path);
			IplImage grayImg;
			grayImg = IplImage.create(img.width(), img.height(), IPL_DEPTH_8U, 1);        
			cvCvtColor(img, grayImg, CV_BGR2GRAY);
			recog.predict(grayImg);
			int prob;
			prob=recog.getProb();
			if(prob<0)
				Toast.makeText(this,"Not Same", Toast.LENGTH_SHORT).show();
			else if(prob<50)
				Toast.makeText(this,"Same-Variance="+prob, Toast.LENGTH_SHORT).show();
			else if(prob<80)
				Toast.makeText(this,"Variance="+prob, Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this,"Not Same-Variance="+prob, Toast.LENGTH_SHORT).show();
		}
		else{
			if(imgview1!=1)
			{
				Toast.makeText(this,"Image 1 Is Not Inserted..!!", Toast.LENGTH_SHORT).show();
			}
			else if(imgview2!=1)
			{
				Toast.makeText(this,"Image 2 Is Not Inserted..!!", Toast.LENGTH_SHORT).show();
			}
		}
	}
	public void insert(){
		String pname=text.getText().toString();
		if(pname.length()>0)
		{
			text.setText("");
			
			File imdir=null;
			
			char c=pname.charAt(0);
			int f=(int)c;
			
			if(f>=97 && f<=122 || f>=65 && f<=90)
			{
				text.setVisibility(View.INVISIBLE);
				go.setVisibility(View.INVISIBLE);
				File im_dir=getImageDirectory();
				String first=pname.substring(0,1);;
				imdir = new File(im_dir,first);
				path=imdir.getAbsolutePath();
				String s=pname+".jpg";
				File imfile=new File(imdir.getAbsolutePath()+"/"+s);
				String filename=imfile.getPath();
				if(imfile.exists())
				{
					try {
						if(imgbutton==1)
						{
							recog.setmPath(path);
							recog.setImgName(s);
							img1 = BitmapFactory.decodeFile(filename);
				            iv1.setImageBitmap(img1);
				            imgbutton=0;
				            imgview1=1;
						}
						else if(imgbutton==2)
						{
							img2path=filename;
							img2 = BitmapFactory.decodeFile(filename);
				            iv2.setImageBitmap(img2);
				            imgbutton=0;
				            imgview2=1;
						}
			            Toast.makeText(this, "Image has been Inserted", Toast.LENGTH_SHORT).show();
			            
			        }  
			        catch (Exception e) {
			            e.printStackTrace();
			        }
				}
				else{
					Toast.makeText(this, "Sorry !! There is No Image Of This Name", Toast.LENGTH_SHORT).show();
				}
				
			}
			else{
				Toast.makeText(this, "Please Enter A Valid Name", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(this, "Enter Image Name Then Go !!", Toast.LENGTH_SHORT).show();
		}
	}
}
