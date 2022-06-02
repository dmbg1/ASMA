from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer

canvasWidth = 20
canvasHeight = 20

def agents_portrayal(agent):
    return agent.portrayal()


def createCanvas():
    return CanvasGrid(agents_portrayal, canvasWidth, canvasHeight, 500, 500)


def createAndStartServer(model):

    grid = createCanvas()

    server = ModularServer(model,
                       [grid],
                       "Monsters VS Heros",
                       {"N":100, "width":canvasWidth, "height":canvasHeight})
    
    server.port = 8521
    server.launch()
