from abc import abstractmethod
from traceback import print_tb
from turtle import position
from mesa import Agent

import Agents
import Utils


class Character(Agent):
    """An agent with fixed initial wealth."""

    def __init__(self, unique_id, model, hp, hp_decrease, damage_per_second):
        super().__init__(unique_id, model)

        self.model = model
        self.grid = model.grid
        self.hp = hp
        self.hp_decrease = hp_decrease
        self.damage_per_second = damage_per_second
        self.state = {"state": "Move"}

    def move(self):
        possible_steps = self.grid.get_neighborhood(
            self.pos,
            moore=True,
            include_center=False)

        Utils.removeThroughWallSteps(self.pos, possible_steps, self.grid.width, self.grid.height)
        new_position = self.chooseBestPosition(possible_steps)
        self.grid.move_agent(self, new_position)

    def reproduction(self):

        neighbors = self.getAgentsInSameCell()

        for neigh in neighbors:
            if type(neigh).__name__ == type(self).__name__:
                for _ in range(
                        self.model.random.randrange(0, 1)):  # TODO: Alterar o 1 para 2 para os agentes se reproduzirem
                    self.model.createAgent(type(self).__name__)

    def getAgentsInSameCell(self):
        return self.grid.get_neighbors(self.pos, True, True, 0)

    def getNearAgents(self, radius):

        nearAgents = []

        for r in range(1, radius + 1):
            nearAgents.append(self.grid.get_neighbors(
                self.pos, True, False, r))

        return [agent for subAgents in nearAgents for agent in subAgents]

    @abstractmethod
    def chooseBestPosition(self, possible_steps):
        pass

    @abstractmethod
    def action(self):
        pass

    def set_hp(self, hp):
        self.hp = hp

    def hurtEnemy(self, enemy):
        enemy.set_hp(enemy.hp - self.damage_per_second)

    def step(self):
        self.move()
        self.action()
        self.reproduction()

        if self.state["state"] == "InFight":
            enemy = self.state["enemy"]
            self.hurtEnemy(enemy)
            if enemy.hp <= 0:
                self.state = {"state": "Move"}
                if self.__class__ == Agents.MonsterAgent:
                    self.hp = self.state["heal"]
        else:
            self.hp -= self.hp_decrease

        if self.hp <= 0:
            self.grid.remove_agent(self)
            self.model.schedule.remove(self)
