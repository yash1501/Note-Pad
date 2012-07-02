package com.yash.mnotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.yash.mnotes.dbmanager.Note;
import com.yash.mnotes.dbmanager.NotesManager;

public class NotesOnClickListener implements OnItemClickListener {
	Context context;

	public NotesOnClickListener(Context context) {
		this.context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		System.out.println("one");
		Note note = (Note) view.getTag();
		Intent intent = new Intent("com.yash.mnote.NoteDetail");
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				NotesManager.NOTE_ID, Context.MODE_PRIVATE
						| Context.MODE_WORLD_WRITEABLE);
		sharedPreferences.edit().clear();
		Editor editor = sharedPreferences.edit();
		editor.putString(NotesManager.NOTE_ID, note.getNoteId());
		editor.commit();
		// intent.putExtra(NotesManager.NOTE_ID,
		// NotesManager.getInstance(context.getApplicationContext()).searchNote(noteId));
		((Activity) context).startActivityForResult(intent, 0);
	}
}
