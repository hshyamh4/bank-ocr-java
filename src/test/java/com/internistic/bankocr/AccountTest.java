package com.internistic.bankocr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AccountTest {

	@Test
	public void testCreateValidAccounts() {

		Account account000000000 = new Account("000000000");
		assertEquals("000000000", account000000000.getAccountNumber());
		assertEquals(0, (int) account000000000.getChecksum());
		assertNull(account000000000.getError());

		Account account123456789 = new Account("123456789");
		assertEquals("123456789", account123456789.getAccountNumber());
		assertEquals(0, (int) account123456789.getChecksum());
		assertNull(account123456789.getError());

	}

	@Test
	public void testCreateInvalidAccounts() {

		Account account000000000 = new Account("000?00000");
		assertEquals("000?00000", account000000000.getAccountNumber());
		assertNull(account000000000.getChecksum());
		assertEquals(Account.ERROR.ILL, account000000000.getError());

	}

	@Test
	public void testCreateIllegibleAccounts() {
	}

	@Test
	public void testChecksumCalculations() {

		assertTrue(Account.checksumFor("711111111") == 0);
		assertTrue(Account.checksumFor("123456789") == 0);
		assertTrue(Account.checksumFor("490867715") == 0);

		assertFalse(Account.checksumFor("888888888") == 0);
		assertFalse(Account.checksumFor("490067715") == 0);
		assertFalse(Account.checksumFor("012345678") == 0);

	}

	@Test
	public void testValidation() {

		assertEquals(Account.ERROR.ILL, Account.validate("49006771?"));
		assertEquals(Account.ERROR.ILL, Account.validate("1234?678?"));

		assertEquals(Account.ERROR.ILL, Account.validate(null));
		assertEquals(Account.ERROR.ILL, Account.validate("invalid"));
		assertEquals(Account.ERROR.ILL, Account.validate(""));

		assertEquals(Account.ERROR.ERR, Account.validate("012345678"));

	}
}
