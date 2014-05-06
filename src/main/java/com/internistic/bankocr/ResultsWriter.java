package com.internistic.bankocr;

import java.io.IOException;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pumbers
 *
 */
public class ResultsWriter {

	private static final Logger log = LoggerFactory
			.getLogger(ResultsWriter.class);

	private PrintStream out;

	/**
	 * @param resultsFileName
	 * @throws IOException
	 */
	public ResultsWriter(String resultsFileName) throws IOException {
		this(new PrintStream(resultsFileName));
	}

	/**
	 * @param out
	 */
	public ResultsWriter(PrintStream out) {
		this.out = out;
	}

	/**
	 * @param account
	 */
	public void write(Account account) {
		log.debug("write(): account={}", account);
		out.println(String.format("%s %s", account.getAccountNumber(),
				account.hasError() ? account.getError() : ""));
	}

}
