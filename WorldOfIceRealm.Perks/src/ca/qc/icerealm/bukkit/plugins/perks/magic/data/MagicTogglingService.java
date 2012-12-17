package ca.qc.icerealm.bukkit.plugins.perks.magic.data;

import java.util.Hashtable;

import ca.qc.icerealm.bukkit.plugins.perks.magic.MagicPerk;


public class MagicTogglingService 
{
    private static final MagicTogglingService instance = new MagicTogglingService();
    private Hashtable<String, MagicTogglingData> togglingData = new Hashtable<String, MagicTogglingData>();

    private MagicTogglingService() 
    {

    }
   
    public static synchronized MagicTogglingService getInstance() 
    {
        return instance;
    }

	public Hashtable<String, MagicTogglingData> getMagicTogglingData() 
	{
		return togglingData;
	}
	
	public MagicTogglingData getMagicTogglingData(String playerName) 
	{
		MagicTogglingData data = togglingData.get(playerName);
		
		if (data == null)
		{
			togglingData.put(playerName, new MagicTogglingData());
		}
			
		return togglingData.get(playerName);
	}
	
	public MagicPerk getMagicTogglingData(String playerName, int school) 
	{
		MagicTogglingData data = getMagicTogglingData(playerName);
		return data.getCurrentMagic(school);
	}
	
	public void toggle(String playerName, int school)
	{
		MagicTogglingData data = getMagicTogglingData(playerName);
		data.setCurrentMagic(school, MagicData.getMagicInstance(data.getCurrentMagic(school).getNextTogglingMagicId()));
	}
}