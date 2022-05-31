import math

def calculateDistance(pos1, pos2):
    
    return math.sqrt((pos2[0]-pos1[0])**2 + (pos2[1]-pos1[1])**2)


def getNearPoint(agentPos, possible_steps):

    minDistance = 9999999

    for step in possible_steps:
        distance = calculateDistance(agentPos, step)
        if distance <= minDistance:
            minDistance = distance
            pos = step
    
    return pos


