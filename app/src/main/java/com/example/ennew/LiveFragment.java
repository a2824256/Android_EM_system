package com.example.ennew;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ColumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

import com.ruin.db.SqlOpenHelper;
import com.ruin.util.Config;
import com.ruin.util.SwapMessage;
import com.ruin.view.CheckStateListener;
import com.ruin.view.SearchDevicesView;
import com.ruin.view.TitleBarView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 实时显示界面Fragment类，继承BaseFragment
 * @author LHH
 *
 */
@SuppressLint({ "NewApi", "SimpleDateFormat", "InflateParams", "HandlerLeak" })
public class LiveFragment extends BaseFragment {
	private TitleBarView mTitleBarView;//标题布局UI
	private Context mContext;//上下文
	private Handler handler;//处理获取消息
	private Handler timehandler;//处理时间消息
	private TextView txtTime;//显示时间布局
	private Map<String, String> newDate=new HashMap<String, String>();//更新时间
	private  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化时间 
	private  int  count=0;//记录连接失败次数
	private List<sendMsgThread> sendThread=new ArrayList<sendMsgThread>(); //用来存放发送线程
	private List<RecvMsgThread> recvThread=new ArrayList<RecvMsgThread>();//用来存放接收线程
	private List<SocketConnect> connectThread=new ArrayList<SocketConnect>();//用来存放连接线程
	public final static String[] months = new String[] { "温度(℃)", "湿度(%RH)", "光强(Lx)",  "雨滴(%)"};//显示立方图底部文字数组
	private ColumnChartView chartBottom;
	private ColumnChartData columnData;
	private List<ColumnValue> liveData=new ArrayList<ColumnValue>();
	private String tempDate="";//记录上次时间
	private LinearLayout baseLayout;//基础布局
	private TextView txtSign;//开关控件
	private SocketConnect conn;//连接对象
	private String tip="C";//记录开关状态
	private View mBaseView;//界面基础布局


	@SuppressLint("HandlerLeak")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();//获取上下文

		mBaseView = inflater.inflate(R.layout.fragment_live, null);//挂载布局文件

