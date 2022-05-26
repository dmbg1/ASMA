from mesa.space import MultiGrid
from sched import scheduler
from mesa import Agent, Model
from mesa.time import RandomActivation
from heroagent import HeroAgent

from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer

class MonstersVsHeros(Model):
    """A model with some number of agents."""

    def __init__(self, N, width, height):
        self.num_agents = N
        self.grid = MultiGrid(width, height, True)
        self.schedule = RandomActivation(self)

        # Create agents
        for i in range(self.num_agents):
            a = HeroAgent(i, self)
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


def agent_portrayal(agent):
    portrayal = {"Shape": "circle",
                 "Color": "red",
                 "Filled": "true",
                 "Layer": 0,
                 "r": 0.5}
    return portrayal


grid = CanvasGrid(agent_portrayal, 10, 10, 500, 500)


server = ModularServer(MonstersVsHeros,
                       [grid],
                       "Monsters VS Heros",
                       {"N":100, "width":10, "height":10})

server.port = 8521 # The default
server.launch()


