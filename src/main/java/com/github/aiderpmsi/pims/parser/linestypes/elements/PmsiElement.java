package com.github.aiderpmsi.pims.parser.linestypes.elements;

import com.github.aiderpmsi.pims.parser.linestypes.Segment;

public interface PmsiElement {

	public String getName();

	public Segment getContent();

	public void setContent(Segment segment);

	public int getSize();

	public boolean validate();

}
