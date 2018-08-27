#Instruções
Para executar a análise use, no termnal, o comando a seguir
```text
:~$ php Analyser.php
```
Isso gera dois arquivos `analise.json` e `analise.dot`

Para Gerar o gráfico uso o software `dot`
```text
:~$ dot -O -Tpng analise.dot
```
Isso gera o arquvio de imagem `analise.dot.png` na raiz da aplicação
