package org.aider.pmsi2sql.dbtypes;

/**
 * Types utilisés dans la classe {@link pmsistandarddbtype} pour définir
 * le type de champ associé à ce nom de champ
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
