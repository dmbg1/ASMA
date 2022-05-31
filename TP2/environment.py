from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer


def agents_portrayal(agent):
    return agent.portrayal()


def createCanvas():
    return CanvasGrid(agents_portrayal, 20, 20, 500, 500)


def createAndStartServer(model):

    grid = createCanvas()

    server = ModularServer(model,
                       [grid],
                       "Monsters VS Heros",
                       {"N":50, "width":20, "height":20})
    
    server.port = 8521
    server.launch()
