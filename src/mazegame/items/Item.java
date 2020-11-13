package mazegame.items;

public class Item {
	private ItemType itemType;
	private int count;
	
	public Item(ItemType itemType)
	{
		this.itemType = itemType;
	}
	
	public Item(ItemType itemType, int count)
	{
		this.itemType = itemType;
		this.count = count;
	}
	
	public String getItemName()
	{
		return itemType.getItemName();
	}
	
	public int getItemId()
	{
		return itemType.getUniqueId();
	}
	
	public int getItemCount()
	{
		return this.count;
	}
	
	public void setItemCount(int count)
	{
		this.count = count;
	}
}
