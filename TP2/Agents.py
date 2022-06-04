from Portrayal import Portrayal
from character import Character
from mesa import Agent
import Utils


class MonsterAgent(Character, Portrayal):

    def __init__(self, unique_id, model, shape, color, radius, hp, hp_decrease, damage_per_second):
        Character.__init__(self, unique_id, model, hp, hp_decrease, damage_per_second)
        Portrayal.__init__(self, shape, color, radius)
        self.maxHP = hp

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

    def __init__(self, unique_id, model, shape, color, radius, hp, hp_decrease, damage_per_second):
        Character.__init__(self, unique_id, model, hp, hp_decrease, damage_per_second)
        Portrayal.__init__(self, shape, color, radius)

    def action(self):

        neighbors = self.getAgentsInSameCell()

        for neig in neighbors:
            if type(neig).__name__ == "Fruit":
                if neig.state["state"] == "BadQuality":
                    self.state = {"state": "TurningMonster"}
                    break
                if self.hp < 100:
                    self.hp += neig.levelOfHPRecovery
                    self.grid.remove_agent(neig)
                    self.model.schedule.remove(neig)
                else:
                    self.grid.remove_agent(neig)
                    self.model.schedule.remove(neig)
                    if neig.state["state"] == "GoodQuality":
                        self.state = {"state": "TurningHero"}

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

    def __init__(self, unique_id, model, shape, color, radius, hp, hp_decrease, damage_per_second):
        Character.__init__(self, unique_id, model, hp, hp_decrease, damage_per_second)
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
        self.levelOfHPRecovery = 15
        self.probTurningToMonster = 20
        self.state = {"state": "GoodQuality"}

    def step(self):

        rotIncrease = self.random.randrange(0, 5)
        levelDecreaseHP = self.random.randrange(0, 9)

        self.levelRotRottenness += rotIncrease

        if self.levelOfHPRecovery - levelDecreaseHP >= 0:
            self.levelOfHPRecovery -= levelDecreaseHP

        if self.levelRotRottenness > 5:
            self.set_color("#618358")
            self.state = {"state": "BadQuality"}
