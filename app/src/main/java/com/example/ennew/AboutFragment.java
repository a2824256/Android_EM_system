package com.example.ennew;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ruin.view.TitleBarView;
import com.lidynast.customdialog.dialog.effects.BaseEffects;
import com.lidynast.customdialog.dialog.effects.Effectstype;
/**
 * 关于界面Fragment类，继承BaseFragment
 * @author LHH
 *
 */

public class AboutFragment extends BaseFragment {
	@SuppressWarnings("unused")
	private FragmentActivity mContext;
	private View mBaseView;
	private TitleBarView mTitleBarView;
	private LinearLayout about;
	private List<Effectstype> myeffect;
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();

		mBaseView = inflater.inflate(R.layout.fragment_about, null);
		mTitleBarView=(TitleBarView) mBaseView.findViewById(R.id.title_bar);	
		mTitleBarView.setCommonTitle(View.GONE, View.VISIBLE, View.GONE,
				View.GONE);


		mTitleBarView.setTitleText(R.string.about);
		about=(LinearLayout) mBaseView.findViewById(R.id.parentPanel);
		myeffect=new ArrayList<Effectstype>();
		myeffect.add(Effectstype.Fall);
		myeffect.add(Effectstype.Fliph);
		myeffect.add(Effectstype.Flipv);
		myeffect.add(Effectstype.Newspager);
		myeffect.add(Effectstype.RotateBottom);
		myeffect.add(Effectstype.RotateLeft);
		myeffect.add(Effectstype.Shake);
		myeffect.add(Effectstype.Sidefill);
		myeffect.add(Effectstype.SlideBottom);
		myeffect.add(Effectstype.Slideleft);
		myeffect.add(Effectstype.Slideright);
		myeffect.add(Effectstype.Slidetop);
		myeffect.add(Effectstype.Slit);
		BaseEffects animator = myeffect.get((int)(Math.random()*(myeffect.size()-1))).getAnimator();	  
		animator.setDuration(Math.abs(1000));
		animator.start(about);
		return mBaseView;
	}

}
