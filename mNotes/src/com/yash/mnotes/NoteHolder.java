package com.yash.mnotes;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoteHolder extends LinearLayout {

	TextView noteTitle;

	public NoteHolder(Context context) {
		super(context);
		setOrientation(VERTICAL);
		noteTitle = new TextView(context);
		addView(noteTitle);
	}

	public void setNoteTitle(String title) {
		noteTitle.setText(title);
	}

}
