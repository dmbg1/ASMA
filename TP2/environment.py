from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer
from mesa.visualization.modules import ChartModule
from mesa.visualization.UserParam import UserSettableParameter

canvasWidth = 20
canvasHeight = 20

model_params = {
    'N': UserSettableParameter(
        'slider', 'Number of agents', 100, 0, canvasWidth * canvasHeight),
    'width': canvasHeight,
    'height': canvasHeight,
    'init_humans': UserSettableParameter(
        'slider', '% of initial pop. of humans', 0.4, 0, 1, 0.05),
    'init_monsters': UserSettableParameter(
        'slider', '% of initial pop. of monsters', 0.3, 0, 1, 0.05),
    'init_heroes': UserSettableParameter(
        'slider', '% of initial pop. of heroes', 0.2, 0, 1, 0.05),
    'init_food': UserSettableParameter(
            'slider', '% of initial amount of food', 0.1, 0, 1, 0.05)
}


def agents_portrayal(agent):
    return agent.portrayal()


def createCanvas():
    return CanvasGrid(agents_portrayal, canvasWidth, canvasHeight, 500, 500)


def createAndStartServer(model):
    grid = createCanvas()
    #chart = ChartModule([{"Label": "Population of humans",
      #                    "Color": "Black"}],
       #                 data_collector_name='datacollector')
    server = ModularServer(model,
                           [grid],
                           "Monsters VS Heroes",
                           model_params)

    server.port = 8521
    server.launch()
