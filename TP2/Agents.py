from Portrayal import Portrayal
from character import Character
from mesa import Agent
import Utils


class MonsterAgent(Character, Portrayal):

    def __init__(self, unique_id, model, shape, color, radius, hp, hp_decrease, damage_per_second,
                 canReproduce, age, maxAge):
        Character.__init__(self, unique_id, model, hp, hp_decrease, damage_per_second, canReproduce, age, maxAge)
        Portrayal.__init__(self, shape, color, radius)
        self.maxHP = hp

    def agentsInSameCellAction(self, sameCellAgent):
        if type(sameCellAgent).__name__ == "PersonAgent":  # Start Fight
            self.state = {
                "state": "InFight",
                "enemy": sameCellAgent,
                "heal": sameCellAgent.hp  # Heal if enemy is killed
            }
            sameCellAgent.set_state({
                "state": "InFight",
                "enemy": self
            })
            self.inFightAction()

    def action(self):
        if self.state["state"] == "InFight":
            self.inFightAction()
        else:  # Check agents in same cell
            sameCellAgents = self.getAgentsInSameCell()

            for a in sameCellAgents:
                self.agentsInSameCellAction(a)
                if self.state["state"] == "InFight":
                    break  # One enemy at a time
                # No reproduction if agent is in a fight
                self.reproduction(a)

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
                 canReproduce, probTurningHero, age, maxAge):
        Character.__init__(self, unique_id, model, hp, hp_decrease, damage_per_second, canReproduce, age, maxAge)
        Portrayal.__init__(self, shape, color, radius)

        self.probTurningHero = probTurningHero

    def agentsInSameCellAction(self, sameCellAgent):
        if type(sameCellAgent).__name__ == "Fruit":
            self.grid.remove_agent(sameCellAgent)
            self.model.schedule.remove(sameCellAgent)
            if sameCellAgent.state["state"] == "BadQuality":  # Degraded food consumption
                self.state = {"state": "TurningMonster"}
            else:
                if self.random.randrange(0, 101) >= self.probTurningHero:
                    self.hp += 75
                else:
                    self.state = {"state": "TurningHero"}

    def action(self):

        sameCellAgents = self.getAgentsInSameCell()

        if self.state["state"] == "InFight":  # In fight action
            self.inFightAction()

        for a in sameCellAgents:
            self.agentsInSameCellAction(a)
            # No reproduction if agent is in a fight
            if self.state["state"] != "InFight":
                self.reproduction(a)

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
                 canReproduce, age, maxAge):
        Character.__init__(self, unique_id, model, hp, hp_decrease, damage_per_second, canReproduce, age, maxAge)
        Portrayal.__init__(self, shape, color, radius)

    def agentsInSameCellAction(self, sameCellAgent):
        if type(sameCellAgent).__name__ == "MonsterAgent":
            self.state = {
                "state": "InFight",
                "enemy": sameCellAgent
            }
            sameCellAgent.set_state({
                "state": "InFight",
                "enemy": self
            })
            self.inFightAction()

    def action(self):
        if self.state["state"] == "InFight":
            self.inFightAction()
        else:
            sameCellAgents = self.getAgentsInSameCell()

            for a in sameCellAgents:
                self.agentsInSameCellAction(a)
                if self.state["state"] == "InFight":
                    break  # One enemy at a time
                # No reproduction if agent is in a fight
                self.reproduction(a)

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

        self.levelOfRottenness = 0
        self.levelOfHPRecovery = 30
        self.probTurningToMonster = 20
        self.state = {"state": "GoodQuality"}

    def step(self):

        rotIncrease = self.random.randrange(0, 4)
        levelDecreaseHP = self.random.randrange(0, 9)

        self.levelOfRottenness += rotIncrease

        if self.levelOfHPRecovery - levelDecreaseHP >= 0:
            self.levelOfHPRecovery -= levelDecreaseHP

        if self.levelOfRottenness > 15:
            self.set_color("yellow")
            self.state = {"state": "BadQuality"}
