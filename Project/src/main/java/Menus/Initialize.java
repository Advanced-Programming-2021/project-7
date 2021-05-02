package Menus;

import Model.Monster;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Initialize {
    public void importMonsterCardDate() throws IOException {
        String filePath = "DataBase\\Cards\\Monster.csv";
        String line;
        BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
        csvReader.readLine();
        while ((line = csvReader.readLine()) != null) {
            String[] data = line.split(",(?=\\S)");
            // date[ 0 ]:name date[ 1 ]:level data[ 2 ]:attribute data[ 3 ]:MonsterType data[ 4 ]:CardType
            // data[ 5 ]: AttackPoint data[ 6 ]:DefencePoint data[ 7 ]:Description data[ 8 ]:price
            Monster monster = new Monster(data[ 0 ], Integer.parseInt(data[ 1 ]), data[ 2 ], data[ 3 ], data[ 4 ],
                    Integer.parseInt(data[ 5 ]), Integer.parseInt(data[ 6 ]), data[ 7 ], Integer.parseInt(data[ 8 ]),
                    "Monster");
        }
        csvReader.close();
    }
}
