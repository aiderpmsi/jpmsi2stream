package org.aider.pmsi2sql.dbtypes;

/**
 * Types utilis�s dans la classe {@link pmsistandarddbtype} pour d�finir
 * le type de champ associ� � ce nom de champ
 * @author delabre
 *
 */
public enum PmsiStandardDbTypeEnum {
	/**
	 * Type BIGSERIAL
	 */
	BIGSERIAL,
	/**
	 * Type INT
	 */
	INT,
	/**
	 * Type BIGINT
	 */
	BIGINT,
	/**
	 * TYPE NUMERIC
	 */
	NUMERIC,
	/**
	 * Type VARCHAR
	 */
	VARCHAR,
	/**
	 * Type DATE
	 */
	DATE,
	/**
	 * Type TIMESTAMP
	 */
	TIMESTAMP,
	/**
	 * Type CHAR
	 */
	CHAR,
	/**
	 * Type TEXT
	 */
	TEXT,
	/**
	 * Type FILE
	 */
	FILE;
}
