package com.example.emojidemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import com.emoji.view.EmojiView;
import com.emoji.view.EmojiView.OnClickSendBtnListener;

public class MainActivity extends Activity {

	private TextView mOutputText = null;
	private EmojiView mEmojiView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mOutputText = (TextView) findViewById(R.id.outputText);
		mEmojiView = (EmojiView) findViewById(R.id.emojiView);
		mEmojiView.setOnClickSendBtnListener(new OnClickSendBtnListener() {
			@Override
			public void send(Editable editable) {
				mOutputText.setText(editable);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		mEmojiView.onActivityResult(requestCode, resultCode, intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mEmojiView.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mEmojiView.onDestroy();
	}

	public void showKeyboard(View v) {
		mEmojiView.showKeyboard();
	}

	public void clearDiskCache(View v) {
		mEmojiView.clearDiskCache();
	}

}
