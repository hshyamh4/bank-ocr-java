package com.internistic.bankocr;

import java.io.IOException;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes account numbers and error flags to either a file or STDOUT.
 * 
 * @author pumbers
 * 
 */
public class AccountWriter {

	private static final Logger log = LoggerFactory
			.getLogger(AccountWriter.class);

	private PrintStream out;

	/**
	 * Create an AccountWriter that writes to the specified file.
	 * 
	 * @param resultsFileName
	 * @throws IOException
	 */
	public AccountWriter(final String resultsFileName) throws IOException {
		this(new PrintStream(resultsFileName));
	}

	/**
	 * Create an AccountWriter that writes to the specified PrintStream.
	 * 
	 * @param out
	 */
	public AccountWriter(final PrintStream out) {
		this.out = out;
	}

	/**
	 * Create an AccountWriter that writes to STDOUT.
	 */
	public AccountWriter() {
		this(System.out);
	}

	/**
	 * Write the account to the specified PrintStream. Format is '<account
	 * number> <error>'
	 * 
	 * @param account
	 */
	public void write(final Account account) {
		log.debug("write(): account={}", account);
		out.println(String.format("%s %s", account.getAccountNumber(),
				account.hasError() ? account.getError() : ""));
	}

}
