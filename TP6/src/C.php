<?php
//muitos métodos de bando de dados, sugestão de namespace (<<NS_Atual>> / database)

namespace NS1;


class C
{

    //objeto do tipo PDO
    public function Methodc1($dsn, $user, $pass)
    {
        $options = array(PDO::ATTR_AUTOCOMMIT => FALSE);
        $pdo = new PDO($dsn, $user, $pass, $options);

        return 0;
    }

    //função mssql (SQLServer)
    public function MethodC2()
    {

        $dbhandle = mssql_connect($myServer, $myUser, $myPass)
        or die("Couldn't connect to SQL Server on $myServer");
        return 0;
    }

    //Objeto SQLite
    public function MethodC3()
    {
        $db = new SQLite3('mysqlitedb.db');
        $db->exec('CREATE TABLE bar (bar STRING)');
        return 0;
    }

    public function Method4()
    {
        //metodo vasio
        return 0;
    }


}