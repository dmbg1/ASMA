from Portrayal import Portrayal
from character import Character
from mesa import Agent
import Utils


class MonsterAgent(Character, Portrayal):

    def __init__(self, unique_id, model, shape, color, radius, hp, hp_decrease, damage_per_second,
                 canReproduce):
        Character.__init__(self, unique_id, model, hp, hp_decrease, damage_per_second, canReproduce)
        Portrayal.__init__(self, shape, color, radius)
        self.maxHP = hp

    def action(self):
        if self.state["state"] == "InFight":
            enemy = self.state["enemy"]
            self.hurtEnemy(enemy)
            if enemy.hp <= 0:
                if "heal" in self.state:
                    heal = self.state["heal"]
                    self.hp = heal + self.hp if heal + self.hp < self.maxHP else self.maxHP
                self.state = {"state": "Move"}
        else:
            neighbors = self.getAgentsInSameCell()

            for neig in neighbors:
                if type(neig).__name__ == "PersonAgent":
                    self.state = {
                        "state": "InFight",
                        "enemy": neig,
                        "heal": neig.hp  # Heal if enemy is killed
                    }
                    neig.set_state({
                        "state": "InFight",
                        "enemy": self
                    })
                    self.hurtEnemy(neig)
                    if neig.hp <= 0:
                        self.state = {"state": "Move"}
                    continue  # One enemy at a time

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

    def __init__(self, unique_id, model, shape, color, radius, hp, hp_decrease, damage_per_second,
                 canReproduce, probTurningHero):
        Character.__init__(self, unique_id, model, hp, hp_decrease, damage_per_second, canReproduce)
        Portrayal.__init__(self, shape, color, radius)

        self.probTurningHero = probTurningHero

    def action(self):

        neighbors = self.getAgentsInSameCell()

        if self.state["state"] == "InFight":
            enemy = self.state["enemy"]
            self.hurtEnemy(enemy)
            if enemy.hp <= 0:
                self.state = {"state": "Move"}

        for neig in neighbors:
            if type(neig).__name__ == "Fruit":
                self.grid.remove_agent(neig)
                self.model.schedule.remove(neig)
                if neig.state["state"] == "BadQuality":
                    self.state = {"state": "TurningMonster"}
                else:
                    if self.random.randrange(0, 101) >= self.probTurningHero:
                        self.hp += 75
                    else:
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

    def __init__(self, unique_id, model, shape, color, radius, hp, hp_decrease, damage_per_second,
                 canReproduce):
        Character.__init__(self, unique_id, model, hp, hp_decrease, damage_per_second, canReproduce)
        Portrayal.__init__(self, shape, color, radius)

    def action(self):
        if self.state["state"] == "InFight":
            enemy = self.state["enemy"]
            self.hurtEnemy(enemy)
            if enemy.hp <= 0:
                self.state = {"state": "Move"}
        else:
            neighbors = self.getAgentsInSameCell()

            for neig in neighbors:
                if type(neig).__name__ == "MonsterAgent":
                    self.state = {
                        "state": "InFight",
                        "enemy": neig
                    }
                    neig.set_state({
                        "state": "InFight",
                        "enemy": self
                    })
                    self.hurtEnemy(neig)
                    if neig.hp <= 0:
                        # self.datacollector2.collect(self)
                        self.state = {"state": "Move"}
                    continue  # One enemy at a time

    def chooseBestPosition(self, possible_steps):

        nearAgents = self.getNearAgents(7)
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
        self.levelOfHPRecovery = 30
        self.probTurningToMonster = 20
        self.numSteps = 0
        self.state = {"state": "GoodQuality"}

    def step(self):

        self.numSteps += 1

        rotIncrease = self.random.randrange(0, 4)
        levelDecreaseHP = self.random.randrange(0, 9)

        self.levelRotRottenness += rotIncrease

        if self.levelOfHPRecovery - levelDecreaseHP >= 0:
            self.levelOfHPRecovery -= levelDecreaseHP

        if self.levelRotRottenness > 15:
            self.set_color("#618358")
            self.state = {"state": "BadQuality"}
