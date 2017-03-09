package ca.poutineqc.base.datastorage.serializable.sitems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class SItemBook extends SItem {

	private static final String PAGES = "pages";
	private static final String AUTHOR = "author";
	private static final String TITLE = "title";
	private static final String GENERATION = "generation";

	private List<String> pages;
	private String author;
	private String title;
	private Generation generation;

	public SItemBook(boolean finished, String title, String author, Generation generation) {
		super(finished ? Material.WRITTEN_BOOK : Material.BOOK_AND_QUILL);

		this.pages = new ArrayList<String>(Arrays.asList(""));
		this.title = title.substring(0, 16);
		this.author = author;
		this.generation = generation;
	}

	public SItemBook(ItemStack itemStack) {
		super(itemStack);

		if (!(itemStack.getItemMeta() instanceof BookMeta))
			return;

		BookMeta meta = (BookMeta) itemStack.getItemMeta();
		this.pages = new ArrayList<String>(meta.getPages());

		this.author = meta.getAuthor();
		this.title = meta.getTitle();
		this.generation = meta.getGeneration();
	}

	public SItemBook(JsonObject json) {
		super(json);

		this.pages = new ArrayList<String>(Arrays.asList(""));
		for (JsonElement page : json.get(PAGES).getAsJsonArray())
			this.append(page.getAsString());
		
		if (!json.get(AUTHOR).isJsonNull())
			this.author = json.get(AUTHOR).getAsString();

		if (!json.get(TITLE).isJsonNull())
			this.title = json.get(TITLE).getAsString();

		if (!json.get(GENERATION).isJsonNull()) {
			String genName = json.get(GENERATION).getAsString();
			for (Generation generation : Generation.values())
				if (generation.name().equals(genName))
					this.generation = generation;
		}

	}

	public void append(String page) {
		if (pages.size() == 1)
			if (pages.get(0).equals(""))
				pages.remove(0);

		pages.add(page);
	}

	public void clear() {
		this.pages = new ArrayList<String>(Arrays.asList(""));
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = super.toJsonObject();

		JsonArray pages = new JsonArray();
		for (String page : this.pages)
			pages.add(new JsonPrimitive(page));

		json.add(PAGES, pages);
		json.addProperty(AUTHOR, author);
		json.addProperty(TITLE, title);
		json.addProperty(GENERATION, generation == null ? null : generation.name());

		return json;
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();

		BookMeta meta = (BookMeta) item.getItemMeta();
		meta.setAuthor(author);
		meta.setPages(pages);
		meta.setGeneration(generation);
		meta.setTitle(title);
		item.setItemMeta(meta);
		
		return item;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SItemBook))
			return false;

		SItemBook book = (SItemBook) o;

		if (this.pages.size() != book.pages.size())
			return false;

		for (int i = 0; i < pages.size(); i++)
			if (!this.pages.get(i).equals(book.pages.get(i)))
				return false;

		if (this.author != book.author)
			return false;

		if (this.title != book.title)
			return false;

		if (this.generation != book.generation)
			return false;

		return super.equals(o);
	}

}
