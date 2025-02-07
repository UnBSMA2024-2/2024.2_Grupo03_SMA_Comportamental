# Simulador de caminhões de entrega

**Disciplina**: FGA0134 - Sistemas Multiagentes <br>
**Nro do Grupo (de acordo com a Planilha de Divisão dos Grupos)**: 03<br>
**Frente de Pesquisa**: Agentes comportamentais com ACO<br>

## Alunos
|Matrícula | Aluno |
| ---------- | ---------------------------------------- |
| 19/0012200 | Douglas da Silva Monteles                |
| 18/0042378 | Kathlyn Lara Murussi                     |
| 21/1062240 | Mateus Bastos dos Santos                 |
| 18/0027352 | Rodrigo Carvalho dos Santos              |

## Artigo

Disponível [aqui](./artigo/SMA.pdf)

## Sobre

Este projeto tem como objetivo desenvolver uma simulação para otimizar o processo de entrega de caminhões em um cenário logístico utilizando sistemas multiagentes.<br>
A principal abordagem utilizada é o Ant Colony Optimization (ACO), um algoritmo inspirado no comportamento de formigas para resolver problemas de otimização, especialmente para o cálculo de rotas em problemas de roteirização de veículos (VRP, do inglês Vehicle Routing Problem).

No projeto, cada agente representa um caminhão e suas interações com o ambiente  são modeladas para realizar entregas de maneira eficiente, buscando minimizar custos com o tempo e distância percorrida.


- Acesse a Documentação completa do projeto [aqui](https://unbsma2024-2.github.io/2024.2_Grupo03_SMA_Comportamental/about/).

## Screenshots

Adicione 2 ou mais screenshots do projeto em termos de interface e/ou funcionamento.

## Instalação 

**Linguagens**: Java e Javascript<br>
**Tecnologias**: Java, Javascript, HTML5, CSS3, JADE, Spring boot, Makefile<br>

### Pré-requisitos para rodar o projeto

- Possuir instalado o [JDK 21](https://www.oracle.com/br/java/technologies/downloads/)

  ![image](https://github.com/user-attachments/assets/1004ae82-98c3-42d1-b5d1-8deb7e67d4ad)

## Uso 

Recomenda-se o uso do sistema operacional Linux ou alguma distribuição baseada no linux.

1. clone o repositório: 
   1. ssh: `git@github.com:UnBSMA2024-2/2024.1_Grupo03_SMA_Comportamental.git`
   2. http: `https://github.com/UnBSMA2024-2/2024.1_Grupo03_SMA_Comportamental.git`
2. Entre no diretório: `2024.1_Grupo03_SMA_Comportamental`
3. Baixe as dependências e gere a build do projeto com: `make build`

   ![image](https://github.com/user-attachments/assets/dd0420e5-203b-4d72-b6a8-2c6b63a86df0)

4. Execute o projeto com: `make up`
   1. Ao executar esse comando, a tela do Jade Remote Agent Management deve abrir como na imagem abaixo:

      ![image](https://github.com/user-attachments/assets/5d39608e-5278-4698-93c7-04c37152e0a6)

   2. Adicionalmente, um servidor web também foi iniciado e pode ser acessado via: <http://localhost:8080>
5. Para parar a execução do projeto, rode o comando: `make down`
   1. Esse comando exige usuário `sudo`
6. Adicionalmente, para gerar a build e executar o projeto, rode o comando `make build-up`


## Vídeo

Apresentação final do Artigo e d o projeto:
[Apresentação](https://youtu.be/mWgfeyMcyHs)

## Participações

Apresente, brevemente, como cada membro do grupo contribuiu para o projeto.

|Nome do Membro | Contribuição | Significância da Contribuição para o Projeto (Excelente/Boa/Regular/Ruim/Nula) | Comprobatórios (ex. links para commits) |
| -- | -- | -- | -- |
| Douglas  | Criou a base do projeto | Excelente | [commit](https://github.com/UnBSMA2024-2/2024.1_Grupo03_SMA_Comportamental/commit/65c2dacbbac3a6acab3d453928c51de18200ecb4) |
| Mateus  | Estruturou a documentação do Projeto, testes nos agentes | boa | [commit](https://unbsma2024-2.github.io/2024.2_Grupo03_SMA_Comportamental/about/) |
| Lara  | Fez o dataset, e testou o protótipo do ACO| boa | [commit](https://github.com/UnBSMA2024-2/2024.1_Grupo03_SMA_Comportamental/commit/65c2dacbbac3a6acab3d453928c51de18200ecb4) |
| Rodrigo  | Desenvolveu Front-end | boa | [commit](https://github.com/UnBSMA2024-2/2024.1_Grupo03_SMA_Comportamental/commit/65c2dacbbac3a6acab3d453928c51de18200ecb4) |

## Outros 

(i) Lições Aprendidas: Problemas do mundo real que aparentam ter soluções computacionais complexas podem ser resulvidos simplismente observando como a natureza lida com esses mesmos problemas.

(ii) Percepções: Quantidade nem sempre é sinônimo de qualidade. Ao implementar o sistema multiagentes que utiliza feromônio para determinar a melhor rota de entrega, aumentar a quantidade de caminhões percorrendo o mapa não faz com que a melhor solução se apresente mais rápido. 

(iii) Contribuições e Fragilidades: Com este projeto, agora tem-se um exemplo de sistemas multiagentes com JADE e Springboot que se comunica utilizando requisições HTTP e Web Sockets.

(iV) Trabalhos Futuros: Este projeto ainda está em fase inicial e precisa de ajustes para atingir o nível ideal. Por enquanto, pretende-se continuar estudando sobre o assunto e talvez tentar aplicar correções que melhorem esse algoritmo.

## Fontes

> BRAND, Michael et al. Ant colony optimization algorithm for robot path planning. In: 2010 international conference on computer design and applications. IEEE, 2010. p. V3-436-V3-440.

> COLORNI, Alberto et al. Distributed optimization by ant colonies. In: Proceedings of the first European conference on artificial life. 1991. p. 134-142.

> DORIGO, Marco; BIRATTARI, Mauro; STUTZLE, Thomas. Ant colony optimization. IEEE computational intelligence magazine, v. 1, n. 4, p. 28-39, 2006.

> FAISHAL, Rahaman Md. Research on Formation Control and Path Planning Optimization for Multiple Unmanned Vehicles Using Virtual Structure Approach. 2024.

> GLABOWSKI, Mariusz et al. Shortest path problem solving based on ant colony optimization metaheuristic. Image Processing & Communications, v. 17, n. 1-2, p. 7, 2012.

> STÜTZLE, Thomas; HOOS, Holger H. MAX–MIN Ant System. Future Generation Computer Systems, v. 16, n. 8, p. 889–914, 2000. DOI: https://doi.org/10.1016/S0167-739X(00)00043-1. Disponível em: https://www.sciencedirect.com/science/article/pii/S0167739X00000431. Acesso em: 25 nov. 2024.

### Algoritmo utilizando
> ZITO, Michele. Disponível em: <https://github.com/MicheleZito/Ant_Colony_Optimization_for_Shortest_Path_Problem>. Acesso em: 25 Novembro 2024.
