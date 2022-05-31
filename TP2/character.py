from abc import abstractmethod
from turtle import position
from mesa import Agent

class Character(Agent):
    """An agent with fixed initial wealth."""

    def __init__(self, unique_id, model):
        super().__init__(unique_id, model)
        
        self.model = model
    

    def move(self):
        possible_steps = self.model.grid.get_neighborhood(
            self.pos,
            moore=True,
            include_center=False)
        
        new_position = self.random.choice(possible_steps)

        self.chooseBestPosition(possible_steps)

        self.model.grid.move_agent(self, new_position)

    @abstractmethod
    def chooseBestPosition(self, possible_steps):
        pass

    @abstractmethod
    def action(self):
        pass

    def getAgentsInSameCell(self):
        return self.model.grid.get_neighbors(self.pos, False, True, 0)

    def getNearAgents(self, radius):
        return self.model.grid.get_neighbors(self.pos, False, False, radius)

    def step(self):
        self.move()
        self.action()