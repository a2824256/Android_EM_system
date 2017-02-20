package com.example.ennew;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.ruin.adapter.NewsAdapter;
import com.ruin.db.SqlOpenHelper;
import com.ruin.util.AsyncTaskBase;
import com.ruin.util.Config;
import com.ruin.util.SwapMessage;
import com.ruin.view.CalendarView;
import com.ruin.view.CalendarView.CalOnItemClickListener;
import com.ruin.view.CustomListView;
import com.ruin.view.LoadingView;
import com.ruin.view.TitleBarView;
import com.ruin.view.CustomListView.OnLoadMoreListener;
import com.ruin.view.CustomListView.OnRefreshListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 环境记录界面Fragment类，继承BaseFragment
 * @author LHH
 *
 */
@SuppressLint("InflateParams")
public class HistoryFragment extends Fragment {
	private Context mContext;
	private View mBaseView;
	private CustomListView mCustomListView;
	private LoadingView mLoadingView;
	private View mCalendarView;
	private NewsAdapter adapter;
	private CalendarView calendar;
	private ImageButton calendarLeft;
	private TextView calendarCenter;
	private ImageButton calendarRight;
	private SimpleDateFormat sdf;
	private boolean showday=true;
	private List<SwapMessage> msglist=new ArrayList<SwapMessage>();
	private TitleBarView mTitleBarView;
    private final static int PageSize=10;
    private static int PageID=0;
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_history, null);
		mCalendarView=inflater.inflate(R.layout.calendar, null);
		initView(mBaseView);
		initCalendar();
		init();	
		return mBaseView;
	}

	private void initView(View mBaseView) {//初始化布局文件
		mTitleBarView=(TitleBarView) mBaseView.findViewById(R.id.title_bar);	
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE);
		mTitleBarView.setBtnRight(R.drawable.skin_conversation_title_right_btn);
	
		mTitleBarView.setTitleText(R.string.mytitle1);
	
		mCustomListView = (CustomListView) mBaseView.findViewById(R.id.lv_news);
		mLoadingView = (LoadingView) mBaseView.findViewById(R.id.loading);
		


	}
	@SuppressLint("SimpleDateFormat")
	private void initCalendar() {//初始化日历控件
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		//获取日历控件对象
		calendar = (CalendarView)mCalendarView.findViewById(R.id.calendar);
		calendar.setSelectMore(false); //单选  
		
		calendarLeft = (ImageButton)mCalendarView.findViewById(R.id.calendarLeft);
		calendarCenter = (TextView)mCalendarView.findViewById(R.id.calendarCenter);
		calendarRight = (ImageButton)mCalendarView.findViewById(R.id.calendarRight);
		try {
			//设置日历日期
			Date date = sdf.parse("2015-01-01");
			calendar.setCalendarData(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//获取日历中年月 ya[0]为年，ya[1]为月（格式大家可以自行在日历控件中改）
		String[] ya = calendar.getMyDate().split("-"); 
		if(ya[1].length()==1)
			ya[1]="0"+ya[1];
		if(ya[2].length()==1)
			ya[2]="0"+ya[2];
		calendarCenter.setText(ya[0]+"-"+ya[1]+"-"+ya[2]);
		calendarCenter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				 mLoadingView.setVisibility(View.GONE);
				showday=false;
				String leftYearAndmonth = calendar.clickLeftDay(); 
				String[] ya = leftYearAndmonth.split("-"); 
				if(ya[1].length()==1)
					ya[1]="0"+ya[1];
				calendarCenter.setText(ya[0]+"-"+ya[1]);
				calendar.setVisibility(View.VISIBLE);
				
			}
		});
		calendarLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击上一月 同样返回年月 
				if(showday)	{
				String leftDay = calendar.clickLeftDay(); 
				String[] ya = leftDay.split("-"); 
				if(ya[1].length()==1)
					ya[1]="0"+ya[1];
				if(ya[2].length()==1)
					ya[2]="0"+ya[2];
				calendarCenter.setText(ya[0]+"-"+ya[1]+"-"+ya[2]);
				firstRequest(calendarCenter.getText().toString().trim());
			
				} else {
					String leftDay = calendar.clickLeftMonth(); 
					String[] ya = leftDay.split("-"); 
					if(ya[1].length()==1)
						ya[1]="0"+ya[1];
					calendarCenter.setText(ya[0]+"-"+ya[1]);
					
					
					
				}
			}
		});
		
		calendarRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击下一月
				if(showday) {
				String rightDay = calendar.clickRightDay();
				String[] ya = rightDay.split("-"); 
				if(ya[1].length()==1)
					ya[1]="0"+ya[1];
				if(ya[2].length()==1)
					ya[2]="0"+ya[2];
				calendarCenter.setText(ya[0]+"-"+ya[1]+"-"+ya[2]);
				firstRequest(calendarCenter.getText().toString().trim());
				
				}
				else {
					String rightDay = calendar.clickRightMonth(); 
					String[] ya = rightDay.split("-"); 
					if(ya[1].length()==1)
						ya[1]="0"+ya[1];
					calendarCenter.setText(ya[0]+"-"+ya[1]);
					
				}
			}
		});
		
		//设置控件监听，可以监听到点击的每一天（大家也可以在控件中根据需求设定）
		calendar.setCalOnItemClickListener(new CalOnItemClickListener() {
			
			@Override
			public void OnItemClick(Date selectedStartDate, Date selectedEndDate,
					Date downDate) {
				// TODO 自动生成的方法存根
              
				if(calendar.isSelectMore()) {
					//Toast.makeText(getActivity(), format.format(selectedStartDate)+"到"+format.format(selectedEndDate), Toast.LENGTH_SHORT).show();
				} else {
					//Toast.makeText(getActivity(), format.format(downDate), Toast.LENGTH_SHORT).show();
					showday=true;
					String[] ya = sdf.format(downDate).split("-"); 
					calendarCenter.setText(ya[0]+"-"+ya[1]+"-"+ya[2]);
                    calendar.curDate=downDate;
                    firstRequest(calendarCenter.getText().toString().trim());
				}
				calendar.setVisibility(View.GONE);
			
				
			}
		});
	}

	private void init() { //初始化布局
		
		adapter = new NewsAdapter(mContext, msglist);
		mCustomListView.setAdapter(adapter);
		mCustomListView.addHeaderView(mCalendarView);	
		mCustomListView.setCanLoadMore(true);		
		firstRequest(calendarCenter.getText().toString().trim()); //第一次加载
		
		mCustomListView.setOnRefreshListener(new OnRefreshListener() {//更新加载
			@Override
			public void onRefresh() { 
				PageID=0;
				mLoadingView.setVisibility(View.VISIBLE);
			    mLoadingView.setText(R.string.tv_loading);
			    msglist.clear();
			    msglist.addAll(SqlOpenHelper.getInstance(mContext, Config.DB_NAME).searchInfo(calendarCenter.getText().toString().trim(), PageID, PageSize));
			    	
			    adapter.notifyDataSetChanged();
			        
			    if(msglist.size()==0) {
			    	mLoadingView.setText("该天没有采集到数据");	
			    } else if(msglist.size()<10) {
			    	mLoadingView.setVisibility(View.GONE);
			    	
			    } else { 
			    	mLoadingView.setVisibility(View.GONE);
			    }
			
			    mCustomListView.onRefreshComplete();
			
				
			
			
				
			}
		});
		mCustomListView.setOnLoadListener(new OnLoadMoreListener() {//读取加载
			
			
			@SuppressLint("ShowToast")
			@Override
			public void onLoadMore() { 
				
				
				PageID++;
				ArrayList<SwapMessage> tempList=SqlOpenHelper.getInstance(mContext, Config.DB_NAME).searchInfo(calendarCenter.getText().toString().trim(), PageID, PageSize);
			    msglist.addAll(tempList);
			    mCustomListView.onLoadMoreComplete();
			    adapter.notifyDataSetChanged();
			    	    
			    if(tempList.size()<10) {
			    	Toast.makeText(mContext, "加载完毕", 0).show();			    	
			    	
			    } 
				
			
				
			}
		});
		mCustomListView.setOnItemClickListener(new OnItemClickListener() {//单击删除条目

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO 自动生成的方法存根
				
				new AlertDialog.Builder(mContext).setTitle("提示").setMessage("确定要删除该条记录？")
			     .setPositiveButton("确定",new DialogInterface.OnClickListener() {
			         @SuppressLint("ShowToast")
					@Override  
			         public void onClick(DialogInterface dialog, int which) {
			            if(SqlOpenHelper.getInstance(mContext, Config.DB_NAME).deleteInfo(msglist.get(position-2).getID())>0) {
			            	Toast.makeText(mContext, "删除成功!!!", 0).show();
			            	 String date = calendarCenter.getText().toString().trim();
			            	firstRequest(date);
			            }
			            	
			         }  
			     }).show();
			}
		});
		mCustomListView.setOnItemLongClickListener(new OnItemLongClickListener() {//双击删除该天数据

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				new AlertDialog.Builder(mContext).setTitle("提示").setMessage("确定要删除该天全部记录？")
			     .setPositiveButton("确定",new DialogInterface.OnClickListener() {
			         @SuppressLint("ShowToast")
					@Override  
			         public void onClick(DialogInterface dialog, int which) {
			        	 String date = calendarCenter.getText().toString().trim();
			            if(SqlOpenHelper.getInstance(mContext, Config.DB_NAME).deleteInfoByDate(date)>0) {
			            	Toast.makeText(mContext, "删除成功!!!", 0).show();
			            	firstRequest(date);
			            }
			            	
			         }  
			     }).show();
				return true;
			}
		});
		
	}
	
	private void firstRequest(String info) {   //首次获取数据
	
		PageID=0;
		mLoadingView.setVisibility(View.VISIBLE);
	    mLoadingView.setText(R.string.tv_loading);
	    msglist.clear();
	    msglist.addAll(SqlOpenHelper.getInstance(mContext, Config.DB_NAME).searchInfo(calendarCenter.getText().toString().trim(), PageID, PageSize));	    	
	    adapter.notifyDataSetChanged();    	    
	    if(msglist.size()==0) {
	    	mLoadingView.setText("该天没有采集到数据");	
	    } else if(msglist.size()<10) {
	    	mLoadingView.setVisibility(View.GONE);
	    	
	    } else { 
	    	mLoadingView.setVisibility(View.GONE);
	    }
	}

	@SuppressWarnings("unused")
	private class NewsAsyncTask extends AsyncTaskBase {	//后台操作
		private int result = -1;
		public NewsAsyncTask(LoadingView loadingView) {
			super(loadingView);
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			
			return 1;
			
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	}

	@SuppressWarnings("unused")
	private class AsyncRefresh extends
			AsyncTask<Integer, Integer, List<SwapMessage>> {
		

		@Override
		protected List<SwapMessage> doInBackground(Integer... params) {
			
			return msglist;
		}

		@Override
		protected void onPostExecute(List<SwapMessage> result) {
			super.onPostExecute(result);
			if (result != null) {
				adapter.notifyDataSetChanged();
				mCustomListView.onRefreshComplete();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

	}

}
