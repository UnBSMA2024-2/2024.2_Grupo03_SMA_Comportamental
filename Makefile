AGENTS_JAR       = agents/target/agents-1.0-SNAPSHOT.jar
SERVER_JAR       = server/target/server-3.3.5.jar
PROJECT_GROUP    = br.com.fga
AGENTS_LOG       = logs/agents.log
SERVER_LOG       = logs/server.log
AGENTS_PORT      = 1099
SERVER_PORT      = 8080

.PHONY:
	clean
	build-and-run

build-up:
	@echo "Gerando a build e executando o projeto"
	make build up

build:
	@echo "Gerando a build do projeto"
	./mvnw clean install

up:
	@echo "Executando o projeto com a última build criada"

	@nohup java -jar $(AGENTS_JAR) > $(AGENTS_LOG) 2>&1 &
	@echo "Agentes iniciados"

	@nohup java -jar $(SERVER_JAR) > $(SERVER_LOG) 2>&1 &
	@echo "Server iniciado"

down:
	@echo "Parando os serviços..."
	@if [ $$(sudo lsof -ti:$(AGENTS_PORT) | wc -l) -gt 0 ]; then \
		echo "Parando os agentes na porta $(AGENTS_PORT)"; \
		sudo kill -9 $$(sudo lsof -ti:$(AGENTS_PORT)); \
	else \
		echo "Não há agentes em execução na porta $(AGENTS_PORT)."; \
	fi

	@if [ $$(sudo lsof -ti:$(SERVER_PORT) | wc -l) -gt 0 ]; then \
		echo "Parando o servidor na porta $(SERVER_PORT)"; \
		sudo kill -9 $$(sudo lsof -ti:$(SERVER_PORT)); \
	else \
		echo "Não há servidor em execução na porta $(SERVER_PORT)."; \
	fi

clear:
	@echo "Removendo a build do projeto"
	./mvnw clean

build-up-win:
	@echo "Gerando a build e executando o projeto"
	make build-win up

build-win:
	@echo "Gerando a build do projeto"
	.\mvnw clean install
