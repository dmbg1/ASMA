import math


def removeThroughWallSteps(pos, possible_steps, width, height):
    for possible_step in possible_steps:
        if (possible_step[1] == height - 1 and pos[1] == 0) or (possible_step[1] == 0 and pos[1] == height - 1) or\
                (possible_step[0] == width - 1 and pos[0] == 0) or (possible_step[0] == 0 and pos[0] == width - 1):
            possible_steps.remove(possible_step)


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

    for step in possible_steps:
        distance = calculateDistance(agentPos, step)
        if distance >= maxDistance:
            maxDistance = distance
            pos = step

    return pos
