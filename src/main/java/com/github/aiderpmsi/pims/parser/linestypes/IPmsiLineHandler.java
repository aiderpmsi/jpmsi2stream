package com.github.aiderpmsi.pims.parser.linestypes;

@FunctionalInterface
public interface IPmsiLineHandler {

	public void handle(IPmsiLine line);

}
