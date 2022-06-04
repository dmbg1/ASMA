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
# Vida das pessoas vai descendo (done)
# Priorizar os monstros e dps as pessoas nos paths dos heróis (done)
# Priorizar as pessoas e dps os herois nos paths dos monstros (done)
# HP, DPS (aleatório) e %dano absorvido
# Lutar parados numa células

# Aumentar atributos de defesa (hp) e de ataque ao longo do tempo

class MonstersVsHeroes(Model):
    """A model with some number of agents."""

    def __init__(self, N, width, height, init_humans, init_monsters, init_heroes, init_food):
        self.num_agents = N
        self.init_humans = init_humans
        self.init_monsters = init_monsters
        self.init_heroes = init_heroes
        self.init_food = init_food
        self.grid = MultiGrid(width, height, True)
        self.schedule = RandomActivation(self)
        self.numSteps = 0
        self.id = 0
        self.generateAgents()

    def generateAgents(self):

        numHeroes = round(self.num_agents * self.init_heroes)
        numMonsters = round(self.num_agents * self.init_monsters)
        numPersons = round(self.num_agents * self.init_humans)
        numFoods = round(self.num_agents * self.init_food)

        for _ in range(numHeroes):
            self.createAgent("HeroAgent")

        for _ in range(numMonsters):
            self.createAgent("MonsterAgent")

        for _ in range(numPersons):
            self.createAgent("PersonAgent")

        for _ in range(numFoods):
            self.createAgent("Fruit")

    def createAgent(self, type, pos=None):

        if type == "HeroAgent":
            a = agent.HeroAgent(self.id, self, "circle", "blue", 0.7, 100, 0)
            self.schedule.add(a)
            self.setAgentPosition(a, pos)
            self.id += 1
        elif type == "MonsterAgent":
            a = agent.MonsterAgent(self.id, self, "circle", "red", 0.8, 100, 20)
            self.schedule.add(a)
            self.setAgentPosition(a, pos)
            self.id += 1
        elif type == "PersonAgent":
            a = agent.PersonAgent(self.id, self, "circle", "black", 0.6, 100, 20)
            self.schedule.add(a)
            self.setAgentPosition(a, pos)
            self.id += 1
        else:
            a = agent.Fruit(self.id, self, "circle", "green", 0.4)
            self.schedule.add(a)
            self.setAgentPosition(a, pos)
            self.id += 1

    def setAgentPosition(self, a, pos):

        if pos is None:
            x = self.random.randrange(self.grid.width)
            y = self.random.randrange(self.grid.height)
            self.grid.place_agent(a, (x, y))
        else:
            self.grid.place_agent(a, pos)

    def removeAgent(self, agent):
        self.grid.remove_agent(agent)
        self.schedule.remove(agent)

    def step(self):
        self.schedule.step()
        self.numSteps += 1

        for a in self.schedule.agents:
            if a.state == "TurningMonster":
                self.createAgent("MonsterAgent", a.pos)
                self.removeAgent(a)
            elif a.state == "TurningHero":
                self.createAgent("HeroAgent", a.pos)
                self.removeAgent(a)

        if self.numSteps % 5 == 0:
            for _ in range(self.random.randrange(0, 10)):
                self.createAgent("Fruit", None)

    def running(self):
        self.step()


def main():
    model = MonstersVsHeroes
    env.createAndStartServer(model)


if __name__ == "__main__":
    main()
