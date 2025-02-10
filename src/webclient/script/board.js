import { CellType } from './objects/cellType.js';

const canvas = document.getElementById('myCanvas');
const ctx = canvas.getContext('2d');

const Constants = {
  CELL_SIZE: 100,
  ANIMATION_DURATION: 300,
  FIELD_LINE_WIDTH: 8,
  FIELD_LINE_COLOR: '#0da192',
  X_SYMBOL_COLOR: '#545454',
  O_SYMBOL_COLOR: '#f2ebd3'
};

import { eventManager } from './objects/eventManager.js';

canvas.addEventListener('click', (event) => {
  const rect = canvas.getBoundingClientRect();
  const x = event.clientX - rect.left;
  const y = event.clientY - rect.top;

  const row = Math.floor(y / Constants.CELL_SIZE);
  const col = Math.floor(x / Constants.CELL_SIZE);

  const isOnLine = 
      x % Constants.CELL_SIZE < Constants.FIELD_LINE_WIDTH || 
      y % Constants.CELL_SIZE < Constants.FIELD_LINE_WIDTH ||
      x > Constants.CELL_SIZE * (col + 1) - Constants.FIELD_LINE_WIDTH ||
      y > Constants.CELL_SIZE * (row + 1) - Constants.FIELD_LINE_WIDTH;

  if(!isOnLine)
    eventManager.emit('newCellClicked', { row, col });
});

export async function drawBoard(field) {
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  drawAxis(field == null ? Constants.ANIMATION_DURATION : 1);

  if(field != null){
    drawCellsSymbols(field);
  }
}

function drawCellsSymbols(field) {
  // field is matrix 3x3
  const cellSize = Constants.CELL_SIZE;
  const halfCellSize = cellSize / 2;
  const lineWidth = Constants.FIELD_LINE_WIDTH * 1.2;
  const symbolSize = cellSize * 0.5; // Adjust symbol size as neede

  for (let i = 0; i < 3; i++) {
    for (let j = 0; j < 3; j++) {
      const x = cellSize * j + halfCellSize;
      const y = cellSize * i + halfCellSize;

      if (field[i][j] === CellType.X) {
        drawLineAnimated(x - symbolSize / 2, y - symbolSize / 2,
           x + symbolSize / 2, y + symbolSize / 2, Constants.X_SYMBOL_COLOR, lineWidth, 1);
        drawLineAnimated(x - symbolSize / 2, y + symbolSize / 2, 
          x + symbolSize / 2, y - symbolSize / 2, Constants.X_SYMBOL_COLOR, lineWidth, 1);
      } else if (field[i][j] === CellType.O) {
        ctx.strokeStyle = Constants.O_SYMBOL_COLOR;
        ctx.lineWidth = lineWidth;
        ctx.beginPath();
        ctx.arc(x, y, symbolSize / 2, 0, 2 * Math.PI);
        ctx.stroke();
      }
    }
  }
}
  
function drawAxis(animation_duration) {
  const width = canvas.width;
  const height = canvas.height;

  const cellSize = Constants.CELL_SIZE;
  const animationDuration = animation_duration;
  const c = Constants.FIELD_LINE_COLOR;
  const lw = Constants.FIELD_LINE_WIDTH;

  drawLineAnimated(cellSize, height/2, cellSize, 0, c, lw, animationDuration);
  drawLineAnimated(cellSize, width/2, cellSize, height, c, lw, animationDuration);

  drawLineAnimated(cellSize * 2, height/2, cellSize * 2, 0, c, lw, animationDuration);
  drawLineAnimated(cellSize * 2, width/2, cellSize * 2, height, c, lw, animationDuration);

  drawLineAnimated(width/2, cellSize, 0, cellSize, c, lw, animationDuration);
  drawLineAnimated(width/2, cellSize, width, cellSize, c, lw, animationDuration);

  drawLineAnimated(width/2, cellSize * 2, 0, cellSize * 2, c, lw, animationDuration);
  drawLineAnimated(width/2, cellSize * 2, width, cellSize * 2, c, lw, animationDuration);
}
  
function drawLineAnimated(x1, y1, x2, y2, color, lineWidth, duration, callback) {
  let start = null;
  
  const draw = (timestamp) => {
    if (!start) start = timestamp;
    const progress = timestamp - start;
    const percent = Math.min(progress / duration, 1.0);

    ctx.strokeStyle = color;
    ctx.lineWidth = lineWidth;

    ctx.beginPath();
    ctx.moveTo(x1, y1);
    ctx.lineTo(x1 + (x2 - x1) * percent, y1 + (y2 - y1) * percent);
    ctx.stroke();

    if (percent < 1) {
      requestAnimationFrame(draw);
    } else if (callback) {
      callback();
    }
  };

  requestAnimationFrame(draw);
}
