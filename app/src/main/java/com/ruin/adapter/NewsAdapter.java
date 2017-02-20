package com.ruin.adapter;


import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ennew.R;
import com.ruin.util.SwapMessage;
/**
 * 用于显示环境记录条目
 * @author LHH
 *
 */
public class NewsAdapter extends BaseAdapter {
	protected static final String TAG = "NewsAdapter";
	private Context mContext;
	private List<SwapMessage> lists;
	
	

	public NewsAdapter(Context context, List<SwapMessage> lists) {
		this.mContext = context;
		this.lists = lists;
		
	}

	@Override
	public int getCount() {
		if (lists != null) {
			return lists.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		SwapMessage message = lists.get(position);
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.fragment_history_item,
					null);
			holder = new Holder();
			holder.myWeather = (ImageView) convertView.findViewById(R.id.myWeather);
			holder.myTmep = (TextView) convertView
					.findViewById(R.id.myTemp);
			holder.myHumi = (TextView) convertView
					.findViewById(R.id.myHumi);
			holder.myLight = (TextView) convertView
					.findViewById(R.id.myLight);
		
			holder.myRain = (TextView) convertView
					.findViewById(R.id.myRain);
		
			holder.myTime = (TextView) convertView
					.findViewById(R.id.myTime);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.myWeather.setBackgroundResource(R.drawable.ic_launcher);
		
		
		if(Float.parseFloat(message.getLight())>26)
			holder.myWeather.setImageResource(R.drawable.sun);
		else
			holder.myWeather.setImageResource(R.drawable.night);
		if(Float.parseFloat(message.getRain())>10)
			holder.myWeather.setImageResource(R.drawable.rain);
		
			
		holder.myTmep.setText("温度:"+message.getTemp()+"℃");
		holder.myHumi.setText("湿度:"+message.getHumi()+"%RH");
		holder.myLight.setText("光强:"+message.getLight()+"Lx");
		
		holder.myRain.setText("雨滴:"+message.getRain()+"%");
		
		String date=message.getDate().trim();
		date=date.substring(date.lastIndexOf(" ")+1,date.lastIndexOf(":"));
		holder.myTime.setText(date);
		return convertView;
	}

	class Holder {
		ImageView myWeather;
		TextView myTmep;
		TextView myHumi;
		TextView myLight;
		TextView myRain;
		TextView myTime;
	}

}
