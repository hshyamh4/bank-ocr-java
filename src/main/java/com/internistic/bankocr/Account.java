package com.internistic.bankocr;

/**
 * Representation of bank account, including account number, checksum and error
 * flag. Also includes static methods for calculating a checksum and validating
 * account numbers & checksums.
 * 
 * @author pumbers
 * 
 */
public class Account {

	/**
	 * Regex used to validate a correctly parsed account number (9 digits)
	 */
	public static final String VALID_ACCOUNT_NUMBER_REGEX = "\\d{9}";

	/**
	 * Regex used to check for allowed characters in account numbers, 9 digits
	 * or illegible character flags (nominally '?')
	 * 
	 * @see OcrReader
	 * 
	 */
	public static final String ALLOWED_ACCOUNT_NUMBER_REGEX = "[\\d\\?]{9}";

	/**
	 * Error type for incorrectly parsed account numbers: ERR (non-zero
	 * checksum), ILL (contains illegible characters)
	 */
	public static enum ERROR {
		ERR, ILL
	}

	private String accountNumber = null;
	private Integer checksum = null;
	private ERROR error = null;

	/**
	 * Create a new Account instance using the supplied account number. On
	 * creation, a checksum is calculated (for valid account numbers only) or,
	 * if an invalid or illegible account number is provided, an error type is
	 * set.
	 * 
	 * @param accountNumber
	 *            Must be non-null, 9-digits or illegible character flags
	 */
	public Account(final String accountNumber) {
		if (accountNumber == null) {
			throw new IllegalArgumentException("Account number cannot be null");
		} else if (!accountNumber.matches(ALLOWED_ACCOUNT_NUMBER_REGEX)) {
			throw new IllegalArgumentException(
					"Account number must contain only digits or illegible character flags");
		}
		this.accountNumber = accountNumber;
		this.checksum = checksumFor(accountNumber);
		this.error = validate(accountNumber, checksum);
	}

	/**
	 * Calculate a checksum for a valid account number (9-digits).
	 * 
	 * @param accountNumber
	 * @return Calculated checksum, null if the supplied account number was null
	 *         or invalid
	 */
	public static final Integer checksumFor(final String accountNumber) {

		if (accountNumber != null
				&& accountNumber.matches(VALID_ACCOUNT_NUMBER_REGEX)) {

			int total = 0;
			int i = 1;
			for (char c : new StringBuilder(accountNumber).reverse().toString()
					.toCharArray()) {
				total += (c - '0') * i++;
			}

			return total % 11;

		} else {
			return null;
		}

	}

	/**
	 * Validate an account number
	 * 
	 * @param accountNumber
	 *            Account number
	 * @return Error enum, or null if the account number & checksum its are
	 *         valid
	 */
	public static final ERROR validate(final String accountNumber) {
		return validate(accountNumber, checksumFor(accountNumber));
	}

	/**
	 * Validate an account number & checksum.
	 * 
	 * @param accountNumber
	 *            Account number
	 * @param checksum
	 *            Calculated checksum for the account number
	 * @return Error enum, or null if both the account number & checksum are
	 *         valid
	 */
	private static final ERROR validate(final String accountNumber, final Integer checksum) {

		if (accountNumber == null
				|| !accountNumber.matches(VALID_ACCOUNT_NUMBER_REGEX)) {
			return ERROR.ILL;
		} else if (checksum == null || checksum != 0) {
			return ERROR.ERR;
		} else {
			return null;
		}

	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public Integer getChecksum() {
		return checksum;
	}

	public ERROR getError() {
		return error;
	}

	public boolean hasError() {
		return error != null;
	}

	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", checksum="
				+ checksum + ", error=" + error + "]";
	}

}
