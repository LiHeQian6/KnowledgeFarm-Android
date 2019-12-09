package com.li.knowledgefarm.Main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatImageView;


public class DrawView extends AppCompatImageView {

	private float moveX;
	private float moveY;

	public DrawView(Context context) {
		super(context);
	}

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				moveX = event.getX();
				moveY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				setTranslationX(getX() + (event.getX() - moveX));
				setTranslationY(getY() + (event.getY() - moveY));
				break;
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_CANCEL:
				break;
		}

		return true;
	}
}