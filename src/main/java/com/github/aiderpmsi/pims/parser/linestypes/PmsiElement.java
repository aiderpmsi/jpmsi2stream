package com.github.aiderpmsi.pims.parser.linestypes;

import javax.swing.text.Segment;

public interface PmsiElement {

	public String getName();

	public Segment getContent();

	public int getSize();

	public boolean parse(Segment segt);

}
