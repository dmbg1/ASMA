import random

from mesa.datacollection import DataCollector
from mesa.space import MultiGrid
from mesa import Model
from mesa.time import RandomActivation

import Agents
import Agents as agent
import environment as env


class MonstersVsHeroes(Model):
    """A model with some number of agents."""

    def __init__(self, N, width, height, initHumans, initMonsters, initHeroes, initFood, generateQuantityFruit,
                 humanReproduction, monsterReproduction, heroReproduction, humanHpDecrease, monsterHpDecrease,
                 heroHpDecrease, probTurningHero):

        self.numAgents = N

        if initHumans + initHeroes + initFood + initMonsters > 1:
            print("Error: The sum of the given percentages is higher than 100%! (Empty grid given)")
            initHumans = initFood = initHeroes = initMonsters = 0

        self.initHumans = initHumans
        self.initMonsters = initMonsters
        self.initHeroes = initHeroes
        self.initFood = initFood

        self.generateQuantityFruit = generateQuantityFruit
        self.humanReproduction = humanReproduction
        self.monsterReproduction = monsterReproduction
        self.heroReproduction = heroReproduction

        self.humanHpDecrease = humanHpDecrease
        self.monsterHpDecrease = monsterHpDecrease
        self.heroHpDecrease = heroHpDecrease

        self.probTurningHero = probTurningHero

        self.grid = MultiGrid(width, height, True)
        self.schedule = RandomActivation(self)
        self.numSteps = 0

        self.monsterNumDeathsByKill = 0
        self.monsterNumDeathsByHunger = 0
        self.humanNumDeathsByKill = 0
        self.humanNumDeathsByHunger = 0
        self.heroNumDeathsByKill = 0
        self.heroNumDeathsByHunger = 0

        self.numHumanReproductions = 0
        self.numMonsterReproductions = 0
        self.numHeroReproductions = 0

        self.datacollector = DataCollector({
            'Human Pop.': 'human',
            'Hero Pop.': 'hero',
            'Monster Pop.': 'monster'})
        self.deathsCollector = DataCollector({
            'Human Deaths by enemy attacks': 'humanNumDeathsByKill_',
            'Human Deaths by hunger': 'humanNumDeathsByHunger_',
            'Monster Deaths by enemy attacks': 'monsterNumDeathsByKill_',
            'Monster Deaths by hunger': 'monsterNumDeathsByHunger_',
            'Hero Deaths by enemy attacks': 'heroNumDeathsByKill_',
            'Hero Deaths by hunger': 'heroNumDeathsByHunger_'
        })
        self.humanReproductionCollector = DataCollector({
            "Human Reproduction amount": "numHumanReproductions_"
        })
        self.monsterReproductionCollector = DataCollector({
            "Monster Reproduction amount": "numMonsterReproductions_"
        })
        self.heroReproductionCollector = DataCollector({
            "Hero Reproduction amount": "numHeroReproductions_"
        })

        self.id = 0
        self.generateAgents()

    def generateAgents(self):

        numHeroes = int(self.numAgents * self.initHeroes)
        numMonsters = int(self.numAgents * self.initMonsters)
        numPersons = int(self.numAgents * self.initHumans)
        numFoods = int(self.numAgents * self.initFood)

        availablePositions = []

        for i in range(self.grid.width):
            for j in range(self.grid.height):
                availablePositions.append((i, j))

        # Start agents at an initial age that permits reproduction
        for _ in range(numHeroes):
            self.createAgent("HeroAgent", 150 * 0.2, availablePositions=availablePositions)

        for _ in range(numMonsters):
            self.createAgent("MonsterAgent", 75 * 0.2, availablePositions=availablePositions)

        for _ in range(numPersons):
            self.createAgent("PersonAgent", 75 * 0.2, availablePositions=availablePositions)

        for _ in range(numFoods):
            self.createAgent("Fruit", 0, availablePositions=availablePositions)

        self.datacollector.collect(self)

    def createAgent(self, type, age, pos=None, availablePositions=None):

        if type == "HeroAgent":
            a = agent.HeroAgent(self.id, self, "circle", "blue", 0.7, 100, self.heroHpDecrease, 20,
                                self.heroReproduction, age, 150)
            self.schedule.add(a)
            self.setAgentPosition(a, pos, availablePositions)
            self.id += 1
        elif type == "MonsterAgent":
            a = agent.MonsterAgent(self.id, self, "circle", "red", 0.8, 100, self.monsterHpDecrease, 20,
                                   self.monsterReproduction, age, 75)
            self.schedule.add(a)
            self.setAgentPosition(a, pos, availablePositions)
            self.id += 1
        elif type == "PersonAgent":
            a = agent.PersonAgent(self.id, self, "circle", "black", 0.6, 100, self.
                                  humanHpDecrease, 0,
                                  self.humanReproduction, self.probTurningHero, age, 75)
            self.schedule.add(a)
            self.setAgentPosition(a, pos, availablePositions)
            self.id += 1
        else:
            a = agent.Fruit(self.id, self, "circle", "green", 0.4)
            self.schedule.add(a)
            self.setAgentPosition(a, pos, availablePositions)
            self.id += 1

    def setAgentPosition(self, a, pos, availablePositions):
        if pos is None:
            if availablePositions is not None:
                pos = random.choice(availablePositions)
                availablePositions.remove(pos)
            else:
                pos = (self.random.randrange(self.grid.width), self.random.randrange(self.grid.height))

        self.grid.place_agent(a, pos)

    def removeAgent(self, agent):
        self.grid.remove_agent(agent)
        self.schedule.remove(agent)

    def step(self):
        self.schedule.step()
        self.numSteps += 1

        for a in self.schedule.agents:
            if a.state["state"] == "TurningMonster":
                self.createAgent("MonsterAgent", a.age, pos=a.pos)
                self.removeAgent(a)
            elif a.state["state"] == "TurningHero":
                self.createAgent("HeroAgent", a.age, pos=a.pos)
                self.removeAgent(a)

            if type(a).__name__ == "Fruit":
                if a.levelOfRottenness >= 20:
                    self.removeAgent(a)

        if self.numSteps % 5 == 0:
            for _ in range(self.generateQuantityFruit):
                self.createAgent("Fruit", 0, pos=None)

        self.datacollector.collect(self)
        self.deathsCollector.collect(self)
        self.humanReproductionCollector.collect(self)
        self.monsterReproductionCollector.collect(self)
        self.heroReproductionCollector.collect(self)

        for a in self.schedule.agents:
            if a.__class__ == Agents.MonsterAgent:
                return
        self.running = False

    def running(self):
        self.step()

    def incrNumDeathsByKill(self, agentType):
        if agentType == "MonsterAgent":
            self.monsterNumDeathsByKill += 1
        elif agentType == "PersonAgent":
            self.humanNumDeathsByKill += 1
        elif agentType == "HeroAgent":
            self.heroNumDeathsByKill += 1

    def incrNumDeathsByHunger(self, agentType):
        if agentType == "MonsterAgent":
            self.monsterNumDeathsByHunger += 1
        elif agentType == "PersonAgent":
            self.humanNumDeathsByHunger += 1
        elif agentType == "HeroAgent":
            self.heroNumDeathsByHunger += 1

    def incrNumReproductions(self, agentType):
        if agentType == "MonsterAgent":
            self.numMonsterReproductions += 1
        elif agentType == "PersonAgent":
            self.numHumanReproductions += 1
        elif agentType == "HeroAgent":
            self.numHeroReproductions += 1

    @property
    def human(self):
        agents = self.schedule.agents
        amount = 0
        for a in agents:
            if a.__class__ == Agents.PersonAgent:
                amount += 1
        return amount

    @property
    def hero(self):
        agents = self.schedule.agents
        amount = 0
        for a in agents:
            if a.__class__ == Agents.HeroAgent:
                amount += 1
        return amount

    @property
    def monster(self):
        agents = self.schedule.agents
        amount = 0
        for a in agents:
            if a.__class__ == Agents.MonsterAgent:
                amount += 1
        return amount

    @property
    def humanNumDeathsByKill_(self):
        return self.humanNumDeathsByKill

    @property
    def humanNumDeathsByHunger_(self):
        return self.humanNumDeathsByHunger

    @property
    def monsterNumDeathsByKill_(self):
        return self.monsterNumDeathsByKill

    @property
    def monsterNumDeathsByHunger_(self):
        return self.monsterNumDeathsByHunger

    @property
    def heroNumDeathsByKill_(self):
        return self.heroNumDeathsByKill

    @property
    def heroNumDeathsByHunger_(self):
        return self.heroNumDeathsByHunger

    @property
    def numHumanReproductions_(self):
        return self.numHumanReproductions

    @property
    def numMonsterReproductions_(self):
        return self.numMonsterReproductions

    @property
    def numHeroReproductions_(self):
        return self.numHeroReproductions


def main():
    model = MonstersVsHeroes
    env.createAndStartServer(model)


if __name__ == "__main__":
    main()
