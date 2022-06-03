from abc import abstractmethod
from traceback import print_tb
from turtle import position
from mesa import Agent

class Character(Agent):
    """An agent with fixed initial wealth."""

    def __init__(self, unique_id, model, hp):
        super().__init__(unique_id, model)
        
        self.model = model
        self.hp = hp
    

    def move(self):
        possible_steps = self.model.grid.get_neighborhood(
            self.pos, 
            moore=True,
            include_center=False)
        
        new_position = self.chooseBestPosition(possible_steps)
        self.model.grid.move_agent(self, new_position)

    def getAgentsInSameCell(self):
        return self.model.grid.get_neighbors(self.pos, True, True, 0)

    def getNearAgents(self, radius):

        nearAgents = [] 

        for r in range(1, radius+1):
            nearAgents.append(self.model.grid.get_neighbors(self.pos, True, False, r))
        
        return [agent for subagents in nearAgents for agent in subagents]
    
    @abstractmethod
    def chooseBestPosition(self, possible_steps):
        pass

    @abstractmethod
    def action(self):
        pass

    def step(self):
        self.move()
        self.action()

