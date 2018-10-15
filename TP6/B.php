<?php
//muitos métodos de criptografia, sugestão de namespace (<<NS_Atual>> / security)

namespace NS1;


class B
{
    //método usando a funções opessh_*
    public function MethodB1($cifher)
    {
        $ivlen = openssl_cipher_iv_length($cipher);
        $iv = openssl_random_pseudo_bytes($ivlen);
        openssl_encrypt(SOURCE, $cipher, KEY, $options = 0, $iv);

        return true;
    }

    //método usando a funções mhash*
    public function MethodB2()
    {
        $input = "what do ya want for nothing?";
        $hash = mhash(MHASH_MD5, $input);
        echo "The hash is " . bin2hex($hash) . "<br />\n";
        $hash = mhash(MHASH_MD5, $input, "Jefe");
        echo "The hmac is " . bin2hex($hash) . "<br />\n";

        return true;
    }

    //método usando a funções mcrypt*
    public function MethodB3()
    {
        $key = hash('sha256', 'this is a secret key', true);
        $input = "Let us meet at 9 o'clock at the secret place.";

        $td = mcrypt_module_open('rijndael-128', '', 'cbc', '');
        $iv = mcrypt_create_iv(mcrypt_enc_get_iv_size($td), MCRYPT_DEV_URANDOM);
        mcrypt_generic_init($td, $key, $iv);
        $encrypted_data = mcrypt_generic($td, $input);
        mcrypt_generic_deinit($td);
        mcrypt_module_close($td);
        return true;

    }

    public function MethodB4()
    {
        //metodo vasio
        return false;
    }
}