package me.graindcafe.gls;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;
import org.bukkit.util.config.Configuration;

public class DefaultLanguage extends Language {

	/**
	 * Your language version, use it as you want.
	 */
	public static byte version = 1;
	/**
	 * You
	 */
	private static String author = "Graindcafe";
	/**
	 * The default language folder. DON'T FORGET THE LAST SLASH
	 */
	protected static String languagesFolder = "plugins/";
	/**
	 * This language name
	 */
	protected static String languageName = "english";
	/**
	 * The default sentences
	 */
	private static HashMap<String, String> Strings = new HashMap<String, String>() {
		private static final long serialVersionUID = 7261634788355469790L;

		{
			put("File.TheDefaultLanguageFile",
					"# This is the plugin default language file \n# You should not edit it ! All changes will be undone !\n# Create another language file (custom.yml) \n# and put 'Default: english' if your default language is english\n");
			put("File.DefaultLanguageFile",
					"# This is your default language file \n# You should not edit it !\n# Create another language file (custom.yml) \n# and put 'Default: english' if your default language is english\n");
			put("File.LanguageFileComplete",
					"# Your language file is complete\n");
			put("File.TranslationsToDo",
					"# Translations to do in this language file\n");
			put("Info.ChosenLanguage", "Chosen language : %s. Provided by %s.");
			put("Warning.LanguageFileMissing",
					"The specified language file is missing!");
			put("Warning.LanguageOutdated", "Your language file is outdated!");
		}
	};

	/**
	 * Check if a language contains all sentences that the Default has. If not,
	 * it add the missing ones in head of the language file
	 * 
	 * @param l
	 *            Language to check
	 * @return if the language was complete
	 */
	public static boolean checkLanguage(Language l) {
		if (l == null)
			return false;
		while (!l.isDefault())
			l = l.getDefault();
		boolean retour = true;
		Configuration lFile = l.getFile();
		String value;
		Stack<String> todo = new Stack<String>();
		if (lFile == null)
			return false;
		for (String key : Strings.keySet()) {
			value = lFile.getString(key, null);
			if (value == null) {
				todo.push(key);
			}
		}
		String header = l.get("File.DefaultLanguageFile");
		if (lFile.getInt("Version", 0) != DefaultLanguage.version) {
			retour = false;
			header += "# " + Strings.get("Warning.LanguageFileOutdated") + "\n";
		}
		if (todo.isEmpty())
			header += l.get("File.LanguageFileComplete");
		else {
			retour = false;
			header += l.get("File.TranslationsToDo");
		}
		while (!todo.isEmpty())
			header += "# - " + todo.peek() + ": " + Strings.get(todo.pop())
					+ "\n";
		lFile.setHeader(header);
		lFile.save();
		return retour;
	}

	/**
	 * Save the default language to a YAML file
	 * 
	 * @param fileName
	 *            the name of this language
	 */
	public static void saveDefaultLanguage(String fileName) {
		File dir = new File(DefaultLanguage.languagesFolder);
		if (!dir.exists()) {
			String[] languageFolders = DefaultLanguage.languagesFolder
					.split("/");
			String tmplevelFolder = "";
			for (byte i = 0; i < languageFolders.length; i++) {
				tmplevelFolder = tmplevelFolder.concat(languageFolders[i]
						+ java.io.File.separatorChar);
				dir = new File(tmplevelFolder);
				dir.mkdir();
			}
		}
		if (!fileName.substring(fileName.length() - 3).equalsIgnoreCase("yml"))
			fileName += ".yml";
		Configuration f = new Configuration(new File(
				DefaultLanguage.languagesFolder + fileName.toLowerCase()));
		f.setProperty("Author", DefaultLanguage.author);
		f.setProperty("Version", DefaultLanguage.version);
		for (Entry<String, String> e : Strings.entrySet()) {
			f.setProperty(e.getKey(), e.getValue());
		}

		f.setHeader("# This is the plugin default language file \n# You should not edit it ! All changes will be undone !\n# Create another language file (custom.yml) \n# and put 'Default: english' if your default language is english\n");
		f.save();
	}

	/**
	 * Set the author of the default language
	 * 
	 * @param name
	 *            : his name
	 */
	public static void setAuthor(String name) {
		author = name;
	}

	/**
	 * Set the default language.
	 * 
	 * @param locales
	 *            HashMap<String,String>
	 */
	public static void setLocales(HashMap<String, String> locales) {
		if (locales.containsKey("File.TheDefaultLanguageFile"))
			locales.put(
					"File.TheDefaultLanguageFile",
					"# This is the plugin default language file \n# You should not edit it ! All changes will be undone !\n# Create another language file (custom.yml) \n# and put 'Default: english' if your default language is english\n");
		if (locales.containsKey("File.DefaultLanguageFile"))
			locales.put(
					"File.DefaultLanguageFile",
					"# This is your default language file \n# You should not edit it !\n# Create another language file (custom.yml) \n# and put 'Default: english' if your default language is english\n");
		if (locales.containsKey("File.TheDefaultLanguageFile"))
			locales.put("File.TranslationsToDo",
					"# Your language file is complete\n");
		if (locales.containsKey("File.TheDefaultLanguageFile"))
			locales.put("File.TranslationsToDo",
					"# Translations to do in this language file\n");
		if (locales.containsKey("Info.ChosenLanguage"))
			locales.put("Info.ChosenLanguage",
					"Chosen language : %s. Provided by %s.");
		if (locales
				.containsKey("Warning.TheDeLanguageFileMissingfaultLanguageFile"))
			locales.put("Warning.LanguageFileMissing",
					"The specified language file is missing!");
		if (locales.containsKey("Warning.LanguageOutdated"))
			locales.put("Warning.LanguageOutdated",
					"Your language file is outdated!");
		Strings = locales;
	}

	/**
	 * Set the name of the default language (mostly "English")
	 * 
	 * @param name
	 *            : the name
	 */
	public static void setName(String name) {
		languageName = name;
	}

	/**
	 * Set the version of your language file
	 * 
	 * @param version
	 */
	public static void setVersion(byte version) {
		DefaultLanguage.version = version;
	}

	/**
	 * Set the language folder, should be
	 * plugin.getDataFolder().getPath()+"/languages/" =
	 * plugins/YOURPLUGIN/languages/
	 * 
	 * @param languagesFolder
	 */
	public static void setLanguagesFolder(String languagesFolder) {
		if (!languagesFolder.endsWith("/"))
			languagesFolder += "/";
		DefaultLanguage.languagesFolder = languagesFolder;
	}

	@Override
	public String get(String key) {
		return Strings.get(key);
	}

	@Override
	public String getAuthor() {
		return DefaultLanguage.author;
	}

	@Override
	public byte getVersion() {
		return DefaultLanguage.version;
	}
}