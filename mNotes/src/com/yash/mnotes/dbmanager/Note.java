package com.yash.mnotes.dbmanager;

public class Note {

	private String noteId;
	private String noteTitle = "Sample Title";
	private String noteData;
	private boolean isTemp = false;

	public Note() {
	}

	public void setNoteData(String noteData) {
		this.noteData = noteData;
	}

	public String getNoteData() {
		return noteData;
	}

	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}

	public String getNoteTitle() {
		return noteTitle;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	public String getNoteId() {
		return noteId;
	}

	public void setTemporary(boolean isTemp) {
		this.isTemp = isTemp;
	}

	public boolean isTemporary() {
		return isTemp;
	}

}
