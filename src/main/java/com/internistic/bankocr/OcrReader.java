package com.internistic.bankocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads OCR lines from an input source - either a file or a string - and
 * converts them to account numbers.
 * 
 * @author pumbers
 * 
 */
public class OcrReader {

	private static final Logger log = LoggerFactory.getLogger(OcrReader.class);

	/**
	 * Character used to indicate illegible or unrecognized OCR digit in parsed
	 * account numbers
	 */
	public static final char ILLEGIBLE_OCR_DIGIT = '?';

	// OCR digit templates, used to identify OCR digit string (3x3 OCR elements
	// represented as 9-character string) and map to a digit character
	private static final Map<String, Character> OCR_DIGITS = new HashMap<String, Character>();

	static {
		OCR_DIGITS.put(" _ | ||_|", '0');
		OCR_DIGITS.put("     |  |", '1');
		OCR_DIGITS.put(" _  _||_ ", '2');
		OCR_DIGITS.put(" _  _| _|", '3');
		OCR_DIGITS.put("   |_|  |", '4');
		OCR_DIGITS.put(" _ |_  _|", '5');
		OCR_DIGITS.put(" _ |_ |_|", '6');
		OCR_DIGITS.put(" _   |  |", '7');
		OCR_DIGITS.put(" _ |_||_|", '8');
		OCR_DIGITS.put(" _ |_| _|", '9');
	}

	// BufferedReader to read OCR lines
	private BufferedReader reader;

	// Flag to show OcrReader is ready to deliver more account numbers
	private boolean ready = true;

	/**
	 * Create an OCR Reader which reads OCR lines from a file.
	 * 
	 * @param inputFile
	 *            The file to read OCR characters from
	 * @throws FileNotFoundException
	 *             If the specified file does not exist
	 */
	public OcrReader(final File inputFile) throws FileNotFoundException {
		this.reader = new BufferedReader(new FileReader(inputFile));
	}

	/**
	 * Create an OCR Reader which reads OCR lines from a String (mainly for test
	 * purposes).
	 * 
	 * @param lines
	 *            OCR lines - each line should terminate with a lf (\n) or cr/lf
	 *            (\r\n).
	 */
	public OcrReader(final String lines) {
		this.reader = new BufferedReader(new StringReader(lines));
	}

	/**
	 * Read the next lines from the input (file) and convert to an account
	 * number.
	 * 
	 * @return An account number string - should be numeric, but may include
	 *         illegible characters if the OCR characters cannot be parsed
	 *         correctly. Will return null if there are no more account numbers
	 *         to be read.
	 * @throws IOException
	 * @see ILLEGIBLE_OCR_DIGIT
	 */
	public String getNextAccountNumber() throws IOException {
		return parseOcrDigits(getNextAccountLines());
	}

	/**
	 * Read the next three OCR lines from the input (file) and convert to a list
	 * of OCR digits for parsing.
	 * 
	 * @return A list of OCR digits (3x3 OCR characters as a 1x9 string)
	 * @throws IOException
	 */
	List<String> getNextAccountLines() throws IOException {
		log.debug("getNextAccountLines()");

		if (reader.ready() && ready) {

			List<StringBuilder> ocrDigits = new ArrayList<>();

			// read the three OCR lines
			for (int i = 0; i < 3; i++) {
				String line = reader.readLine();
				log.debug("... i={}, line='{}'", i, line);
				if (line != null) {
					int j = 0;
					// split each line into groups of 3 OCR characters
					for (String digit : String.format("%-27s", line).split(
							"(?<=\\G.{3})")) {
						try {
							// try to append the ocr characters to a
							// StringBuilder for the current digit
							ocrDigits.set(j, ocrDigits.get(j).append(digit));
						} catch (IndexOutOfBoundsException e) {
							// if there is no StringBuilder for the current
							// digit, make one
							ocrDigits.add(new StringBuilder(digit));
						}
						j++;
					}
				}
			}

			// read the blank line following the OCR lines
			reader.readLine();

			log.debug("... ocrDigits={}", ocrDigits);

			// set the ready flag if we have OCR digits
			if (ready = !ocrDigits.isEmpty()) {
				// convert the StringBuilders to strings
				List<String> digits = new ArrayList<>();
				for (StringBuilder sb : ocrDigits) {
					digits.add(sb.toString());
				}
				log.debug("... returning digits={}", digits);
				return digits;
			}

		}

		log.debug("... returning null");

		// default return if the ready is unready, or if there were no OCR
		// digits read
		return null;

	}

	/**
	 * Take a list of (1x9) OCR character strings and attempt to convert them
	 * into an account number string. Illegible OCR characters are returned as
	 * an "illegible" flag character, nominally '?'
	 * 
	 * @param ocrDigits
	 * @return An account number string - should be numeric, but may include
	 *         illegible characters if the OCR characters cannot be parsed
	 *         correctly.
	 */
	String parseOcrDigits(final List<String> ocrDigits) {
		log.debug("parseOcrDigits(): ocrDigits={}", ocrDigits);

		if (ocrDigits == null || ocrDigits.isEmpty()) {
			return null;
		}

		StringBuilder digitBuilder = new StringBuilder();

		for (String digit : ocrDigits) {
			digitBuilder.append(OCR_DIGITS.containsKey(digit) ? OCR_DIGITS
					.get(digit) : ILLEGIBLE_OCR_DIGIT);
		}

		log.debug("... returning '{}'", digitBuilder);

		return digitBuilder.toString();
	}

	/**
	 * Check if the OcrReader is ready to deliver more account numbers
	 * 
	 * @return true if reader is ready, false if there are no more account
	 *         numbers
	 * @throws IOException
	 */
	public boolean isReady() throws IOException {
		return reader.ready() && ready;
	}

}
