package mazegame.java;

import java.util.HashMap;
import java.util.Map;

import mazegame.items.Item;
import mazegame.items.ItemType;
import mazegame.items.RedBull;

public class BackPack {
	private HashMap<Integer, Item> backpack;
	
	public BackPack()
	{
		backpack = new HashMap<Integer, Item>();
	}
	
	public void addItem(Item item)
	{
		int id = item.getItemId();
		if(backpack.containsKey(id))
		{
			Item backpackItem = backpack.get(id);
			backpackItem.setItemCount(backpackItem.getItemCount()+1);
		}
		else
		{
			backpack.put(id, item);
		}
	}

	public RedBull getRedBull()
	{
		RedBull redBull = null;

		Item item =  backpack.get(ItemType.RED_BULL.getUniqueId());

		if(item != null)
		{
			redBull = (RedBull) item;
		}
		
		return redBull;
	}
	
	public int getItemCount(ItemType itemType)
	{
		int count = 0;
		Item item =  backpack.get(ItemType.RED_BULL.getUniqueId());

		if(item != null)
		{
			count = item.getItemCount();
		}
		
		return count;
	}
	
	public void reduceItemAmount(ItemType itemType)
	{
		Item item =  backpack.get(ItemType.RED_BULL.getUniqueId());

		if(item != null)
		{
			item.setItemCount(item.getItemCount()-1);
		}		
	}
	
	public void increaseItemAmount(ItemType itemType)
	{
		Item item =  backpack.get(ItemType.RED_BULL.getUniqueId());

		if(item != null)
		{
			item.setItemCount(item.getItemCount()+1);
		}		
	}
	
	public String listAllItems()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Items in backpack: \n" );
		for(Map.Entry<Integer, Item> entry: backpack.entrySet())
		{
			Item item = entry.getValue();
			sb.append(item.getItemName() + ": " + item.getItemCount() + "\n");
		}
		
		return sb.toString();
	}
}
