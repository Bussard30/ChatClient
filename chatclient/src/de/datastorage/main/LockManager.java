package de.datastorage.main;

import java.io.File;
import java.util.HashMap;

/**
 * stores locks for certain files / urls
 * @author Jonas S_
 *
 */
public class LockManager
{
	private HashMap<String, Boolean> locks;
	
	public LockManager()
	{
		locks = new HashMap<>();
	}
	
	/**
	 * gets if the file is locked
	 * @param f
	 * @return true == locked
	 */
	public synchronized boolean getLock(File f)
	{
		return locks.get(f);
	}
	
	/**
	 * Sets the lock boolean in the hashmap to true
	 * @param f
	 */
	public void edit(File f)
	{
		locks.put(f.getAbsolutePath(), true);
	}
	public void done(File f)
	{
		locks.put(f.getAbsolutePath(), false);
	}
}
