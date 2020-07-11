package com.sif.enums;

public enum PageLocation {
		
	MAIN("main"),
	BOARD("board"),
	COMMENT("comment");
	
	private final String page_location;
	
	private PageLocation(String page_location) {
		this.page_location = page_location;
	}
	
	public String getPage_location() {
		return this.page_location;
	}
	
}