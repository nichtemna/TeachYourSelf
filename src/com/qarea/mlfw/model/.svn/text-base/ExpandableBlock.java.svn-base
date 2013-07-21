package com.qarea.mlfw.model;

import android.view.View;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.qarea.mlfw.interfaces.Expandable;

public class ExpandableBlock extends Block implements Expandable {
	private static final long serialVersionUID = 1L;
	private Block block;

	public ExpandableBlock(TextView textView, int id) {
		super(textView, id);
	}

	@Override
	public void expand(Block block) {
		this.block = block;
		move(block.getX() - block.getWidth() / 2,
				block.getY() - block.getHeight());
		scale(ViewHelper.getScaleX(getTextView()) / 10,
				ViewHelper.getScaleY(getTextView()) / 10);

		setText(block.getText());
		getTextView().setVisibility(View.VISIBLE);

		ObjectAnimator.ofFloat(getTextView(), "scaleX", 1).setDuration(500)
				.start();
		ObjectAnimator.ofFloat(getTextView(), "scaleY", 1).setDuration(500)
				.start();

		ObjectAnimator.ofFloat(getTextView(), "translationX", getX())
				.setDuration(500).start();
		ObjectAnimator.ofFloat(getTextView(), "translationY", getY())
				.setDuration(500).start();
	}

	@Override
	public void hide() {
		ObjectAnimator.ofFloat(getTextView(), "scaleX", 1 / 10)
				.setDuration(500).start();
		ObjectAnimator.ofFloat(getTextView(), "scaleY", 1 / 10)
				.setDuration(500).start();
		ObjectAnimator
				.ofFloat(getTextView(), "translationX",
						block.getX() - block.getWidth() / 2).setDuration(500)
				.start();
		ObjectAnimator
				.ofFloat(getTextView(), "translationY",
						block.getY() - block.getHeight()).setDuration(500)
				.start();
	}

}
