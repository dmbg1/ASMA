from Portryable import Portrayable
from character import Character
from mesa import Agent
import Utils

class MonsterAgent(Character, Portrayable):

    def __init__(self, unique_id, model, shape, color, radius, hp, hp_decrease):
        Character.__init__(self, unique_id, model, hp, hp_decrease)
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
        nearAgent = Utils.getNearAgent(self, nearAgents)

        if nearAgent == -1:
            return self.random.choice(possible_steps)

        if "PersonAgent" == type(nearAgent).__name__:
            return Utils.getNearPoint(nearAgent.pos, possible_steps)
        
        if "MonsterAgent" == type(nearAgent).__name__: 
            return Utils.getFurtherPoint(nearAgent.pos, possible_steps)
        
        return self.random.choice(possible_steps)
        

class PersonAgent(Character, Portrayable):

    def __init__(self, unique_id, model, shape, color, radius, hp, hp_decrease):
        Character.__init__(self, unique_id, model, hp, hp_decrease)
        Portrayable.__init__(self, shape, color, radius)
    
    def chooseBestPosition(self, possible_steps):

        Utils.removeThroughWallSteps(self.pos, possible_steps, self.model.grid.width, self.model.grid.height)
        nearAgents = self.getNearAgents(6)
        nearAgent = Utils.getNearAgent(self, nearAgents)

        if nearAgent == -1:
            return self.random.choice(possible_steps)

        if "MonsterAgent" == type(nearAgent).__name__:
            return Utils.getFurtherPoint(nearAgent.pos, possible_steps)
        
        if "PersonAgent" == type(nearAgent).__name__: 
            return Utils.getFurtherPoint(nearAgent.pos, possible_steps)
        
        return self.random.choice(possible_steps)
        

class HeroAgent(Character, Portrayable):

    def __init__(self, unique_id, model, shape, color, radius, hp, hp_decrease):
        Character.__init__(self, unique_id, model, hp, hp_decrease)
        Portrayable.__init__(self, shape, color, radius)

    def chooseBestPosition(self, possible_steps):

        Utils.removeThroughWallSteps(self.pos, possible_steps, self.model.grid.width, self.model.grid.height)
        nearAgents = self.getNearAgents(6)
        nearAgent = Utils.getNearAgent(self, nearAgents)

        if nearAgent == -1:
            return self.random.choice(possible_steps)

        if "MonsterAgent" == type(nearAgent).__name__:
            return Utils.getNearPoint(nearAgent.pos, possible_steps)
        
        if "PersonAgent" == type(nearAgent).__name__:
            return Utils.getNearPoint(nearAgent.pos, possible_steps)
        
        if "HeroAgent" == type(nearAgent).__name__: 
            return Utils.getFurtherPoint(nearAgent.pos, possible_steps)

        return self.random.choice(possible_steps)


class TurnIntoHeroAgent(Agent, Portrayable):  

    def __init__(self, unique_id, model, shape, color, radius):
        Agent.__init__(self, unique_id, model)
        Portrayable.__init__(self, shape, color, radius)

