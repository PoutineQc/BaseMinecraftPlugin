package ca.poutineqc.base.data.values;

import org.bukkit.Note;
import org.bukkit.Note.Tone;

import com.google.gson.JsonObject;

import ca.poutineqc.base.data.JSONSavableValue;

public class SNote implements JSONSavableValue {

	private static final String OCTAVE = "octave";
	private static final String TONE = "tone";
	private static final String SHARPED = "sharped";

	private int octave;
	private Tone tone;
	private boolean sharped;

	public SNote(int octave, Tone tone, boolean sharped) {
		this.octave = octave;
		this.tone = tone;
		this.sharped = sharped;
	}

	public SNote(Note note) {
		this.octave = note.getOctave();
		this.tone = note.getTone();
		this.sharped = note.isSharped();
	}

	public SNote(JsonObject json) {
		this.octave = json.get(OCTAVE).getAsInt();

		String toneName = json.get(TONE).getAsString();
		for (Tone tone : Tone.values())
			if (tone.name().equals(toneName))
				this.tone = tone;

		this.sharped = json.get(SHARPED).getAsBoolean();
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.addProperty(OCTAVE, octave);
		json.addProperty(TONE, tone.name());
		json.addProperty(SHARPED, sharped);
		return json;
	}

	public Note getNote() {
		return new Note(octave, tone, sharped);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SNote))
			return false;

		SNote note = (SNote) o;

		if (note.octave != this.octave)
			return false;

		if (!note.tone.equals(this.octave))
			return false;

		if (note.sharped ^ this.sharped)
			return false;
		
		return false;
	}

	public int getOctave() {
		return octave;
	}

	public void setOctave(int octave) {
		this.octave = octave;
	}

	public Tone getTone() {
		return tone;
	}

	public void setTone(Tone tone) {
		this.tone = tone;
	}

	public boolean isSharped() {
		return sharped;
	}

	public void setSharped(boolean sharped) {
		this.sharped = sharped;
	}
	
	

}
