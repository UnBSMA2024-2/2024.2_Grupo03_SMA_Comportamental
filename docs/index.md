# SMU grupo 3

Descrição do Projeto: Sistema de Simulação de Entrega de Caminhões com Algoritmo ACO e JADE

---

## Objetivo

Este projeto tem como objetivo desenvolver uma simulação para otimizar o processo de entrega de caminhões em um cenário logístico utilizando sistemas multiagentes. A principal abordagem utilizada é o Ant Colony Optimization (ACO), um algoritmo inspirado no comportamento de formigas para resolver problemas de otimização, especialmente para o cálculo de rotas em problemas de roteirização de veículos (VRP, do inglês Vehicle Routing Problem).

A aplicação foi desenvolvida em Java, utilizando a plataforma JADE (Java Agent DEvelopment Framework), que facilita a construção de sistemas baseados em agentes autônomos. No projeto, cada agente representa um caminhão e suas interações com o ambiente (clientes, depósitos, etc.) são modeladas para realizar entregas de maneira eficiente, buscando minimizar custos com o tempo e distância percorrida.

Funcionalidades:
Simulação de rotas de entrega: O sistema simula o processo de entrega de caminhões, levando em consideração diversos fatores como a localização dos clientes e depósitos, restrições de capacidade dos caminhões e horários de entrega.
Algoritmo ACO: O algoritmo ACO é aplicado para encontrar as melhores rotas para os caminhões, levando em consideração múltiplos objetivos como tempo de viagem e minimização de custos operacionais.
Plataforma JADE: Utilização da plataforma JADE para modelagem e gerenciamento dos agentes que simulam as entregas, permitindo a comunicação e interação entre eles de forma distribuída e eficiente.

Objetivos:
Eficiência nas rotas de entrega: Minimizar o tempo e o custo das rotas de entrega, utilizando a técnica ACO para encontrar soluções de otimização.
Escalabilidade: Permitir que o sistema se adapte a diferentes cenários, desde operações pequenas até grandes redes logísticas.
Monitoramento em tempo real: Acompanhamento da movimentação dos caminhões e do desempenho do sistema ao longo da simulação, possibilitando ajustes em tempo real.
Este projeto é uma aplicação prática da inteligência artificial em sistemas logísticos, com grande potencial para otimizar operações de transporte e entrega, contribuindo para um gerenciamento mais eficiente e reduzindo custos operacionais.