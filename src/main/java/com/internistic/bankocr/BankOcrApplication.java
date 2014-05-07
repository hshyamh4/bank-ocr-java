package com.internistic.bankocr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application class for Bank OCR Kata Java implementation.
 * 
 * @author pumbers
 * 
 */
public class BankOcrApplication {

	private static final Logger log = LoggerFactory
			.getLogger(BankOcrApplication.class);

	public static void main(String[] args) throws IOException {

		if (args.length == 1) {
			new BankOcrApplication().run(args[0], null);
		} else if (args.length == 2) {
			new BankOcrApplication().run(args[0], args[1]);
		} else {
			log.error("Usage:\tBankOcrApplication <input file> [output file]\n\n\tIf output file is not specified, results will be printed to the console.");
		}

	}

	private void run(final String accountsFileName, String resultsFileName)
			throws IOException {

		log.info("Starting OCR run ...");
		log.info("Reading account OCR data from {}", accountsFileName);

		try {

			OcrReader reader = new OcrReader(new File(accountsFileName));

			AccountWriter writer = null;
			if (resultsFileName != null && !resultsFileName.isEmpty()) {
				log.info("Writing account details to {}", resultsFileName);
				writer = new AccountWriter(resultsFileName);
			} else {
				log.info("Writing account details to STDOUT");
				writer = new AccountWriter();
			}

			Account account;
			while (reader.isReady()
					&& (account = new Account(reader.getNextAccountNumber())) != null) {
				writer.write(account);
			}

			log.info("OCR run completed successfully");

		} catch (FileNotFoundException fnfe) {
			log.error(
					"OCR run aborted: The file '{}' could not be found.  Please try a different file name.",
					accountsFileName);
		} catch (Exception e) {
			log.error("OCR run aborted", e);
		}

	}

}
