package com.hbm.entity.mob.ai.task;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.hbm.entity.mob.ai.snpc.solo.EntityAISmartBase;
import com.hbm.entity.mob.ai.squad.SquadBase;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.util.Tuple.Pair;
import com.hbm.util.lists.SortedList;

public abstract class TaskHelper
{
	protected final Map<EntitySmartNPCBase, List<Pair<Integer, EntityAISmartBase>>> aiTaskMap = new HashMap<EntitySmartNPCBase, List<Pair<Integer,EntityAISmartBase>>>(), executingAiTaskMap = new HashMap<EntitySmartNPCBase, List<Pair<Integer,EntityAISmartBase>>>();
	protected final List<EntitySmartNPCBase> squadMembers;
	protected SquadBase hostSquad;
	public TaskHelper(SquadBase hostSquad)
	{
		this.hostSquad = hostSquad;

		squadMembers = hostSquad.getMembers();
//		squadMembers = new SortedList<>(hostSquad.getMembers(), Comparator.comparing(EntitySmartNPCBase::getRank));
		
		initializeMap(squadMembers, aiTaskMap);
		
		initializeTaskRegistry();
	}

	protected abstract void initializeTaskRegistry();
	
	public void update()
	{
		if (squadMembers.removeIf(EntitySmartNPCBase::isDead))
		{
			aiTaskMap.keySet().retainAll(squadMembers);
			executingAiTaskMap.keySet().retainAll(squadMembers);
		}
		
		final Map<EntitySmartNPCBase, List<Pair<Integer, EntityAISmartBase>>> newTasks = new HashMap<EntitySmartNPCBase, List<Pair<Integer,EntityAISmartBase>>>(aiTaskMap.size());
		initializeMap(squadMembers, newTasks);
		
		for (Entry<EntitySmartNPCBase, List<Pair<Integer, EntityAISmartBase>>> mapEntry : aiTaskMap.entrySet())
		{
			if (mapEntry.getValue().isEmpty())
				continue;
			
			final List<Pair<Integer, EntityAISmartBase>> taskEntries = mapEntry.getValue();
			sortTaskList(taskEntries);
			if (taskEntries.size() > 2)
			{
				final Iterator<Pair<Integer, EntityAISmartBase>> iterator = taskEntries.iterator();
				while (iterator.hasNext())
				{
					final Pair<Integer, EntityAISmartBase> task = iterator.next();
					final List<Pair<Integer, EntityAISmartBase>> executingTasks = executingAiTaskMap.get(mapEntry.getKey());
					final boolean isRunning = executingTasks.contains(task);
					
					if (isRunning)
					{
						if (canUse(task, taskEntries, executingTasks) && task.getValue().continueExecuting())
							continue;
						
						task.getValue().resetTask();
						executingTasks.remove(task);
					}
					
					if (canUse(task, taskEntries, executingTasks) && task.getValue().shouldExecute())
					{
						newTasks.get(mapEntry.getKey()).add(task);
						executingTasks.add(task);
					}
				}
			} else if (taskEntries.get(0).getValue().shouldExecute())
				taskEntries.get(0).getValue().startExecuting();
		}
		
		newTasks.values().forEach(l -> l.forEach(t -> t.getValue().startExecuting()));
		
		executingAiTaskMap.values().forEach(l -> l.forEach(t -> t.getValue().updateTask()));
	}
	
	private static boolean canUse(Pair<Integer, EntityAISmartBase> task, List<Pair<Integer, EntityAISmartBase>> taskEntries, List<Pair<Integer, EntityAISmartBase>> executingTaskEntries)
	{
		final Iterator<Pair<Integer, EntityAISmartBase>> iterator = taskEntries.iterator();
		
		while (iterator.hasNext())
		{
			final Pair<Integer, EntityAISmartBase> otherTask = iterator.next();
			
			if (otherTask != task)
			{
				if (task.getKey() >= otherTask.getKey())
				{
					if (executingTaskEntries.contains(otherTask) && !canTasksRunConcurrently(task.getValue(), otherTask.getValue()))
						return false;
				} else if (executingTaskEntries.contains(otherTask) && !otherTask.getValue().isInterruptible())
					return false;
			}
		}
		
		return true;
	}
	
	public static boolean canTasksRunConcurrently(EntityAISmartBase task1, EntityAISmartBase task2)
	{
		return (task1.getMutexBits() & task2.getMutexBits()) == 0;
	}
	
	protected static void sortTaskList(List<Pair<Integer, EntityAISmartBase>> tasks)
	{
		tasks.sort(Comparator.comparing(Pair::getKey));
	}
	
	protected static void initializeMap(List<EntitySmartNPCBase> squadMembers, Map<EntitySmartNPCBase, List<Pair<Integer, EntityAISmartBase>>> aiTaskMap)
	{
		for (EntitySmartNPCBase member : squadMembers)
			aiTaskMap.put(member, new SortedList<Pair<Integer,EntityAISmartBase>>(Comparator.comparing(Pair::getKey)));
	}
}
