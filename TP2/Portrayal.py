class Portrayal:

    def __init__(self, shape, color, radius):
        self.shape = shape
        self.color = color
        self.radius = radius

    def portrayal(self):
        portrayal = {"Shape": self.shape,
                     "Color": self.color,
                     "Filled": "true",
                     "Layer": 0,
                     "r": self.radius}

        return portrayal

    def set_color(self, color):
        self.color = color
