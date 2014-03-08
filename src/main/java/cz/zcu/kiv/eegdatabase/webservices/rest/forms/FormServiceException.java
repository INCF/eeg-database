/***********************************************************************************************************************
 *
 * This file is part of the EEG-database project
 *
 * =============================================
 *
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * FormServiceException.java, 8. 3. 2014 14:41:22, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.forms;

/**
 * Exception that indicates that the required operation was rejected by the {@link FormService}.
 * 
 * @author Jakub Krauz
 */
public class FormServiceException extends Exception {
	
	/** Generated serial version UID. */
	private static final long serialVersionUID = 3686549252972033330L;


	/**
	 * Enumeration of possible causes of the operation refusal.
	 */
	public enum Cause {
		PERMISSION,
		CONFLICT,
		NOT_FOUND,
		OTHER
	}
	
	
	/** The actual cause. */
	private Cause cause;
	
	
	/**
	 * Creates a new exception with the specified cause of operation refusal.
	 * @param cause - the cause
	 */
	public FormServiceException(Cause cause) {
		this.cause = cause;
	}
	
	
	/**
	 * Retrieves the cause of the operation refusal.
	 * @return the cause
	 */
	public Cause what() {
		return cause;
	}
	
}
