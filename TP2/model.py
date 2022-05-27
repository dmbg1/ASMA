import random
from mesa.space import MultiGrid
from sched import scheduler
from mesa import Model
from mesa.time import RandomActivation
from heroagent import HeroAgent
from monsteragent import MonsterAgent

from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer

class MonstersVsHeros(Model):
    """A model with some number of agents."""

    def __init__(self, N, width, height):
        self.num_agents = N
        self.grid = MultiGrid(width, height, True)
        self.schedule = RandomActivation(self)
        self.genereteAgents()


    def genereteAgents(self):

        for i in range(self.num_agents):
            r = random.randint(0, 1)

            if r == 0:
                a = HeroAgent(i, self)
            else:
                a = MonsterAgent(i, self)

            self.schedule.add(a)

            # Add the agent to a random grid cell
            x = self.random.randrange(self.grid.width)
            y = self.random.randrange(self.grid.height)
            self.grid.place_agent(a, (x, y))

    
    def step(self):
        self.schedule.step()


    def running(self):
        for i in range(20):
            self.step()


def agents_portrayal(agent):
    return agent.portrayal()


grid = CanvasGrid(agents_portrayal, 20, 20, 500, 500)


server = ModularServer(MonstersVsHeros,
                       [grid],
                       "Monsters VS Heros",
                       {"N":50, "width":20, "height":20})

server.port = 8521 # The default
server.launch()


