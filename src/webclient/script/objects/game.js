import { CellType } from './cellType.js';

export class Game {
    constructor(uuid, playerSide) {
        this.uuid = uuid;
        this.playerSide = playerSide;
        this.gameField = [
            [CellType.EMPTY, CellType.EMPTY, CellType.EMPTY],
            [CellType.EMPTY, CellType.EMPTY, CellType.EMPTY],
            [CellType.EMPTY, CellType.EMPTY, CellType.EMPTY]
        ];
    }
    
    getGameField() {
        return this.gameField;
    }

    setUUID(uuid) {
        this.uuid = uuid;
    }

    getUUID() {
        return this.uuid;
    }

    setPlayerSide(playerSide) {
        this.playerSide = playerSide;
    }

    getPlayerSide() {
        return this.playerSide;
    }

    updateCell(row, col, value) {
        if (this.gameField[row][col] !== ' ')
            return false;

        this.gameField[row][col] = value;

        return true;
    }

    getCell(row, col) {
        return this.gameField[row][col];
    }
}
