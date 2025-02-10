import { CellType } from './cellType.js';

export class FieldDTO {
    constructor() {
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