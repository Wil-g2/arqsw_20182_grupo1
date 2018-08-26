<?php
error_reporting(E_ALL & ~E_NOTICE & ~E_WARNING);

class Analyser
{
    function listaArquivos()
    {
        if ($handle = @opendir('./')) {

            $excluir = array(".", "..", "Analyser.php");

            while ($arquivo = readdir($handle)) {
                if (!in_array($arquivo, $excluir)) $emAnalise[] = $arquivo;
            }
            closedir($handle);
            return $emAnalise;
        }
    }

    function analisaArquivo($arquivo)
    {
        //Nome da Classes - encontra o nome das classes no arquivo... uma ou todas...
        $reClasses = '/^((class)|(final class)|(abstract class)).(\w*).*$/m';

        //Metodos e seus parâmetros
        $reMetodos = '/^.*function *(\w*) *\((\w*.*)\)/m';

        //conteúdo dos arquivos para a memória
        foreach ($arquivo as $item) {
            $conteudo[] = file_get_contents($item);
        }

        foreach ($conteudo as $codigo) {
            preg_match_all($reClasses, $codigo, $matchesClasses, PREG_SET_ORDER, 0);

            if (count($matchesClasses) == 1) {
                preg_match_all($reMetodos, $codigo, $matchesMetodos, PREG_SET_ORDER, 0);

                foreach ($matchesMetodos as $metodos) {
                    $analise['classe'][$matchesClasses[0][5]]['metodo'][] = $metodos[1];
                    $analise['classe'][$matchesClasses[0][5]]['metodo'][]['parametro'] = $metodos[2];

                }

            } else {
                foreach ($matchesClasses as $classe) {
                    $analise['classe'][$classe[5]]['Não Conformidade'] = true;
                    $analise['info'][] = "[[ATENÇÃO]] A classe {$classe[5]} está no mesmo arquivo que outra classe!".PHP_EOL;
                }
            }
        }
        return $analise;
    }

    function geraSaidaDot($analise)
    {
        $saidaDot .= "// Arquivo de analise gerado no formato DOT Graphitz". PHP_EOL;
        $saidaDot .= "digraph AnaliseEstatica {". PHP_EOL;


        foreach ($analise['classe'] as $key => $value) {
            $saidaDot .= "{$key};". PHP_EOL;
        }
        $cont = 0;
        foreach ($analise['classe'] as $key => $value) {
            //para cada key deve buscar todos os metodos, quando houver
            $nomeClasse = $key;
            $nMetodos = count($analise['classe'][$key]['metodo']);
            if($nMetodos > 0){

                //echo "<pre>";
                //print_r( $analise['classe'][$key]['metodo']).PHP_EOL;
                if(!empty($analise['classe'][$key]['metodo'][1]['parametro'])) {
                    $temp1 = $key . " -> ". $analise['classe'][$key]['metodo'][$cont] . ";";
                    $temp2 = $analise['classe'][$key]['metodo'][$cont] . " -> " .
                        str_replace("$","",$analise['classe'][$key]['metodo'][$cont+1]['parametro'].";");
                }
                $saidaDot .= "{$temp1}". PHP_EOL;
                $saidaDot .= "{$temp2}". PHP_EOL;


            }else{
                //classe sem método
                $saidaDot .= "{$key} [shape=box,style=filled,color=\"red\"];". PHP_EOL;
            }
            $cont = $cont + 2;
        }


        $saidaDot .= "}". PHP_EOL;
        $dotFile = fopen("analise.dot", "w");
        fwrite($dotFile, $saidaDot);
        fclose($dotFile);

    }
}

$analisador = new Analyser();
$arquivos = $analisador->listaArquivos();
$out = $analisador->analisaArquivo($arquivos);

//saída DOT Graphiz
$analisador->geraSaidaDot($out);


//json export
$json = json_encode($out);
$analiseFileJson = fopen("analise.json", "w");
fwrite($analiseFileJson, $json);
fclose($analiseFileJson);
//Ferramenta básica de json http://jsonviewer.stack.hu/


