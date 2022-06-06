from abc import abstractmethod
import math
from mesa import Agent

import Utils


class Character(Agent):
    """An agent with fixed initial wealth."""

    def __init__(self, unique_id, model, hp, hp_decrease, damage_per_second, canReproduce, age, maxAge):
        super().__init__(unique_id, model)

        self.model = model
        self.grid = model.grid
        self.hp = hp
        self.hp_decrease = hp_decrease
        self.damage_per_second = damage_per_second
        self.state = {"state": "Move"}
        self.canReproduce = canReproduce
        self.noReprodSteps = 0
        self.age = age
        self.maxAge = maxAge
        if type(self).__name__ == "HeroAgent":
            self.maxAge = 150
        else:
            self.maxAge = 75

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
            own_agent_type = type(self).__name__
            if type(neigh).__name__ == own_agent_type and self.canReproduce and self.age > 0.2 * self.maxAge \
                    and self.noReprodSteps == 0:
                for _ in range(self.model.random.randrange(0, 2)):
                    self.model.createAgent(own_agent_type, 0)
                    self.model.incr_num_reproductions(own_agent_type)
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

    def agent_death(self, death_cause):
        agent_type = type(self).__name__

        if death_cause == "fight":
            self.model.incr_num_deaths_by_kill(agent_type)
        elif death_cause == "hunger":
            self.model.incr_num_deaths_by_hunger(agent_type)

        self.model.removeAgent(self)

    def step(self):
        death_cause = "fight"  # Death cause if agent dies

        self.age += 1
        if self.age >= self.maxAge:
            self.hp = 0

        if self.state["state"] != "InFight" and self.hp > 0:
            self.move()
            self.hp -= self.hp_decrease
            death_cause = "hunger"

        if self.hp <= 0:
            self.agent_death(death_cause)
            return

        self.action()
        self.reproduction()
        if self.noReprodSteps > 0:
            self.noReprodSteps -= 1
