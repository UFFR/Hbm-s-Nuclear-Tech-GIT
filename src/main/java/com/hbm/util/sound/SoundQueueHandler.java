package com.hbm.util.sound;

import java.util.ArrayList;
import java.util.List;

public class SoundQueueHandler
{
	private final List<SoundQueue> soundQueues = new ArrayList<SoundQueue>();
	public SoundQueueHandler()
	{
		//
	}
	
	public void tick()
	{
		soundQueues.removeIf(SoundQueue::isDone);
		soundQueues.forEach(SoundQueue::tick);
	}

}
