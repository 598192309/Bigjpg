package com.bigjpg.exception;
/**
 * ChoosePictureException
 */
public class ChoosePictureException extends Exception {

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getMessage() {
		return "Choose picture from original gallery cause exception!";
	}

}
