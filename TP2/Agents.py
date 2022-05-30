from Portryable import Portrayable
from character import Character
from mesa import Agent

class MonsterAgent(Character, Portrayable):

    def __init__(self, unique_id, model, shape, color, radius):
        Character.__init__(self, unique_id, model)
        Portrayable.__init__(self, shape, color, radius)

    
    def action(self):

        neighbors = self.getAgentsInSameCell()

        for neig in neighbors:
            if type(neig) == PersonAgent:
                print(neig)
                self.model.grid.remove_agent(neig)
                self.model.schedule.remove(neig)
    
    
class PersonAgent(Character, Portrayable):

    def __init__(self, unique_id, model, shape, color, radius):
        Character.__init__(self, unique_id, model)
        Portrayable.__init__(self, shape, color, radius)
        

class HeroAgent(Character, Portrayable):

    def __init__(self, unique_id, model, shape, color, radius):
        Character.__init__(self, unique_id, model)
        Portrayable.__init__(self, shape, color, radius)


class TurnIntoHeroAgent(Agent, Portrayable):  

    def __init__(self, unique_id, model, shape, color, radius):
        Agent.__init__(self, unique_id, model)
        Portrayable.__init__(self, shape, color, radius)

