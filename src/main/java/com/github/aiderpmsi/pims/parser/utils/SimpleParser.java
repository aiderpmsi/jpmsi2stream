package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;

import com.github.aiderpmsi.pims.parser.linestypes.LineBuilder;
import com.github.aiderpmsi.pims.parser.linestypes.LineConfDictionary;
import com.github.aiderpmsi.pims.parser.utils.Utils.ErrorHandler;
import com.github.aiderpmsi.pims.parser.utils.Utils.LineHandler;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowser;
import com.github.aiderpmsi.pims.treebrowser.TreeBrowserException;
import com.github.aiderpmsi.pims.treemodel.Node;

public class SimpleParser {

	private final Node<?> tree;

	private final String type;
	
	private final LineConfDictionary dico;

	private final Collection<LineHandler> lineHandlers;
	
	private final ErrorHandler errorHandler;
	
	public SimpleParser(
			final Node<?> tree,
			final LineConfDictionary dico,
			final String type,
			final Collection<LineHandler> lineHandlers,
			final ErrorHandler errorHandler) throws TreeBrowserException {
		this.tree = tree;
		this.type = type;
		this.dico = dico;
		this.lineHandlers = lineHandlers;
		this.errorHandler = errorHandler;
	}

	public void parse(final Reader reader) throws IOException {
		// CREATES THE MEMORYBUFFERED READER (KEEPS IN MEMORY THE LAST READING LINE AND CONSUMES ONLY EXPLICITELY CHARACTERS)
		final MemoryBufferedReader mbr = new MemoryBufferedReader(reader);

		final HashMap<String, Object> context = new HashMap<>(4);
		context.put("lb", new LineBuilder(dico));
		context.put("br", mbr);
		context.put("utils", new Utils(mbr, lineHandlers, errorHandler));
		context.put("start", type);
	
		// CREATES AND EXECUTES THE TREE BROWSER
		final TreeBrowser tb = new TreeBrowser(tree, context);

		try {
			tb.go();
		} catch (Exception e) {
			// BE SURE TO CATCH EVERY EXCEPTION FROM MACHINE
			throw new IOException(e);
		}
	}

	public String getType() {
		return type;
	}

}


