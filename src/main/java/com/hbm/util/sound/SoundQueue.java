package com.hbm.util.sound;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.function.Consumer;

public class SoundQueue
{
	private final Queue<QueueableSound> sounds = new ArrayDeque<QueueableSound>();
	private final Consumer<String> soundAcceptor;
	private QueueableSound currentSound;
	public SoundQueue(Consumer<String> soundAcceptor, QueueableSound... sounds)
	{
		this.soundAcceptor = soundAcceptor;
		this.sounds.addAll(Arrays.asList(sounds));
	}

	public void tick()
	{
		if (!sounds.isEmpty())
		{
			if (currentSound == null)
				currentSound = sounds.poll();
			
			if (!currentSound.isPlaying() && currentSound.isReady())
				soundAcceptor.accept(currentSound.playSound());
			else if (currentSound.getDelay() > 0)
				currentSound.decrementDelay();
			else if (currentSound.isPlaying() && !currentSound.isDone())
				currentSound.decrementDuration();
			else if (currentSound.isDone())
				currentSound = null;
		}
	}
	
	public boolean offerSound(QueueableSound sound)
	{
		return sounds.offer(sound);
	}
	
	public boolean isDone()
	{
		return sounds.isEmpty();
	}
}
