package mazegame.items;

public class RedBull extends Item {
	private static final int HEAL_AMOUNT = 40;

	public RedBull(int count) {
		super(ItemType.RED_BULL, count);
	}

	public static int getHealAmount() {
		return HEAL_AMOUNT;
	}
}
