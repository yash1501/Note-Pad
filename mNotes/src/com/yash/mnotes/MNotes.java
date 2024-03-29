package com.yash.mnotes;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import com.yash.mnotes.dbmanager.Note;
import com.yash.mnotes.dbmanager.NotesManager;

public class MNotes extends Activity {

	NotesManager notesManager = null;
	public List selectedItems = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		selectedItems = new ArrayList();
		notesManager = NotesManager.getInstance(getApplicationContext());

		ListView listView = (ListView) findViewById(R.id.notesList);

		// notes = notesManager.getAvailableNotes();

		updateListViewFromDB(listView, notesManager.getAvailableNotes());

		NotesOnClickListener notesOnClickListener = new NotesOnClickListener(
				this);

		listView.setOnItemClickListener(notesOnClickListener);
		listView.setSelector(android.R.color.transparent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.createMenu:
			SharedPreferences sharedPreferences = getSharedPreferences(
					NotesManager.NOTE_ID, Context.MODE_PRIVATE
							| Context.MODE_WORLD_WRITEABLE);
			sharedPreferences.edit().clear();
			Editor editor = sharedPreferences.edit();
			editor.putString(NotesManager.NOTE_ID, notesManager.createNote()
					.getNoteId());
			editor.commit();
			startActivityForResult(new Intent("com.yash.mnote.NoteDetail"), 0);
			break;
		case R.id.deleteMenu:
			if (selectedItems.size() != 0) {
				for (int i = 0; i < selectedItems.size(); i++) {
					notesManager.deleteNote(((Note) selectedItems.get(i)));
				}
				ListView listView = (ListView) findViewById(R.id.notesList);
				// notes = notesManager.getAllNotesFromDB();
				updateListViewFromDB(listView, notesManager.getAllNotesFromDB());
			}
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ListView listView = (ListView) findViewById(R.id.notesList);
		updateListViewFromDB(listView, notesManager.getAllNotesFromDB());
	}

	private void updateListViewFromDB(ListView listView, List<Note> notes) {

		NotesAdapter notesAdapter = new NotesAdapter(this, R.layout.noteslist,
				notes);
		for (int i = 0; i < notes.size(); i++) {
			Note note = notes.get(i);
			notesAdapter.add(note.getNoteTitle());
		}
		listView.setAdapter(notesAdapter);
		listView.invalidate();
	}

	@Override
	protected void onStop() {
		super.onStop();
		NotesManager.closeDB();
	}

	@Override
	protected void onResume() {
		super.onResume();
		notesManager = NotesManager.getInstance(getApplicationContext());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			NotesManager.closeDB();
		}
		return super.onKeyDown(keyCode, event);
	}

}