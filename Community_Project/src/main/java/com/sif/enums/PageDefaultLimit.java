package com.sif.enums;

public enum PageDefaultLimit {
		
	MAIN(5);
	
	private final int limit_value;

	private PageDefaultLimit(int limit_value) {
		this.limit_value = limit_value;
	}

	public int getLimit_value() {
		return this.limit_value;
	}
	
}