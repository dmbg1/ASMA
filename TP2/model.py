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

    def __init__(self, N, width, height, init_humans, init_monsters, init_heroes, init_food, generateQuantityFruit,
                 human_reproduction, monster_reproduction, hero_reproduction, human_HPDecrease, monster_HPDecrease,
                 hero_HPDecrease, probTurningHero):

        self.num_agents = N

        if init_humans + init_heroes + init_food + init_monsters > 1:
            print("Error: The sum of the given percentages is higher than 100%! (Empty grid given)")
            init_humans = init_food = init_heroes = init_monsters = 0

        self.init_humans = init_humans
        self.init_monsters = init_monsters
        self.init_heroes = init_heroes
        self.init_food = init_food

        self.generateQuantityFruit = generateQuantityFruit
        self.human_reproduction = human_reproduction
        self.monster_reproduction = monster_reproduction
        self.hero_reproduction = hero_reproduction

        self.human_HPDecrease = human_HPDecrease
        self.monster_HPDecrease = monster_HPDecrease
        self.hero_HPDecrease = hero_HPDecrease

        self.probTurningHero = probTurningHero

        self.grid = MultiGrid(width, height, True)
        self.schedule = RandomActivation(self)
        self.numSteps = 0

        self.monster_num_deaths_by_kill = 0
        self.monster_num_deaths_by_hunger = 0
        self.human_num_deaths_by_kill = 0
        self.human_num_deaths_by_hunger = 0
        self.hero_num_deaths_by_kill = 0
        self.hero_num_deaths_by_hunger = 0

        self.num_human_reproductions = 0
        self.num_monster_reproductions = 0
        self.num_hero_reproductions = 0

        self.datacollector = DataCollector({
            'Human Pop.': 'human',
            'Hero Pop.': 'hero',
            'Monster Pop.': 'monster'})
        self.deaths_collector = DataCollector({
            'Human Deaths by enemy attacks': 'humanNumDeathsByKill',
            'Human Deaths by hunger': 'humanNumDeathsByHunger',
            'Monster Deaths by enemy attacks': 'monsterNumDeathsByKill',
            'Monster Deaths by hunger': 'monsterNumDeathsByHunger',
            'Hero Deaths by enemy attacks': 'heroNumDeathsByKill',
            'Hero Deaths by hunger': 'heroNumDeathsByHunger'
        })
        self.human_reproduction_collector = DataCollector({
            "Human Reproduction amount": "numHumanReproductions"
        })
        self.monster_reproduction_collector = DataCollector({
            "Monster Reproduction amount": "numMonsterReproductions"
        })
        self.hero_reproduction_collector = DataCollector({
            "Hero Reproduction amount": "numHeroReproductions"
        })

        self.id = 0
        self.generateAgents()

    def generateAgents(self):

        numHeroes = int(self.num_agents * self.init_heroes)
        numMonsters = int(self.num_agents * self.init_monsters)
        numPersons = int(self.num_agents * self.init_humans)
        numFoods = int(self.num_agents * self.init_food)

        availablePositions = []

        for i in range(self.grid.width):
            for j in range(self.grid.height):
                availablePositions.append((i, j))

        for _ in range(numHeroes):
            self.createAgent("HeroAgent", availablePositions=availablePositions)

        for _ in range(numMonsters):
            self.createAgent("MonsterAgent", availablePositions=availablePositions)

        for _ in range(numPersons):
            self.createAgent("PersonAgent", availablePositions=availablePositions)

        for _ in range(numFoods):
            self.createAgent("Fruit", availablePositions=availablePositions)

        self.datacollector.collect(self)

    def createAgent(self, type, pos=None, availablePositions=None, noReprodSteps=0):

        if type == "HeroAgent":
            a = agent.HeroAgent(self.id, self, "circle", "blue", 0.7, 100, self.hero_HPDecrease, 30, noReprodSteps,
                                self.hero_reproduction)
            self.schedule.add(a)
            self.setAgentPosition(a, pos, availablePositions)
            self.id += 1
        elif type == "MonsterAgent":
            a = agent.MonsterAgent(self.id, self, "circle", "red", 0.8, 100, self.monster_HPDecrease, 20, noReprodSteps,
                                   self.monster_reproduction)
            self.schedule.add(a)
            self.setAgentPosition(a, pos, availablePositions)
            self.id += 1
        elif type == "PersonAgent":
            a = agent.PersonAgent(self.id, self, "circle", "black", 0.6, 100, self.human_HPDecrease, 0, noReprodSteps,
                                  self.human_reproduction, self.probTurningHero)
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
                self.createAgent("MonsterAgent", pos=a.pos, noReprodSteps=a.noReprodSteps)
                self.removeAgent(a)
            elif a.state["state"] == "TurningHero":
                self.createAgent("HeroAgent", pos=a.pos, noReprodSteps=a.noReprodSteps)
                self.removeAgent(a)

            if type(a).__name__ == "Fruit":
                if a.levelRotRottenness >= 20:
                    self.removeAgent(a)

        if self.numSteps % 5 == 0:
            for _ in range(self.generateQuantityFruit):
                self.createAgent("Fruit", pos=None)

        self.datacollector.collect(self)
        self.deaths_collector.collect(self)
        self.human_reproduction_collector.collect(self)
        self.monster_reproduction_collector.collect(self)
        self.hero_reproduction_collector.collect(self)

        for a in self.schedule.agents:
            if a.__class__ == Agents.MonsterAgent:
                return
        self.running = False

    def running(self):
        self.step()

    def incr_num_deaths_by_kill(self, agent_type):
        if agent_type == "MonsterAgent":
            self.monster_num_deaths_by_kill += 1
        elif agent_type == "PersonAgent":
            self.human_num_deaths_by_kill += 1
        elif agent_type == "HeroAgent":
            self.hero_num_deaths_by_kill += 1

    def incr_num_deaths_by_hunger(self, agent_type):
        if agent_type == "MonsterAgent":
            self.monster_num_deaths_by_hunger += 1
        elif agent_type == "PersonAgent":
            self.human_num_deaths_by_hunger += 1
        elif agent_type == "HeroAgent":
            self.hero_num_deaths_by_hunger += 1

    def incr_num_deaths_by_hunger(self, agent_type):
        if agent_type == "MonsterAgent":
            self.monster_num_deaths_by_hunger += 1
        elif agent_type == "PersonAgent":
            self.human_num_deaths_by_hunger += 1
        elif agent_type == "HeroAgent":
            self.hero_num_deaths_by_hunger += 1

    def incr_num_reproductions(self, agent_type):
        if agent_type == "MonsterAgent":
            self.num_monster_reproductions += 1
        elif agent_type == "PersonAgent":
            self.num_human_reproductions += 1
        elif agent_type == "HeroAgent":
            self.num_hero_reproductions += 1

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
    def humanNumDeathsByKill(self):
        return self.human_num_deaths_by_kill

    @property
    def humanNumDeathsByHunger(self):
        return self.human_num_deaths_by_hunger

    @property
    def monsterNumDeathsByKill(self):
        return self.monster_num_deaths_by_kill

    @property
    def monsterNumDeathsByHunger(self):
        return self.monster_num_deaths_by_hunger

    @property
    def heroNumDeathsByKill(self):
        return self.hero_num_deaths_by_kill

    @property
    def heroNumDeathsByHunger(self):
        return self.hero_num_deaths_by_hunger

    @property
    def numHumanReproductions(self):
        return self.num_human_reproductions

    @property
    def numMonsterReproductions(self):
        return self.num_monster_reproductions

    @property
    def numHeroReproductions(self):
        return self.num_hero_reproductions


def main():
    model = MonstersVsHeroes
    env.createAndStartServer(model)


if __name__ == "__main__":
    main()
