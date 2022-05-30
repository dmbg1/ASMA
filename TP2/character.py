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
            moore=False,
            include_center=False)
        new_position = self.random.choice(possible_steps)
        self.model.grid.move_agent(self, new_position)

    
    def action(self):
        pass

    def getAgentsInSameCell(self):
        neighbors = self.model.grid.get_neighbors(self.pos, False, True, 0)
        return neighbors

    def step(self):
        self.move()
        self.action()