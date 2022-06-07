from abc import abstractmethod
import math
from mesa import Agent

import Utils


class Character(Agent):
    """An agent with fixed initial wealth."""

    def __init__(self, unique_id, model, hp, hpDecrease, damagePerSecond, canReproduce, age, maxAge):
        super().__init__(unique_id, model)

        self.model = model
        self.grid = model.grid
        self.hp = hp
        self.hp_decrease = hpDecrease
        self.damage_per_second = damagePerSecond
        self.state = {"state": "Move"}
        self.canReproduce = canReproduce
        self.noReprodSteps = 0
        self.age = age
        self.maxAge = maxAge

    # Choose agents' best position and move the agent to that position
    def move(self):
        possible_steps = self.grid.get_neighborhood(
            self.pos,
            moore=True,
            include_center=False)

        # Agents can't move through walls
        Utils.removeThroughWallSteps(self.pos, possible_steps, self.grid.width, self.grid.height)

        new_position = self.chooseBestPosition(possible_steps)
        self.grid.move_agent(self, new_position)

    # Checks if agent in the same cell is of the same type, which can reproduce and has an age higher than 20 % of
    # the max age
    def reproduction(self, sameCellAgent):

        own_agent_type = type(self).__name__
        if type(sameCellAgent).__name__ == own_agent_type and self.canReproduce and self.age > 0.2 * self.maxAge \
                and self.noReprodSteps == 0:
            for _ in range(self.model.random.randrange(0, 2)):
                self.model.createAgent(own_agent_type, 0)
                self.model.incr_num_reproductions(own_agent_type)
                self.noReprodSteps = 5

    # Hurt enemy if in fight (monsters heals if the attack results on the death of a human)
    def inFightAction(self):
        enemy = self.state["enemy"]
        self.hurtEnemy(enemy)
        if "heal" in self.state:
            heal = self.state["heal"]
            self.hp = heal + self.hp if heal + self.hp < self.maxHP else self.maxHP
        if enemy.hp <= 0:
            self.state = {"state": "Move"}

    def getAgentsInSameCell(self):
        return self.grid.get_neighbors(self.pos, True, True, 0)

    # Get neighbour agents within a radius
    def getNearAgents(self, radius):

        nearAgents = []

        for r in range(1, radius + 1):
            nearAgents.append(self.grid.get_neighbors(
                self.pos, True, False, r))

        return [agent for subAgents in nearAgents for agent in subAgents]

    # Increments age and checks if age is higher than the maxAge (if so agent dies)
    def incrAge(self):
        self.age += 1
        if self.age >= self.maxAge:
            self.hp = 0

    # Decrement amount of steps with no reproduction (> 0)
    def decrNoReprodSteps(self):
        if self.noReprodSteps > 0:
            self.noReprodSteps -= 1

    @abstractmethod
    def chooseBestPosition(self, possible_steps):
        pass

    @abstractmethod
    def action(self):
        pass

    @abstractmethod
    def agentsInSameCellAction(self, sameCellAgent):
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
        death_cause = "fight"  # Death cause if agent dies (default to fight change if not in fight)

        self.incrAge()

        self.decrNoReprodSteps()

        # if in fight agent doesn't move and hunger factor doesn't come into play
        if self.state["state"] != "InFight" and self.hp > 0:
            self.move()
            self.hp -= self.hp_decrease
            death_cause = "hunger"

        if self.hp <= 0:
            self.agent_death(death_cause)
            return  # Don't do action if hp <= 0

        self.action()
