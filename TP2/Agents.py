from Portrayal import Portrayal
from character import Character
from mesa import Agent
import Utils


class MonsterAgent(Character, Portrayal):

    def __init__(self, unique_id, model, shape, color, radius, hp, hp_decrease):
        Character.__init__(self, unique_id, model, hp, hp_decrease)
        Portrayal.__init__(self, shape, color, radius)

    def action(self):

        neighbors = self.getAgentsInSameCell()

        for neig in neighbors:
            if type(neig).__name__ == "PersonAgent":
                self.hp += int(neig.hp * 0.5)
                self.grid.remove_agent(neig)
                self.model.schedule.remove(neig)

    def chooseBestPosition(self, possible_steps):

        nearAgents = self.getNearAgents(4)
        nearAgent = Utils.getNearAgent(self, nearAgents)

        if nearAgent == -1:
            return self.random.choice(possible_steps)

        if "PersonAgent" == type(nearAgent).__name__:
            return Utils.getNearPoint(nearAgent.pos, possible_steps)

        if "MonsterAgent" == type(nearAgent).__name__:
            return Utils.getFurtherPoint(nearAgent.pos, possible_steps)

        return self.random.choice(possible_steps)


class PersonAgent(Character, Portrayal):

    def __init__(self, unique_id, model, shape, color, radius, hp, hp_decrease):
        Character.__init__(self, unique_id, model, hp, hp_decrease)
        Portrayal.__init__(self, shape, color, radius)

    def action(self):

        neighbors = self.getAgentsInSameCell()

        for neig in neighbors:
            if type(neig).__name__ == "Fruit":
                if self.hp < 100:
                    self.hp += neig.levelOfHPREcovery
                    self.grid.remove_agent(neig)
                    self.model.schedule.remove(neig)
                else:
                    self.grid.remove_agent(neig)
                    self.model.schedule.remove(neig)
                    if neig.state == "GoodQuality":
                        self.state = "TurningHero"
                    else:
                        self.state = "TurningMonster"

    def chooseBestPosition(self, possible_steps):

        nearAgents = self.getNearAgents(6)
        nearAgent = Utils.getNearAgent(self, nearAgents)

        if nearAgent == -1:
            return self.random.choice(possible_steps)

        if "MonsterAgent" == type(nearAgent).__name__:
            return Utils.getFurtherPoint(nearAgent.pos, possible_steps)

        if "Fruit" == type(nearAgent).__name__:
            return Utils.getNearPoint(nearAgent.pos, possible_steps)

        if "PersonAgent" == type(nearAgent).__name__:
            return Utils.getNearPoint(nearAgent.pos, possible_steps)

        return self.random.choice(possible_steps)


class HeroAgent(Character, Portrayal):

    def action(self):
        pass

    def __init__(self, unique_id, model, shape, color, radius, hp, hp_decrease):
        Character.__init__(self, unique_id, model, hp, hp_decrease)
        Portrayal.__init__(self, shape, color, radius)

    def chooseBestPosition(self, possible_steps):

        nearAgents = self.getNearAgents(6)
        nearAgent = Utils.getNearAgent(self, nearAgents)

        if nearAgent == -1:
            return self.random.choice(possible_steps)

        if "MonsterAgent" == type(nearAgent).__name__:
            return Utils.getNearPoint(nearAgent.pos, possible_steps)

        if "PersonAgent" == type(nearAgent).__name__:
            return Utils.getNearPoint(nearAgent.pos, possible_steps)

        if "HeroAgent" == type(nearAgent).__name__:
            return Utils.getFurtherPoint(nearAgent.pos, possible_steps)

        return self.random.choice(possible_steps)


class Fruit(Agent, Portrayal):

    def __init__(self, unique_id, model, shape, color, radius):
        Agent.__init__(self, unique_id, model)
        Portrayal.__init__(self, shape, color, radius)

        self.levelRotRottenness = 0
        self.levelOfHPREcovery = 15
        self.probTurningToMonster = 20
        self.state = "GoodQuality"

    def step(self):

        rotIncrease = self.random.randrange(0, 5)
        levelDecreaseHP = self.random.randrange(0, 9)

        self.levelRotRottenness += rotIncrease

        if self.levelOfHPREcovery - levelDecreaseHP >= 0:
            self.levelOfHPREcovery -= levelDecreaseHP

        if self.levelRotRottenness < 5:
            self.state == "BadQuality"
