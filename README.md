## Gestão de Empréstimo
Esse projeto tem como intuito simular um sistema de validação e verificação de empréstimos baseado no valor que um contribuente recebe de benefícios cedidos pelo governo.

## Arquitetura do projeto
<img src="./images/Classe UML.png" alt="image_name png" />

## Estrutura
O projeto é estruturado com base nos seguintes diretórios, a partir da raiz src/main/java/"nome do projeto": <br>
**Controller:** Destinada a funções de roteamento.<br>
**DTO:** Objetos destinados a encapsular requisições e respostas provenientes de requisições http. <br>
**Services:** Classes destinadas à lógica da aplicação, essencialmente qualquer função que não faça uso dos metódos das classes do diretório Controller. <br>
**DAO:** Classes que encapsulam valores provenientes de uma classe que se comunica diretamente com um banco de dados. <br>
**Models:** Classes que representam tabelas em um banco de dados ou qualquer classe que manipule diretamente um banco de dados. <br> 

# Pré-requisitos
Docker <br>
docker-compose

## Como rodar
Baixe diretámento ou clone o repositório, em seguida abra o terminal e execute o seguinte comando: <br>
**OBS: Verifique se o terminal está apontando para o diretório onde está o arquivo docker-compose.yaml**

```
  docker compose up --build
```

Após a inicialização dos containers, o serviço vai estar disponível na porta 9000. <br>
**Para fazer uso dos endpoints da API use a documentação presente no** [arquivo de assinutura](https://github.com/Projeto-previdencia-privada/Documentacao/blob/main/Gest%C3%A3o%20de%20Empr%C3%A9stimos%20-%20Documenta%C3%A7%C3%A3o/assinaturaAPI.yaml). 
 Recomenda-se copiar  e colar o conteúdo do arquivo de assinatura dentro do editor https://editor.swagger.io/ a fim de ter uma vizualização melhor da documentação da API.

#### Exemplo de requisição
```
  http://localhost:9000/api/v1/emprestimos/vizualizaremprestimo?id-emprestimo=fa81f244-d11d-47ed-946d-e0165482cb1e
```
