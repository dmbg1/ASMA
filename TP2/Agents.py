from Portryable import Portrayable
from character import Character
from mesa import Agent
import Utils

class MonsterAgent(Character, Portrayable):

    def __init__(self, unique_id, model, shape, color, radius, hp):
        Character.__init__(self, unique_id, model, hp)
        Portrayable.__init__(self, shape, color, radius)

    
    def action(self):

        neighbors = self.getAgentsInSameCell()

        for neig in neighbors:
            if type(neig).__name__ == "PersonAgent":
                self.model.grid.remove_agent(neig)
                self.model.schedule.remove(neig)
    

    def chooseBestPosition(self, possible_steps):

        Utils.removeThroughWallSteps(self.pos, possible_steps, self.model.grid.width, self.model.grid.height)
        nearAgents = self.getNearAgents(4)
        for nearAgent in nearAgents:
            if "MonsterAgent" == type(nearAgent).__name__ and nearAgent.pos in possible_steps: 
                possible_steps.remove(nearAgent.pos)
            if "PersonAgent" == type(nearAgent).__name__:
                return Utils.getNearPoint(self, nearAgent.pos, possible_steps)
        
        if len(possible_steps) == 0:
            return self.pos
        return self.random.choice(possible_steps)
        

class PersonAgent(Character, Portrayable):

    def __init__(self, unique_id, model, shape, color, radius, hp):
        Character.__init__(self, unique_id, model, hp)
        Portrayable.__init__(self, shape, color, radius)
    
    def chooseBestPosition(self, possible_steps):

        Utils.removeThroughWallSteps(self.pos, possible_steps, self.model.grid.width, self.model.grid.height)
        nearAgents = self.getNearAgents(7)

        for nearAgent in nearAgents:
            if "PersonAgent" == type(nearAgent).__name__ and nearAgent.pos in possible_steps: 
                possible_steps.remove(nearAgent.pos)
            if "MonsterAgent" == type(nearAgent).__name__:
                return Utils.getFurtherPoint(self, nearAgent.pos, possible_steps)
        
        if len(possible_steps) == 0:
            return self.pos

        return self.random.choice(possible_steps)
        

class HeroAgent(Character, Portrayable):

    def __init__(self, unique_id, model, shape, color, radius, hp):
        Character.__init__(self, unique_id, model, hp)
        Portrayable.__init__(self, shape, color, radius)

    def chooseBestPosition(self, possible_steps):

        Utils.removeThroughWallSteps(self.pos, possible_steps, self.model.grid.width, self.model.grid.height)
        nearAgents = self.getNearAgents(7)

        for nearAgent in nearAgents:
            if "HeroAgent" == type(nearAgent).__name__ and nearAgent.pos in possible_steps: 
                possible_steps.remove(nearAgent.pos)
            if "PersonAgent" == type(nearAgent).__name__:
                return Utils.getNearPoint(self, nearAgent.pos, possible_steps)
        
        if len(possible_steps) == 0:
            return self.pos

        return self.random.choice(possible_steps)


class TurnIntoHeroAgent(Agent, Portrayable):  

    def __init__(self, unique_id, model, shape, color, radius):
        Agent.__init__(self, unique_id, model)
        Portrayable.__init__(self, shape, color, radius)

