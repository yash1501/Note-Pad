package com.yash.mnotes.dbmanager;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesManager implements iNoteManager {

	public static final String NOTE_ID = "note_id";
	private static int NOTES_DB_VERSION = 1;
	private static String NOTES_DB_NAME = "noteDB";
	private static String NOTES_DB_TABLE_NAME = "Notes";

	private final DBOpenHelper dbOpenHelper;
	private SQLiteDatabase noteDb;

	private List<Note> availableNotes = null;

	private static NotesManager INSTANCE = null;

	private static class DBOpenHelper extends SQLiteOpenHelper {

		private static final String DB_CREATE = "CREATE TABLE "
				+ NotesManager.NOTES_DB_TABLE_NAME
				+ " (id INTEGER PRIMARY KEY, title TEXT , noteData TEXT);";

		public DBOpenHelper(Context context, String dbName, int version) {
			super(context, NOTES_DB_NAME, null, NOTES_DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DBOpenHelper.DB_CREATE);
			} catch (Exception e) {
				Log.e("ERROR", e.getMessage());
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			db.execSQL("DROP TABLE IF EXISTS" + NOTES_DB_TABLE_NAME);
			onCreate(db);
		}
	}

	protected NotesManager(Context context) {
		dbOpenHelper = new DBOpenHelper(context, NOTES_DB_NAME,
				NOTES_DB_VERSION);
		establishDB();
		initNotesFromDB();
	}

	public static NotesManager getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new NotesManager(context);
		}
		return INSTANCE;
	}

	public static void closeDB() {
		if (INSTANCE != null) {
			INSTANCE.dbOpenHelper.close();
			INSTANCE = null;
		}
	}

	public void initNotesFromDB() {
		availableNotes = getAllNotesFromDB();
	}

	private void establishDB() {
		if (noteDb == null) {
			noteDb = dbOpenHelper.getWritableDatabase();
		} else if (!noteDb.isOpen()) {
			noteDb = dbOpenHelper.getWritableDatabase();
		}
	}

	public Note createNote() {
		Note newNote = new Note();
		newNote.setNoteId(availableNotes.size() + "");
		newNote.setTemporary(true);
		availableNotes.add(newNote);
		return newNote;
	}

	@Override
	public boolean saveNote(Note note) {
		// noteDB
		Note searchNote = searchNote(note.getNoteId());
		if (searchNote == null) {
			insertNoteIntoDB(note);
			availableNotes.add(note);
		} else {
			updateNote(note);
		}
		return false;
	}

	public boolean insertNoteIntoDB(Note note) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", note.getNoteId());
		contentValues.put("title", note.getNoteTitle());
		contentValues.put("noteData", note.getNoteData());
		noteDb.insert(NOTES_DB_TABLE_NAME, null, contentValues);
		return true;
	}

	@Override
	public boolean deleteNote(Note note) {
		int update = noteDb.delete(NOTES_DB_TABLE_NAME,
				"id=" + note.getNoteId(), null);
		System.out.println("No of rows afftected : " + update);
		return false;
	}

	@Override
	public boolean updateNote(Note note) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", note.getNoteId());
		contentValues.put("title", note.getNoteTitle());
		contentValues.put("noteData", note.getNoteData());
		int update = noteDb.update(NOTES_DB_TABLE_NAME, contentValues, "id="
				+ note.getNoteId(), null);
		System.out.println("No of rows afftected : " + update);
		return false;
	}

	@Override
	public Note searchNote(String noteId) {
		for (int i = 0; i < availableNotes.size(); i++) {
			Note note = availableNotes.get(i);
			if (note.getNoteId().equalsIgnoreCase(noteId)) {
				return note;
			}
		}
		return null;
	}

	public List<Note> getAllNotesFromDB() {
		availableNotes = new ArrayList<Note>();
		establishDB();
		Cursor resultSet = noteDb.rawQuery("SELECT * FROM "
				+ NOTES_DB_TABLE_NAME + ";", null);

		System.out.println(resultSet.getCount());
		if (resultSet.getCount() > 0) {
			resultSet.moveToFirst();
			do {
				Note newNote = new Note();
				newNote.setNoteId(resultSet.getString(0));
				newNote.setNoteTitle(resultSet.getString(1));
				newNote.setNoteData(resultSet.getString(2));
				availableNotes.add(newNote);
			} while (resultSet.moveToNext());
		}
		return availableNotes;
	}

	public List<Note> getAvailableNotes() {
		return availableNotes;
	}

	public void deleteTemporaryNote() {
		if (availableNotes.get(availableNotes.size() - 1).isTemporary()) {
			availableNotes.remove(availableNotes.size() - 1);
		}
	}

}
