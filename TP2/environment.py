from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer
from mesa.visualization.modules import ChartModule
from mesa.visualization.modules import BarChartModule
from mesa.visualization.UserParam import UserSettableParameter

canvasWidth = 40
canvasHeight = 40

modelParams = {
    'N': UserSettableParameter(
        'slider', 'Number of agents', 250, 0, canvasWidth * canvasHeight),
    'width': canvasHeight,
    'height': canvasHeight,
    'initHumans': UserSettableParameter(
        'slider', '% of initial pop. of humans', 0.3, 0, 1, 0.05),
    'initMonsters': UserSettableParameter(
        'slider', '% of initial pop. of monsters', 0.2, 0, 1, 0.05),
    'initHeroes': UserSettableParameter(
        'slider', '% of initial pop. of heroes', 0.3, 0, 1, 0.05),
    'initFood': UserSettableParameter(
        'slider', '% of initial amount of food', 0.2, 0, 1, 0.05),
    'humanHpDecrease': UserSettableParameter(
        'slider', 'Amount of HP decrease each step for human', 10, 0, 30),
    'monsterHpDecrease': UserSettableParameter(
        'slider', 'Amount of HP decrease each step for monster', 10, 0, 30),
    'heroHpDecrease': UserSettableParameter(
        'slider', 'Amount of HP decrease each step for hero', 0, 0, 30),
    'probTurningHero': UserSettableParameter(
        'slider', 'Probability of human turning to a hero', 1, 0, 100),
    'generateQuantityFood': UserSettableParameter(
        'slider', 'Amount of food generated every 5 steps', 20, 0, canvasWidth * canvasHeight * 0.05),
    'humanReproduction': UserSettableParameter(
        'checkbox', 'Human can reproduce', True),
    'monsterReproduction': UserSettableParameter(
        'checkbox', 'Monster can reproduce', True),
    'heroReproduction': UserSettableParameter(
        'checkbox', 'Hero can reproduce', False)
}


def agentsPortrayal(agent):
    return agent.portrayal()


def createCanvas():
    return CanvasGrid(agentsPortrayal, canvasWidth, canvasHeight, 400, 400)


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
                                 data_collector_name="deathsCollector", canvas_height=300)

    humanReproductionsChart = ChartModule([{"Label": "Human Reproduction amount", "Color": "Black"}],
                                          data_collector_name="humanReproductionCollector", canvas_height=300)
    monsterReproductionsChart = ChartModule([{"Label": "Monster Reproduction amount", "Color": "Red"}],
                                            data_collector_name="monsterReproductionCollector", canvas_height=300)
    heroReproductionsChart = ChartModule([{"Label": "Hero Reproduction amount", "Color": "Blue"}],
                                         data_collector_name="heroReproductionCollector", canvas_height=300)

    server = ModularServer(model,
                           [grid, chart, deathsChart, humanReproductionsChart, monsterReproductionsChart,
                            heroReproductionsChart],
                           "Monsters VS Heroes",
                           modelParams)

    server.port = 8521
    server.launch()
