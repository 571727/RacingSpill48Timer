package file_manipulation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public abstract class Settings {

	protected File file;
	protected List<String> lines;

	/**
	 * @param filename without the type
	 * @return true if file did not exist from before
	 */
	protected boolean init(String filename) {
		boolean res = false;
		file = new File(filename);

		try {
			if (!file.isFile()) {
				if (file.createNewFile()) {
					res = true;
					PrintWriter pw = new PrintWriter(file);
					pw.flush();
					pw.close();
				}
			}
			readSettingsLines();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}

	private void readSettingsLines() throws IOException {
		lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
	}

	/**
	 * overrides the previous line, but be vary of wrong linenr order
	 */
	protected void writeToLine(String line, int linenr) {

		while (linenr >= lines.size()) {
			lines.add("null");
		}

		lines.set(linenr, line);

		try {
			Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
			readSettingsLines();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected long getSettingLong(int linenr) {
		return Long.valueOf(getSetting(linenr));
	}
	
	protected double getSettingDouble(int linenr) {
		return Double.valueOf(getSetting(linenr));
	}

	protected int getSettingInteger(int linenr) {
		return Integer.valueOf(getSetting(linenr));
	}

	protected boolean getSettingBoolean(int linenr) {
		return Integer.valueOf(getSetting(linenr)) == 1;
	}

	protected String getSetting(int linenr) {
		String res = null;

		try {
			if (linenr < lines.size())
				res = lines.get(linenr).split("=")[1];
		} catch (Exception e) {
			ErrorLogger.err("Setting line " + linenr + " not found!!!\n" + e.getMessage());
			e.printStackTrace();
		}

		return res;
	}
}
