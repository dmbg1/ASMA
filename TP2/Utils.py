import math
from copy import copy


# Removes moves through walls from the possible steps
def removeThroughWallSteps(pos, possibleSteps, width, height):
    aux = copy(possibleSteps)
    for possibleStep in aux:
        if (possibleStep[1] == height - 1 and pos[1] == 0) or (possibleStep[1] == 0 and pos[1] == height - 1) or \
                (possibleStep[0] == width - 1 and pos[0] == 0) or (possibleStep[0] == 0 and pos[0] == width - 1):
            possibleSteps.remove(possibleStep)


# Calculate distance between two cells of the grid
def calculateDistance(pos1, pos2):
    return math.sqrt((pos2[0] - pos1[0]) ** 2 + (pos2[1] - pos1[1]) ** 2)


# Get the nearest point to a specific agent
def getNearPoint(agentPos, possibleSteps):
    minDistance = 9999999

    for step in possibleSteps:
        distance = calculateDistance(agentPos, step)
        if distance <= minDistance:
            minDistance = distance
            pos = step

    return pos


# Get the furthest point to a specific agent
def getFurtherPoint(agentPos, possibleSteps):
    maxDistance = -99999999

    for step in possibleSteps:
        distance = calculateDistance(agentPos, step)
        if distance >= maxDistance:
            maxDistance = distance
            pos = step

    return pos


# Get the nearest agent in a set of agents
def getNearAgent(agent, nearAgents):
    minDistance = 99999

    if len(nearAgents) == 0:
        return -1

    for nearAgent in nearAgents:
        distance = calculateDistance(agent.pos, nearAgent.pos)
        if distance <= minDistance:
            minDistance = distance
            agent = nearAgent

    return agent


def average(n1, n2):
    return int(n1 + n2 / 2)