		String nowTime = sdf.format(new Date());// 按以上格式 将当前时间转换成字符串
		newDate.put("date", nowTime);//记录当前时间
		conn= new SocketConnect(); //创建连接线程
		conn.start();//启动连接线程
		connectThread.add(conn);//记录连接线程
		handler=new Handler() {

			@SuppressLint("HandlerLeak")
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根
				super.handleMessage(msg);
				String info=msg.getData().getString("data"); //获取数据				
				if(info.equals("OK")) {
					count=0;
					Toast.makeText(mContext, "连接成功！", Toast.LENGTH_SHORT).show();
					String nowTime = sdf.format(new Date());// 按以上格式 将当前时间转换成字符串
					newDate.put("date", nowTime);//更新时间
				} else if(info.startsWith("CNOK")) {
					if(count>0)
					Toast.makeText(mContext, "再次连接成功！", Toast.LENGTH_SHORT).show();	
					count=0;
					String nowTime = sdf.format(new Date());// 按以上格式 将当前时间转换成字符串
					newDate.put("date", nowTime);//更新时间
					if(info.length()>4) {

						String[] content=info.split(";");//把接收到的数据拆分放到数组
						SwapMessage message=new SwapMessage();
						message.setTemp(content[1]);
						message.setHumi(content[2]);
						message.setLight(content[3]);
						message.setRain(content[4]);
						message.setDate(nowTime);
						tempDate=nowTime;
						SqlOpenHelper.getInstance(mContext, Config.DB_NAME).insert2db(message);//把接收到的数据插入数据库
						tip=content[5];
						if(tip.equals("C")) {
							tip="关闭";
						} else {
							tip="开启";
						}
						txtSign.setText("当前状态:"+tip);//更新开关状态
						generateColumnData(message);

					}
				} else if(info.equals("FAL"))	{
					count++;
					Toast.makeText(mContext, "连接失败！正在尝试第("+String.valueOf(count)+")连接...", Toast.LENGTH_SHORT).show();
				} else {
				count=0; 
				if(tip.equals("C")) {
					tip="关闭";
				} else {
					tip="开启";
				}
				txtSign.setText("当前状态:"+tip);
				String nowTime = sdf.format(new Date());// 按以上格式 将当前时间转换成字符串
				newDate.put("date", nowTime);//更新时间

				}


			}






		};
		timehandler=new Handler() {
			@SuppressLint("HandlerLeak")
			@Override
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根
				super.handleMessage(msg);
				if(msg.arg1==12115) {
					txtTime.setText("当前时间:"+(String)msg.obj+"\r\n上一次更新时间:"+tempDate);//更新时间

				}
			}
		};
		initView(mBaseView);
		return mBaseView;




	}
	@SuppressLint("ShowToast")
	private void  initView(View mBaseView) {  //初始化页面布局
		DisplayMetrics dm = new DisplayMetrics();getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;//宽度
		int height = dm.heightPixels ;//高度 
		mTitleBarView=(TitleBarView)mBaseView.findViewById(R.id.title_bar);

		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE);		

		mTitleBarView.setTitleText(R.string.showontime);

		baseLayout=(LinearLayout)mBaseView.findViewById(R.id.test);
		SearchDevicesView searchView=new SearchDevicesView(mContext);
		chartBottom=new ColumnChartView(mContext);

		searchView.setWillNotDraw(false);

		LayoutParams paramchart=new LayoutParams(width,height-width-110);	
		LayoutParams paramsearch=new LayoutParams((int) (0.8*width),(int) (0.8*width));
		baseLayout.addView(chartBottom,paramchart);
		baseLayout.addView(searchView,paramsearch);


		txtTime=(TextView)mBaseView.findViewById(R.id.txtTime);
		txtSign=(TextView)mBaseView.findViewById(R.id.txtSign);
		searchView.setCheckStateListener(new CheckStateListener() {

			@Override
			public void onCheckStateListener(Boolean sign)   {
				// TODO 自动生成的方法存根
				String operate=null;
				String s=null;
				if(sign==true) {
					operate="op";
					s="开启";

				} else {
					operate="cl";
					s="关闭";
				}

				try {
					conn.sendMsg(operate);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				Toast.makeText(mContext, "正在尝试("+s+")请耐心等待...", 1000).show();

			}
		});

		SwapMessage msg=new SwapMessage();
		msg.setTemp("30");
		msg.setHumi("60");
		msg.setLight("30");	
		msg.setRain("10");
		msg.setDate("2015-07-12 22:17:59");
		generateColumnData(msg);//测试用
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				while(true) {
					Message msg=Message.obtain();
					msg.arg1=12115;
					msg.obj=sdf.format(new Date());
					timehandler.sendMessage(msg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}

				}

			}
		}).start();

	}

	private void generateColumnData(SwapMessage msg) {//把数据显示到直方图
		liveData.clear();
		liveData.add(new ColumnValue(Float.parseFloat(msg.getTemp()),getResources().getColor(R.color.mygreen)));
		liveData.add(new ColumnValue(Float.parseFloat(msg.getHumi()),getResources().getColor(R.color.mygreen)));
		liveData.add(new ColumnValue(Float.parseFloat(msg.getLight()),getResources().getColor(R.color.mygreen)));
		liveData.add(new ColumnValue(Float.parseFloat(msg.getRain()),getResources().getColor(R.color.mygreen)));		
		int numSubcolumns = 1;
		int numColumns = months.length;

		List<AxisValue> axisValues = new ArrayList<AxisValue>();
		List<Column> columns = new ArrayList<Column>();
		List<ColumnValue> values;
		String s="";
		String stemp="";
		for (int i = 0; i < numColumns; ++i) {

			values = new ArrayList<ColumnValue>();
			for (int j = 0; j < numSubcolumns; ++j) {

				values.add(liveData.get(i));
				stemp=String.valueOf((int)liveData.get(i).getValue());
				if(stemp.length()>s.length())
					s=stemp;

				axisValues.add(new AxisValue(i, months[i].toCharArray()));
			}


			columns.add(new Column(values).setHasLabels(true));
		}

		columnData = new ColumnChartData(columns);

		columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));	 
		columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(s.length()));

		chartBottom.setColumnChartData(columnData);


		chartBottom.setValueSelectionEnabled(true);

		chartBottom.setZoomType(ZoomType.HORIZONTAL);



	}

	class SocketConnect extends Thread {
		private Socket socket;
		private PrintWriter writer;
		private boolean first=true;
		private boolean startRun=true;


		@Override
		public void run() {
			// TODO 自动生成的方法存根


			try {

				while(startRun) {   
					if(first) {
						socket=new Socket(Config.SERVER_IP,Config.SERVER_PORT);
						first=false;
						RecvMsgThread receive=new RecvMsgThread(socket);
						receive.start();
						sendMsgThread send=new sendMsgThread(socket);
						send.start();	
						recvThread.add(receive);
						sendThread.add(send);
						Bundle b=new Bundle();
						b.putString("data", "OK");
						Message msg=Message.obtain();
						msg.setData(b);
						handler.sendMessage(msg);
					}


					if(datedistnace(newDate.get("date"), sdf.format(new Date()))>17) {//连接超时，发送消息
						Bundle b=new Bundle();
						b.putString("data", "FAL");
						Message msg=Message.obtain();
						msg.setData(b);
						handler.sendMessage(msg);

					}
					Thread.sleep(15000);



				}

			} catch (UnknownHostException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}




		}
		private void sendMsg(String packet) throws IOException {//发送数据
			// TODO 自动生成的方法存根
			if(socket!=null) {
				writer = new PrintWriter(socket.getOutputStream());
				writer.println(packet);
				writer.flush();	
			}


		}

		public boolean isstartRun() {
			return startRun;
		}


		public void setStartrun(boolean startRun) {
			this.startRun = startRun;
		}


	}


	class sendMsgThread extends Thread {   
		private boolean startrun=true;
		private Socket sc;
		private PrintWriter writer;
		private String request="re";
		public sendMsgThread(Socket sc) {
			// TODO 自动生成的构造函数存根
			this.sc=sc;
		}
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			while(startrun)
			{

				try {
					writer = new PrintWriter(sc.getOutputStream());

					writer.println(connectInfo());

					writer.flush();
					Thread.sleep(15000);// 线程暂停15秒，单位毫秒，每隔15s请求一次数据  
				} catch (IOException | InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}

			}	
		}
		private String connectInfo() {
			// TODO 自动生成的方法存根
			return request;

		}

		public boolean isStartrun() {
			return startrun;
		}
		public void setStartrun(boolean startrun) {
			this.startrun = startrun;
		}


	}
	class RecvMsgThread extends Thread {
		private boolean startrun=true;
		private BufferedReader reader;
		private Socket sc;
		public RecvMsgThread(Socket sc) {
			// TODO 自动生成的构造函数存根
			this.sc=sc;
		}
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			while(startrun) {

				try {
					reader = new BufferedReader(new InputStreamReader(sc.getInputStream(), "UTF-8"));
					char[] buffer=new char[100];
					reader.read(buffer);
					String info=new String(buffer);
					if(info!=null) {

						Bundle b=new Bundle();
						b.putString("data", info);
						Message msg=Message.obtain();
						msg.setData(b);
						handler.sendMessage(msg);

					}
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}


			}
		}
		public boolean isStartrun() {
			return startrun;
		}
		public void setStartrun(boolean startrun) {
			this.startrun = startrun;
		}


	}
	private long datedistnace(String datesStart,String dateEnd) { 
		long result = 0;
		try {
			result = (sdf.parse(dateEnd).getTime() - sdf.parse(datesStart)  
					.getTime()) / 1000;
		} catch (ParseException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}// 当前时间减去测试时间  
		// 这个的除以1000得到秒，相应的60000得到分，3600000得到小时 
		return result;
	}


	@Override
	public void onDestroyView() { //销毁所有线程
		Log.d("lhhdestroy", "this is destroy view");
		for(sendMsgThread send:sendThread)
			send.setStartrun(false);
		for(RecvMsgThread recv:recvThread)
			recv.setStartrun(false);
		for(SocketConnect conn:connectThread)
			conn.setStartrun(false);
		super.onDestroyView();
	}

}
