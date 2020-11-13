package mazegame.items;

public enum ItemType
{
	RED_BULL(0, "Red Bull"),
	KEY(1, "Key");
	
	private final String itemName;
	private final int uniqueId;
	
	private ItemType(int uniqueId, String itemName)
	{
		this.itemName = itemName;
		this.uniqueId = uniqueId;
	}
	
	public String getItemName()
	{
		return this.itemName;
	}
	
	public int getUniqueId()
	{
		return this.uniqueId;
	}
}
