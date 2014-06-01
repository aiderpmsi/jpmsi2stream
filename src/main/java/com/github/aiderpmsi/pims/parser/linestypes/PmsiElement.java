package com.github.aiderpmsi.pims.parser.linestypes;

public interface PmsiElement {

	public String getName();

	public Segment getContent();

	public void setContent(Segment segment);

	public int getSize();

	public boolean validate();

}
