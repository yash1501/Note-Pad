package com.yash.mnotes;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.yash.mnotes.dbmanager.Note;

public class NotesAdapter extends ArrayAdapter {

	Context context;
	int resourceId;
	List<Note> notes;

	public NotesAdapter(Context context, int resourceId, List<Note> notes) {
		super(context, resourceId);
		this.resourceId = resourceId;
		this.context = context;
		this.notes = notes;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(resourceId, parent, false);
		row.setTag(notes.get(position));
		TextView textView = (TextView) row.findViewById(R.id.listItem);
		Note note = notes.get(position);
		textView.setText(note.getNoteTitle());
		CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkBox);
		checkBox.setFocusable(false);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				System.out.println(isChecked + "");
				Note currentnote = notes.get(position);
				if (isChecked) {
					((MNotes) NotesAdapter.this.context).selectedItems
							.add(currentnote);
				} else {
					((MNotes) NotesAdapter.this.context).selectedItems
							.remove(currentnote);
				}
			}
		});
		return row;
	}
}
