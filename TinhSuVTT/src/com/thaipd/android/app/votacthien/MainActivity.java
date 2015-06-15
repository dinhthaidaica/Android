package com.thaipd.android.app.votacthien;

import com.xolem.android.app.tinhsuvtt.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends BaseActivity implements OnClickListener {
	Button btn_Start;
	Button btn_Continue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btn_Start = (Button) findViewById(R.id.btn_Menu);
		btn_Continue = (Button) findViewById(R.id.btn_Continue);

		btn_Start.setOnClickListener(this);
		btn_Continue.setOnClickListener(this);
		

		
	}
	
	@Override
	protected void onResume() {
		currentIndex = getPrefInt(ReadingPageActivity.DATA_PAGE);
		
		if (currentIndex != -1) {
			btn_Start.setText(R.string.str_menu);
			btn_Continue.setText(R.string.str_continue);
			btn_Continue.setVisibility(View.VISIBLE);
		} else {
			btn_Start.setText(R.string.str_start);
			btn_Continue.setText(R.string.str_continue);
			btn_Continue.setVisibility(View.GONE);
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_Continue:
			Intent i = new Intent(this, ReadingPageActivity.class);
			i.putExtra(AppUtils.INTENT_POSITION, getPrefInt(ReadingPageActivity.DATA_PAGE));
			startActivity(i);
			break;
		case R.id.btn_Menu:
			Intent in = new Intent(this, ChapterListActivity.class);
			startActivity(in);
			break;

		default:
			break;
		}

	}
}
