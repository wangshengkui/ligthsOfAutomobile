package com.example.java_lightui;

import com.example.java_lightui.*;
import java.io.Console;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.security.auth.PrivateCredentialPermission;
import com.example.java_lightui.R;
import com.hanheng.a53.dip.DipClass;
import com.hanheng.a53.led.LedClass;
import com.hanheng.a53.seg7.Seg7Class;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.provider.MediaStore.Video;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener{
	private TextView text1;
	private TextView text2;
	private TextView text3;
	
	private EditText Spd;
	private EditText Ang;
	private EditText Visib;

	private Button btnButton;
	private boolean flag;
	public AutoCtl autocontrol;
	public int segshow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		this.btnButton.setOnClickListener(this);
	}

	
	public int speed;
	public int angle;
	public int visibility;
	private boolean auto_flag;
	private int auto_open;
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

//	鍒濆鍖栫粍浠�
	public void initView(){
		Spd=(EditText)findViewById(R.id.editText1);
		Ang=(EditText)findViewById(R.id.editText2);
		Visib=(EditText)findViewById(R.id.editText3);

		text1=(TextView)findViewById(R.id.textView1);
		text2=(TextView)findViewById(R.id.textView2);
		text3=(TextView)findViewById(R.id.textView3);

		btnButton=(Button)findViewById(R.id.button1);		

		LedClass.Init();
		Seg7Class.Init();
		DipClass.Init();
		speed=120;
		visibility=120;
		auto_flag=true;
		auto_open=0;
		segshow=1111;
		updateText(segshow);
		openThread();
		
		

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int key=arg0.getId();
		switch (key) {
		case R.id.button1:
			exit();
			break;
		default:
			break;
		}

	}

