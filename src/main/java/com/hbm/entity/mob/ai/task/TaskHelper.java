package com.hbm.entity.mob.ai.task;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hbm.entity.mob.ai.snpc.EntityAISmartBase;
import com.hbm.entity.mob.ai.squad.SquadBase;
import com.hbm.entity.mob.smart.EntitySmartNPCBase;
import com.hbm.util.Tuple.Pair;

public abstract class TaskHelper
{
	protected final Map<EntitySmartNPCBase, List<Pair<Integer, EntityAISmartBase>>> aiTaskMap = new HashMap<EntitySmartNPCBase, List<Pair<Integer,EntityAISmartBase>>>();
	protected final List<EntitySmartNPCBase> squadMembers;
	protected SquadBase hostSquad;
	public TaskHelper(SquadBase hostSquad)
	{
		this.hostSquad = hostSquad;

		squadMembers = hostSquad.getMembers();
		initializeTaskRegistry();
	}

	protected abstract void initializeTaskRegistry();
	
	protected static void sortTaskList(List<Pair<Integer, EntityAISmartBase>> tasks)
	{
		tasks.sort(Comparator.comparing(Pair::getKey));
	}
}
