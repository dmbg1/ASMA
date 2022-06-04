from abc import abstractmethod
from mesa import Agent

import Utils


class Character(Agent):
    """An agent with fixed initial wealth."""

    def __init__(self, unique_id, model, hp, hp_decrease, damage_per_second, noReprodSteps):
        super().__init__(unique_id, model)

        self.model = model
        self.grid = model.grid
        self.hp = hp
        self.hp_decrease = hp_decrease
        self.damage_per_second = damage_per_second
        self.state = {"state": "Move"}
        self.noReprodSteps = noReprodSteps

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
            if type(neigh).__name__ == type(self).__name__ and self.noReprodSteps + neigh.noReprodSteps == 0 and\
                    type(self).__name__ != "HeroAgent":
                for _ in range(self.model.random.randrange(0, 2)):
                    self.model.createAgent(type(self).__name__, noReprodSteps=10)
                    self.noReprodSteps = 5

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

    def set_state(self, state):
        self.state = state

    def hurtEnemy(self, enemy):
        enemy.set_hp(enemy.hp - self.damage_per_second)

    def step(self):
        if self.state["state"] != "InFight":
            self.move()
            self.hp -= self.hp_decrease
        if self.hp <= 0:
            self.grid.remove_agent(self)
            self.model.schedule.remove(self)
            return

        self.action()
        self.reproduction()
        if self.noReprodSteps > 0:
            self.noReprodSteps -= 1
