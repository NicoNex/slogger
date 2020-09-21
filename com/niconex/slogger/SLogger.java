/*
 * SLogger - Generates beautiful logs enclosed in boxes for humans.
 * Copyright (C) 2020  Nicolò Santamaria
 *
 * SLogger is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * SLogger is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with SLogger; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

package com.niconex.slogger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Simple logger.
public class SLogger {
	private static final int DATE_LEN = 19;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	private List<String> lines = new ArrayList<>();
	private int width = 0;
	private String name;

	private SLogger(String name) {
		this.name = name;
		// Add 3 because we have to consider the '-' character that gets added
		// in addition and the 2 spaces that surround it.
		this.width = name.length() + DATE_LEN + 3;
	}

	private SLogger() {
		this.width = DATE_LEN;
	}

	// Returns a string containing the current date.
	private static String now() {
		return sdf.format(new Date());
	}

	// Used to begin with the logging instructions, it will insert in the box
	// title the name provided in input alongside with the current date.
	public static SLogger begin(String name) {
		return new SLogger(name);
	}

	// Used to begin with the loggin instructions, it will only insert the
	// current date in the box title.
	public static SLogger begin() {
		return new SLogger();
	}

	// Adds a key-value field in the box.
	public SLogger field(String key, String val) {
		String l = String.format("%s: %s", key, val);
		int len = l.length();
		if (len > width) {
			width = len;
		}
        lines.add(l);
        return this;
	}

	// Adds a simple line in the box.
	public SLogger line(String s) {
		int len = s.length();
		if (len > width) {
			width = len;
		}
		lines.add(s);
		return this;
	}

	// Adds a new line in the box.
	public SLogger newLine() {
		lines.add("");
		return this;
	}

	// Returns the the box top and bottom lines without the corners.
	private String hline() {
		String ret = "";
		for (int i = 0; i < width + 2; i++) {
			ret += "─";
		}
		return ret;
	}

	// Returns the remaining part of each line with the '│' character in the end.
	private String genTail(int len) {
		String buf = "";

		for (int i = 0; i < len-1; i++) {
			buf += " ";
		}
		buf += "│";
		return buf;
	}

	// Returns a single box line.
	private String genLine(String cnt) {
		return String.format("│ %s%s", cnt, genTail(width - cnt.length() + 2));
	}

	// Returns a list of lines that compose the box.
	private List<String> genBox() {
		List<String> ret = new ArrayList<>();
		String h = hline();
		String title = name != null ? String.format("%s - %s", now(), name) : now();

		ret.add(String.format("┌%s┐", h));
		ret.add(genLine(title));
		ret.add(String.format("├%s┤", h));
		lines.forEach(l -> ret.add(genLine(l)));
		ret.add(String.format("└%s┘", h));
		return ret;
	}

	// Returns the string containing the box.
	private String digest() {
		return String.join("\n", genBox());
	}

	// Prints the box using System.out.println.
	public void print() {
		System.out.println(digest());
	}

	// Logs the box using log4j and the class passed in input.
	public void log(Class<?> clazz) {
		Logger logger = LogManager.getLogger(clazz);
		genBox().forEach(l -> logger.info(l));
	}

	@Override
	public String toString() {
		return digest();
	}
}
