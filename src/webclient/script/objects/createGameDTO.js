import { CellType } from './cellType.js';

export class CreateGameDTO {
    constructor(playerSide) {
        this.playerSide = playerSide;
        this.gameField = [
            [CellType.EMPTY, CellType.EMPTY, CellType.EMPTY],
            [CellType.EMPTY, CellType.EMPTY, CellType.EMPTY],
            [CellType.EMPTY, CellType.EMPTY, CellType.EMPTY]
        ];
    }

    updateCell(row, col, value) {
        this.gameField[row][col] = value;

        return true;
    }
}