import { readFileSync, writeFileSync } from "fs";
import path from 'path';

const __filename = new URL(import.meta.url).pathname;
const __dirname = path.dirname(__filename);

const inputfile = path.resolve(__dirname, '..', '..', '..', '..', '..', '..', '..', 'external-data', 'graph.json');
const outputfile = "index.js"; 

const fileContent = readFileSync(inputfile, "utf8");

const _nodes = [];
const _edges = [];

const lines = JSON.parse(fileContent).nodes; 

lines.forEach((node, index) => {
  _nodes.push({
    id: index,
    label: node.node,
    x: node.position.x,
    y: node.position.y,
  });
});

lines.forEach((node, indexNode) => {
  node.edges.forEach((edge, indexEdge) => {
    _edges.push({
      id: indexNode * 100 + indexEdge,
      from: indexNode,
      to: _nodes.findIndex((n) => n.label === edge.node),
      distance: edge.distance,
      value: 0,
    });
  });
});

const outputContent = `export const _nodes = ${JSON.stringify(_nodes, null, 2)};\nexport const _edges = ${JSON.stringify(_edges, null, 2)};\n`;

try {
  writeFileSync(outputfile, outputContent, "utf8");
  console.log(`Arquivo de saída salvo em: ${outputfile}`);
} catch (error) {
  console.error(`Erro ao salvar o arquivo: ${error.message}`);
}
