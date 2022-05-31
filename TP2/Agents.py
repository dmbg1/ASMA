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

        nearAgents = self.getNearAgents(4)

        print("=> ", self.pos, "\n")

        for nearAgent in nearAgents:
            if "PersonAgent" == type(nearAgent).__name__:
                print("\t", nearAgent.pos)
                return Utils.getNearPoint(nearAgent.pos, possible_steps)
        
        return self.random.choice(possible_steps)
        

class PersonAgent(Character, Portrayable):

    def __init__(self, unique_id, model, shape, color, radius, hp):
        Character.__init__(self, unique_id, model, hp)
        Portrayable.__init__(self, shape, color, radius)
    
    def chooseBestPosition(self, possible_steps):
        return self.random.choice(possible_steps)
        

class HeroAgent(Character, Portrayable):

    def __init__(self, unique_id, model, shape, color, radius, hp):
        Character.__init__(self, unique_id, model, hp)
        Portrayable.__init__(self, shape, color, radius)

    def chooseBestPosition(self, possible_steps):
        return self.random.choice(possible_steps)


class TurnIntoHeroAgent(Agent, Portrayable):  

    def __init__(self, unique_id, model, shape, color, radius):
        Agent.__init__(self, unique_id, model)
        Portrayable.__init__(self, shape, color, radius)

