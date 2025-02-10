const http = require('http');
const fs = require('fs');
const path = require('path');

const hostname = '0.0.0.0';
const port = 3000;

const server = http.createServer((req, res) => requestHandler(req, res) );

function requestHandler(req, res) {
  let filePath = '.' + req.url; 

  if (filePath === './')
    filePath = './index.html';

  fs.readFile(filePath, (err, data) => {
    if (err) {
      res.writeHead(404, {'Content-Type': 'text/plain'});
      res.end('File not found');
    } else {
      const extname = path.extname(filePath);

      const contentType = {
        '.html': 'text/html',
        '.css': 'text/css',
        '.js': 'text/javascript',
      }[extname];

      res.writeHead(200, {'Content-Type': contentType});
      res.end(data);
    }
  });
}

server.listen(port, hostname, () => {
  console.log(`Server running at http://${hostname}:${port}/`);
});
