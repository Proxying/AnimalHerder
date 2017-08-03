package uk.co.proxying.animalHerders.upgrades;

/**
 * Created by Kieran (Proxying) on 01-Aug-17.
 */
public enum UpgradeAmounts {
	FIVE(1, "&a+5", 5), TEN(2, "&b +10", 10), TWENTY_FIVE(3, "&e +25", 25), FIFTY(4, "&d +50", 50);

	int id;
	String display;
	int integer;

	UpgradeAmounts(int id, String display, int integer) {
		this.id = id;
		this.display = display;
		this.integer = integer;
	}

	public static UpgradeAmounts fromID(int id) {
		for (UpgradeAmounts upAm : values()) {
			if (upAm.id == id) {
				return upAm;
			}
		}

		return null;
	}

	public String getDisplay() {
		return this.display;
	}

	public int getInteger() {
		return this.integer;
	}
}

