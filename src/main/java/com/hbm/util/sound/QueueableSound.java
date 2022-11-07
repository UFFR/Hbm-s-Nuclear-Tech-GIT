package com.hbm.util.sound;

import java.util.Objects;

public class QueueableSound
{
	private final String soundName;
	private int delay, duration;
	private boolean playing = false;
	public QueueableSound(String soundName, int delay, int duration)
	{
		this.soundName = soundName;
		this.delay = delay;
		this.duration = duration;
	}
	public QueueableSound(String soundName, int duration)
	{
		this.soundName = soundName;
		this.duration = duration;
	}
	public int getDelay()
	{
		return delay;
	}
	public void decrementDelay()
	{
		delay--;
	}
	public void setDelay(int delay)
	{
		this.delay = delay;
	}
	public int getDuration()
	{
		return duration;
	}
	public void decrementDuration()
	{
		duration--;
	}
	public void setDuration(int duration)
	{
		this.duration = duration;
	}
	public String getSoundName()
	{
		return soundName;
	}
	public String playSound()
	{
		playing = true;
		return soundName;
	}
	public boolean isReady()
	{
		return delay <= 0;
	}
	public boolean isDone()
	{
		return duration <= 0;
	}
	public boolean isPlaying()
	{
		return playing;
	}
	@Override
	public int hashCode()
	{
		return Objects.hash(delay, duration, playing, soundName);
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof QueueableSound))
			return false;
		final QueueableSound other = (QueueableSound) obj;
		return delay == other.delay && duration == other.duration && playing == other.playing
				&& Objects.equals(soundName, other.soundName);
	}
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("QueueableSound [soundName=").append(soundName).append(", delay=").append(delay)
				.append(", duration=").append(duration).append(", playing=").append(playing).append(']');
		return builder.toString();
	}
}
