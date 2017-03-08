package ca.poutineqc.base.data.values;

import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.block.NoteBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import com.google.gson.JsonObject;

public class SItemNoteBlock extends SItem {

	private static final String NOTE = "note";
	
	SNote note;
	
	public SItemNoteBlock(Note note) {
		super(Material.NOTE_BLOCK);
		this.note = new SNote(note);
	}
	
	public SItemNoteBlock(int octave, Tone tone, boolean sharped) {
		super(Material.NOTE_BLOCK);
		this.note = new SNote(octave, tone, sharped);
	}

	public SItemNoteBlock(ItemStack itemStack) {
		super(itemStack);
		BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
		NoteBlock state = (NoteBlock) meta.getBlockState();
		
		this.note = new SNote(state.getNote());
	}

	public SItemNoteBlock(JsonObject json) {
		super(json);
		this.note = new SNote(json.get(NOTE).getAsJsonObject());
	}
	
	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();
		json.add(NOTE, note.toJsonObject());
		return json;
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
		NoteBlock state = (NoteBlock) meta.getBlockState();
		
		state.setNote(note.getNote());
		
		meta.setBlockState(state);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemNoteBlock))
			return false;

		SItemNoteBlock item = (SItemNoteBlock) o;
		
		if (!item.note.equals(this.note))
			return false;
		
		return super.equals(o);
	}
	
	public void setNote(Note note) {
		this.note = new SNote(note);
	}
	
	public Note getNote() {
		return note.getNote();
	}

}
