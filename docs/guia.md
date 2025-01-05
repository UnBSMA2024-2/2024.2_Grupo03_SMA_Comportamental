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

