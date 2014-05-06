package com.internistic.bankocr;

/**
 * @author pumbers
 * 
 */
public class Account {

	/**
	 * 
	 */
	public static final String ACCOUNT_NUMBER_REGEX = "\\d{9}";

	/**
	 * 
	 */
	public static enum ERROR {
		ERR, ILL
	}

	private String accountNumber = null;
	private Integer checksum = null;
	private ERROR error = null;

	/**
	 * @param accountNumber
	 */
	public Account(String accountNumber) {
		if (accountNumber == null) {
			throw new IllegalArgumentException("Account number cannot be null");
		}
		this.accountNumber = accountNumber;
		checksum = checksumFor(accountNumber);
		error = validate(accountNumber, checksum);
	}

	/**
	 * @param accountNumber
	 * @return
	 */
	public static final Integer checksumFor(final String accountNumber) {

		if (accountNumber != null
				&& accountNumber.matches(ACCOUNT_NUMBER_REGEX)) {

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

	public static final ERROR validate(String accountNumber) {
		return validate(accountNumber, checksumFor(accountNumber));
	}

	/**
	 * @param accountNumber
	 * @param checksum
	 * @return
	 */
	private static final ERROR validate(String accountNumber, Integer checksum) {

		if (accountNumber == null
				|| !accountNumber.matches(ACCOUNT_NUMBER_REGEX)) {
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
