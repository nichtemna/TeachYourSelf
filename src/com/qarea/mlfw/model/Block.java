package com.qarea.mlfw.model;

import java.io.Serializable;

import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class Block implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int MARGIN = 10;

	private int id;
	private TextView textView;
	private int x;
	private int y;
	private int height;
	private int width;
	private boolean visible = false;
	float startMoveX, startMoveY;

	public Block(TextView textView, int id) {
		this.textView = textView;
		this.id = id;
	}

	public static int getMargin() {
		return MARGIN;
	}

	public void setLayoutParams(int width, int height) {
		textView.setLayoutParams(new LinearLayout.LayoutParams(width - MARGIN,
				height));
		setMargins();
	}

	private void setMargins() {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView
				.getLayoutParams();
		params.setMargins(MARGIN, 0, MARGIN, 0);
		textView.setLayoutParams(params);
	}

	public void move(float moveX, float moveY) {
		ViewHelper.setX(textView, moveX);
		ViewHelper.setY(textView, moveY);
	}

	public void scale(float scaleX, float scaleY) {
		ViewHelper.setScaleX(textView, scaleX);
		ViewHelper.setScaleY(textView, scaleY);
	}

	public void animateScaling(float scaleX, float scaleY) {
		ObjectAnimator.ofFloat(textView, "scaleX", 1).setDuration(500).start();
		ObjectAnimator.ofFloat(textView, "scaleY", 1).setDuration(500).start();
	}

	public void animateMoving(float moveX, float moveY) {
		ObjectAnimator.ofFloat(textView, "translationX", moveX)
				.setDuration(500).start();
		ObjectAnimator.ofFloat(textView, "translationY", moveY)
				.setDuration(500).start();
	}

	public TextView getTextView() {
		return textView;
	}

	public void setTextView(TextView textView) {
		this.textView = textView;
	}

	public void setLocation() {
		int[] location = new int[2];
		textView.getLocationOnScreen(location);
		this.x = location[0];
		this.y = location[1];
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		Log.d("tag", id + " x " + x);
		return x;
	}

	public int getY() {
		Log.d("tag", id + " y " + y);
		return y;
	}

	public int getHeight() {
		return height;
	}

	public void setWidthHeight(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeightWidth() {
		this.width = textView.getWidth();
		this.height = textView.getHeight();
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getText() {
		return textView.getText().toString();
	}

	public void setText(String text) {
		this.textView.setText(text);
	}

	public void setBackgroundColor(int color) {
		((GradientDrawable) textView.getBackground()).setColor(color);
	}

	public int getId() {
		return id;
	}

	public void getStartTouchPos(MotionEvent event) {
		this.startMoveX = event.getRawX();
		this.startMoveY = event.getRawY();
	}

	public boolean sameEventCoordinates(MotionEvent event) {
		return (startMoveX == event.getRawX() && startMoveY == event.getRawY());
	}

	public float getStartMoveX() {
		return startMoveX;
	}

	public float getStartMoveY() {
		return startMoveY;
	}

}
