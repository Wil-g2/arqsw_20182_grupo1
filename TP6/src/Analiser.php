<?php

error_reporting(~E_ALL);
require_once __DIR__ . "/vendor/autoload.php";

use PhpParser\Parser;
use PhpParser\ParserFactory;
use PhpParser\NodeTraverser;
use PhpParser\Node;
use PhpParser\NodeFinder;
use PhpParser\NodeVisitorAbstract;
use PhpParser\Node\Stmt\ClassMethod;
use PhpParser\NodeDumper;
use PhpParser\PrettyPrinter;

$files = preg_grep('~\.(php)$~', scandir(__DIR__));
foreach ($files as $arquivo) {
    //Jump this file
    if ($arquivo == "Analiser.php") {
        continue;
    }

    echo "Arquivo sob análise: " . $arquivo . PHP_EOL;


//get the code
    $code = file_get_contents(__DIR__ . '/C.php');

//get the conf
    $params = file_get_contents(__DIR__ . '/config_remod.json');
    $params = json_decode($params, true);

    foreach ($params as $value => $key) {
        echo PHP_EOL . "Analisyng: {$value}..." . PHP_EOL . " Ref.: {$params[$value][0]}... " . PHP_EOL;
        echo "- - - - - - - - - - - - - - - - -" . PHP_EOL;

    }

    $parser = (new ParserFactory)->create(ParserFactory::PREFER_PHP7); # or PREFER_PHP5, if your code is older
    $nodes = $parser->parse($code);

//find methods nodes
    $nodeTraverser = new NodeTraverser;
    $traversedNodes = $nodeTraverser->traverse($nodes);

    $nodeDumper = new NodeDumper;
    $nodeFinder = new NodeFinder;

    try {
        $stmts = $parser->parse($code);
        $stmts = $nodeTraverser->traverse($stmts);
        // $stmts is an array of statement nodes
    } catch (Error $e) {
        echo 'Parse Error: ', $e->getMessage();
    }

//var_dump($params["database"]);


//$classes = $nodeFinder->findInstanceOf($stmts, Node\Stmt\Class_::class);
//echo json_encode($classes, JSON_PRETTY_PRINT), "\n";
//var_dump($classes);

//echo json_encode($stmts, JSON_PRETTY_PRINT), "\n";
//echo leaveNode($node);
//var_dump($stmts);

}//all files