from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer
from mesa.visualization.modules import ChartModule
from mesa.visualization.UserParam import UserSettableParameter

canvasWidth = 40
canvasHeight = 40

model_params = {
    'N': UserSettableParameter(
        'slider', 'Number of agents', 250, 0, canvasWidth * canvasHeight),
    'width': canvasHeight,
    'height': canvasHeight,
    'init_humans': UserSettableParameter(
        'slider', '% of initial pop. of humans', 0.4, 0, 1, 0.05),
    'init_monsters': UserSettableParameter(
        'slider', '% of initial pop. of monsters', 0.3, 0, 1, 0.05),
    'init_heroes': UserSettableParameter(
        'slider', '% of initial pop. of heroes', 0.2, 0, 1, 0.05),
    'init_food': UserSettableParameter(
        'slider', '% of initial amount of fruit', 0.1, 0, 1, 0.05),
    'human_HPDecrease': UserSettableParameter(
        'slider', 'Amount of HP decrease each step', 10, 0, 30),
    'monster_HPDecrease': UserSettableParameter(
        'slider', 'Amount of HP decrease each step', 10, 0, 30),
    'hero_HPDecrease': UserSettableParameter(
        'slider', 'Amount of HP decrease each step', 0, 0, 30),
    'probTurningHero': UserSettableParameter(
        'slider', 'Probability of human turning to a Hero', 1, 0, 100),
    'generateQuantityFruit': UserSettableParameter(
        'slider', '% of fruit generated every 5 steps', 7, 0, canvasWidth * canvasHeight * 0.05),
    'human_reproduction': UserSettableParameter(
        'checkbox', 'Human can reproduce', True),
    'monster_reproduction': UserSettableParameter(
        'checkbox', 'Monster can reproduce', True),
    'hero_reproduction': UserSettableParameter(
        'checkbox', 'Hero can reproduce', False)
}


def agents_portrayal(agent):
    return agent.portrayal()


def createCanvas():
    return CanvasGrid(agents_portrayal, canvasWidth, canvasHeight, 400, 400)


def createAndStartServer(model):
    grid = createCanvas()
    chart = ChartModule([{"Label": "Humans", "Color": "Black"},
                         {"Label": "Heroes", "Color": "Blue"},
                         {"Label": "Monsters", "Color": "Red"},
                         {"Label": "Food", "Color": "Green"}])

    humanDeathsChart = ChartModule([{"Label": "Human Deaths by enemy attacks", "Color": "Black"},
                                    {"Label": "Human Deaths by hunger", "Color": "#3B3C36"}],
                                   data_collector_name="human_deaths_collector")
    monsterDeathsChart = ChartModule([{"Label": "Monster Deaths by enemy attacks", "Color": "Red"},
                                      {"Label": "Monster Deaths by hunger", "Color": "#910D09"}],
                                     data_collector_name="monster_deaths_collector")
    heroDeathsChart = ChartModule([{"Label": "Hero Deaths by enemy attacks", "Color": "Blue"},
                                   {"Label": "Hero Deaths by hunger", "Color": "#002366"}],
                                  data_collector_name="hero_deaths_collector")

    humanReproductionsChart = ChartModule([{"Label": "Human Reproduction amount", "Color": "Black"}],
                                          data_collector_name="human_reproduction_collector")
    monsterReproductionsChart = ChartModule([{"Label": "Monster Reproduction amount", "Color": "Red"}],
                                            data_collector_name="monster_reproduction_collector")
    heroReproductionsChart = ChartModule([{"Label": "Hero Reproduction amount", "Color": "Blue"}],
                                         data_collector_name="hero_reproduction_collector")

    server = ModularServer(model,
                           [grid, chart, humanDeathsChart, monsterDeathsChart, heroDeathsChart,
                            humanReproductionsChart, monsterReproductionsChart, heroReproductionsChart],
                           "Monsters VS Heroes",
                           model_params)

    server.port = 8521
    server.launch()
