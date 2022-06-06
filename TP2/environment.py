from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer
from mesa.visualization.modules import ChartModule
from mesa.visualization.modules import BarChartModule
from mesa.visualization.UserParam import UserSettableParameter

canvasWidth = 40
canvasHeight = 40

model_params = {
    'N': UserSettableParameter(
        'slider', 'Number of agents', 250, 0, canvasWidth * canvasHeight),
    'width': canvasHeight,
    'height': canvasHeight,
    'init_humans': UserSettableParameter(
        'slider', '% of initial pop. of humans', 0.3, 0, 1, 0.05),
    'init_monsters': UserSettableParameter(
        'slider', '% of initial pop. of monsters', 0.3, 0, 1, 0.05),
    'init_heroes': UserSettableParameter(
        'slider', '% of initial pop. of heroes', 0.2, 0, 1, 0.05),
    'init_food': UserSettableParameter(
        'slider', '% of initial amount of fruit', 0.2, 0, 1, 0.05),
    'human_HPDecrease': UserSettableParameter(
        'slider', 'Amount of HP decrease each step for human', 10, 0, 30),
    'monster_HPDecrease': UserSettableParameter(
        'slider', 'Amount of HP decrease each step for monster', 10, 0, 30),
    'hero_HPDecrease': UserSettableParameter(
        'slider', 'Amount of HP decrease each step for hero', 0, 0, 30),
    'probTurningHero': UserSettableParameter(
        'slider', 'Probability of human turning to a Hero', 1, 0, 100),
    'generateQuantityFruit': UserSettableParameter(
        'slider', 'Amount of fruit generated every 5 steps', 20, 0, canvasWidth * canvasHeight * 0.05),
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
    chart = ChartModule([{"Label": "Human Pop.", "Color": "Black"},
                         {"Label": "Hero Pop.", "Color": "Blue"},
                         {"Label": "Monster Pop.", "Color": "Red"}], canvas_height=300)

    deathsChart = BarChartModule([{"Label": "Human Deaths by enemy attacks", "Color": "Black"},
                                  {"Label": "Human Deaths by hunger", "Color": "#3B3C36"},
                                  {"Label": "Monster Deaths by enemy attacks", "Color": "Red"},
                                  {"Label": "Monster Deaths by hunger", "Color": "#910D09"},
                                  {"Label": "Hero Deaths by enemy attacks", "Color": "Blue"},
                                  {"Label": "Hero Deaths by hunger", "Color": "#002366"}],
                                 data_collector_name="deaths_collector", canvas_height=300)

    humanReproductionsChart = ChartModule([{"Label": "Human Reproduction amount", "Color": "Black"}],
                                          data_collector_name="human_reproduction_collector", canvas_height=300)
    monsterReproductionsChart = ChartModule([{"Label": "Monster Reproduction amount", "Color": "Red"}],
                                            data_collector_name="monster_reproduction_collector", canvas_height=300)
    heroReproductionsChart = ChartModule([{"Label": "Hero Reproduction amount", "Color": "Blue"}],
                                         data_collector_name="hero_reproduction_collector", canvas_height=300)

    server = ModularServer(model,
                           [grid, chart, deathsChart, humanReproductionsChart, monsterReproductionsChart,
                            heroReproductionsChart],
                           "Monsters VS Heroes",
                           model_params)

    print(chart.series)
    server.port = 8521
    server.launch()
