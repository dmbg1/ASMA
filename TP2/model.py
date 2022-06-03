import random
from turtle import pos
from mesa.space import MultiGrid
from mesa import Model
from mesa.time import RandomActivation
import Agents as agent
import environment as env

# TODO
# Colocar paredes 
# Comida e mutações provocadas pela comida (gerando periodicamente em posiçoes aleatorias)
# Vida das pessoas vai descendo
# Priorizar os monstros e dps as pessoas nos paths dos heróis
# Priorizar as pessoas e dps os herois nos paths dos monstros
# HP, DPS (aleatório) e %dano absorvido
# Lutar parados numa células
# Aumentar atributos de defesa (hp) e de ataque ao longo do tempo

class MonstersVsHeros(Model):
    """A model with some number of agents."""

    def __init__(self, N, width, height):
        self.num_agents = N
        self.grid = MultiGrid(width, height, True)
        self.schedule = RandomActivation(self)
        self.generateAgents()

    def generateAgents(self):

        numHeroes = int(self.num_agents * 0.2)
        numMonsters = int(self.num_agents * 0.3)
        numPersons = int(self.num_agents * 0.45)
        numFoods = int(self.num_agents * 0.05)
        id = 0

        for _ in range(numHeroes):
            a = agent.HeroAgent(id, self, "circle", "blue", 0.7, 0)
            self.schedule.add(a)
            self.setAgentPosition(a)
            id+=1
            
        for _ in range(numMonsters):
            a = agent.MonsterAgent(id, self, "circle", "red", 0.8, 0)
            self.schedule.add(a)
            self.setAgentPosition(a)
            id+=1
            
        for _ in range(numPersons):
            a = agent.PersonAgent(id, self, "circle", "black", 0.6, 0)
            self.schedule.add(a)
            self.setAgentPosition(a)
            id+=1
        
        for _ in range(numFoods):
            a = agent.TurnIntoHeroAgent(id, self, "circle", "green", 0.4)
            self.schedule.add(a)
            self.setAgentPosition(a)
            id+=1
        

    def setAgentPosition(self, a):
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
