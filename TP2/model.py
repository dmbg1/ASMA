import random
from turtle import pos
from mesa.space import MultiGrid
from mesa import Model
from mesa.time import RandomActivation
import Agents as agent
import environment as env


class MonstersVsHeros(Model):
    """A model with some number of agents."""

    def __init__(self, N, width, height):
        self.num_agents = N
        self.grid = MultiGrid(width, height, True)
        self.schedule = RandomActivation(self)
        self.generateAgents()

    def generateAgents(self):

        for i in range(self.num_agents):
            r = random.randint(0, 4)

            if r == 0:
                a = agent.HeroAgent(i, self, "circle", "blue", 0.7, 0)
            elif r == 1:
                a = agent.MonsterAgent(i, self, "circle", "red", 0.7, 0)
            elif r == 2:
                a = agent.PersonAgent(i, self, "circle", "black", 0.7, 0)
            else:
                a = agent.TurnIntoHeroAgent(i, self, "circle", "green", 0.4)

            self.schedule.add(a)

            # Add the agent to a random grid cell
            x = self.random.randrange(self.grid.width)
            y = self.random.randrange(self.grid.height)
            self.grid.place_agent(a, (x, y))
    
    def step(self):
        self.schedule.step()

    def running(self):
      self.step()

def main():
    model = MonstersVsHeros
    env.createAndStartServer(model)


if __name__ == "__main__":
    main()
