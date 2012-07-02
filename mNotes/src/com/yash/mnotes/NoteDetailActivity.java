package com.yash.mnotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;

import com.yash.mnotes.dbmanager.Note;
import com.yash.mnotes.dbmanager.NotesManager;

public class NoteDetailActivity extends Activity {

	private boolean isTextChanged;
	private TextWatcher textWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
			if (((currentNote.getNoteData() == null) && !s.toString()
					.equals(""))
					|| (currentNote.getNoteData() != null && !currentNote
							.getNoteData().equalsIgnoreCase(s.toString()))) {
				isTextChanged = true;
				currentNote.setNoteData(s.toString());
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}
	};

	private String noteId;
	public Note currentNote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notedetail);
		EditText noteEditor = (EditText) findViewById(R.id.noteEditor);
		noteEditor.addTextChangedListener(textWatcher);

		SharedPreferences sharedPreferences = getSharedPreferences(
				NotesManager.NOTE_ID, Context.MODE_PRIVATE
						| Context.MODE_WORLD_WRITEABLE);
		noteId = sharedPreferences.getString(NotesManager.NOTE_ID, null);
		currentNote = NotesManager.getInstance(getApplicationContext())
				.searchNote(noteId);
		if (currentNote != null) {
			noteEditor.setText(currentNote.getNoteData());
		}
		isTextChanged = false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isTextChanged) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setMessage("Save Now?")
						.setCancelable(false)
						.setPositiveButton("Save",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										NotesManager.getInstance(
												getApplicationContext())
												.deleteTemporaryNote();
										NotesManager.getInstance(
												getApplicationContext())
												.saveNote(currentNote);
										NoteDetailActivity.this.setResult(1);
										NoteDetailActivity.this.finish();
									}
								})
						.setNegativeButton("Cancle",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										NotesManager.getInstance(
												getApplicationContext())
												.deleteTemporaryNote();

										NoteDetailActivity.this.setResult(0);
										NoteDetailActivity.this.finish();
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			} else {
				NoteDetailActivity.this.setResult(0);
				NoteDetailActivity.this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
