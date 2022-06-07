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

    def __init__(self, N, width, height, initHumans, initMonsters, initHeroes, initFood, generateQuantityFood,
                 humanReproduction, monsterReproduction, heroReproduction, humanHpDecrease, monsterHpDecrease,
                 heroHpDecrease, probTurningHero):

        # If percentages of population sum is higher than 100% error
        if initHumans + initHeroes + initFood + initMonsters > 1:
            print("Error: The sum of the given percentages is higher than 100%! (Empty grid given)")
            initHumans = initFood = initHeroes = initMonsters = 0

        # Population parameters
        self.numAgents = N
        self.initHumans = initHumans
        self.initMonsters = initMonsters
        self.initHeroes = initHeroes
        self.initFood = initFood

        # Quantity of food generated periodically
        self.generateQuantityFood = generateQuantityFood

        # Reproduction variables (true/false)
        self.humanReproduction = humanReproduction
        self.monsterReproduction = monsterReproduction
        self.heroReproduction = heroReproduction

        # Hp decrease by tick/step (food factor)
        self.humanHpDecrease = humanHpDecrease
        self.monsterHpDecrease = monsterHpDecrease
        self.heroHpDecrease = heroHpDecrease

        # Probability of a human turning into a hero when good quality food is consumed
        self.probTurningHero = probTurningHero

        # Initialize grid and scheduler
        self.grid = MultiGrid(width, height, True)
        self.schedule = RandomActivation(self)

        self.numSteps = 0

        # Deaths data
        self.monsterNumDeathsByKill = 0
        self.monsterNumDeathsByHunger = 0
        self.humanNumDeathsByKill = 0
        self.humanNumDeathsByHunger = 0
        self.heroNumDeathsByKill = 0
        self.heroNumDeathsByHunger = 0

        # Reproduction data
        self.numHumanReproductions = 0
        self.numMonsterReproductions = 0
        self.numHeroReproductions = 0

        # Data Collectors for graphics
        self.datacollector = DataCollector({
            'Human Pop.': 'humanPop',
            'Hero Pop.': 'heroPop',
            'Monster Pop.': 'monsterPop'})
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
        numHumans = int(self.numAgents * self.initHumans)
        numFoods = int(self.numAgents * self.initFood)

        availablePositions = []

        for i in range(self.grid.width):
            for j in range(self.grid.height):
                availablePositions.append((i, j))

        # Start agents at an initial age that permits reproduction in different positions
        for _ in range(numHeroes):
            self.createAgent("HeroAgent", 150 * 0.2, availablePositions=availablePositions)

        for _ in range(numMonsters):
            self.createAgent("MonsterAgent", 75 * 0.2, availablePositions=availablePositions)

        for _ in range(numHumans):
            self.createAgent("HumanAgent", 75 * 0.2, availablePositions=availablePositions)

        for _ in range(numFoods):
            self.createAgent("Food", 0, availablePositions=availablePositions)

        self.datacollector.collect(self)

    def createAgent(self, type_, age, pos=None, availablePositions=None):

        if type_ == "HeroAgent":
            a = agent.HeroAgent(self.id, self, "circle", "blue", 0.7, 100, self.heroHpDecrease, 20,
                                self.heroReproduction, age, 150)
            self.schedule.add(a)
            self.setAgentPosition(a, pos, availablePositions)
            self.id += 1
        elif type_ == "MonsterAgent":
            a = agent.MonsterAgent(self.id, self, "circle", "red", 0.8, 100, self.monsterHpDecrease, 20,
                                   self.monsterReproduction, age, 75)
            self.schedule.add(a)
            self.setAgentPosition(a, pos, availablePositions)
            self.id += 1
        elif type_ == "HumanAgent":
            a = agent.HumanAgent(self.id, self, "circle", "black", 0.6, 100, self.
                                 humanHpDecrease, 0, self.humanReproduction, self.probTurningHero, age, 75)
            self.schedule.add(a)
            self.setAgentPosition(a, pos, availablePositions)
            self.id += 1
        else:
            a = agent.Food(self.id, self, "circle", "green", 0.4)
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

    def removeAgent(self, a):
        self.grid.remove_agent(a)
        self.schedule.remove(a)

    def step(self):
        self.schedule.step()
        self.numSteps += 1

        # Update world according to some agents' states
        for a in self.schedule.agents:
            if a.state["state"] == "TurningMonster":
                self.createAgent("MonsterAgent", a.age, pos=a.pos)
                self.removeAgent(a)
            elif a.state["state"] == "TurningHero":
                self.createAgent("HeroAgent", a.age, pos=a.pos)
                self.removeAgent(a)

            if type(a).__name__ == "Food":
                if a.levelOfRottenness >= 20:
                    self.removeAgent(a)

        # Generate food
        if self.numSteps % 5 == 0:
            for _ in range(self.generateQuantityFood):
                self.createAgent("Food", 0, pos=None)

        # Collect data
        self.datacollector.collect(self)
        self.deathsCollector.collect(self)
        self.humanReproductionCollector.collect(self)
        self.monsterReproductionCollector.collect(self)
        self.heroReproductionCollector.collect(self)

        # End simulation if monster agent is extinct
        for a in self.schedule.agents:
            if a.__class__ == Agents.MonsterAgent:
                return
        self.running = False

    def running(self):
        self.step()

    def incrNumDeathsByKill(self, agentType):
        if agentType == "MonsterAgent":
            self.monsterNumDeathsByKill += 1
        elif agentType == "HumanAgent":
            self.humanNumDeathsByKill += 1
        elif agentType == "HeroAgent":
            self.heroNumDeathsByKill += 1

    def incrNumDeathsByHunger(self, agentType):
        if agentType == "MonsterAgent":
            self.monsterNumDeathsByHunger += 1
        elif agentType == "HumanAgent":
            self.humanNumDeathsByHunger += 1
        elif agentType == "HeroAgent":
            self.heroNumDeathsByHunger += 1

    def incrNumReproductions(self, agentType):
        if agentType == "MonsterAgent":
            self.numMonsterReproductions += 1
        elif agentType == "HumanAgent":
            self.numHumanReproductions += 1
        elif agentType == "HeroAgent":
            self.numHeroReproductions += 1

    # DATA FOR GRAPHICS AND HISTOGRAMS

    # Population Graphic Data
    @property
    def humanPop(self):
        agents = self.schedule.agents
        amount = 0
        for a in agents:
            if a.__class__ == Agents.HumanAgent:
                amount += 1
        return amount

    @property
    def heroPop(self):
        agents = self.schedule.agents
        amount = 0
        for a in agents:
            if a.__class__ == Agents.HeroAgent:
                amount += 1
        return amount

    @property
    def monsterPop(self):
        agents = self.schedule.agents
        amount = 0
        for a in agents:
            if a.__class__ == Agents.MonsterAgent:
                amount += 1
        return amount

    # Deaths Histogram Data
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

    # Reproduction Graphics Data
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
