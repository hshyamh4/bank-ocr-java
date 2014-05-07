package com.internistic.bankocr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OcrReaderTest {

	private final String testLines0123456789 = new StringBuilder()
			.append("000111222333444555666777888999\n")
			.append("000111222333444555666777888999\n")
			.append("000111222333444555666777888999\n").append("\n").toString();

	private final String validOcrLines111111111 = new StringBuilder()
			.append("                           \n")
			.append("  |  |  |  |  |  |  |  |  |\n")
			.append("  |  |  |  |  |  |  |  |  |\n").append("\n").toString();

	private final String validOcrLines222222222 = new StringBuilder()
			.append(" _  _  _  _  _  _  _  _  _ \n")
			.append(" _| _| _| _| _| _| _| _| _|\n")
			.append("|_ |_ |_ |_ |_ |_ |_ |_ |_ \n").append("\n").toString();

	private final String validOcrLines123456789 = new StringBuilder()
			.append("    _  _     _  _  _  _  _ \n")
			.append("  | _| _||_||_ |_   ||_||_|\n")
			.append("  ||_  _|  | _||_|  ||_| _|\n").append("\n").toString();

	private final List<String> validOcrDigits0123456789 = Arrays
			.asList(new String[] { " _ | ||_|", "     |  |", " _  _||_ ",
					" _  _| _|", "   |_|  |", " _ |_  _|", " _ |_ |_|",
					" _   |  |", " _ |_||_|", " _ |_| _|" });

	private final List<String> illegibleOcrDigits012X456X89 = Arrays
			.asList(new String[] { " _ | ||_|", "     |  |", " _  _||_ ",
					"|_  _| _|", "   |_|  |", " _ |_  _|", " _ |_ |_|",
					"?_   |  |", " _ |_||_|", " _ |_| _|" });

	/**
	 * Test that the reader correctly reads three OCR lines and re-works them in
	 * to a list of OCR digit strings. For this test, we don't use OCR character
	 * strings, but simple numbers for simplicity.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testGetNextAccountLines() throws IOException {

		OcrReader reader = new OcrReader(testLines0123456789);

		List<String> result = reader.getNextAccountLines();

		assertEquals("000000000", result.get(0));
		assertEquals("111111111", result.get(1));
		assertEquals("222222222", result.get(2));
		assertEquals("333333333", result.get(3));
		assertEquals("444444444", result.get(4));
		assertEquals("555555555", result.get(5));
		assertEquals("666666666", result.get(6));
		assertEquals("777777777", result.get(7));
		assertEquals("888888888", result.get(8));
		assertEquals("999999999", result.get(9));

	}

	/**
	 * Test that the reader can correctly parse a list of OCR digit strings into
	 * an account number.
	 */
	@Test
	public void testParseOcrDigits() {

		OcrReader reader = new OcrReader(new String());

		String validResult = reader.parseOcrDigits(validOcrDigits0123456789);
		assertEquals("0123456789", validResult);

		String illegibleResult = reader
				.parseOcrDigits(illegibleOcrDigits012X456X89);
		assertEquals("012?456?89", illegibleResult);
	}

	/**
	 * Test that the reader can read a set of OCR lines and parse them into
	 * account numbers.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testGetNextAccountNumber() throws IOException {

		// Set up three OCR account numbers
		OcrReader reader = new OcrReader(validOcrLines111111111
				+ validOcrLines222222222 + validOcrLines123456789);

		// ... the first 3 accounts should read correctly
		// and the reader remain ready
		assertEquals("111111111", reader.getNextAccountNumber());
		assertTrue(reader.isReady());
		assertEquals("222222222", reader.getNextAccountNumber());
		assertTrue(reader.isReady());
		assertEquals("123456789", reader.getNextAccountNumber());
		assertTrue(reader.isReady());

		// ... the fourth account should be empty and ready changed to false
		assertNull(reader.getNextAccountNumber());
		assertFalse(reader.isReady());

		// ... subsequent reads should all return null
		assertNull(reader.getNextAccountNumber());
		assertNull(reader.getNextAccountNumber());
		assertNull(reader.getNextAccountNumber());

	}

}
