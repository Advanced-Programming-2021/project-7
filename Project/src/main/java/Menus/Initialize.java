package Menus;

import Model.Monster;
import Model.Spell;
import Model.Trap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Initialize {

    public void importMonsterCardDate() throws IOException {
        String line, filePath = "DataBase\\Cards\\Monster.csv";
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
    public void importSpellTrapCardData() throws IOException {
        String line, filePath = "DataBase\\Cards\\SpellTrap.csv";
        BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
        csvReader.readLine();
        while (true) {
            line = csvReader.readLine();
            String[] data = line.split(",(?=\\S)");
            if (data[ 1 ].equals("Spell")) break;
            // date[ 0 ]:name date[ 1 ]:type data[ 2 ]:icon data[ 3 ]:Description data[ 4 ]:status
            // data[ 5 ]:price
            Trap trap = new Trap(data[ 0 ], data[ 1 ], data[ 2 ], data[ 3 ], data[ 4 ], Integer.parseInt(data[ 5 ]));
        }
        while ((line = csvReader.readLine()) != null) {
            line = csvReader.readLine();
            String[] data = line.split(",(?=\\S)");
            // date[ 0 ]:name date[ 1 ]:type data[ 2 ]:icon data[ 3 ]:Description data[ 4 ]:status
            // data[ 5 ]:price
            Spell spell = new Spell(data[ 0 ], data[ 1 ], data[ 2 ], data[ 3 ], data[ 4 ], Integer.parseInt(data[ 5 ]));
        }
        csvReader.close();
    }
}