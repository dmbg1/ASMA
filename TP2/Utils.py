import math

def calculateDistance(pos1, pos2):
    
    return math.sqrt((pos2[0]-pos1[0])**2 + (pos2[1]-pos1[1])**2)


def getNearPoint(agent, agentPos, possible_steps):

    minDistance = 9999999

    if len(possible_steps) == 0:
        return agent.pos

    for step in possible_steps:
        distance = calculateDistance(agentPos, step)
        if distance <= minDistance:
            minDistance = distance
            pos = step
    
    return pos


def getFurtherPoint(agent, agentPos, possible_steps):

    maxDistance = -99999999

    if len(possible_steps) == 0:
        return agent.pos

    print(possible_steps)

    for step in possible_steps:
        distance = calculateDistance(agentPos, step)
        if distance >= maxDistance:
            maxDistance = distance
            pos = step
    
    print(maxDistance)
    return pos


