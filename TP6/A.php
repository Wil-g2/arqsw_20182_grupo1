<?php
//sem definição de uso de métodos, sugestão de namespace (<<NS_Atual>>)

namespace NS1;

class A
{

    public function MetodA1()
    {
        //metodo vasio
        return false;
    }
    public function MethodA2($imap_stream)
    {
        //metodo acesso a email
        $imap_obj = imap_check($imap_stream);
        var_dump($imap_obj);
        return true;
    }
    public function MethodA3()
    {
        //metodo vasio
        return false;
    }
    public function MethodA4()
    {
        //metodo vasio
        return false;
    }
    public function Method5()
    {
        //metodo vasio
        return false;
    }
}