//	瀛楃涓茶ˉ闆�
	public String addZero(int b){
		String val = Integer.toBinaryString(b&0xFF);
		String str="";
		if(val.length()<8){
			for(int i=0;i<8-val.length();i++){
				str+=0;
			}			
			return str+=val;
		}
		return val;
	}
	
	public void exit(){		
		AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("绋嬪簭閫�鍑�")
		.setMessage("鎮ㄧ‘瀹氳閫�鍑哄悧锛�")
		.setIcon(R.drawable.ic_launcher);
		dialog.setCancelable(false);
		dialog.setPositiveButton("纭", new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				flag=false;

				MainActivity.this.finish(); //鎿嶄綔缁撴潫
			}
		});
		
		dialog.setNegativeButton("鍙栨秷", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		dialog.show();
			
	}


	public void computed(int val){
		String str=addZero(val);
		char[] cr=str.toCharArray();
		int tag;
		for(int i=0;i<cr.length;i++){
			if(cr[i]=='0'){
				tag=0;
				changeState(i,tag);
			}else{		
				changeState(i,1);				
			}
		}
		
	}	
	public void changeState(int i,int tag){
		
			if(tag==0){
				//System.out.println("ffffffffffffffffffffffffffffffffffffffffffffffffff");
				switch (i) {
				case 0:
					auto_flag=true;
					//LedClass.IoctlLed(0, 1);
					//tb8.setChecked(true);				
					break;
				case 1:
					if(!auto_flag)
					{
						LedClass.IoctlLed(1, 1);
						//LedClass.IoctlLed(2, 1);
						if((segshow%100)/10==8)
						  break;
						segshow+=70;
						updateText(segshow);
					}
					   
					//tb7.setChecked(true);				
					break;
				case 2:
					if(!auto_flag)
					{
						if(segshow%10==8)
						  break;
						segshow+=7;
						updateText(segshow);
					}
					//tb6.setChecked(true);				
					break;
				case 3:
					if(!auto_flag)
					{
						if(segshow%1000/100==8)
						  break;
						segshow+=700;
						updateText(segshow);
					}
					//tb5.setChecked(true);				
					break;
				case 4:
					if(!auto_flag)
					{
						if(segshow/1000==8)
						  break;
						segshow+=7000;
						updateText(segshow);
					}
					//tb4.setChecked(true);				
					break;
				case 5:
					if(!auto_flag)
					LedClass.IoctlLed(3, 1);
					//tb3.setChecked(true);				
					break;
				case 6:
					if(!auto_flag)
					LedClass.IoctlLed(2, 1);
					//tb2.setChecked(true);				
					break;
				case 7:
					if(!auto_flag)
					LedClass.IoctlLed(0, 1);
					//tb1.setChecked(true);				
					break;
				default:
					break;
				}
			}else{
				switch (i) {
				case 0:
					auto_flag=false;
					auto_open=0;
					//tb8.setChecked(false);				
					break;
				case 1:
					if(!auto_flag)
					{
					    LedClass.IoctlLed(1, 0);
					    if(segshow%100/10==1)
							  break;
							segshow-=70;
							updateText(segshow);
				    }
					//Seg7Class.Seg7Show(0);
					//tb7.setChecked(false);				
					break;
				case 2:
					if(!auto_flag)
					{
						if(segshow%10==1)
						  break;
						segshow-=7;
						updateText(segshow);
					}
					//tb6.setChecked(false);				
					break;
				case 3:
					if(!auto_flag)
					{
						if(segshow%1000/100==1)
						  break;
						segshow-=700;
						updateText(segshow);
					}
					//tb5.setChecked(false);				
					break;
				case 4:
					if(!auto_flag)
					{
						if(segshow/1000==1)
						  break;
						segshow-=7000;
						updateText(segshow);
					}
					//tb4.setChecked(false);				
					break;
				case 5:
					if(!auto_flag)
					LedClass.IoctlLed(3, 0);
					//tb3.setChecked(false);				
					break;
				case 6:
					if(!auto_flag)
					  LedClass.IoctlLed(2, 0);
					//tb2.setChecked(false);				
					break;
				case 7:
					if(!auto_flag)
					  LedClass.IoctlLed(0, 0);
					//tb1.setChecked(false);				
					break;
				default:
					break;
				}
			}
		
		
	}
	//	鍒濆鍖栨寜閽枃瀛�
	private Handler uiHandler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==1){	
				Log.i("鑾峰彇鏁版嵁", ""+msg.arg1);
				computed(msg.arg1);
				
			}
		}
	};
    
    
	public void updateText(final int arg){
		new Thread(new Runnable() {
			public void run() {
				Seg7Class.Seg7Show(arg);
			}
		}).start();
	}
	
public void openThread(){
			if(!flag){
				MyThread thread=new MyThread();
				this.flag=true;
				thread.start();
			}		
	}
	//	璇诲彇瀛楃绾跨▼
	class MyThread extends Thread{
		public void run(){
			int num = 0;	
			autocontrol=new AutoCtl();
			while(flag){
				try {
					Message msgMessage=new Message();
					int value=DipClass.ReadValue();
					msgMessage.what=1;
					msgMessage.arg1=value;
					uiHandler.sendMessage(msgMessage);
					 	
					
					speed=Integer.parseInt(Spd.getText().toString());
					angle=Integer.parseInt(Ang.getText().toString());
					visibility=Integer.parseInt(Visib.getText().toString());
					
					
					
					if(auto_flag && auto_open==0)
					{
						autocontrol=new AutoCtl();
						autocontrol.speed=speed;
						autocontrol.angle=angle;
						autocontrol.visiablity=visibility;
						autocontrol.flag_auto=auto_flag;
						//autocontrol.auto_open=auto_open;
						
						autocontrol.start();
						auto_open=1;
					}
					else
					{
							autocontrol.speed=speed;
							autocontrol.angle=angle;
							autocontrol.visiablity=visibility;
							autocontrol.flag_auto=auto_flag;
							//auto_open=autocontrol.auto_open;
					}
					
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
