<?php
$username = "root";
$password = "";
$host = "localhost";
$dbname = "booktbl_db";
$connect = @mysqli_connect($host, $username, $password) or die("Unable to connect to host.");
$db = @mysqli_select_db($connect, $dbname) or die("Unable to connect to students database");

if (function_exists('get_magic_quotes_gpc')) {
    function undo_magic_qoutes_gpc($array) {
        foreach ($array as $value) {
            if (is_array($value)) {
                // undo_magic_goutes_gpc( $value);
            } else {
                // $value = stripstashes ($value);
            }
        }
    }
    undo_magic_qoutes_gpc($_POST);
    undo_magic_qoutes_gpc($_GET);
    undo_magic_qoutes_gpc($_COOKIE);
}