 Escolha da Arquitetura

Optei por utilizar a Arquitetura Hexagonal (Ports e Adapters). O primeiro motivo é prático: como já vínhamos trabalhando com esse padrão nas aulas anteriores da disciplina, eu tinha mais familiaridade e segurança para estruturar o projeto do zero de forma independente.

O modelo hexagonal se encaixa perfeitamente nas necessidades técnicas desse tipo de serviço por dois fatores:

Independência tecnológica: O ecossistema da AWS (SQS) e o banco de dados H2 são tratados puramente como detalhes de infraestrutura. Se amanhã o banco mudar para PostgreSQL ou a fila para RabbitMQ, o coração da aplicação (onde fica a regra da triagem) continua intacto, sem precisar de nenhuma alteração.

Facilidade de testes: Como as regras de negócio ficam limpas e isoladas, sem depender do Spring ou de SDKs da AWS, é possível testar toda a lógica de severidade de forma rápida e isolada, usando testes unitários simples.

 Organizacao do Projeto (Pacotes e Classes)

A estrutura foi dividida rigorosamente para proteger o ecossistema do Domínio. Abaixo fiz uma breve explicação sobre os principais elementos.

 Core (com.example.trabalho.core)

É o coração do sistema. Aqui fica apenas o que é regra de negócio pura. Essa pasta não tem frameworks para garantir que as regras da aplicação nunca quebrem caso o Spring seja atualizado ou o banco de dados mude.

domain.bo (Business Objects): Contém RegistroFalhaBO e ItemPedidoBO. Usamos BO em vez de Entity do JPA aqui porque o Core não pode saber que existe um banco de dados. O BO serve para carregar os dados na memória e rodar a lógica da triagem (HIGH, MEDIUM, LOW) de forma totalmente isolada.

domain.exceptions: Contém a RegraNegocioException. Isolamos as exceções aqui para que o sistema lance erros baseados nas regras do negócio, e não erros genéricos de infraestrutura ou banco.

 Application (com.example.trabalho.application)

É a camada que dita o fluxo do sistema usando contratos (interfaces). Ela não sabe como as coisas são feitas do lado de fora, apenas o que precisa ser feito.

ports.in.service (Portas de Entrada): Contém a TratamentoFalhaPort. É a interface que expõe o nosso serviço para o mundo externo. Quem quiser mandar uma falha para o Core processar precisa obedecer a esse contrato.

ports.out.persistence.h2 (Portas de Saída): Contém a SalvamentoAuditoriaPort. O Core precisa salvar os dados, mas não sabe como fazer isso. Então, ele cria essa interface como quem diz: Eu preciso que salvem isso aqui, virem-se. Quem resolve isso de verdade é a camada de infraestrutura.

services: Contém o TratamentoFalhaService. É a classe que orquestra o caso de uso. Ela recebe a chamada da porta de entrada, chama o BO para rodar a lógica da triagem de severidade e, por fim, aciona a porta de saída para mandar o dado para o banco.

 Infrastructure (com.example.trabalho.infrastructure)
 
É a camada mais externa, onde injetamos as tecnologias e frameworks (Spring, JPA, AWS). É onde o trabalho sujo de infraestrutura acontece.

adapters.in.sqs: Contém o ReceptorDlqAdapter. O MensagemSqsDTO serve apenas para mapear o JSON exato que chega da fila do SQS. Não podemos jogar esse DTO direto para o Core porque, se o formato do JSON da fila mudar, a nossa regra de negócio quebraria. Por isso, usamos o MensagemSqsMapper para converter o DTO (infraestrutura) em um BO (domínio) antes de passá-lo para a porta de entrada.

adapters.out.persistence.h2: Contém a estrutura do banco. Aqui, a separação de classes foi essencial para manter a arquitetura limpa:

AuditoriaEntity: Classe que representa a tabela física no banco H2, com as anotações Entity e Id.

AuditoriaJPAEntity: A nossa interface que estende o JpaRepository do Spring Data para fazer o salvamento no banco.

AuditoriaEntityMapper: Transforma o BO de domínio em uma AuditoriaEntity aceita pelo banco.

SalvamentoAuditoriaPortAdapter: É a classe que implementa a porta de saída criada pelo Core. Ela recebe o objeto de domínio, usa o Mapper para converter em entidade de banco e chama o repository do Spring para salvar.

Garantia de que a mensagem nao sera perdida

Para cumprir a exigência de que nenhuma mensagem se perca, o fluxo foi desenhado de forma estritamente síncrona e transacional dentro do adaptador:

O ReceptorDlqAdapter pega a mensagem da DLQ.

Os dados brutos e os metadados são processados e convertidos.

A regra de triagem categoriza o nível de severidade com base na quantidade total de produtos.

O SalvamentoAuditoriaPortAdapter grava os dados no banco H2 com o status inicial PENDING_ANALYSIS.

Apenas após a confirmação de sucesso (commit) da gravação no banco, o aviso de recebimento (ACK) é enviado ao SQS para remover a mensagem em definitivo da DLQ.

Se acontecer qualquer falha ou erro no meio desse caminho, a mensagem não é apagada; ela permanece na fila de origem para uma nova tentativa (Retry), garantindo total segurança contra perda de dados.
