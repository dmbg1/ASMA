from character import Character

class MonsterAgent(Character):

    def portrayal(self):
        portrayal = {"Shape": "circle",
                 "Color": "red",
                 "Filled": "true",
                 "Layer": 0,
                 "r": 0.5}
        
        return portrayal