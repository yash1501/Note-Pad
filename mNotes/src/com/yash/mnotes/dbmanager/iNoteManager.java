package com.yash.mnotes.dbmanager;

public interface iNoteManager {

	public boolean saveNote(Note note);

	public boolean deleteNote(Note note);

	public boolean updateNote(Note note);

	public Note searchNote(String noteId);
}
