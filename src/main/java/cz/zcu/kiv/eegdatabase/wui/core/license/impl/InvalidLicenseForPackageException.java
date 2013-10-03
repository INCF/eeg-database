/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.wui.core.license.impl;

/**
 *
 * @author bydga
 */
public class InvalidLicenseForPackageException extends RuntimeException {

	public InvalidLicenseForPackageException() {
	}

	public InvalidLicenseForPackageException(String message) {
		super(message);
	}

	public InvalidLicenseForPackageException(Throwable cause) {
		super(cause);
	}

	public InvalidLicenseForPackageException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}
